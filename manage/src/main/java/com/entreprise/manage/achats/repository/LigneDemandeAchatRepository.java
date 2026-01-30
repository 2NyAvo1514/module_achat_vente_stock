package com.entreprise.manage.achats.repository;

import com.entreprise.manage.achats.model.LigneDemandeAchat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneDemandeAchatRepository extends JpaRepository<LigneDemandeAchat, Long> {
}
