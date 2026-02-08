// src/main/java/com/entreprise/manage/achats/demande-achat/repository/DemandeAchatRepository.java
package com.entreprise.manage.achats.repository;

import com.entreprise.manage.achats.model.DemandeAchat;
import com.entreprise.manage.core.auth.model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DemandeAchatRepository extends JpaRepository<DemandeAchat, Long> {

    Page<DemandeAchat> findByDemandeur(Utilisateur demandeur, Pageable pageable);

    List<DemandeAchat> findByStatut(String statut);

    @Query("SELECT da FROM DemandeAchat da WHERE da.statut IN :statuts")
    Page<DemandeAchat> findByStatuts(@Param("statuts") List<String> statuts, Pageable pageable);

    DemandeAchat findByReference(String reference);

    @Query("SELECT COUNT(da) FROM DemandeAchat da WHERE da.statut = :statut")
    long countByStatut(@Param("statut") String statut);

    @Query("SELECT da FROM DemandeAchat da WHERE da.demandeur.id = :userId AND da.statut = 'SOUMISE'")
    List<DemandeAchat> findDemandesSoumisesParUtilisateur(@Param("userId") Long userId);
}