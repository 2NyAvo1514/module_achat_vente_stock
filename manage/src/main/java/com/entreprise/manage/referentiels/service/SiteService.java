package com.entreprise.manage.referentiels.service;

import com.entreprise.manage.referentiels.model.Site;
import java.util.List;

public interface SiteService {
    List<Site> listerTous();

    Site trouverParId(Long id);
}
