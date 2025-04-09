package com.example.serverside_coursework.controller;
import com.example.serverside_coursework.model.Customer;
import com.example.serverside_coursework.model.Ticket;
import com.example.serverside_coursework.service.CustomerService;
import com.example.serverside_coursework.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {"http://localhost:4200"}) // Approach 1: Controller-level CORS

public class CustomerController {
    private final CustomerService customerService;
    private final TicketService ticketService;

    public CustomerController(CustomerService customerService, TicketService ticketService) {
        this.customerService = customerService;
        this.ticketService = ticketService;
    }


    @PostMapping("/{customerId}/{numOfTickets}/purchase-async")
    public ResponseEntity<?> purchaseTicketAsync(
            @PathVariable Integer customerId,
            @PathVariable Integer numOfTickets,
            @RequestParam String eventName) {
        try {
            // Find customer
            Optional<Customer> customerOptional = customerService.findCustomerById(customerId);
            if (customerOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Customer not found");
            }
            Customer customer = customerOptional.get();

            // Find available tickets by event name
            List<Ticket> availableTickets = ticketService.getAvailableTicketsByEventName(eventName);

            // Check if any tickets are available
            if (availableTickets.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No available tickets for event: " + eventName);
            }

            // Select the first available ticket
            Ticket availableTicket = availableTickets.get(0);

            // Initiate async purchase and get the ticket
            List<Ticket> purchasedTickets = customerService.purchaseTicketAsync(customer,numOfTickets);

            // Check if ticket purchase was successful
            if (purchasedTickets.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No tickets could be purchased");
            }

            // Return success response with ticket details
            return ResponseEntity.ok()
                    .body(new TicketPurchaseResponse(
                            "Ticket successfully purchased for customer: " + customer.getName(),
                            purchasedTickets
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to initiate ticket purchase: " + e.getMessage());
        }
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Ticket>> getTicketsByCustomerId(@PathVariable Integer customerId) {
        List<Ticket> tickets = ticketService.getTicketsByCustomerId(customerId);

        // If no tickets found, return 404 Not Found
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Return the list of tickets with 200 OK status
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        boolean isValid = customerService.validateLogin(email, password);
        return ResponseEntity.ok(isValid);
    }
    // Optional DTO for a more structured response
    public class TicketPurchaseResponse {
        private String message;
        private List<Ticket> tickets;

        public TicketPurchaseResponse(String message, List<Ticket> tickets) {
            this.message = message;
            this.tickets = tickets;
        }

        // Getters and setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Ticket> getTickets() {
            return tickets;
        }

        public void setTickets(List<Ticket> tickets) {
            this.tickets = tickets;
        }
    }
}