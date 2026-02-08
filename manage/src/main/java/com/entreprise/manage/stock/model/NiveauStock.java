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
@Table(name = "niveaux_stock", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "article_id", "depot_id" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NiveauStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "depot_id", nullable = false)
    private Depot depot;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantiteDisponible; // Stock en main

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantiteReservee; // Stock réservé par commandes

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantiteCommandee; // Stock en attente de réception

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal stockMinimum; // Seuil d'alerte min

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal stockMaximum; // Seuil max (suggestion réappro)

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal coutMoyenPondere; // CUMP actuel

    @Column(nullable = false)
    private LocalDateTime dateMAJ; // Dernière mise à jour

    @Version
    private Long version;

    public BigDecimal getQuantiteUtilisable() {
        return quantiteDisponible.subtract(quantiteReservee);
    }

    public boolean estBelowMinimum() {
        return quantiteDisponible.compareTo(stockMinimum) < 0;
    }

    public boolean estAboveMaximum() {
        return quantiteDisponible.compareTo(stockMaximum) > 0;
    }
}
