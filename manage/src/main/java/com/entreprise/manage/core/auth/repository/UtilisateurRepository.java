package com.entreprise.manage.core.auth.repository;

import com.entreprise.manage.core.auth.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByNomUtilisateur(String username);
    @Query("select u from Utilisateur u where u.nomUtilisateur = 'acheteur1'")
    Utilisateur utilisateurCourant();
}
