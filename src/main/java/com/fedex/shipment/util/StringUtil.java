package com.fedex.shipment.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {

    public boolean isValid(String str) {
        //check if the string is alphanumeric or numeric, if not return false
        return str.matches("^[a-zA-Z0-9]+$");
    }
}
