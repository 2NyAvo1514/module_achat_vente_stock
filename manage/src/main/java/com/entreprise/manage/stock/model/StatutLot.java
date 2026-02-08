package com.entreprise.manage.stock.model;

public enum StatutLot {
    ACTIF("Actif"),
    ALERTE_PEREMPTION("Alerte péremption"),
    EXPIRE("Expiré"),
    UTILISE("Utilisé");

    private final String label;

    StatutLot(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
