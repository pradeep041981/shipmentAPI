package com.fedex.shipment.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequest {
    private String shipmentId;
    private String shipmentType;
    private List<String> attributes;
    private String comments;
}

