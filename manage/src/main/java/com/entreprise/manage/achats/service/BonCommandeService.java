package com.entreprise.manage.achats.service;

// src/main/java/com/entreprise/manage/achats/commandes/service/BonCommandeService.java

import com.entreprise.manage.achats.model.BonCommande;
import com.entreprise.manage.achats.model.LigneBonCommande;
import com.entreprise.manage.achats.model.DemandeAchat;
import com.entreprise.manage.core.exception.BusinessException;
import com.entreprise.manage.core.auth.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BonCommandeService {

    BonCommande creerBonCommande(BonCommande bonCommande, Utilisateur acheteur);

    BonCommande creerBonCommandeFromDemande(DemandeAchat demandeAchat, Utilisateur acheteur) throws BusinessException;

    BonCommande ajouterLigneBonCommande(Long bonCommandeId, LigneBonCommande ligne);

    BonCommande validerBonCommande(Long bonCommandeId, Utilisateur validateur) throws BusinessException;

    BonCommande annulerBonCommande(Long bonCommandeId, Utilisateur annulateur, String motif) throws BusinessException;

    BonCommande getBonCommandeById(Long id);

    Page<BonCommande> getAllBonCommandes(Pageable pageable);

    Page<BonCommande> getBonCommandesByStatut(String statut, Pageable pageable);

    List<BonCommande> getBonCommandesEnCours();

    BonCommande genererReference(BonCommande bonCommande);

    void verifierSeparationTaches(BonCommande bonCommande, Utilisateur validateur) throws BusinessException;
}
