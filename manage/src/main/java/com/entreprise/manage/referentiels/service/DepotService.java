package com.entreprise.manage.referentiels.service;

import java.util.List;
import java.util.Optional;

import com.entreprise.manage.referentiels.model.Depot;

public interface DepotService {
    List<Depot> findAll();

    Optional<Depot> findById(Long id);

    Depot save(Depot depot);

    void deleteById(Long id);

    Optional<Depot> findByCode(String code);
}
