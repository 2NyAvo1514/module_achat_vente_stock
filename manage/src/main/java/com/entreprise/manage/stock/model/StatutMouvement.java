package com.entreprise.manage.stock.model;

public enum StatutMouvement {
    PLANIFIEE("Planifiée"),
    EXECUTEE("Exécutée"),
    ANNULEE("Annulée");

    private final String label;

    StatutMouvement(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
