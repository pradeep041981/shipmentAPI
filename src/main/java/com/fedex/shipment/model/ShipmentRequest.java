package com.fedex.shipment.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequest {
    @NotBlank(message = "Shipment ID is required")
    private String shipmentId;
    private String shipmentType;
    private String templateType;
    private List<String> carriers;
    private List<String> attributes;
    private String comments;
}

