package com.entreprise.manage.stock.service;

import com.entreprise.manage.stock.model.NiveauStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface NiveauStockService {

    /**
     * Récupère ou crée le niveau de stock pour un article dans un dépôt
     */
    NiveauStock getOrCreateNiveauStock(Long articleId, Long depotId);

    /**
     * Met à jour le coût moyen pondéré (CUMP) lors d'une entrée
     */
    void updateCUMPOnEntree(Long articleId, Long depotId, BigDecimal quantite, BigDecimal prixUnitaire);

    /**
     * Augmente la quantité disponible
     */
    void increaseDisponible(Long articleId, Long depotId, BigDecimal quantite, BigDecimal prixUnitaire);

    /**
     * Diminue la quantité disponible
     */
    void decreaseDisponible(Long articleId, Long depotId, BigDecimal quantite);

    /**
     * Réserve une quantité de stock
     */
    void addReservation(Long articleId, Long depotId, BigDecimal quantite);

    /**
     * Libère une réservation
     */
    void removeReservation(Long articleId, Long depotId, BigDecimal quantite);

    /**
     * Récupère les articles en dessous du minimum
     */
    List<NiveauStock> getArticlesEnAlerte(Long depotId);

    /**
     * Récupère les articles en surstock
     */
    List<NiveauStock> getArticlesEnSurstock(Long depotId);

    /**
     * Récupère les niveaux par dépôt
     */
    Page<NiveauStock> getNiveauxByDepot(Long depotId, Pageable pageable);

    /**
     * Récupère le niveau pour un article/dépôt
     */
    Optional<NiveauStock> getNiveauStock(Long articleId, Long depotId);

    /**
     * Vérifie la disponibilité
     */
    boolean isAvailable(Long articleId, Long depotId, BigDecimal quantite);
}
