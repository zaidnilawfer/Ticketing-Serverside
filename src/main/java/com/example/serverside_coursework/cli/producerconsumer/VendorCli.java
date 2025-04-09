package com.example.serverside_coursework.cli.producerconsumer;

public class VendorCli implements Runnable {
    private final TicketPoolCli ticketPoolCli;
    private final long releaseRate;

    public VendorCli(TicketPoolCli ticketPoolCli, long releaseRate) {
        this.ticketPoolCli = ticketPoolCli;
        this.releaseRate = releaseRate;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000); // Sleep for 1 second
                ticketPoolCli.addTickets((int) releaseRate); // Use releaseRate as number of tickets to add
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
 }
}
}
