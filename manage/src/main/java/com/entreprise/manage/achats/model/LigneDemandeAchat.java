package com.entreprise.manage.achats.model;

import com.entreprise.manage.referentiels.model.Article;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ligne_demande_achat")
public class LigneDemandeAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "demande_achat_id")
    private DemandeAchat demandeAchat;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantite;

    @Column(name = "prix_unitaire", nullable = false, precision = 12, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "total_ligne", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalLigne;

    @PrePersist
    @PreUpdate
    public void calculerTotal() {
        this.totalLigne = quantite.multiply(prixUnitaire);
    }

    // Getters / Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DemandeAchat getDemandeAchat() {
        return demandeAchat;
    }

    public void setDemandeAchat(DemandeAchat demandeAchat) {
        this.demandeAchat = demandeAchat;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getTotalLigne() {
        return totalLigne;
    }

    public void setTotalLigne(BigDecimal totalLigne) {
        this.totalLigne = totalLigne;
    }
}
