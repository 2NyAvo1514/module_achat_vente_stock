// src/main/java/com/entreprise/manage/achats/commandes/controller/BonCommandeController.java
package com.entreprise.manage.achats.controller;

import com.entreprise.manage.achats.model.BonCommande;
import com.entreprise.manage.achats.service.BonCommandeService;
import com.entreprise.manage.achats.model.DemandeAchat;
import com.entreprise.manage.achats.service.DemandeAchatService;
import com.entreprise.manage.core.auth.SecurityUtil;
import com.entreprise.manage.core.auth.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/achats/commandes")
public class BonCommandeController {

    private final BonCommandeService bonCommandeService;
    private final DemandeAchatService demandeAchatService;
    private final SecurityUtil securityUtil;

    public BonCommandeController(BonCommandeService bonCommandeService,
            DemandeAchatService demandeAchatService,
            SecurityUtil securityUtil) {
        this.bonCommandeService = bonCommandeService;
        this.demandeAchatService = demandeAchatService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public String listCommandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String statut,
            Model model) {

        Page<BonCommande> commandesPage;
        if (statut != null && !statut.isEmpty()) {
            commandesPage = bonCommandeService.getBonCommandesByStatut(
                    statut, PageRequest.of(page, size));
        } else {
            commandesPage = bonCommandeService.getAllBonCommandes(
                    PageRequest.of(page, size));
        }

        model.addAttribute("commandes", commandesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", commandesPage.getTotalPages());
        model.addAttribute("totalItems", commandesPage.getTotalElements());
        model.addAttribute("statut", statut);

        return "achats/commandes/liste";
    }

    @GetMapping("/creer/{demandeId}")
    public String showCreateFormFromDemande(@PathVariable Long demandeId, Model model) {
        DemandeAchat demande = demandeAchatService.getDemandeById(demandeId);

        if (!"VALIDEE".equals(demande.getStatut())) {
            model.addAttribute("error",
                    "Seules les demandes VALIDEES peuvent être transformées en commande");
            return "redirect:/achats/demandes/" + demandeId;
        }

        BonCommande bonCommande = new BonCommande();
        bonCommande.setDemandeAchat(demande);
        // bonCommande.setFournisseur(
        //         demande.getLignes().get(0).getArticle().getFournisseurs().stream().findFirst().orElse(null));

        model.addAttribute("bonCommande", bonCommande);
        model.addAttribute("demande", demande);

        return "achats/commandes/nouvelle";
    }

    @PostMapping("/enregistrer")
    public String enregistrerBonCommande(@ModelAttribute BonCommande bonCommande,
            RedirectAttributes redirectAttributes) {
        try {
            Utilisateur acheteur = securityUtil.getCurrentUser();
            bonCommandeService.creerBonCommande(bonCommande, acheteur);

            redirectAttributes.addFlashAttribute("success",
                    "Bon de commande créé avec succès !");
            return "redirect:/achats/commandes/" + bonCommande.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur: " + e.getMessage());
            return "redirect:/achats/commandes/nouvelle";
        }
    }

    @GetMapping("/{id}")
    public String viewBonCommande(@PathVariable Long id, Model model) {
        BonCommande bonCommande = bonCommandeService.getBonCommandeById(id);
        model.addAttribute("bonCommande", bonCommande);
        return "achats/commandes/detail";
    }

    @PostMapping("/{id}/valider")
    public String validerBonCommande(@PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            Utilisateur validateur = securityUtil.getCurrentUser();
            bonCommandeService.validerBonCommande(id, validateur);

            redirectAttributes.addFlashAttribute("success",
                    "Bon de commande validé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur: " + e.getMessage());
        }
        return "redirect:/achats/commandes/" + id;
    }
}