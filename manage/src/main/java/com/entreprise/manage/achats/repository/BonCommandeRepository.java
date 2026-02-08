// src/main/java/com/entreprise/manage/achats/commandes/repository/BonCommandeRepository.java
package com.entreprise.manage.achats.repository;

import com.entreprise.manage.achats.model.BonCommande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BonCommandeRepository extends JpaRepository<BonCommande, Long> {

    Page<BonCommande> findByStatut(String statut, Pageable pageable);

    BonCommande findByReference(String reference);

    List<BonCommande> findByDemandeAchatId(Long demandeAchatId);

    @Query("SELECT bc FROM BonCommande bc WHERE bc.statut IN ('BROUILLON', 'EN_COURS')")
    List<BonCommande> findCommandesEnCours();

    @Query("SELECT COUNT(bc) FROM BonCommande bc WHERE bc.statut = :statut")
    long countByStatut(@Param("statut") String statut);
}