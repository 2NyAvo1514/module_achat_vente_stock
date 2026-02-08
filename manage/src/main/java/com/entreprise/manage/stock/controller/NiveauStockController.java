package com.entreprise.manage.stock.controller;

import com.entreprise.manage.stock.service.NiveauStockService;
import com.entreprise.manage.stock.model.NiveauStock;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/stock/niveaux")
@AllArgsConstructor
public class NiveauStockController {

    private final NiveauStockService niveauStockService;

    @GetMapping
    public String listNiveaux(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long depotId,
            Model model) {

        if (depotId == null) {
            model.addAttribute("error", "Veuillez sélectionner un dépôt");
            return "stock/niveaux/liste";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<NiveauStock> niveaux = niveauStockService.getNiveauxByDepot(depotId, pageable);

        model.addAttribute("niveaux", niveaux);
        model.addAttribute("depotId", depotId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", niveaux.getTotalPages());

        return "stock/niveaux/liste";
    }

    @GetMapping("/alertes/{depotId}")
    public String listAlertes(@PathVariable Long depotId, Model model) {
        List<NiveauStock> alertes = niveauStockService.getArticlesEnAlerte(depotId);
        model.addAttribute("alertes", alertes);
        model.addAttribute("depotId", depotId);
        model.addAttribute("type", "ALERTE_MIN");
        return "stock/niveaux/alertes";
    }

    @GetMapping("/surstock/{depotId}")
    public String listSurstock(@PathVariable Long depotId, Model model) {
        List<NiveauStock> surstock = niveauStockService.getArticlesEnSurstock(depotId);
        model.addAttribute("articles", surstock);
        model.addAttribute("depotId", depotId);
        model.addAttribute("type", "SURSTOCK");
        return "stock/niveaux/surstock";
    }

    @GetMapping("/{articleId}/{depotId}")
    public String detail(@PathVariable Long articleId, @PathVariable Long depotId, Model model) {
        niveauStockService.getNiveauStock(articleId, depotId).ifPresent(niveau -> {
            model.addAttribute("niveau", niveau);
        });
        return "stock/niveaux/detail";
    }
}
