package com.entreprise.manage.stock.model;

public enum TypeMouvement {
    ENTREE("Entrée"),
    SORTIE("Sortie"),
    TRANSFERT("Transfert"),
    AJUSTEMENT("Ajustement");

    private final String label;

    TypeMouvement(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
