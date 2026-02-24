package com.fedex.shipment.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    private final String EMPTY_STRING = "";

    public boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
