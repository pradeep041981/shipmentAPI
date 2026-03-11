package com.fedex.shipment.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores the OAuth2 authorization request in memory (keyed by a random UUID)
 * and keeps only the UUID key in a short-lived HTTP-only cookie.
 * This allows a fully stateless (no HttpSession) OAuth2 login flow.
 */
@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String COOKIE_NAME = "oauth2_auth_request";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    // Short-lived in-memory store only used during the OAuth2 handshake
    private final ConcurrentHashMap<String, OAuth2AuthorizationRequest> store = new ConcurrentHashMap<>();

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String key = getCookieValue(request, COOKIE_NAME);
        return key != null ? store.get(key) : null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeRequest(request, response);
            return;
        }
        String key = UUID.randomUUID().toString();
        store.put(key, authorizationRequest);
        addCookie(response, COOKIE_NAME, key, COOKIE_EXPIRE_SECONDS);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                  HttpServletResponse response) {
        String key = getCookieValue(request, COOKIE_NAME);
        if (key != null) {
            OAuth2AuthorizationRequest authRequest = store.remove(key);
            deleteCookie(response, COOKIE_NAME);
            return authRequest;
        }
        return null;
    }

    private void removeRequest(HttpServletRequest request, HttpServletResponse response) {
        String key = getCookieValue(request, COOKIE_NAME);
        if (key != null) {
            store.remove(key);
        }
        deleteCookie(response, COOKIE_NAME);
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}

