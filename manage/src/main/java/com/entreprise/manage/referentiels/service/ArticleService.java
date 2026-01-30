package com.entreprise.manage.referentiels.service;

import com.entreprise.manage.referentiels.model.Article;
import java.util.List;

public interface ArticleService {
    List<Article> listerTous();

    Article trouverParId(Long id);
}
