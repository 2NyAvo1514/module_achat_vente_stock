package com.entreprise.manage.achats.controller;

import com.entreprise.manage.achats.dto.DemandeAchatForm;
import com.entreprise.manage.achats.service.DemandeAchatService;
import com.entreprise.manage.referentiels.service.ArticleService;
import com.entreprise.manage.referentiels.service.SiteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/achats/demandes")
public class DemandeAchatController {

    private final DemandeAchatService demandeService;
    private final SiteService siteService;
    private final ArticleService articleService;

    public DemandeAchatController(DemandeAchatService demandeService,
            SiteService siteService,
            ArticleService articleService) {
        this.demandeService = demandeService;
        this.siteService = siteService;
        this.articleService = articleService;
    }

    // -----------------------------
    // LISTE
    // -----------------------------
    @GetMapping
    public String lister(Model model) {
        model.addAttribute("demandes", demandeService.listerToutes());
        return "achats/demandes/liste";
    }

    // -----------------------------
    // FORMULAIRE CREATION
    // -----------------------------
    @GetMapping("/nouvelle")
    public String nouvelle(Model model) {
        model.addAttribute("form", new DemandeAchatForm());
        model.addAttribute("sites", siteService.listerTous());
        model.addAttribute("articles", articleService.listerTous());
        return "achats/demandes/nouvelle-demande";
    }

    // -----------------------------
    // CREATION
    // -----------------------------
    @PostMapping("/creer")
    public String creer(@Valid @ModelAttribute("form") DemandeAchatForm form,
            BindingResult result,
            @RequestParam String action,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("sites", siteService.listerTous());
            model.addAttribute("articles", articleService.listerTous());
            return "achats/demandes/nouvelle-demande";
        }

        try {
            if ("soumettre".equals(action)) {
                demandeService.creerEtSoumettre(form);
            } else {
                demandeService.creerBrouillon(form);
            }
        } catch (IllegalStateException e) {
            model.addAttribute("erreurMetier", e.getMessage());
            model.addAttribute("sites", siteService.listerTous());
            model.addAttribute("articles", articleService.listerTous());
            return "achats/demandes/nouvelle-demande";
        }

        return "redirect:/achats/demandes";
    }

    // -----------------------------
    // DETAIL / VALIDATION
    // -----------------------------
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("articles", articleService.listerTous());
        model.addAttribute("demande", demandeService.trouverParId(id));
        return "achats/demandes/detail";
    }

    @PostMapping("/{id}/valider")
    public String valider(@PathVariable Long id) {
        demandeService.valider(id);
        return "redirect:/achats/demandes";
    }
}
