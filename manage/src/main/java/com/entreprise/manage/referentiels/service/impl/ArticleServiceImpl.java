package com.entreprise.manage.referentiels.service.impl;

import com.entreprise.manage.referentiels.model.Article;
import com.entreprise.manage.referentiels.repository.ArticleRepository;
import com.entreprise.manage.referentiels.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository repo;

    public ArticleServiceImpl(ArticleRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Article> listerTous() {
        return repo.findAll();
    }

    @Override
    public Article trouverParId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Article introuvable"));
    }
}
