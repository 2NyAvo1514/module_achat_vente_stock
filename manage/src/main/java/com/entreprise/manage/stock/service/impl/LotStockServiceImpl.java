package com.entreprise.manage.stock.service.impl;

import com.entreprise.manage.stock.service.LotStockService;
import com.entreprise.manage.stock.model.LotStock;
import com.entreprise.manage.stock.model.StatutLot;
import com.entreprise.manage.stock.repository.LotStockRepository;
import com.entreprise.manage.referentiels.repository.ArticleRepository;
import com.entreprise.manage.referentiels.repository.DepotRepository;
import com.entreprise.manage.core.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class LotStockServiceImpl implements LotStockService {

    private final LotStockRepository lotRepository;
    private final ArticleRepository articleRepository;
    private final DepotRepository depotRepository;

    @Override
    public LotStock creerLot(Long articleId, Long depotId, String numerolot,
            BigDecimal quantite, BigDecimal prixUnitaire, LocalDate dateExpiration) {
        if (lotRepository.findByNumerolot(numerolot).isPresent()) {
            throw new BusinessException("Le lot " + numerolot + " existe déjà");
        }

        LotStock lot = new LotStock();
        lot.setArticle(articleRepository.getReferenceById(articleId));
        lot.setDepot(depotRepository.getReferenceById(depotId));
        lot.setNumerolot(numerolot);
        lot.setQuantiteInitiale(quantite);
        lot.setQuantiteDisponible(quantite);
        lot.setPrixUnitaire(prixUnitaire);
        lot.setDateExpiration(dateExpiration);
        lot.setDateProduction(LocalDate.now());
        lot.setDateCreation(LocalDateTime.now());

        // Déterminer le statut initial
        if (dateExpiration != null && dateExpiration.isBefore(LocalDate.now())) {
            lot.setStatut(StatutLot.EXPIRE);
        } else if (dateExpiration != null && dateExpiration.isBefore(LocalDate.now().plusDays(30))) {
            lot.setStatut(StatutLot.ALERTE_PEREMPTION);
        } else {
            lot.setStatut(StatutLot.ACTIF);
        }

        return lotRepository.save(lot);
    }

    @Override
    public void updateStatutLot(Long lotId, StatutLot newStatut) {
        LotStock lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new BusinessException("Lot non trouvé"));

        lot.setStatut(newStatut);
        lotRepository.save(lot);
    }

    @Override
    public void preleveFromLot(Long lotId, BigDecimal quantite) {
        LotStock lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new BusinessException("Lot non trouvé"));

        if (lot.getQuantiteDisponible().compareTo(quantite) < 0) {
            throw new BusinessException("Quantité insuffisante dans le lot " + lot.getNumerolot());
        }

        lot.setQuantiteDisponible(lot.getQuantiteDisponible().subtract(quantite));

        if (lot.getQuantiteDisponible().compareTo(BigDecimal.ZERO) == 0) {
            lot.setStatut(StatutLot.UTILISE);
        }

        lotRepository.save(lot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotStock> getLotsEnAlerte(Long depotId) {
        return lotRepository.findLotsEnAlertePetemption(depotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotStock> getLotsExpires(Long depotId) {
        return lotRepository.findLotsExpires(depotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotStock> getLotsDisponiblesFIFO(Long articleId, Long depotId) {
        return lotRepository.findLotsDisponiblesFIFO(articleId, depotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotStock> getLotsDisponiblesFEFO(Long articleId, Long depotId) {
        return lotRepository.findLotsDisponiblesFEFO(articleId, depotId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LotStock> getLotByNumero(String numerolot) {
        return lotRepository.findByNumerolot(numerolot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LotStock> getLotsByArticleAndDepot(Long articleId, Long depotId) {
        return lotRepository.findByArticleIdAndDepotId(articleId, depotId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LotStock> getLotsByDepot(Long depotId, Pageable pageable) {
        return lotRepository.findByDepotId(depotId, pageable);
    }

    @Override
    public void refreshStatuts(Long depotId) {
        List<LotStock> lots = lotRepository.findByDepotId(depotId, null).getContent();
        LocalDate today = LocalDate.now();

        for (LotStock lot : lots) {
            if (lot.getDateExpiration() != null) {
                if (lot.getDateExpiration().isBefore(today)) {
                    lot.setStatut(StatutLot.EXPIRE);
                } else if (lot.getDateExpiration().isBefore(today.plusDays(30))) {
                    lot.setStatut(StatutLot.ALERTE_PEREMPTION);
                } else if (lot.getStatut() == StatutLot.ALERTE_PEREMPTION) {
                    lot.setStatut(StatutLot.ACTIF);
                }
            }
            lotRepository.save(lot);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countExpirationsInRange(Long depotId, LocalDate debut, LocalDate fin) {
        return lotRepository.countExpirationsInRange(depotId, debut, fin);
    }
}
