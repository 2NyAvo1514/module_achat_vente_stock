package com.entreprise.manage.stock.controller;

import com.entreprise.manage.stock.service.ReservationService;
import com.entreprise.manage.stock.model.Reservation;
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
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/stock/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ArticleService articleService;
    private final DepotService depotService;

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        Page<Reservation> reservations = reservationService.getReservationsByStatut(
                null, PageRequest.of(page, size));
        model.addAttribute("reservations", reservations);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reservations.getTotalPages());

        return "stock/reservations/liste";
    }

    @GetMapping("/expirees")
    public String listExpirees(Model model) {
        List<Reservation> expirees = reservationService.getReservationsExpirees();
        model.addAttribute("reservations", expirees);
        model.addAttribute("type", "EXPIREES");
        return "stock/reservations/expirees";
    }

    @GetMapping("/nouvelle")
    public String newReservation(Model model) {
        List<Article> articles = articleService.listerTous();
        List<Depot> depots = depotService.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("depots", depots);
        return "stock/reservations/form";
    }

    @PostMapping("/creer")
    public String creerReservation(
            @RequestParam Long articleId,
            @RequestParam Long depotId,
            @RequestParam String referenceCommande,
            @RequestParam BigDecimal quantite,
            @RequestParam(required = false) LocalDateTime delaiPrelevement,
            Model model) {

        try {
            if (delaiPrelevement == null) {
                delaiPrelevement = LocalDateTime.now().plusDays(7); // Délai par défaut 7 jours
            }

            Reservation reservation = reservationService.creerReservation(
                    articleId, depotId, referenceCommande, quantite, delaiPrelevement);
            return "redirect:/stock/reservations/" + reservation.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "stock/reservations/form";
        }
    }

    @PostMapping("/{id}/preleve")
    public String preleveReservation(
            @PathVariable Long id,
            @RequestParam BigDecimal quantite,
            Model model) {

        try {
            reservationService.preleveReservation(id, quantite);
            model.addAttribute("success", "Prélèvement effectué");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/stock/reservations/" + id;
    }

    @PostMapping("/{id}/liberer")
    public String libererReservation(@PathVariable Long id, Model model) {
        try {
            reservationService.libereReservation(id);
            model.addAttribute("success", "Réservation libérée");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/stock/reservations/" + id;
    }

    @PostMapping("/{id}/livrer")
    public String livrerReservation(@PathVariable Long id, Model model) {
        try {
            reservationService.marquerLivree(id);
            model.addAttribute("success", "Réservation marquée comme livrée");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/stock/reservations/" + id;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        reservationService.getReservation(id).ifPresent(reservation -> {
            model.addAttribute("reservation", reservation);
        });
        return "stock/reservations/detail";
    }

    @PostMapping("/cancel-expired")
    public String cancelExpired() {
        reservationService.cancelExpired();
        return "redirect:/stock/reservations";
    }
}
