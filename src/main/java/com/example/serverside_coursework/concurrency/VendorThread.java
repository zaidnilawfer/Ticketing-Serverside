package com.example.serverside_coursework.concurrency;

import com.example.serverside_coursework.model.Ticket;
import com.example.serverside_coursework.model.Vendor;
import com.example.serverside_coursework.service.TicketService;

import java.util.List;
import java.util.logging.Logger;

public class VendorThread extends Thread {
    private static final Logger LOGGER = Logger.getLogger(VendorThread.class.getName());
    private final TicketPool ticketPool;
    private final Vendor vendor;
    private final List<Ticket> ticketsToAdd;

    public VendorThread(TicketPool ticketPool, TicketService ticketService, Vendor vendor, List<Ticket> ticketsToAdd) {
        this.ticketPool = ticketPool;
        this.vendor = vendor;
        this.ticketsToAdd = ticketsToAdd;
    }

    @Override
    public void run() {
        for (Ticket ticket : ticketsToAdd) {
            try {
                ticket.setVendor(vendor);
                ticketPool.addTickets(ticket, 1); // Add each ticket to the TicketPool
                LOGGER.info("Vendor " + vendor.getName() + " added ticket: " + ticket.getTicketUuid());
                Thread.sleep(500); // Simulate time taken to add tickets
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.severe("VendorThread interrupted: " + e.getMessage());
            }
        }
    }
}
