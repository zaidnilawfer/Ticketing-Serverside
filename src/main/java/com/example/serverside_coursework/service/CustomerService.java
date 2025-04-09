package com.example.serverside_coursework.service;

import com.example.serverside_coursework.concurrency.CustomerThread;
import com.example.serverside_coursework.concurrency.TicketPool;
import com.example.serverside_coursework.model.Customer;
import com.example.serverside_coursework.model.Ticket;
import com.example.serverside_coursework.repository.CustomerRepository;
import com.example.serverside_coursework.repository.TicketRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Service
public class CustomerService {
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());
    private static final int THREAD_POOL_SIZE = 5;

    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final ExecutorService executorService;
    private final TicketPool ticketPool;
    private final TicketService ticketService;

    public CustomerService(
            CustomerRepository customerRepository,
            TicketRepository ticketRepository,
            TicketService ticketService,
            TicketPool ticketPool) {
        this.customerRepository = customerRepository;
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.ticketPool = ticketPool;
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    // Save a new customer
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Asynchronous ticket purchase using Thread
    public List<Ticket> purchaseTicketAsync(Customer customer, int requestAmount) {
        CustomerThread customerThread = new CustomerThread(ticketPool, customer, requestAmount);
        List<Ticket> purchasedTickets = new ArrayList<>();

        try {
            String purchaseResult = customerThread.call();

            if (purchaseResult != null && !purchaseResult.contains("No tickets available")) {
                // Extract UUIDs from the purchase result
                List<String> ticketUUIDs = extractTicketUUIDs(purchaseResult);

                // Process each purchased ticket
                for (String ticketUUID : ticketUUIDs) {
                    // Find the existing ticket in the database
                    LOGGER.info("Searching for ticket with UUID: " + ticketUUID);
                    Ticket existingTicket = ticketRepository.findByTicketUuidCustom(ticketUUID);

                    if (existingTicket != null) {
                        // Update existing ticket
                        existingTicket.setCustomer(customer);
                        existingTicket.setSold(true);
                        Ticket savedTicket = ticketRepository.save(existingTicket);
                        purchasedTickets.add(savedTicket);

                        LOGGER.info("Existing ticket updated for customer: " + customer.getName()
                                + ", Ticket UUID: " + ticketUUID);
                    } else {
                        LOGGER.warning("Ticket with UUID " + ticketUUID + " not found in database");
                    }
                }

                return purchasedTickets;
            } else {
                LOGGER.warning("Async ticket purchase unsuccessful for customer: " + customer.getName());
                return new ArrayList<>();
            }
        } catch (Exception e) {
            LOGGER.severe("Error during async ticket purchase: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Helper method to extract ticket UUIDs from the purchase result
    private List<String> extractTicketUUIDs(String purchaseResult) {
        List<String> ticketUUIDs = new ArrayList<>();

        // Find the part of the string containing UUIDs
        int startIndex = purchaseResult.indexOf("Ticket UUIDs:") + "Ticket UUIDs:".length();
        if (startIndex > 0) {
            String uuidsString = purchaseResult.substring(startIndex).trim();

            // Split UUIDs and trim whitespace
            String[] uuidArray = uuidsString.split(",");
            for (String uuid : uuidArray) {
                ticketUUIDs.add(uuid.trim());
            }
        }

        return ticketUUIDs;
    }
    public boolean validateLogin(String email, String password) {
        return customerRepository.findByEmailAndPassword(email, password) != null;
    }

    // Find a customer by ID
    public Optional<Customer> findCustomerById(Integer customerId) {
        return customerRepository.findById(customerId);
    }

    // Clean up resources when the service is destroyed
    public void destroy() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}