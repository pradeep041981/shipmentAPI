package com.fedex.shipment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AppIdInterceptor appIdInterceptor;

    public WebConfig(AppIdInterceptor appIdInterceptor) {
        this.appIdInterceptor = appIdInterceptor;
    }

    // CORS is handled centrally by SecurityConfig.corsConfigurationSource()

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appIdInterceptor)
                .addPathPatterns("/api/**");
    }
}
