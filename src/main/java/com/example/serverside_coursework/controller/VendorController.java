package com.example.serverside_coursework.controller;

import com.example.serverside_coursework.model.Ticket;
import com.example.serverside_coursework.model.Vendor;
import com.example.serverside_coursework.service.VendorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = {"http://localhost:4200"}) // Approach 1: Controller-level CORS

public class VendorController {
    private final VendorService vendorService;

    @Autowired
    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public Vendor createVendor(@RequestBody Vendor vendor) {
        return vendorService.saveVendor(vendor);
    }

    @PostMapping("/{vendorId}/{multiplier}/tickets")
    public List<Ticket> addMultipliedTickets(
            @PathVariable Integer vendorId,
            @PathVariable Integer multiplier,
            @RequestBody Ticket ticket
    ) {
        // Create a list of tickets by multiplying the original ticket
        List<Ticket> multipliedTickets = IntStream.range(0, multiplier)
                .mapToObj(i -> {
                    Ticket multipliedTicket = new Ticket();
                    BeanUtils.copyProperties(ticket, multipliedTicket);
                    return multipliedTicket;
                })
                .collect(Collectors.toList());

        return vendorService.findVendorById(vendorId)
                .map(vendor -> vendorService.addTickets(vendor, multipliedTickets))
                .orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + vendorId));
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        boolean isValid = vendorService.validateLogin(email, password);
        return ResponseEntity.ok(isValid);
    }

}