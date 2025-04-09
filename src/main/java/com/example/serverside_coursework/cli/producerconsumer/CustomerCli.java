package com.example.serverside_coursework.cli.producerconsumer;

public class CustomerCli implements Runnable {
    private final TicketPoolCli ticketPoolCli;
    private final long retrievalRate;

    public CustomerCli(TicketPoolCli ticketPoolCli, long retrievalRate) {
        this.ticketPoolCli = ticketPoolCli;
        this.retrievalRate = retrievalRate;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000); // Sleep for 1 second
                ticketPoolCli.removeTickets((int) retrievalRate); // Use retrievalRate as number of tickets to remove
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
 }
}
}