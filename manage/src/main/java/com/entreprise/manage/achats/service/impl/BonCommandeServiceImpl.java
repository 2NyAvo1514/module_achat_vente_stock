package com.entreprise.manage.achats.service.impl;

import com.entreprise.manage.achats.model.BonCommande;
import com.entreprise.manage.achats.model.LigneBonCommande;
import com.entreprise.manage.achats.repository.BonCommandeRepository;
import com.entreprise.manage.achats.repository.LigneBonCommandeRepository;
import com.entreprise.manage.achats.service.BonCommandeService;
import com.entreprise.manage.achats.service.DemandeAchatService;
import com.entreprise.manage.core.auth.model.Utilisateur;
import com.entreprise.manage.core.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BonCommandeServiceImpl implements BonCommandeService {

    private final BonCommandeRepository bonCommandeRepository;
    private final LigneBonCommandeRepository ligneBonCommandeRepository;
    private final DemandeAchatService demandeAchatService;

    public BonCommandeServiceImpl(BonCommandeRepository bonCommandeRepository,
            LigneBonCommandeRepository ligneBonCommandeRepository,
            DemandeAchatService demandeAchatService) {
        this.bonCommandeRepository = bonCommandeRepository;
        this.ligneBonCommandeRepository = ligneBonCommandeRepository;
        this.demandeAchatService = demandeAchatService;
    }

    @Override
    public BonCommande creerBonCommande(BonCommande bonCommande, Utilisateur acheteur) {
        bonCommande.setStatut("BROUILLON");
        bonCommande.setDateCreation(LocalDateTime.now());
        bonCommande = genererReference(bonCommande);

        final BonCommande finalBonCommande = bonCommande;
        if (bonCommande.getLignes() != null) {
            bonCommande.getLignes().forEach(l -> {
                l.setBonCommande(finalBonCommande);
                l.calculerTotal();
            });
        }

        bonCommande.calculerMontantTotal();
        return bonCommandeRepository.save(bonCommande);
    }

    @Override
    public BonCommande creerBonCommandeFromDemande(com.entreprise.manage.achats.model.DemandeAchat demandeAchat,
            Utilisateur acheteur) throws BusinessException {
        // Basic conversion from demande to bon de commande
        BonCommande bc = new BonCommande();
        bc.setDemandeAchat(demandeAchat);
        bc.setDateCreation(LocalDateTime.now());
        bc.setStatut("BROUILLON");
        bc = genererReference(bc);
        return bonCommandeRepository.save(bc);
    }

    @Override
    public BonCommande ajouterLigneBonCommande(Long bonCommandeId, LigneBonCommande ligne) {
        BonCommande bc = getBonCommandeById(bonCommandeId);
        if (!"BROUILLON".equals(bc.getStatut())) {
            throw new BusinessException("Impossible d'ajouter une ligne à une commande non brouillon");
        }
        ligne.setBonCommande(bc);
        ligne.calculerTotal();
        bc.getLignes().add(ligne);
        bc.calculerMontantTotal();
        return bonCommandeRepository.save(bc);
    }

    @Override
    public BonCommande validerBonCommande(Long bonCommandeId, Utilisateur validateur) throws BusinessException {
        BonCommande bc = getBonCommandeById(bonCommandeId);
        if (!"BROUILLON".equals(bc.getStatut()) && !"EN_COURS".equals(bc.getStatut())) {
            throw new BusinessException("Seules les commandes BROUILLON ou EN_COURS peuvent être validées");
        }

        verifierSeparationTaches(bc, validateur);

        bc.setStatut("VALIDEE");
        bc.setDateValidation(LocalDateTime.now());
        bc.setValidePar(validateur);

        return bonCommandeRepository.save(bc);
    }

    @Override
    public BonCommande annulerBonCommande(Long bonCommandeId, Utilisateur annulateur, String motif)
            throws BusinessException {
        BonCommande bc = getBonCommandeById(bonCommandeId);
        bc.setStatut("ANNULEE");
        return bonCommandeRepository.save(bc);
    }

    @Override
    public BonCommande getBonCommandeById(Long id) {
        return bonCommandeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Bon de commande non trouvé avec l'ID: " + id));
    }

    @Override
    public Page<BonCommande> getAllBonCommandes(Pageable pageable) {
        return bonCommandeRepository.findAll(pageable);
    }

    @Override
    public Page<BonCommande> getBonCommandesByStatut(String statut, Pageable pageable) {
        return bonCommandeRepository.findByStatut(statut, pageable);
    }

    @Override
    public List<BonCommande> getBonCommandesEnCours() {
        return bonCommandeRepository.findCommandesEnCours();
    }

    @Override
    public BonCommande genererReference(BonCommande bonCommande) {
        String annee = String.valueOf(LocalDateTime.now().getYear());
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String reference = "BC-" + annee + "-" + uuid;
        bonCommande.setReference(reference);
        return bonCommande;
    }

    @Override
    public void verifierSeparationTaches(BonCommande bonCommande, Utilisateur validateur) throws BusinessException {
        if (bonCommande.getDemandeAchat() != null && bonCommande.getDemandeAchat().getDemandeur() != null) {
            if (bonCommande.getDemandeAchat().getDemandeur().getId().equals(validateur.getId())) {
                throw new BusinessException("Le validateur ne peut pas être le même que le demandeur");
            }
        }
    }
}
