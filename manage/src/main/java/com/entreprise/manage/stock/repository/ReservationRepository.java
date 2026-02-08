package com.entreprise.manage.stock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entreprise.manage.stock.model.Reservation;
import com.entreprise.manage.stock.model.StatutReservation;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByReferenceCommande(String referenceCommande);

    List<Reservation> findByArticleIdAndDepotIdAndStatut(
            Long articleId,
            Long depotId,
            StatutReservation statut);

    @Query("SELECT r FROM Reservation r WHERE r.article.id = :articleId " +
            "AND r.depot.id = :depotId " +
            "AND r.statut = 'ACTIVE' " +
            "ORDER BY r.dateReservation ASC")
    List<Reservation> findReservationsActives(
            @Param("articleId") Long articleId,
            @Param("depotId") Long depotId);

    @Query("SELECT SUM(r.quantiteReservee - r.quantitePrelevee) FROM Reservation r " +
            "WHERE r.article.id = :articleId " +
            "AND r.depot.id = :depotId " +
            "AND r.statut IN ('ACTIVE', 'PRELEVEE')")
    Long getTotalReservationsActives(
            @Param("articleId") Long articleId,
            @Param("depotId") Long depotId);

    Page<Reservation> findByStatut(StatutReservation statut, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.dateExpiration < CURRENT_TIMESTAMP " +
            "AND r.statut = 'ACTIVE'")
    List<Reservation> findReservationsExpirees();
}
