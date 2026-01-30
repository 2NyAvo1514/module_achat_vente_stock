package com.entreprise.manage.achats.service.impl;

import com.entreprise.manage.achats.dto.DemandeAchatForm;
import com.entreprise.manage.achats.model.*;
import com.entreprise.manage.achats.repository.DemandeAchatRepository;
import com.entreprise.manage.achats.service.DemandeAchatService;
import com.entreprise.manage.referentiels.model.Article;
import com.entreprise.manage.referentiels.model.Site;
import com.entreprise.manage.referentiels.repository.ArticleRepository;
import com.entreprise.manage.referentiels.repository.SiteRepository;
import com.entreprise.manage.core.auth.model.Utilisateur;
import com.entreprise.manage.core.auth.repository.UtilisateurRepository;
import com.entreprise.manage.achats.enums.StatutDemandeAchat;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DemandeAchatServiceImpl implements DemandeAchatService {

    private final DemandeAchatRepository demandeRepo;
    private final ArticleRepository articleRepo;
    private final SiteRepository siteRepo;
    private final UtilisateurRepository utilisateurRepo;

    public DemandeAchatServiceImpl(DemandeAchatRepository demandeRepo,
            ArticleRepository articleRepo,
            SiteRepository siteRepo,
            UtilisateurRepository utilisateurRepo) {
        this.demandeRepo = demandeRepo;
        this.articleRepo = articleRepo;
        this.siteRepo = siteRepo;
        this.utilisateurRepo = utilisateurRepo;
    }

    @Override
    public DemandeAchat creerBrouillon(DemandeAchatForm form) {
        return creer(form, false);
    }

    @Override
    public DemandeAchat creerEtSoumettre(DemandeAchatForm form) {
        return creer(form, true);
    }

    private DemandeAchat creer(DemandeAchatForm form, boolean soumettre) {

        if (form.getLignes().isEmpty()) {
            throw new IllegalStateException("La demande doit contenir au moins une ligne.");
        }

        Site site = siteRepo.findById(form.getSiteId())
                .orElseThrow(() -> new IllegalStateException("Site introuvable"));

        Utilisateur demandeur = utilisateurRepo.utilisateurCourant();

        DemandeAchat da = new DemandeAchat();
        da.setReference(genererReference());
        da.setSite(site);
        da.setDemandeur(demandeur);
        da.setDateCreation(LocalDateTime.now());
        da.setStatut(soumettre ? "SOUMISE" : "BROUILLON");

        BigDecimal total = BigDecimal.ZERO;

        for (var l : form.getLignes()) {
            Article article = articleRepo.findById(l.getArticleId())
                    .orElseThrow(() -> new IllegalStateException("Article introuvable"));

            if (l.getQuantite().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException("Quantité invalide pour " + article.getLibelle());
            }

            LigneDemandeAchat ligne = new LigneDemandeAchat();
            ligne.setArticle(article);
            ligne.setQuantite(l.getQuantite());
            ligne.setPrixUnitaire(l.getPrixUnitaire());
            ligne.setTotalLigne(l.getQuantite().multiply(l.getPrixUnitaire()));
            ligne.setDemandeAchat(da);

            da.getLignes().add(ligne);
            total = total.add(ligne.getTotalLigne());
        }

        da.setMontantTotal(total);
        return demandeRepo.save(da);
    }

    @Override
    public void valider(Long id) {
        DemandeAchat da = trouverParId(id);

        Utilisateur valideur = utilisateurRepo.utilisateurCourant();

        if (da.getDemandeur().getId().equals(valideur.getId())) {
            throw new IllegalStateException("Vous ne pouvez pas valider votre propre demande.");
        }

        if (!da.getStatut().equals("SOUMISE")) {
            throw new IllegalStateException("Seule une demande soumise peut être validée.");
        }

        da.setStatut("VALIDEE");
        da.setDateValidation(LocalDateTime.now());
        da.setValidePar(valideur);
        demandeRepo.save(da);
    }

    @Override
    public List<DemandeAchat> listerToutes() {
        return demandeRepo.findAll();
    }

    @Override
    public DemandeAchat trouverParId(Long id) {
        return demandeRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Demande introuvable"));
    }

    private String genererReference() {
        return "DA-" + System.currentTimeMillis();
    }
    
    @Override
    public DemandeAchat getById(Long id) {
        return demandeRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Demande introuvable pour id=" + id));
    }

}
