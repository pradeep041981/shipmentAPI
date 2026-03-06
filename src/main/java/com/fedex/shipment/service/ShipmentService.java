package com.fedex.shipment.service;

import com.fedex.shipment.model.ShipmentRequest;
import com.fedex.shipment.model.ShipmentResponse;
import com.fedex.shipment.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShipmentService {

    private final StringUtil util;

    public ShipmentService(StringUtil util) {
        this.util = util;
    }

    public List<ShipmentResponse> getShipments(ShipmentRequest shipmentRequest) {

        if (!util.isValid(shipmentRequest.getShipmentId())) {
            throw new IllegalArgumentException("Shipment ID Should be in numeric or alphanumeric format");
        }
        // Mock data for demonstration
        List<ShipmentResponse> shipments = new ArrayList<>();
        shipments.add(new ShipmentResponse("SHP001", "New York", "Los Angeles", "In Transit"));
        shipments.add(new ShipmentResponse("SHP002", "Chicago", "Houston", "Delivered"));
        return shipments;
    }


    public List<ShipmentResponse> getShipments() {
        List<ShipmentResponse> shipments = new ArrayList<>();
        shipments.add(new ShipmentResponse("SHP001", "New York", "Los Angeles", "In Transit"));
        shipments.add(new ShipmentResponse("SHP002", "Chicago", "Houston", "Delivered"));
        return shipments;
    }

}
