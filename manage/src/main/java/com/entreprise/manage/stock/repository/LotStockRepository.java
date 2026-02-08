package com.entreprise.manage.stock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entreprise.manage.stock.model.LotStock;
import com.entreprise.manage.stock.model.StatutLot;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotStockRepository extends JpaRepository<LotStock, Long> {

    Optional<LotStock> findByNumerolot(String numerolot);

    List<LotStock> findByArticleIdAndDepotId(Long articleId, Long depotId);

    @Query("SELECT l FROM LotStock l WHERE l.depot.id = :depotId " +
            "AND l.statut = 'ALERTE_PEREMPTION' " +
            "ORDER BY l.dateExpiration ASC")
    List<LotStock> findLotsEnAlertePetemption(@Param("depotId") Long depotId);

    @Query("SELECT l FROM LotStock l WHERE l.depot.id = :depotId " +
            "AND l.dateExpiration < CURRENT_DATE " +
            "AND l.statut != 'UTILISE' " +
            "ORDER BY l.dateExpiration ASC")
    List<LotStock> findLotsExpires(@Param("depotId") Long depotId);

    @Query("SELECT l FROM LotStock l WHERE l.article.id = :articleId " +
            "AND l.depot.id = :depotId " +
            "AND l.statut = 'ACTIF' " +
            "AND l.quantiteDisponible > 0 " +
            "ORDER BY l.dateProduction ASC") // FIFO
    List<LotStock> findLotsDisponiblesFIFO(
            @Param("articleId") Long articleId,
            @Param("depotId") Long depotId);

    @Query("SELECT l FROM LotStock l WHERE l.article.id = :articleId " +
            "AND l.depot.id = :depotId " +
            "AND l.statut = 'ACTIF' " +
            "AND l.quantiteDisponible > 0 " +
            "ORDER BY l.dateExpiration ASC") // FEFO
    List<LotStock> findLotsDisponiblesFEFO(
            @Param("articleId") Long articleId,
            @Param("depotId") Long depotId);

    Page<LotStock> findByDepotId(Long depotId, Pageable pageable);

    @Query("SELECT COUNT(l) FROM LotStock l WHERE l.depot.id = :depotId " +
            "AND l.dateExpiration IS NOT NULL " +
            "AND l.dateExpiration BETWEEN :debut AND :fin")
    long countExpirationsInRange(
            @Param("depotId") Long depotId,
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);
}
