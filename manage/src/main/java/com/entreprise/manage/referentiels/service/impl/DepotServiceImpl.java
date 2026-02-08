package com.entreprise.manage.referentiels.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entreprise.manage.referentiels.model.Depot;
import com.entreprise.manage.referentiels.repository.DepotRepository;
import com.entreprise.manage.referentiels.service.DepotService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DepotServiceImpl implements DepotService {

    private final DepotRepository depotRepository;

    @Override
    public List<Depot> findAll() {
        return depotRepository.findAll();
    }

    @Override
    public Optional<Depot> findById(Long id) {
        return depotRepository.findById(id);
    }

    @Override
    public Depot save(Depot depot) {
        return depotRepository.save(depot);
    }

    @Override
    public void deleteById(Long id) {
        depotRepository.deleteById(id);
    }

    @Override
    public Optional<Depot> findByCode(String code) {
        return depotRepository.findByCode(code);
    }
}
