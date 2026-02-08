package com.entreprise.manage.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.entreprise.manage.referentiels.model.Article;
import com.entreprise.manage.referentiels.model.Depot;
import com.entreprise.manage.core.auth.model.Utilisateur;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mouvements_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMouvement typeMouvement; // ENTREE, SORTIE, TRANSFERT, AJUSTEMENT

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "depot_depart_id")
    private Depot depotDepart; // Pour transferts/sorties

    @ManyToOne
    @JoinColumn(name = "depot_destination_id", nullable = false)
    private Depot depotDestination; // Pour entrées/transferts

    @Column(nullable = false)
    private BigDecimal quantite;

    @Column(nullable = false)
    private BigDecimal prixUnitaire; // Prix d'achat/coût moyen

    private String numeroReference; // Référence bon réception/commande vente/etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutMouvement statut; // PLANIFIEE, EXECUTEE, ANNULEE

    private String motifAjustement; // Pour les ajustements d'inventaire

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateurCreateur;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateExecution;

    private String observations;

    @Version
    private Long version; // Optimistic locking
}
