package com.entreprise.manage.ventes.repository;

import com.entreprise.manage.ventes.model.CommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeClientRepository extends JpaRepository<CommandeClient, Long> {

}
