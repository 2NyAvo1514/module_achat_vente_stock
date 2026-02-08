package com.entreprise.manage.stock.controller;

import com.entreprise.manage.stock.service.MouvementStockService;
import com.entreprise.manage.stock.model.MouvementStock;
import com.entreprise.manage.stock.model.TypeMouvement;
import com.entreprise.manage.referentiels.service.ArticleService;
import com.entreprise.manage.referentiels.service.DepotService;
import com.entreprise.manage.referentiels.model.Article;
import com.entreprise.manage.referentiels.model.Depot;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/stock/mouvements")
@AllArgsConstructor
public class MouvementStockController {

    private final MouvementStockService mouvementStockService;
    private final ArticleService articleService;
    private final DepotService depotService;

    @GetMapping
    public String listMouvements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long depotId,
            @RequestParam(required = false) TypeMouvement type,
            Model model) {

        List<Depot> depots = depotService.findAll();
        model.addAttribute("depots", depots);

        if (depotId != null) {
            Page<MouvementStock> mouvements = mouvementStockService.getMouvementsByDepot(
                    depotId, PageRequest.of(page, size));
            model.addAttribute("mouvements", mouvements);
            model.addAttribute("depotId", depotId);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", mouvements.getTotalPages());
        }

        return "stock/mouvements/liste";
    }

    @GetMapping("/planifiees")
    public String listPlanifiees(Model model) {
        List<MouvementStock> planifiees = mouvementStockService.getMouvementsPlanifiees();
        model.addAttribute("mouvements", planifiees);
        return "stock/mouvements/planifiees";
    }

    @GetMapping("/entrees/nouveau")
    public String newEntree(Model model) {
        List<Article> articles = articleService.listerTous();
        List<Depot> depots = depotService.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("depots", depots);
        return "stock/mouvements/entree-form";
    }

    @PostMapping("/entrees/creer")
    public String creerEntree(
            @RequestParam Long articleId,
            @RequestParam Long depotId,
            @RequestParam BigDecimal quantite,
            @RequestParam BigDecimal prixUnitaire,
            @RequestParam String numeroReference,
            Model model) {

        try {
            MouvementStock mouvement = mouvementStockService.creerEntree(
                    articleId, depotId, quantite, prixUnitaire, numeroReference);
            return "redirect:/stock/mouvements/" + mouvement.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "stock/mouvements/entree-form";
        }
    }

    @GetMapping("/sorties/nouveau")
    public String newSortie(Model model) {
        List<Article> articles = articleService.listerTous();
        List<Depot> depots = depotService.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("depots", depots);
        return "stock/mouvements/sortie-form";
    }

    @PostMapping("/sorties/creer")
    public String creerSortie(
            @RequestParam Long articleId,
            @RequestParam Long depotId,
            @RequestParam BigDecimal quantite,
            @RequestParam String numeroReference,
            Model model) {

        try {
            MouvementStock mouvement = mouvementStockService.creerSortie(
                    articleId, depotId, quantite, numeroReference);
            return "redirect:/stock/mouvements/" + mouvement.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "stock/mouvements/sortie-form";
        }
    }

    @GetMapping("/transferts/nouveau")
    public String newTransfert(Model model) {
        List<Article> articles = articleService.listerTous();
        List<Depot> depots = depotService.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("depots", depots);
        return "stock/mouvements/transfert-form";
    }

    @PostMapping("/transferts/creer")
    public String creerTransfert(
            @RequestParam Long articleId,
            @RequestParam Long depotDepart,
            @RequestParam Long depotDestination,
            @RequestParam BigDecimal quantite,
            @RequestParam String numeroReference,
            Model model) {

        try {
            MouvementStock mouvement = mouvementStockService.creerTransfert(
                    articleId, depotDepart, depotDestination, quantite, numeroReference);
            return "redirect:/stock/mouvements/" + mouvement.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "stock/mouvements/transfert-form";
        }
    }

    @PostMapping("/{id}/executer")
    public String executerMouvement(@PathVariable Long id, Model model) {
        try {
            mouvementStockService.executerMouvement(id);
            model.addAttribute("success", "Mouvement exécuté avec succès");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/stock/mouvements/" + id;
    }

    @PostMapping("/{id}/annuler")
    public String annulerMouvement(@PathVariable Long id) {
        mouvementStockService.annulerMouvement(id);
        return "redirect:/stock/mouvements";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        mouvementStockService.getMouvement(id).ifPresent(mouvement -> {
            model.addAttribute("mouvement", mouvement);
            model.addAttribute("coutTotal", mouvementStockService.calculateCoutTotal(id));
        });
        return "stock/mouvements/detail";
    }
}
