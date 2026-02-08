package com.entreprise.manage.achats.service;

// src/main/java/com/entreprise/manage/achats/demande-achat/service/DemandeAchatService.java

import com.entreprise.manage.achats.model.DemandeAchat;
import com.entreprise.manage.achats.model.LigneDemandeAchat;
import com.entreprise.manage.core.exception.BusinessException;
import com.entreprise.manage.core.auth.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DemandeAchatService {

    DemandeAchat creerDemandeAchat(DemandeAchat demandeAchat, Utilisateur demandeur);

    DemandeAchat ajouterLigneDemande(Long demandeId, LigneDemandeAchat ligne);

    DemandeAchat soumettreDemande(Long demandeId, Utilisateur validateur) throws BusinessException;

    DemandeAchat validerDemande(Long demandeId, Utilisateur validateur) throws BusinessException;

    DemandeAchat rejeterDemande(Long demandeId, Utilisateur validateur, String motif) throws BusinessException;

    DemandeAchat getDemandeById(Long id);

    Page<DemandeAchat> getAllDemandes(Pageable pageable);

    Page<DemandeAchat> getDemandesByStatut(String statut, Pageable pageable);

    Page<DemandeAchat> getDemandesByDemandeur(Utilisateur demandeur, Pageable pageable);

    List<DemandeAchat> getDemandesEnAttenteValidation();

    void supprimerDemande(Long id) throws BusinessException;

    DemandeAchat genererReference(DemandeAchat demande);
}