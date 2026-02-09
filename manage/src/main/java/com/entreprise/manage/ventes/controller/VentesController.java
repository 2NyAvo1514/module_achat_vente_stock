package com.entreprise.manage.ventes.controller;

import com.entreprise.manage.ventes.model.CommandeClient;
import com.entreprise.manage.ventes.model.LigneCommande;
import com.entreprise.manage.ventes.service.VentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/ventes")
public class VentesController {

    @Autowired
    private VentesService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("commandes", service.listAll());
        return "ventes/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        return "ventes/form";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam String client,
            @RequestParam(required = false) String reference,
            @RequestParam String produit,
            @RequestParam Integer quantite,
            @RequestParam Double prixUnitaire
    ) {
        CommandeClient cmd = new CommandeClient();
        cmd.setClient(client);
        cmd.setReference(reference != null && !reference.isEmpty() ? reference : "CMD-" + System.currentTimeMillis());
        LigneCommande ligne = new LigneCommande();
        ligne.setProduit(produit);
        ligne.setQuantite(quantite);
        ligne.setPrixUnitaire(prixUnitaire);
        cmd.setLignes(Arrays.asList(ligne));
        service.createCommande(cmd);
        return "redirect:/ventes";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("commande", service.get(id));
        return "ventes/view";
    }
}
