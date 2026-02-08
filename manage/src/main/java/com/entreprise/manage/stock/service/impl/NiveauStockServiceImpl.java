package com.entreprise.manage.stock.service.impl;

import com.entreprise.manage.stock.service.NiveauStockService;
import com.entreprise.manage.stock.model.NiveauStock;
import com.entreprise.manage.stock.repository.NiveauStockRepository;
import com.entreprise.manage.referentiels.repository.ArticleRepository;
import com.entreprise.manage.referentiels.repository.DepotRepository;
import com.entreprise.manage.referentiels.model.Article;
import com.entreprise.manage.referentiels.model.Depot;
import com.entreprise.manage.core.exception.BusinessException;
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
public class NiveauStockServiceImpl implements NiveauStockService {

    private final NiveauStockRepository niveauStockRepository;
    private final ArticleRepository articleRepository;
    private final DepotRepository depotRepository;

    @Override
    public NiveauStock getOrCreateNiveauStock(Long articleId, Long depotId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException("Article non trouvé: " + articleId));

        Depot depot = depotRepository.findById(depotId)
                .orElseThrow(() -> new BusinessException("Dépôt non trouvé: " + depotId));

        return niveauStockRepository.findByArticleIdAndDepotId(articleId, depotId)
                .orElseGet(() -> {
                    NiveauStock ns = new NiveauStock();
                    ns.setArticle(article);
                    ns.setDepot(depot);
                    ns.setQuantiteDisponible(BigDecimal.ZERO);
                    ns.setQuantiteReservee(BigDecimal.ZERO);
                    ns.setQuantiteCommandee(BigDecimal.ZERO);
                    ns.setStockMinimum(article.getStockMinimum() != null ? article.getStockMinimum() : BigDecimal.ZERO);
                    ns.setStockMaximum(article.getStockMaximum() != null ? article.getStockMaximum() : BigDecimal.ZERO);
                    ns.setCoutMoyenPondere(BigDecimal.ZERO);
                    ns.setDateMAJ(LocalDateTime.now());
                    return niveauStockRepository.save(ns);
                });
    }

    @Override
    public void updateCUMPOnEntree(Long articleId, Long depotId, BigDecimal quantite, BigDecimal prixUnitaire) {
        NiveauStock ns = getOrCreateNiveauStock(articleId, depotId);

        // Formule CUMP: ((QuantitéActuelle * CUMPActuel) + (QuantitéEntrée *
        // PrixEntree)) / (QuantitéActuelle + QuantitéEntrée)
        BigDecimal numerateur = ns.getQuantiteDisponible().multiply(ns.getCoutMoyenPondere())
                .add(quantite.multiply(prixUnitaire));
        BigDecimal denominateur = ns.getQuantiteDisponible().add(quantite);

        BigDecimal newCUMP = denominateur.compareTo(BigDecimal.ZERO) > 0
                ? numerateur.divide(denominateur, 2, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;

        ns.setCoutMoyenPondere(newCUMP);
        ns.setDateMAJ(LocalDateTime.now());
        niveauStockRepository.save(ns);
    }

    @Override
    public void increaseDisponible(Long articleId, Long depotId, BigDecimal quantite, BigDecimal prixUnitaire) {
        NiveauStock ns = getOrCreateNiveauStock(articleId, depotId);
        ns.setQuantiteDisponible(ns.getQuantiteDisponible().add(quantite));
        updateCUMPOnEntree(articleId, depotId, quantite, prixUnitaire);
        ns.setDateMAJ(LocalDateTime.now());
        niveauStockRepository.save(ns);
    }

    @Override
    public void decreaseDisponible(Long articleId, Long depotId, BigDecimal quantite) {
        NiveauStock ns = getOrCreateNiveauStock(articleId, depotId);

        if (ns.getQuantiteUtilisable().compareTo(quantite) < 0) {
            throw new BusinessException("Stock insuffisant pour l'article " + articleId +
                    " au dépôt " + depotId + ". Disponible: " + ns.getQuantiteUtilisable());
        }

        ns.setQuantiteDisponible(ns.getQuantiteDisponible().subtract(quantite));
        ns.setDateMAJ(LocalDateTime.now());
        niveauStockRepository.save(ns);
    }

    @Override
    public void addReservation(Long articleId, Long depotId, BigDecimal quantite) {
        NiveauStock ns = getOrCreateNiveauStock(articleId, depotId);

        if (ns.getQuantiteDisponible().compareTo(quantite) < 0) {
            throw new BusinessException("Stock insuffisant pour la réservation. Article: " + articleId);
        }

        ns.setQuantiteReservee(ns.getQuantiteReservee().add(quantite));
        ns.setDateMAJ(LocalDateTime.now());
        niveauStockRepository.save(ns);
    }

    @Override
    public void removeReservation(Long articleId, Long depotId, BigDecimal quantite) {
        NiveauStock ns = getOrCreateNiveauStock(articleId, depotId);
        ns.setQuantiteReservee(ns.getQuantiteReservee().subtract(quantite));
        ns.setDateMAJ(LocalDateTime.now());
        niveauStockRepository.save(ns);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NiveauStock> getArticlesEnAlerte(Long depotId) {
        return niveauStockRepository.findArticlesEnDessousDuMinimum(depotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NiveauStock> getArticlesEnSurstock(Long depotId) {
        return niveauStockRepository.findArticlesEnDessuseDuMaximum(depotId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NiveauStock> getNiveauxByDepot(Long depotId, Pageable pageable) {
        return niveauStockRepository.findByDepotId(depotId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NiveauStock> getNiveauStock(Long articleId, Long depotId) {
        return niveauStockRepository.findByArticleIdAndDepotId(articleId, depotId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(Long articleId, Long depotId, BigDecimal quantite) {
        return getNiveauStock(articleId, depotId)
                .map(ns -> ns.getQuantiteUtilisable().compareTo(quantite) >= 0)
                .orElse(false);
    }
}
