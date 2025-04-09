package com.example.serverside_coursework.cli.command;

import com.example.serverside_coursework.cli.producerconsumer.CustomerCli;
import com.example.serverside_coursework.cli.producerconsumer.TicketPoolCli;
import com.example.serverside_coursework.cli.producerconsumer.VendorCli;

import java.util.ArrayList;
import java.util.List;

public class TicketSystemController {
    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();
    private final TicketPoolCli ticketPoolCli;

    public TicketSystemController(TicketPoolCli ticketPoolCli) {
        this.ticketPoolCli = ticketPoolCli;
    }

    public void start( long vendorRate, long customerRate,
                       int numVendors, int numCustomers) {
        // Start vendor threads
        for (int i = 0; i < numVendors; i++) {
            VendorCli vendorCli = new VendorCli(ticketPoolCli, vendorRate);
            Thread vendorThread = new Thread(vendorCli, "Vendor-" + (i + 1));
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Start customer threads
        for (int i = 0; i < numCustomers; i++) {
            CustomerCli customerCli = new CustomerCli(ticketPoolCli, customerRate);
            Thread customerThread = new Thread(customerCli, "Customer-" + (i + 1));
            customerThreads.add(customerThread);
            customerThread.start();
        }

        System.out.println("Ticket system started with " + numVendors + " vendors and " + numCustomers + " customers.");
    }

    public void stop() {
        // Interrupt and stop vendor threads
        vendorThreads.forEach(Thread::interrupt);
        vendorThreads.clear();

        // Interrupt and stop customer threads
        customerThreads.forEach(Thread::interrupt);
        customerThreads.clear();

        System.out.println("Ticket system stopped.");
}
}
