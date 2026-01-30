package com.entreprise.manage.achats.model;

import com.entreprise.manage.core.auth.model.Utilisateur;
import com.entreprise.manage.referentiels.model.Site;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "demande_achat")
public class DemandeAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @ManyToOne
    @JoinColumn(name = "demandeur_id")
    private Utilisateur demandeur;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    @Column(nullable = false)
    private String statut; // BROUILLON, SOUMISE, VALIDEE, REJETEE, CONVERTIE_BC

    @Column(name = "montant_total", precision = 15, scale = 2)
    private BigDecimal montantTotal = BigDecimal.ZERO;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @ManyToOne
    @JoinColumn(name = "valide_par")
    private Utilisateur validePar;

    @Version
    private Long version;

    @OneToMany(mappedBy = "demandeAchat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneDemandeAchat> lignes = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }

    public void recalculerTotal() {
        this.montantTotal = lignes.stream()
                .map(LigneDemandeAchat::getTotalLigne)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters / Setters
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<LigneDemandeAchat> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneDemandeAchat> lignes) {
        this.lignes = lignes;
    }
}
