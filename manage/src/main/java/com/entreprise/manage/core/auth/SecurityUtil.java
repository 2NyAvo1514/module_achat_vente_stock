package com.entreprise.manage.core.auth;

// src/main/java/com/entreprise/manage/core/auth/SecurityUtil.java
// package com.entreprise.manage.core.auth;

import com.entreprise.manage.core.auth.model.Utilisateur;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public Utilisateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Utilisateur) {
            return (Utilisateur) authentication.getPrincipal();
        }
        // Pour les tests, retourner un utilisateur mock
        Utilisateur mockUser = new Utilisateur();
        mockUser.setId(1L);
        mockUser.setNomUtilisateur("acheteur1");
        mockUser.setNomComplet("Acheteur Principal");
        return mockUser;
    }

    public boolean hasRole(String roleCode) {
        Utilisateur user = getCurrentUser();
        return user.getRoles().stream()
                .anyMatch(role -> role.getCode().equals(roleCode));
    }
}
