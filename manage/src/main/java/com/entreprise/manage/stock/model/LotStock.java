package com.entreprise.manage.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.entreprise.manage.referentiels.model.Article;
import com.entreprise.manage.referentiels.model.Depot;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lots_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "depot_id", nullable = false)
    private Depot depot;

    @Column(nullable = false, unique = true)
    private String numerolot; // Numéro de lot/série

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantiteInitiale;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantiteDisponible; // Reste en stock

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal prixUnitaire;

    private LocalDate dateExpiration; // DLUO/DLC

    private LocalDate dateProduction; // Pour traçabilité FIFO

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutLot statut; // ACTIF, ALERTE_PEREMPTION, EXPIRE, UTILISE

    private String observations;

    @Version
    private Long version;

    public boolean estProchePeremption() {
        if (dateExpiration == null)
            return false;
        LocalDate seuil = LocalDate.now().plusDays(30);
        return dateExpiration.isBefore(seuil) && dateExpiration.isAfter(LocalDate.now());
    }

    public boolean estExpire() {
        if (dateExpiration == null)
            return false;
        return dateExpiration.isBefore(LocalDate.now());
    }
}
