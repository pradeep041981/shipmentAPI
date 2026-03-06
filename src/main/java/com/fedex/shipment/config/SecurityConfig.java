package com.fedex.shipment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
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
                // After successful login, redirect to home page instead of API
//                .defaultSuccessUrl("/", true)
                .authorizationEndpoint(auth -> auth
                    .baseUri("/oauth2/authorize")
                )
                .redirectionEndpoint(redirect -> redirect
                    .baseUri("/login/oauth2/code/*")
                )
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
//                .logoutSuccessUrl("/")
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
                .ignoringRequestMatchers("/api/**")
            );

        return http.build();
    }
}

