package com.gabrielferreira02.expensesManager.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String health() {
        return "The application is running";
    }


}
