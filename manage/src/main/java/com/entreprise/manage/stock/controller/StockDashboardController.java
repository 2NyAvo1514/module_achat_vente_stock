package com.entreprise.manage.stock.controller;

import com.entreprise.manage.stock.service.NiveauStockService;
import com.entreprise.manage.stock.service.LotStockService;
import com.entreprise.manage.stock.service.ReservationService;
import com.entreprise.manage.referentiels.service.DepotService;
import com.entreprise.manage.referentiels.model.Depot;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/stock")
@AllArgsConstructor
public class StockDashboardController {

    private final NiveauStockService niveauStockService;
    private final LotStockService lotStockService;
    private final ReservationService reservationService;
    private final DepotService depotService;

    @GetMapping
    public String dashboard(@RequestParam(required = false) Long depotId, Model model) {
        List<Depot> depots = depotService.findAll();
        model.addAttribute("depots", depots);

        if (depotId != null) {
            // Articles en alerte (stock minimum)
            model.addAttribute("alertesMin", niveauStockService.getArticlesEnAlerte(depotId));

            // Articles en surstock
            model.addAttribute("surstock", niveauStockService.getArticlesEnSurstock(depotId));

            // Lots en alerte péremption
            model.addAttribute("lotsAlerte", lotStockService.getLotsEnAlerte(depotId));

            // Lots expirés
            model.addAttribute("lotsExpires", lotStockService.getLotsExpires(depotId));

            // Réservations expirées
            model.addAttribute("reservationsExpirees", reservationService.getReservationsExpirees());

            model.addAttribute("depotId", depotId);
        }

        return "stock/dashboard";
    }
}
