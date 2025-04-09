package com.example.serverside_coursework.cli.producerconsumer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketPoolCli {
    // Use the existing logger, but enhance its usage
    private static final Logger LOGGER = Logger.getLogger(TicketPoolCli.class.getName());

    private int currentTicketCount;
    private final int maxCapacity;
    private final Lock lock = new ReentrantLock();

    public TicketPoolCli(int maxCapacity, int initialTicketCount) {
        this.currentTicketCount = initialTicketCount;
        this.maxCapacity = maxCapacity;

        // Log the initialization of the ticket pool
        LOGGER.info(() -> String.format("Ticket pool created with max capacity %d and initial ticket count %d",
                maxCapacity, initialTicketCount));
    }

    public void addTickets(int quantity) {
        lock.lock();
        try {
            if (currentTicketCount + quantity <= maxCapacity) {
                currentTicketCount += quantity;
                // Use parameterized logging for better performance
                LOGGER.log(Level.INFO, "Added {0} tickets. Total tickets now: {1}",
                        new Object[]{quantity, currentTicketCount});
                return;
            }

            // Log when unable to add tickets due to capacity
            LOGGER.log(Level.WARNING,
                    "Cannot add {0} tickets. Capacity full. Current count: {1}, Max capacity: {2}",
                    new Object[]{quantity, currentTicketCount, maxCapacity});
        } finally {
            lock.unlock();
        }
    }

    public void removeTickets(int quantity) {
        lock.lock();
        try {
            if (currentTicketCount >= quantity) {
                currentTicketCount -= quantity;
                LOGGER.log(Level.INFO, "Purchased {0} tickets. Remaining tickets: {1}",
                        new Object[]{quantity, currentTicketCount});
                return;
            }

            // Log when unable to remove tickets due to insufficient stock
            LOGGER.log(Level.WARNING,
                    "Cannot purchase {0} tickets. Insufficient stock. Current count: {1}",
                    new Object[]{quantity, currentTicketCount});
        } finally {
            lock.unlock();
        }
    }

    // Optional: Add a method to get current ticket count
    public int getCurrentTicketCount() {
        lock.lock();
        try {
            LOGGER.log(Level.FINE, "Retrieved current ticket count: {0}", currentTicketCount);
            return currentTicketCount;
        } finally {
            lock.unlock();
        }
    }
}