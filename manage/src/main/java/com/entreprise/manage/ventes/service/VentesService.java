package com.entreprise.manage.ventes.service;

import com.entreprise.manage.ventes.model.CommandeClient;
import com.entreprise.manage.ventes.model.LigneCommande;
import com.entreprise.manage.ventes.repository.CommandeClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VentesService {

    @Autowired
    private CommandeClientRepository repo;

    public CommandeClient createCommande(CommandeClient cmd) {
        if (cmd.getLignes() != null) {
            for (LigneCommande l : cmd.getLignes()) {
                l.setCommande(cmd);
            }
        }
        cmd.setCreatedAt(LocalDateTime.now());
        cmd.setStatus("CREATED");
        cmd.calculateTotal();
        return repo.save(cmd);
    }

    public List<CommandeClient> listAll() {
        return repo.findAll();
    }

    public CommandeClient get(Long id) {
        return repo.findById(id).orElse(null);
    }
}
