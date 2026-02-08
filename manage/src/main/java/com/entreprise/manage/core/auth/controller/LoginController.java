package com.entreprise.manage.core.auth.controller;

// src/main/java/com/entreprise/manage/auth/LoginController.java
// package com.entreprise.manage.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/achats/demandes";
    }
}
