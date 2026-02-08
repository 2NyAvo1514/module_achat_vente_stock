// src/main/java/com/entreprise/manage/achats/commandes/model/BonCommande.java
package com.entreprise.manage.achats.model;

import com.entreprise.manage.achats.model.DemandeAchat;
import com.entreprise.manage.referentiels.model.Fournisseur;
import com.entreprise.manage.core.auth.model.Utilisateur;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "bon_commande")
public class BonCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", unique = true, nullable = false)
    private String reference;

    @OneToOne
    @JoinColumn(name = "demande_achat_id")
    private DemandeAchat demandeAchat;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    @Column(name = "statut", nullable = false)
    private String statut; // BROUILLON, EN_COURS, VALIDEE, ANNULEE, LIVREE

    @Column(name = "montant_total", precision = 15, scale = 2)
    private BigDecimal montantTotal = BigDecimal.ZERO;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @ManyToOne
    @JoinColumn(name = "valide_par")
    private Utilisateur validePar;

    @OneToMany(mappedBy = "bonCommande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LigneBonCommande> lignes = new ArrayList<>();

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public DemandeAchat getDemandeAchat() {
        return demandeAchat;
    }

    public void setDemandeAchat(DemandeAchat demandeAchat) {
        this.demandeAchat = demandeAchat;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public Utilisateur getValidePar() {
        return validePar;
    }

    public void setValidePar(Utilisateur validePar) {
        this.validePar = validePar;
    }

    public List<LigneBonCommande> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneBonCommande> lignes) {
        this.lignes = lignes;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void calculerMontantTotal() {
        if (this.lignes == null || this.lignes.isEmpty()) {
            this.montantTotal = BigDecimal.ZERO;
            return;
        }
        this.montantTotal = this.lignes.stream()
                .map(LigneBonCommande::getTotalLigne)
                .filter(t -> t != null)
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b))
                .setScale(2, RoundingMode.HALF_UP);
    }

}