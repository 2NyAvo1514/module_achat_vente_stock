// src/main/java/com/entreprise/manage/achats/demande-achat/model/DemandeAchat.java
package com.entreprise.manage.achats.model;

// import com.entreprise.manage.referentiels.model.Article;
import com.entreprise.manage.core.auth.model.Utilisateur;
import com.entreprise.manage.referentiels.model.Site;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "demande_achat")
public class DemandeAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", unique = true, nullable = false)
    private String reference;

    @ManyToOne
    @JoinColumn(name = "demandeur_id", nullable = false)
    private Utilisateur demandeur;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @Column(name = "statut", nullable = false)
    private String statut; // BROUILLON, SOUMISE, VALIDEE, REJETEE, ANNULEE

    @Column(name = "montant_total", precision = 15, scale = 2)
    private BigDecimal montantTotal = BigDecimal.ZERO;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @ManyToOne
    @JoinColumn(name = "valide_par")
    private Utilisateur validePar;

    @OneToMany(mappedBy = "demandeAchat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LigneDemandeAchat> lignes = new ArrayList<>();

    @OneToOne(mappedBy = "demandeAchat")
    private BonCommande bonCommande;

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

    public Utilisateur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Utilisateur demandeur) {
        this.demandeur = demandeur;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
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

    public List<LigneDemandeAchat> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneDemandeAchat> lignes) {
        this.lignes = lignes;
    }

    public BonCommande getBonCommande() {
        return bonCommande;
    }

    public void setBonCommande(BonCommande bonCommande) {
        this.bonCommande = bonCommande;
    }
    

    public void calculerMontantTotal() {
        if (this.lignes == null || this.lignes.isEmpty()) {
            this.montantTotal = BigDecimal.ZERO;
            return;
        }
        this.montantTotal = this.lignes.stream()
                .map(LigneDemandeAchat::getTotalLigne)
                .filter(t -> t != null)
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b))
                .setScale(2, RoundingMode.HALF_UP);
    }
}