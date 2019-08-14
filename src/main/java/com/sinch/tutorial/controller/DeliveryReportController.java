package com.sinch.tutorial.controller;

import com.clxcommunications.xms.api.BatchDeliveryReport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
public class DeliveryReportController {
    private static final Logger LOG = Logger.getLogger(DeliveryReportController.class.getName());

    @PostMapping(path = "/sms/deliveryReport")
    public ResponseEntity receiveReport(@RequestBody BatchDeliveryReport report) {

        LOG.info("Received delivery report: " + report);

        return ResponseEntity.ok().build();
    }
}
