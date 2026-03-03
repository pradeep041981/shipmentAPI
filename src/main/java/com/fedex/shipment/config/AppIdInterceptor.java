package com.fedex.shipment.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AppIdInterceptor implements HandlerInterceptor {

    private static final String APP_ID_HEADER = "X-App-Id";
    private static final String ALLOWED_APP_ID = "shipmentUI";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Skip validation for CORS preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String appId = request.getHeader(APP_ID_HEADER);

        if (!ALLOWED_APP_ID.equals(appId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid appId");
            return false;
        }

        return true;
    }
}
