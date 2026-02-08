package com.entreprise.manage.stock.controller;

import com.entreprise.manage.stock.service.LotStockService;
import com.entreprise.manage.stock.model.LotStock;
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
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/stock/lots")
@AllArgsConstructor
public class LotStockController {

    private final LotStockService lotStockService;
    private final ArticleService articleService;
    private final DepotService depotService;

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long depotId,
            Model model) {

        List<Depot> depots = depotService.findAll();
        model.addAttribute("depots", depots);

        if (depotId != null) {
            Page<LotStock> lots = lotStockService.getLotsByDepot(depotId, PageRequest.of(page, size));
            model.addAttribute("lots", lots);
            model.addAttribute("depotId", depotId);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", lots.getTotalPages());
        }

        return "stock/lots/liste";
    }

    @GetMapping("/alertes/{depotId}")
    public String listAlertes(@PathVariable Long depotId, Model model) {
        List<LotStock> alertes = lotStockService.getLotsEnAlerte(depotId);
        model.addAttribute("lots", alertes);
        model.addAttribute("depotId", depotId);
        model.addAttribute("type", "ALERTES");
        return "stock/lots/alertes";
    }

    @GetMapping("/expires/{depotId}")
    public String listExpires(@PathVariable Long depotId, Model model) {
        List<LotStock> expires = lotStockService.getLotsExpires(depotId);
        model.addAttribute("lots", expires);
        model.addAttribute("depotId", depotId);
        model.addAttribute("type", "EXPIRES");
        return "stock/lots/expires";
    }

    @GetMapping("/nouveau")
    public String newLot(Model model) {
        List<Article> articles = articleService.listerTous();
        List<Depot> depots = depotService.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("depots", depots);
        return "stock/lots/form";
    }

    @PostMapping("/creer")
    public String creerLot(
            @RequestParam Long articleId,
            @RequestParam Long depotId,
            @RequestParam String numerolot,
            @RequestParam BigDecimal quantite,
            @RequestParam BigDecimal prixUnitaire,
            @RequestParam(required = false) LocalDate dateExpiration,
            Model model) {

        try {
            LotStock lot = lotStockService.creerLot(
                    articleId, depotId, numerolot, quantite, prixUnitaire, dateExpiration);
            return "redirect:/stock/lots/" + lot.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "stock/lots/form";
        }
    }

    @PostMapping("/{id}/preleve")
    public String preleveFromLot(
            @PathVariable Long id,
            @RequestParam BigDecimal quantite,
            Model model) {

        try {
            lotStockService.preleveFromLot(id, quantite);
            model.addAttribute("success", "Prélèvement effectué");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/stock/lots/" + id;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        lotStockService.getLotByNumero(id.toString()).ifPresent(lot -> {
            model.addAttribute("lot", lot);
        });
        return "stock/lots/detail";
    }

    @PostMapping("/{depotId}/refresh-statuts")
    public String refreshStatuts(@PathVariable Long depotId) {
        lotStockService.refreshStatuts(depotId);
        return "redirect:/stock/lots?depotId=" + depotId;
    }
}
