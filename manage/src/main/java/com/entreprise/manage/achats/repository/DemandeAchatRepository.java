package com.entreprise.manage.achats.repository;

import com.entreprise.manage.achats.model.DemandeAchat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DemandeAchatRepository extends JpaRepository<DemandeAchat, Long> {

    Optional<DemandeAchat> findByReference(String reference);
}
