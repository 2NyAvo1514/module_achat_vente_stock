package com.entreprise.manage.referentiels.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "unite_id")
    private Unite unite;

    @Column(name = "gestion_lot")
    private Boolean gestionLot = true;

    @Column(name = "duree_conservation_jours")
    private Integer dureeConservationJours;

    private Boolean actif = true;

    @Column(name = "stock_minimum", precision = 15, scale = 2)
    private BigDecimal stockMinimum;

    @Column(name = "stock_maximum", precision = 15, scale = 2)
    private BigDecimal stockMaximum;

    // Getters / Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }

    public Boolean getGestionLot() {
        return gestionLot;
    }

    public void setGestionLot(Boolean gestionLot) {
        this.gestionLot = gestionLot;
    }

    public Integer getDureeConservationJours() {
        return dureeConservationJours;
    }

    public void setDureeConservationJours(Integer dureeConservationJours) {
        this.dureeConservationJours = dureeConservationJours;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public BigDecimal getStockMinimum() {
        return stockMinimum;
    }

    public void setStockMinimum(BigDecimal stockMinimum) {
        this.stockMinimum = stockMinimum;
    }

    public BigDecimal getStockMaximum() {
        return stockMaximum;
    }

    public void setStockMaximum(BigDecimal stockMaximum) {
        this.stockMaximum = stockMaximum;
    }

    // Alias pour compatibilité avec les vues
    public String getDesignation() {
        return this.libelle;
    }
}
