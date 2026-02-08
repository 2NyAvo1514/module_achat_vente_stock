package com.entreprise.manage.stock.service.impl;

import com.entreprise.manage.stock.service.MouvementStockService;
import com.entreprise.manage.stock.model.MouvementStock;
import com.entreprise.manage.stock.model.TypeMouvement;
import com.entreprise.manage.stock.model.StatutMouvement;
import com.entreprise.manage.stock.repository.MouvementStockRepository;
import com.entreprise.manage.referentiels.repository.ArticleRepository;
import com.entreprise.manage.referentiels.repository.DepotRepository;
import com.entreprise.manage.core.exception.BusinessException;
import com.entreprise.manage.core.auth.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class MouvementStockServiceImpl implements MouvementStockService {

    private final MouvementStockRepository mouvementRepository;
    private final ArticleRepository articleRepository;
    private final DepotRepository depotRepository;
    private final NiveauStockServiceImpl niveauStockService;
    private final SecurityUtil securityUtil;

    @Override
    public MouvementStock creerEntree(Long articleId, Long depotId, BigDecimal quantite,
            BigDecimal prixUnitaire, String numeroReference) {
        validateArticleAndDepot(articleId, depotId);

        MouvementStock mouvement = new MouvementStock();
        mouvement.setTypeMouvement(TypeMouvement.ENTREE);
        mouvement.setArticle(articleRepository.getReferenceById(articleId));
        mouvement.setDepotDestination(depotRepository.getReferenceById(depotId));
        mouvement.setQuantite(quantite);
        mouvement.setPrixUnitaire(prixUnitaire);
        mouvement.setNumeroReference(numeroReference);
        mouvement.setStatut(StatutMouvement.PLANIFIEE);
        mouvement.setUtilisateurCreateur(securityUtil.getCurrentUser());
        mouvement.setDateCreation(LocalDateTime.now());

        return mouvementRepository.save(mouvement);
    }

    @Override
    public MouvementStock creerSortie(Long articleId, Long depotId, BigDecimal quantite,
            String numeroReference) {
        validateArticleAndDepot(articleId, depotId);

        // Vérifier la disponibilité
        if (!niveauStockService.isAvailable(articleId, depotId, quantite)) {
            throw new BusinessException("Stock insuffisant pour la sortie");
        }

        MouvementStock mouvement = new MouvementStock();
        mouvement.setTypeMouvement(TypeMouvement.SORTIE);
        mouvement.setArticle(articleRepository.getReferenceById(articleId));
        mouvement.setDepotDepart(depotRepository.getReferenceById(depotId));
        mouvement.setDepotDestination(depotRepository.getReferenceById(depotId));
        mouvement.setQuantite(quantite);
        mouvement.setNumeroReference(numeroReference);
        mouvement.setStatut(StatutMouvement.PLANIFIEE);
        mouvement.setUtilisateurCreateur(securityUtil.getCurrentUser());
        mouvement.setDateCreation(LocalDateTime.now());

        return mouvementRepository.save(mouvement);
    }

    @Override
    public MouvementStock creerTransfert(Long articleId, Long depotDepart, Long depotDestination,
            BigDecimal quantite, String numeroReference) {
        validateArticleAndDepot(articleId, depotDepart);
        validateArticleAndDepot(articleId, depotDestination);

        if (depotDepart.equals(depotDestination)) {
            throw new BusinessException("Les dépôts doivent être différents");
        }

        if (!niveauStockService.isAvailable(articleId, depotDepart, quantite)) {
            throw new BusinessException("Stock insuffisant au dépôt de départ");
        }

        MouvementStock mouvement = new MouvementStock();
        mouvement.setTypeMouvement(TypeMouvement.TRANSFERT);
        mouvement.setArticle(articleRepository.getReferenceById(articleId));
        mouvement.setDepotDepart(depotRepository.getReferenceById(depotDepart));
        mouvement.setDepotDestination(depotRepository.getReferenceById(depotDestination));
        mouvement.setQuantite(quantite);
        mouvement.setNumeroReference(numeroReference);
        mouvement.setStatut(StatutMouvement.PLANIFIEE);
        mouvement.setUtilisateurCreateur(securityUtil.getCurrentUser());
        mouvement.setDateCreation(LocalDateTime.now());

        return mouvementRepository.save(mouvement);
    }

    @Override
    public MouvementStock creerAjustement(Long articleId, Long depotId, BigDecimal quantite,
            String motif, String observations) {
        validateArticleAndDepot(articleId, depotId);

        MouvementStock mouvement = new MouvementStock();
        mouvement.setTypeMouvement(TypeMouvement.AJUSTEMENT);
        mouvement.setArticle(articleRepository.getReferenceById(articleId));
        mouvement.setDepotDestination(depotRepository.getReferenceById(depotId));
        mouvement.setQuantite(quantite); // Peut être négatif
        mouvement.setMotifAjustement(motif);
        mouvement.setObservations(observations);
        mouvement.setStatut(StatutMouvement.PLANIFIEE);
        mouvement.setUtilisateurCreateur(securityUtil.getCurrentUser());
        mouvement.setDateCreation(LocalDateTime.now());

        return mouvementRepository.save(mouvement);
    }

    @Override
    public void executerMouvement(Long mouvementId) {
        MouvementStock mouvement = mouvementRepository.findById(mouvementId)
                .orElseThrow(() -> new BusinessException("Mouvement non trouvé"));

        if (!mouvement.getStatut().equals(StatutMouvement.PLANIFIEE)) {
            throw new BusinessException("Le mouvement doit être à l'état PLANIFIEE");
        }

        switch (mouvement.getTypeMouvement()) {
            case ENTREE:
                niveauStockService.increaseDisponible(
                        mouvement.getArticle().getId(),
                        mouvement.getDepotDestination().getId(),
                        mouvement.getQuantite(),
                        mouvement.getPrixUnitaire());
                break;
            case SORTIE:
                niveauStockService.decreaseDisponible(
                        mouvement.getArticle().getId(),
                        mouvement.getDepotDepart().getId(),
                        mouvement.getQuantite());
                break;
            case TRANSFERT:
                niveauStockService.decreaseDisponible(
                        mouvement.getArticle().getId(),
                        mouvement.getDepotDepart().getId(),
                        mouvement.getQuantite());
                niveauStockService.increaseDisponible(
                        mouvement.getArticle().getId(),
                        mouvement.getDepotDestination().getId(),
                        mouvement.getQuantite(),
                        BigDecimal.ZERO);
                break;
            case AJUSTEMENT:
                if (mouvement.getQuantite().compareTo(BigDecimal.ZERO) > 0) {
                    niveauStockService.increaseDisponible(
                            mouvement.getArticle().getId(),
                            mouvement.getDepotDestination().getId(),
                            mouvement.getQuantite(),
                            BigDecimal.ZERO);
                } else {
                    niveauStockService.decreaseDisponible(
                            mouvement.getArticle().getId(),
                            mouvement.getDepotDestination().getId(),
                            mouvement.getQuantite().negate());
                }
                break;
        }

        mouvement.setStatut(StatutMouvement.EXECUTEE);
        mouvement.setDateExecution(LocalDateTime.now());
        mouvementRepository.save(mouvement);
    }

    @Override
    public void annulerMouvement(Long mouvementId) {
        MouvementStock mouvement = mouvementRepository.findById(mouvementId)
                .orElseThrow(() -> new BusinessException("Mouvement non trouvé"));

        mouvement.setStatut(StatutMouvement.ANNULEE);
        mouvementRepository.save(mouvement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MouvementStock> getMouvementsByArticle(Long articleId, Pageable pageable) {
        return mouvementRepository.findByArticleId(articleId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MouvementStock> getMouvementsByDepot(Long depotId, Pageable pageable) {
        return mouvementRepository.findByDepotDestinationId(depotId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStock> getMouvementsByType(TypeMouvement type) {
        return mouvementRepository.findByTypeMouvementAndStatut(type, StatutMouvement.EXECUTEE);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MouvementStock> getMouvement(Long id) {
        return mouvementRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStock> getMouvementsPlanifiees() {
        return mouvementRepository.findByTypeMouvementAndStatut(null, StatutMouvement.PLANIFIEE);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateCoutTotal(Long mouvementId) {
        return mouvementRepository.findById(mouvementId)
                .map(m -> m.getQuantite().multiply(m.getPrixUnitaire()))
                .orElse(BigDecimal.ZERO);
    }

    private void validateArticleAndDepot(Long articleId, Long depotId) {
        if (!articleRepository.existsById(articleId)) {
            throw new BusinessException("Article non trouvé: " + articleId);
        }
        if (!depotRepository.existsById(depotId)) {
            throw new BusinessException("Dépôt non trouvé: " + depotId);
        }
    }
}
