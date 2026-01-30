package com.entreprise.manage.referentiels.repository;

import com.entreprise.manage.referentiels.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
}
