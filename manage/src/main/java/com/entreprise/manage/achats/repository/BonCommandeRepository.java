package com.entreprise.manage.achats.repository;

import com.entreprise.manage.achats.model.BonCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BonCommandeRepository extends JpaRepository<BonCommande, Long> {

    Optional<BonCommande> findByReference(String reference);
}
