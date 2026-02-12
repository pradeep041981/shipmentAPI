package com.accenture.angularapp.controller;

import com.accenture.angularapp.model.ShipmentRequest;
import com.accenture.angularapp.model.ShipmentResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/shipment")
@CrossOrigin(origins = "*")
public class ShipmentController {

    @PostMapping
    public List<ShipmentResponse> trackShipment(@RequestBody ShipmentRequest request) {
        // Dummy data response
        return Arrays.asList(
            new ShipmentResponse("SHIP001", "New York", "Los Angeles", "In Transit"),
            new ShipmentResponse("SHIP002", "Chicago", "Miami", "Out for Delivery"),
            new ShipmentResponse("SHIP003", "Seattle", "Boston", "Delivered")
        );
    }
}

