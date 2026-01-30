package com.entreprise.manage.achats.controller;

// import com.entreprise.manage.achats.model.BonCommande;
import com.entreprise.manage.achats.service.BonCommandeService;
import com.entreprise.manage.achats.service.DemandeAchatService;
import com.entreprise.manage.core.auth.model.Utilisateur;
import com.entreprise.manage.referentiels.repository.FournisseurRepository;
import com.entreprise.manage.core.auth.repository.UtilisateurRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/achats/commandes")
public class BonCommandeController {

    private final BonCommandeService bonCommandeService;
    private final DemandeAchatService demandeAchatService;
    private final FournisseurRepository fournisseurRepository;
    private final UtilisateurRepository utilisateurRepository;

    public BonCommandeController(BonCommandeService bonCommandeService,
            DemandeAchatService demandeAchatService,
            FournisseurRepository fournisseurRepository,
            UtilisateurRepository utilisateurRepository) {
        this.bonCommandeService = bonCommandeService;
        this.demandeAchatService = demandeAchatService;
        this.fournisseurRepository = fournisseurRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("commandes", bonCommandeService.findAll());
        return "achats/commandes/liste";
    }

    @GetMapping("/creer/{daId}")
    public String creer(@PathVariable Long daId, Model model) {
        model.addAttribute("demande", demandeAchatService.getById(daId));
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        return "achats/commandes/nouvelle-commande";
    }

    @PostMapping("/creer")
    public String creerDepuisDA(@RequestParam Long daId, @RequestParam Long fournisseurId) {
        Utilisateur user = utilisateurRepository.findByNomUtilisateur("acheteur1").orElseThrow();
        bonCommandeService.creerDepuisDemande(
                demandeAchatService.getById(daId),
                fournisseurRepository.findById(fournisseurId).orElseThrow(),
                user);
        return "redirect:/achats/commandes";
    }

    @PostMapping("/{id}/valider")
    public String valider(@PathVariable Long id) {
        Utilisateur user = utilisateurRepository.findByNomUtilisateur("manager1").orElseThrow();
        bonCommandeService.valider(id, user);
        return "redirect:/achats/commandes";
    }
}
