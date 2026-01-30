package com.entreprise.manage.achats.service;

import com.entreprise.manage.achats.model.BonCommande;
import com.entreprise.manage.achats.model.LigneBonCommande;
import com.entreprise.manage.achats.repository.BonCommandeRepository;
import com.entreprise.manage.achats.model.DemandeAchat;
import com.entreprise.manage.core.auth.model.Utilisateur;
import com.entreprise.manage.referentiels.model.Fournisseur;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BonCommandeService {

    private final BonCommandeRepository bonCommandeRepository;

    public BonCommandeService(BonCommandeRepository bonCommandeRepository) {
        this.bonCommandeRepository = bonCommandeRepository;
    }

    // -----------------------------
    // CREATION DEPUIS DA
    // -----------------------------

    public BonCommande creerDepuisDemande(DemandeAchat da, Fournisseur fournisseur, Utilisateur createur) {

        if (!da.getStatut().equals("VALIDEE")) {
            throw new IllegalStateException("La DA doit être validée avant conversion en BC.");
        }

        BonCommande bc = new BonCommande();
        bc.setDemandeAchat(da);
        bc.setFournisseur(fournisseur);
        bc.setStatut("BROUILLON");
        bc.setReference("BC-" + System.currentTimeMillis());

        da.getLignes().forEach(ligneDA -> {
            LigneBonCommande ligneBC = new LigneBonCommande();
            ligneBC.setBonCommande(bc);
            ligneBC.setArticle(ligneDA.getArticle());
            ligneBC.setQuantite(ligneDA.getQuantite());
            ligneBC.setPrixUnitaire(ligneDA.getPrixUnitaire());
            bc.getLignes().add(ligneBC);
        });

        bc.recalculerTotal();
        return bonCommandeRepository.save(bc);
    }

    // -----------------------------
    // VALIDATION
    // -----------------------------

    public BonCommande valider(Long bcId, Utilisateur valideur) {
        BonCommande bc = getById(bcId);

        if (!bc.getStatut().equals("BROUILLON")) {
            throw new IllegalStateException("Seuls les BC en brouillon peuvent être validés.");
        }

        bc.setStatut("VALIDE");
        bc.setValidePar(valideur);
        bc.setDateValidation(java.time.LocalDateTime.now());

        return bonCommandeRepository.save(bc);
    }

    // -----------------------------
    // UTILITAIRE
    // -----------------------------

    public BonCommande getById(Long id) {
        return bonCommandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bon de commande introuvable"));
    }

    public List<BonCommande> findAll() {
        return bonCommandeRepository.findAll();
    }
}
