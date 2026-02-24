package com.fedex.shipment.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponse {
    private String shipmentId;
    private String origin;
    private String destination;
    private String trackStatus;
}

