package com.entreprise.manage.achats.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class LigneDemandeAchatForm {

    @NotNull(message = "Article obligatoire")
    private Long articleId;

    @DecimalMin(value = "0.01", message = "Quantité invalide")
    private BigDecimal quantite;

    @DecimalMin(value = "0.0", inclusive = false, message = "Prix invalide")
    private BigDecimal prixUnitaire;

    // Getters / Setters
    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
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
}
