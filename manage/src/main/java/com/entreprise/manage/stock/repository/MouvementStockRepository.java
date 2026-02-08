package com.entreprise.manage.stock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entreprise.manage.stock.model.MouvementStock;
import com.entreprise.manage.stock.model.StatutMouvement;
import com.entreprise.manage.stock.model.TypeMouvement;
import java.time.LocalDateTime;
import java.util.List;

public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

    Page<MouvementStock> findByArticleId(Long articleId, Pageable pageable);

    Page<MouvementStock> findByDepotDestinationId(Long depotId, Pageable pageable);

    List<MouvementStock> findByTypeMouvementAndStatut(TypeMouvement type, StatutMouvement statut);

    List<MouvementStock> findByNumeroReference(String numeroReference);

    @Query("SELECT m FROM MouvementStock m WHERE m.dateCreation BETWEEN :debut AND :fin " +
            "ORDER BY m.dateCreation DESC")
    Page<MouvementStock> findMouvementsBetweenDates(
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin,
            Pageable pageable);

    @Query("SELECT m FROM MouvementStock m WHERE m.article.id = :articleId " +
            "AND m.depotDestination.id = :depotId ORDER BY m.dateCreation DESC")
    List<MouvementStock> findMouvementsArticleDepot(
            @Param("articleId") Long articleId,
            @Param("depotId") Long depotId);
}
