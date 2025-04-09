package com.example.serverside_coursework.concurrency;

import com.example.serverside_coursework.model.Customer;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CustomerThread implements Callable<String> {
    private static final Logger LOGGER = Logger.getLogger(CustomerThread.class.getName());

    private final TicketPool ticketPool;
    private final Customer customer;
    private final int requestAmount;

    public CustomerThread(TicketPool ticketPool, Customer customer, int requestAmount) {
        this.ticketPool = ticketPool;
        this.customer = customer;
        this.requestAmount = requestAmount;
    }

    @Override
    public String call() throws Exception {
        try {
            // Purchase tickets
            List<String> ticketUUIDs = ticketPool.purchaseTicket(customer, requestAmount);

            // Check if any tickets were purchased
            if (ticketUUIDs != null && !ticketUUIDs.isEmpty()) {
                // Log detailed information about the purchase
                String purchaseDetails = String.format(
                        "Customer %s purchased %d ticket(s). Ticket UUIDs: %s",
                        customer.getName(),
                        ticketUUIDs.size(),
                        ticketUUIDs.stream().collect(Collectors.joining(", "))
                );
                LOGGER.info(purchaseDetails);

                // Simulate processing time
                Thread.sleep(1000);

                // Return purchase details as a string
                return purchaseDetails;
            } else {
                // Log if no tickets could be purchased
                String noTicketMessage = String.format(
                        "No tickets available for customer: %s (Requested: %d)",
                        customer.getName(),
                        requestAmount
                );
                LOGGER.warning(noTicketMessage);
                return noTicketMessage;
            }
        } catch (InterruptedException e) {
            // Handle thread interruption
            Thread.currentThread().interrupt();
            String interruptMessage = String.format(
                    "CustomerThread for %s interrupted: %s",
                    customer.getName(),
                    e.getMessage()
            );
            LOGGER.severe(interruptMessage);
            return interruptMessage;
        }
    }
}