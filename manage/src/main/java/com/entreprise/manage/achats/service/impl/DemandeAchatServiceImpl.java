// src/main/java/com/entreprise/manage/achats/demande-achat/service/impl/DemandeAchatServiceImpl.java
package com.entreprise.manage.achats.service.impl;

import com.entreprise.manage.achats.model.DemandeAchat;
import com.entreprise.manage.achats.model.LigneDemandeAchat;
import com.entreprise.manage.achats.repository.DemandeAchatRepository;
import com.entreprise.manage.achats.service.DemandeAchatService;
import com.entreprise.manage.core.auth.SecurityUtil;
import com.entreprise.manage.core.exception.BusinessException;
import com.entreprise.manage.core.auth.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DemandeAchatServiceImpl implements DemandeAchatService {

    private final DemandeAchatRepository demandeAchatRepository;
    private final SecurityUtil securityUtil;

    public DemandeAchatServiceImpl(DemandeAchatRepository demandeAchatRepository,
            SecurityUtil securityUtil) {
        this.demandeAchatRepository = demandeAchatRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public DemandeAchat creerDemandeAchat(DemandeAchat demandeAchat, Utilisateur demandeur) {
        demandeAchat.setDemandeur(demandeur);
        demandeAchat.setStatut("BROUILLON");
        demandeAchat.setDateCreation(LocalDateTime.now());

        // Générer référence
        demandeAchat = genererReference(demandeAchat);

        // Calculer montant total
        // demandeAchat.calculerMontantTotal();

        return demandeAchatRepository.save(demandeAchat);
    }

    @Override
    public DemandeAchat ajouterLigneDemande(Long demandeId, LigneDemandeAchat ligne) {
        DemandeAchat demande = getDemandeById(demandeId);

        if (!"BROUILLON".equals(demande.getStatut())) {
            throw new BusinessException("Impossible d'ajouter une ligne à une demande " + demande.getStatut());
        }

        ligne.setDemandeAchat(demande);
        ligne.calculerTotal();
        demande.getLignes().add(ligne);
        // demande.calculerMontantTotal();

        return demandeAchatRepository.save(demande);
    }

    @Override
    public DemandeAchat soumettreDemande(Long demandeId, Utilisateur validateur) throws BusinessException {
        DemandeAchat demande = getDemandeById(demandeId);

        // Vérifier que c'est le demandeur qui soumet
        if (!demande.getDemandeur().getId().equals(validateur.getId())) {
            throw new BusinessException("Seul le demandeur peut soumettre la demande");
        }

        // Vérifier qu'il y a des lignes
        if (demande.getLignes().isEmpty()) {
            throw new BusinessException("La demande doit contenir au moins une ligne");
        }

        // Vérifier montant minimum
        // if (demande.getMontantTotal() == null || demande.getMontantTotal() <= 0) {
        //     throw new BusinessException("Le montant total doit être positif");
        // }

        demande.setStatut("SOUMISE");
        demande.setDateCreation(LocalDateTime.now());

        return demandeAchatRepository.save(demande);
    }

    @Override
    public DemandeAchat validerDemande(Long demandeId, Utilisateur validateur) throws BusinessException {
        DemandeAchat demande = getDemandeById(demandeId);

        // Vérifier statut
        if (!"SOUMISE".equals(demande.getStatut())) {
            throw new BusinessException("Seules les demandes SOUMISES peuvent être validées");
        }

        // Vérifier séparation des tâches (le validateur ne peut pas être le demandeur)
        if (demande.getDemandeur().getId().equals(validateur.getId())) {
            throw new BusinessException("Le validateur ne peut pas être le même que le demandeur");
        }

        // Appliquer les règles de validation selon les seuils
        // if (!peutValiderSelonSeuil(demande, validateur)) {
        //     throw new BusinessException("Vous n'avez pas les droits pour valider cette demande (seuil insuffisant)");
        // }

        demande.setStatut("VALIDEE");
        demande.setDateValidation(LocalDateTime.now());
        demande.setValidePar(validateur);

        return demandeAchatRepository.save(demande);
    }

    @Override
    public DemandeAchat rejeterDemande(Long demandeId, Utilisateur validateur, String motif) throws BusinessException {
        DemandeAchat demande = getDemandeById(demandeId);

        if (!"SOUMISE".equals(demande.getStatut())) {
            throw new BusinessException("Seules les demandes SOUMISES peuvent être rejetées");
        }

        demande.setStatut("REJETEE");
        demande.setDateValidation(LocalDateTime.now());
        demande.setValidePar(validateur);

        return demandeAchatRepository.save(demande);
    }

    @Override
    public DemandeAchat getDemandeById(Long id) {
        return demandeAchatRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Demande d'achat non trouvée avec l'ID: " + id));
    }

    @Override
    public Page<DemandeAchat> getAllDemandes(Pageable pageable) {
        return demandeAchatRepository.findAll(pageable);
    }

    @Override
    public Page<DemandeAchat> getDemandesByStatut(String statut, Pageable pageable) {
        return (Page<DemandeAchat>) demandeAchatRepository.findByStatut(statut);
    }

    @Override
    public Page<DemandeAchat> getDemandesByDemandeur(Utilisateur demandeur, Pageable pageable) {
        return demandeAchatRepository.findByDemandeur(demandeur, pageable);
    }

    @Override
    public List<DemandeAchat> getDemandesEnAttenteValidation() {
        return demandeAchatRepository.findByStatut("SOUMISE");
    }

    @Override
    public void supprimerDemande(Long id) throws BusinessException {
        DemandeAchat demande = getDemandeById(id);

        if (!"BROUILLON".equals(demande.getStatut())) {
            throw new BusinessException("Seules les demandes en BROUILLON peuvent être supprimées");
        }

        demandeAchatRepository.delete(demande);
    }

    @Override
    public DemandeAchat genererReference(DemandeAchat demande) {
        String annee = String.valueOf(LocalDateTime.now().getYear());
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String reference = "DA-" + annee + "-" + uuid;
        demande.setReference(reference);
        return demande;
    }

    // private boolean peutValiderSelonSeuil(DemandeAchat demande, Utilisateur validateur) {
    //     Double montant = demande.getMontantTotal();

    //     // Règles de seuil selon le rôle
    //     // TODO: Implémenter la logique des seuils N1/N2/N3
    //     // Pour l'instant, on suppose que tous les validateurs peuvent valider
    //     return true;
    // }
}