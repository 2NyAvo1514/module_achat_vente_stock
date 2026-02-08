package com.entreprise.manage.stock.service;

import com.entreprise.manage.stock.model.MouvementStock;
import com.entreprise.manage.stock.model.TypeMouvement;
import com.entreprise.manage.stock.model.StatutMouvement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MouvementStockService {

    /**
     * Crée une entrée de stock (réception)
     */
    MouvementStock creerEntree(Long articleId, Long depotId, BigDecimal quantite,
            BigDecimal prixUnitaire, String numeroReference);

    /**
     * Crée une sortie de stock
     */
    MouvementStock creerSortie(Long articleId, Long depotId, BigDecimal quantite, String numeroReference);

    /**
     * Crée un transfert entre dépôts
     */
    MouvementStock creerTransfert(Long articleId, Long depotDepart, Long depotDestination,
            BigDecimal quantite, String numeroReference);

    /**
     * Crée un ajustement de stock (inventaire)
     */
    MouvementStock creerAjustement(Long articleId, Long depotId, BigDecimal quantite,
            String motif, String observations);

    /**
     * Exécute un mouvement prévu
     */
    void executerMouvement(Long mouvementId);

    /**
     * Annule un mouvement
     */
    void annulerMouvement(Long mouvementId);

    /**
     * Récupère les mouvements par article
     */
    Page<MouvementStock> getMouvementsByArticle(Long articleId, Pageable pageable);

    /**
     * Récupère les mouvements par dépôt
     */
    Page<MouvementStock> getMouvementsByDepot(Long depotId, Pageable pageable);

    /**
     * Récupère l'historique par type
     */
    List<MouvementStock> getMouvementsByType(TypeMouvement type);

    /**
     * Récupère un mouvement
     */
    Optional<MouvementStock> getMouvement(Long id);

    /**
     * Récupère les mouvements en attente
     */
    List<MouvementStock> getMouvementsPlanifiees();

    /**
     * Calcule le coût total d'un mouvement
     */
    BigDecimal calculateCoutTotal(Long mouvementId);
}
