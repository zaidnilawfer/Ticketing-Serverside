package com.example.serverside_coursework.concurrency;

import com.example.serverside_coursework.model.Customer;
import com.example.serverside_coursework.model.Ticket;
import com.example.serverside_coursework.service.TicketService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class TicketPool {
    private static final Logger LOGGER = Logger.getLogger(TicketPool.class.getName());
    private final BlockingQueue<Ticket> tickets;
    private final TicketService ticketService;

    public TicketPool(int maxCapacity, TicketService ticketService) {
        this.tickets = new ArrayBlockingQueue<>(maxCapacity);
        this.ticketService = ticketService;
    }

    public void addTickets(Ticket ticket, int quantity) {
        List<Ticket> ticketsToAdd = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Ticket newTicket = new Ticket(ticket); // Use copy constructor
            ticketsToAdd.add(newTicket);
        }
        for (Ticket t : ticketsToAdd) {
            try {
                // Save the ticket to the database first
                ticketService.saveTickets(List.of(t));
                tickets.put(t);
                TicketWebSocket.broadcastTicketCount(tickets.size());
                LOGGER.info("Added ticket to pool: " + t.getTicketUuid());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.severe("Interrupted while adding ticket: " + e.getMessage());
                break;
            }
        }
    }

    public List<String> purchaseTicket(Customer customer, int requestedQuantity) {
        List<String> purchasedTicketUuids = new ArrayList<>();
        int availableTickets = tickets.size();

        // Determine actual number of tickets to purchase
        int actualQuantity = Math.min(requestedQuantity, availableTickets);

        try {
            // Purchase available tickets
            for (int i = 0; i < actualQuantity; i++) {
                Ticket ticket = tickets.take();
                ticket.setCustomer(customer);
                ticket.setSold(true);
                purchasedTicketUuids.add(ticket.getTicketUuid());

                // Log each ticket purchase
                LOGGER.info("Ticket purchased: " + ticket.getTicketUuid());
            }

            // Broadcast updated ticket count
            TicketWebSocket.broadcastTicketCount(tickets.size());

            // Log if requested quantity differs from purchased
            if (actualQuantity < requestedQuantity) {
                LOGGER.warning(String.format(
                        "Requested %d tickets, but only %d available and purchased",
                        requestedQuantity, actualQuantity
                ));
            }

            return purchasedTicketUuids;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.severe("Interrupted while purchasing tickets: " + e.getMessage());
            return purchasedTicketUuids; // Return partial list if interrupted
        }
    }
}