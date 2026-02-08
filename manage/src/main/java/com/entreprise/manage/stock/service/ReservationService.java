package com.entreprise.manage.stock.service;

import com.entreprise.manage.stock.model.Reservation;
import com.entreprise.manage.stock.model.StatutReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    /**
     * Crée une réservation de stock pour une commande client
     */
    Reservation creerReservation(Long articleId, Long depotId, String referenceCommande,
            BigDecimal quantite, LocalDateTime delaiPrelevement);

    /**
     * Préléve une partie d'une réservation
     */
    void preleveReservation(Long reservationId, BigDecimal quantite);

    /**
     * Libère une réservation complète
     */
    void libereReservation(Long reservationId);

    /**
     * Marque une réservation comme livrée
     */
    void marquerLivree(Long reservationId);

    /**
     * Récupère les réservations actives pour une commande
     */
    List<Reservation> getReservationsByCommande(String referenceCommande);

    /**
     * Récupère les réservations actives pour un article/dépôt
     */
    List<Reservation> getReservationsActives(Long articleId, Long depotId);

    /**
     * Récupère les réservations expirées
     */
    List<Reservation> getReservationsExpirees();

    /**
     * Calcule la quantité totale réservée
     */
    Long getTotalReserve(Long articleId, Long depotId);

    /**
     * Récupère une réservation
     */
    Optional<Reservation> getReservation(Long id);

    /**
     * Récupère les réservations par statut
     */
    Page<Reservation> getReservationsByStatut(StatutReservation statut, Pageable pageable);

    /**
     * Annule une réservation expirée
     */
    void cancelExpired();

    /**
     * Vérifie si une quantité peut être réservée
     */
    boolean canReserve(Long articleId, Long depotId, BigDecimal quantite);
}
