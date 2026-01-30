package com.entreprise.manage.referentiels.model;

import jakarta.persistence.*;

@Entity
@Table(name = "depot")
public class Depot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String nom;

    // Getters / Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
