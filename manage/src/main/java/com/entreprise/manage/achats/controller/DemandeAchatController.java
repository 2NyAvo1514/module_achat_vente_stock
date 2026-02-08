package com.entreprise.manage.achats.controller;

// src/main/java/com/entreprise/manage/achats/demande-achat/controller/DemandeAchatController.java
// package com.entreprise.manage.achats.demandeachat.controller;

import com.entreprise.manage.achats.model.DemandeAchat;
import com.entreprise.manage.achats.model.LigneDemandeAchat;
import com.entreprise.manage.achats.service.DemandeAchatService;
import com.entreprise.manage.core.auth.SecurityUtil;
import com.entreprise.manage.core.auth.model.Utilisateur;
import com.entreprise.manage.referentiels.repository.ArticleRepository;
import com.entreprise.manage.referentiels.repository.SiteRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/achats/demandes")
public class DemandeAchatController {
    
    private final DemandeAchatService demandeAchatService;
    private final ArticleRepository articleRepository;
    private final SiteRepository siteRepository;
    private final SecurityUtil securityUtil;
    
    public DemandeAchatController(DemandeAchatService demandeAchatService, 
                                  SecurityUtil securityUtil,ArticleRepository articleRepository,SiteRepository siteRepository) {
        this.demandeAchatService = demandeAchatService;
        this.securityUtil = securityUtil;
        this.articleRepository = articleRepository;
        this.siteRepository = siteRepository;
    }
    
    @GetMapping
    public String listDemandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String statut,
            Model model) {
        
        Page<DemandeAchat> demandesPage;
        if (statut != null && !statut.isEmpty()) {
            demandesPage = demandeAchatService.getDemandesByStatut(
                statut, PageRequest.of(page, size));
        } else {
            demandesPage = demandeAchatService.getAllDemandes(
                PageRequest.of(page, size));
        }
        
        model.addAttribute("demandes", demandesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", demandesPage.getTotalPages());
        model.addAttribute("totalItems", demandesPage.getTotalElements());
        model.addAttribute("statut", statut);
        
        return "achats/demandes/liste";
    }
    
    @GetMapping("/mes-demandes")
    public String mesDemandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        Utilisateur utilisateur = securityUtil.getCurrentUser();
        Page<DemandeAchat> demandesPage = demandeAchatService.getDemandesByDemandeur(
            utilisateur, PageRequest.of(page, size));
        
        model.addAttribute("demandes", demandesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", demandesPage.getTotalPages());
        model.addAttribute("totalItems", demandesPage.getTotalElements());
        
        return "achats/demandes/mes-demandes";
    }
    
    @GetMapping("/a-valider")
    public String demandesAValider(Model model) {
        List<DemandeAchat> demandes = demandeAchatService.getDemandesEnAttenteValidation();
        model.addAttribute("demandes", demandes);
        return "achats/demandes/a-valider";
    }
    
    @GetMapping("/nouvelle")
    public String showCreateForm(Model model) {
        DemandeAchat demande = new DemandeAchat();
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("sites",siteRepository.findAll());
        model.addAttribute("demande", demande);
        return "achats/demandes/nouvelle-demande";
    }
    
    @PostMapping("/enregistrer")
    public String enregistrerDemande(@ModelAttribute DemandeAchat demande,
                                    RedirectAttributes redirectAttributes) {
        try {
            Utilisateur demandeur = securityUtil.getCurrentUser();
            demandeAchatService.creerDemandeAchat(demande, demandeur);
            
            redirectAttributes.addFlashAttribute("success", 
                "Demande d'achat créée avec succès !");
            return "redirect:/achats/demandes/mes-demandes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erreur lors de la création: " + e.getMessage());
            return "redirect:/achats/demandes/nouvelle-demande";
        }
    }
    
    @GetMapping("/{id}")
    public String viewDemande(@PathVariable Long id, Model model) {
        DemandeAchat demande = demandeAchatService.getDemandeById(id);
        model.addAttribute("demande", demande);
        return "achats/demandes/detail";
    }
    
    @PostMapping("/{id}/soumettre")
    public String soumettreDemande(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            Utilisateur utilisateur = securityUtil.getCurrentUser();
            demandeAchatService.soumettreDemande(id, utilisateur);
            
            redirectAttributes.addFlashAttribute("success", 
                "Demande soumise avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erreur: " + e.getMessage());
        }
        return "redirect:/achats/demandes/" + id;
    }
    
    @PostMapping("/{id}/valider")
    public String validerDemande(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            Utilisateur validateur = securityUtil.getCurrentUser();
            demandeAchatService.validerDemande(id, validateur);
            
            redirectAttributes.addFlashAttribute("success", 
                "Demande validée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erreur: " + e.getMessage());
        }
        return "redirect:/achats/demandes/a-valider";
    }
    
    @PostMapping("/{id}/rejeter")
    public String rejeterDemande(@PathVariable Long id,
                                @RequestParam String motif,
                                RedirectAttributes redirectAttributes) {
        try {
            Utilisateur validateur = securityUtil.getCurrentUser();
            demandeAchatService.rejeterDemande(id, validateur, motif);
            
            redirectAttributes.addFlashAttribute("success", 
                "Demande rejetée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erreur: " + e.getMessage());
        }
        return "redirect:/achats/demandes/a-valider";
    }
    
    @PostMapping("/{id}/ajouter-ligne")
    public String ajouterLigne(@PathVariable Long id,
                              @ModelAttribute LigneDemandeAchat ligne,
                              RedirectAttributes redirectAttributes) {
        try {
            demandeAchatService.ajouterLigneDemande(id, ligne);
            redirectAttributes.addFlashAttribute("success", 
                "Ligne ajoutée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erreur: " + e.getMessage());
        }
        return "redirect:/achats/demandes/" + id;
    }
}