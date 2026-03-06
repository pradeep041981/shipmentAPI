package com.fedex.shipment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public ResponseEntity<Map<String, Object>> home(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> response = new HashMap<>();

        if (principal != null) {
            // User is authenticated
            response.put("authenticated", true);
            response.put("message", "Welcome to Shipment API");
            Map<String, Object> user = new HashMap<>();
            Object name = principal.getAttribute("name");
            Object email = principal.getAttribute("email");
            Object picture = principal.getAttribute("picture");
            if (name != null) user.put("name", name);
            if (email != null) user.put("email", email);
            if (picture != null) user.put("picture", picture);
            response.put("user", user);
        } else {
            // User is not authenticated
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
     * Returns the currently authenticated user's info from Google OAuth2.
     * Accessible at GET /auth/me
     * If not authenticated, returns 401 (handled by Spring Security).
     */
    @GetMapping("/auth/me")
    public ResponseEntity<Map<String, Object>> currentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", principal.getAttribute("name"));
        userInfo.put("email", principal.getAttribute("email"));
        userInfo.put("picture", principal.getAttribute("picture"));
        userInfo.put("sub", principal.getAttribute("sub"));
        userInfo.put("authenticated", true);

        return ResponseEntity.ok(userInfo);
    }

    /**
     * Returns login status.
     * Accessible at GET /auth/status
     */
    @GetMapping("/auth/status")
    public ResponseEntity<Map<String, Object>> loginStatus(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> status = new HashMap<>();
        if (principal != null) {
            status.put("authenticated", true);
            status.put("name", principal.getAttribute("name"));
            status.put("email", principal.getAttribute("email"));
        } else {
            status.put("authenticated", false);
        }
        return ResponseEntity.ok(status);
    }
}

