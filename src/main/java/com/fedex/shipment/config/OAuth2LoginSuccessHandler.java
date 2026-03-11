package com.fedex.shipment.config;

import com.fedex.shipment.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Called after a successful Google OAuth2 login.
 * Generates a signed JWT containing the user's profile claims and
 * redirects the browser to the Angular app with the token as a query parameter.
 * No HttpSession is created.
 */
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String ANGULAR_CALLBACK_URL = "http://localhost:4200/auth/callback";

    private final JwtUtil jwtUtil;

    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauthToken.getPrincipal();

        // Build JWT claims from Google user attributes
        Map<String, Object> claims = new HashMap<>();
        putIfPresent(claims, "name",    oauth2User.getAttribute("name"));
        putIfPresent(claims, "email",   oauth2User.getAttribute("email"));
        putIfPresent(claims, "picture", oauth2User.getAttribute("picture"));
        putIfPresent(claims, "sub",     oauth2User.getAttribute("sub"));

        String email = oauth2User.getAttribute("email");
        String token = jwtUtil.generateToken(email != null ? email : "unknown", claims);

        // Redirect to Angular callback route with the JWT
        getRedirectStrategy().sendRedirect(request, response,
                ANGULAR_CALLBACK_URL + "?token=" + token);
    }

    private void putIfPresent(Map<String, Object> map, String key, Object value) {
        if (value != null) map.put(key, value);
    }
}

