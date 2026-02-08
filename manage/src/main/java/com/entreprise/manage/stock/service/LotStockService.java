package com.entreprise.manage.stock.service;

import com.entreprise.manage.stock.model.LotStock;
import com.entreprise.manage.stock.model.StatutLot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotStockService {

    /**
     * Crée un nouveau lot de stock
     */
    LotStock creerLot(Long articleId, Long depotId, String numerolot,
            BigDecimal quantite, BigDecimal prixUnitaire, LocalDate dateExpiration);

    /**
     * Met à jour le statut d'un lot
     */
    void updateStatutLot(Long lotId, StatutLot newStatut);

    /**
     * Prélève une quantité d'un lot
     */
    void preleveFromLot(Long lotId, BigDecimal quantite);

    /**
     * Récupère les lots en alerte péremption
     */
    List<LotStock> getLotsEnAlerte(Long depotId);

    /**
     * Récupère les lots expirés
     */
    List<LotStock> getLotsExpires(Long depotId);

    /**
     * Récupère les lots disponibles (FIFO)
     */
    List<LotStock> getLotsDisponiblesFIFO(Long articleId, Long depotId);

    /**
     * Récupère les lots disponibles (FEFO)
     */
    List<LotStock> getLotsDisponiblesFEFO(Long articleId, Long depotId);

    /**
     * Récupère un lot par numéro
     */
    Optional<LotStock> getLotByNumero(String numerolot);

    /**
     * Récupère les lots d'un article dans un dépôt
     */
    List<LotStock> getLotsByArticleAndDepot(Long articleId, Long depotId);

    /**
     * Récupère les lots d'un dépôt
     */
    Page<LotStock> getLotsByDepot(Long depotId, Pageable pageable);

    /**
     * Vérifie et met à jour les statuts (alerte, expiration)
     */
    void refreshStatuts(Long depotId);

    /**
     * Compte les expirations prochaines
     */
    long countExpirationsInRange(Long depotId, LocalDate debut, LocalDate fin);
}
