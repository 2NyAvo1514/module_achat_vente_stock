package com.entreprise.manage.stock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entreprise.manage.stock.model.NiveauStock;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface NiveauStockRepository extends JpaRepository<NiveauStock, Long> {

        Optional<NiveauStock> findByArticleIdAndDepotId(Long articleId, Long depotId);

        Page<NiveauStock> findByDepotId(Long depotId, Pageable pageable);

        @Query("SELECT n FROM NiveauStock n WHERE n.depot.id = :depotId " +
                        "AND n.quantiteDisponible < n.stockMinimum " +
                        "ORDER BY n.article.code ASC")
        List<NiveauStock> findArticlesEnDessousDuMinimum(@Param("depotId") Long depotId);

        @Query("SELECT n FROM NiveauStock n WHERE n.depot.id = :depotId " +
                        "AND n.quantiteDisponible > n.stockMaximum " +
                        "ORDER BY n.article.code ASC")
        List<NiveauStock> findArticlesEnDessuseDuMaximum(@Param("depotId") Long depotId);

        @Query("SELECT n FROM NiveauStock n WHERE n.depot.id = :depotId " +
                        "AND n.coutMoyenPondere = 0 " +
                        "ORDER BY n.article.code ASC")
        List<NiveauStock> findArticlesSansValeur(@Param("depotId") Long depotId);

        @Query("SELECT n FROM NiveauStock n WHERE n.article.id IN " +
                        "(SELECT a.id FROM Article a WHERE a.categorie.id = :categorieId) " +
                        "AND n.depot.id = :depotId")
        List<NiveauStock> findByCategoriAndDepot(
                        @Param("categorieId") Long categorieId,
                        @Param("depotId") Long depotId);
}
