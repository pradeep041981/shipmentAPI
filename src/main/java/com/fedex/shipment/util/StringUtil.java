package com.fedex.shipment.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {

    public boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
