package com.fedex.shipment.controller;

import com.fedex.shipment.model.ShipmentRequest;
import com.fedex.shipment.model.ShipmentResponse;
import com.fedex.shipment.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/shipment")
@CrossOrigin(origins = "*")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @PostMapping
    public List<ShipmentResponse> trackShipment(@RequestBody ShipmentRequest request) {
        return shipmentService.getShipments(request);
    }
}

