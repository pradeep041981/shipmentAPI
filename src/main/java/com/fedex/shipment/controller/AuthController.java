package com.fedex.shipment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> principal = extractUserClaims(authentication);

        if (principal != null) {
            response.put("authenticated", true);
            response.put("message", "Welcome to Shipment API");
            response.put("user", principal);
        } else {
            response.put("authenticated", false);
            response.put("message", "UI handles social login. Send a Bearer token to access protected APIs.");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Social login is handled by the UI (Okta OpenID Connect).");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/me")
    public ResponseEntity<Map<String, Object>> currentUser(Authentication authentication) {
        Map<String, Object> principal = extractUserClaims(authentication);
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        Map<String, Object> userInfo = new HashMap<>(principal);
        userInfo.put("authenticated", true);

        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/auth/status")
    public ResponseEntity<Map<String, Object>> loginStatus(Authentication authentication) {
        Map<String, Object> status = new HashMap<>();
        Map<String, Object> principal = extractUserClaims(authentication);

        if (principal != null) {
            status.put("authenticated", true);
            status.put("name",  principal.get("name"));
            status.put("email", principal.get("email"));
        } else {
            status.put("authenticated", false);
        }
        return ResponseEntity.ok(status);
    }

    private Map<String, Object> extractUserClaims(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            Map<String, Object> claims = new HashMap<>(jwt.getClaims());
            claims.putIfAbsent("sub", jwt.getSubject());
            claims.putIfAbsent("email", claims.getOrDefault("email", jwt.getSubject()));
            return claims;
        }

        if (principal instanceof Map<?, ?>) {
            Map<?, ?> mapPrincipal = (Map<?, ?>) principal;
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<?, ?> entry : mapPrincipal.entrySet()) {
                if (entry.getKey() instanceof String) {
                    String key = (String) entry.getKey();
                    map.put(key, entry.getValue());
                }
            }
            return map;
        }

        return null;
    }
}
