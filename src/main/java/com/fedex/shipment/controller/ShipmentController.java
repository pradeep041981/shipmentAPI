package com.fedex.shipment.controller;

import com.fedex.shipment.model.ShipmentRequest;
import com.fedex.shipment.model.ShipmentResponse;
import com.fedex.shipment.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public List<ShipmentResponse> trackShipment(@Valid @RequestBody ShipmentRequest request) {
        return shipmentService.getShipments(request);
    }
}

