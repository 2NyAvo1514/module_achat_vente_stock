package com.entreprise.manage.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.entreprise.manage.referentiels.model.Article;
import com.entreprise.manage.referentiels.model.Depot;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "depot_id", nullable = false)
    private Depot depot;

    @Column(nullable = false)
    private String referenceCommande; // Numéro commande client

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantiteReservee;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantitePrelevee; // Quantité déjà prélevée

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut; // ACTIVE, PRELEVEE, LIVREE, ANNULEE

    @Column(nullable = false)
    private LocalDateTime dateReservation;

    private LocalDateTime dateExpiration; // Délai de prélèvement

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private String observations;

    @Version
    private Long version;

    public boolean estExprimee() {
        return dateExpiration != null && LocalDateTime.now().isAfter(dateExpiration);
    }
}
