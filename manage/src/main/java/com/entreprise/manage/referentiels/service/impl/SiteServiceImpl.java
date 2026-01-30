package com.entreprise.manage.referentiels.service.impl;

import com.entreprise.manage.referentiels.model.Site;
import com.entreprise.manage.referentiels.repository.SiteRepository;
import com.entreprise.manage.referentiels.service.SiteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {

    private final SiteRepository repo;

    public SiteServiceImpl(SiteRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Site> listerTous() {
        return repo.findAll();
    }

    @Override
    public Site trouverParId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Site introuvable"));
    }
}
