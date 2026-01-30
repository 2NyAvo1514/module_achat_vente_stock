package com.entreprise.manage.referentiels.repository;

import com.entreprise.manage.referentiels.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
