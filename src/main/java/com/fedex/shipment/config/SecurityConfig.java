package com.fedex.shipment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // Allow CORS preflight requests without authentication
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Public endpoints - no authentication required
                .requestMatchers("/", "/login", "/login/**", "/auth/**", "/oauth2/**").permitAll()
                // Secure the shipment API endpoint - requires authentication
                .requestMatchers("/api/shipment/**").authenticated()
                // Secure other API endpoints
                .requestMatchers("/api/**").authenticated()
                // Allow everything else without authentication
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                // After successful Google login, redirect back to the Angular app
                .defaultSuccessUrl("http://localhost:4200/", true)
                .authorizationEndpoint(auth -> auth
                    .baseUri("/oauth2/authorize")
                )
                .redirectionEndpoint(redirect -> redirect
                    .baseUri("/login/oauth2/code/*")
                )
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                // Return HTTP 200 instead of redirect so Angular SPA can handle navigation
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            )
            // Session configuration
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionConcurrency(concurrency -> concurrency
                    .maximumSessions(1)
                    .expiredUrl("/login?expired")
                )
            )
            // Disable CSRF for API endpoints (stateless REST calls)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/logout")
            );

        return http.build();
    }
}

