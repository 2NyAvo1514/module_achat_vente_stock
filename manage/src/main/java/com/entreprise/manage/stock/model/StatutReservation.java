package com.entreprise.manage.stock.model;

public enum StatutReservation {
    ACTIVE("Active"),
    PRELEVEE("Prélevée"),
    LIVREE("Livrée"),
    ANNULEE("Annulée");

    private final String label;

    StatutReservation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
