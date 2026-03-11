package com.fedex.shipment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    /**
     * Root endpoint that handles the redirect after OAuth2 login.
     * Accessible at GET /
     * Returns the currently authenticated user's info if logged in.
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
            response.put("authenticated", true);
            response.put("message", "Welcome to Shipment API");
            response.put("user", principal);
        } else {
            response.put("authenticated", false);
            response.put("message", "Please log in to access the API");
            response.put("login_url", "/oauth2/authorize/google");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Initiates Google OAuth2 login flow.
     * Accessible at GET /auth/login
     * Redirects to Google's OAuth2 authorization endpoint
     */
    @GetMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Redirect to /oauth2/authorize/google to initiate OAuth2 login");
        response.put("redirect_url", "/oauth2/authorize/google");
        return ResponseEntity.ok(response);
    }

    /**
     * Returns the currently authenticated user's info extracted from the JWT.
     * Accessible at GET /auth/me
     * If not authenticated, returns 401 (handled by Spring Security).
     */
    @GetMapping("/auth/me")
    public ResponseEntity<Map<String, Object>> currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();

        Map<String, Object> userInfo = new HashMap<>(principal);
        userInfo.put("authenticated", true);

        return ResponseEntity.ok(userInfo);
    }

    /**
     * Returns login status based on JWT presence.
     * Accessible at GET /auth/status
     */
    @GetMapping("/auth/status")
    public ResponseEntity<Map<String, Object>> loginStatus(Authentication authentication) {
        Map<String, Object> status = new HashMap<>();
        if (authentication != null && authentication.isAuthenticated()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
            status.put("authenticated", true);
            status.put("name",  principal.get("name"));
            status.put("email", principal.get("email"));
        } else {
            status.put("authenticated", false);
        }
        return ResponseEntity.ok(status);
    }
}
