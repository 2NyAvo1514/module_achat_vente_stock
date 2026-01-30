package com.entreprise.manage.achats.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DemandeAchatForm {

    @NotNull(message = "Le site est obligatoire")
    private Long siteId;

    @NotEmpty(message = "Au moins une ligne est obligatoire")
    private List<LigneDemandeAchatForm> lignes = new ArrayList<>();

    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être positif")
    private BigDecimal montantTotal;

    // Getters / Setters
    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public List<LigneDemandeAchatForm> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneDemandeAchatForm> lignes) {
        this.lignes = lignes;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }
}
