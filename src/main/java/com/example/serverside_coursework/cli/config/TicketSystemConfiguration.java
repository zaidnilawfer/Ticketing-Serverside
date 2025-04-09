package com.example.serverside_coursework.cli.config;

public class TicketSystemConfiguration {
    private int totalTickets;
    private double ticketReleaseRate;
    private double customerRetrievalRate;
    private int maxTicketCapacity;

    // Default constructor with sensible defaults
    public TicketSystemConfiguration() {
        this.totalTickets = 100;
        this.ticketReleaseRate = 1.0;
        this.customerRetrievalRate = 1.0;
        this.maxTicketCapacity = 200;
    }

    // Getters and setters
    public int getTotalTickets() { return totalTickets; }
    public void setTotalTickets(int totalTickets) { this.totalTickets = totalTickets; }

    public double getTicketReleaseRate() { return ticketReleaseRate; }
    public void setTicketReleaseRate(double ticketReleaseRate) { this.ticketReleaseRate = ticketReleaseRate; }

    public double getCustomerRetrievalRate() { return customerRetrievalRate; }
    public void setCustomerRetrievalRate(double customerRetrievalRate) { this.customerRetrievalRate = customerRetrievalRate; }

    public int getMaxTicketCapacity() { return maxTicketCapacity; }
    public void setMaxTicketCapacity(int maxTicketCapacity) { this.maxTicketCapacity = maxTicketCapacity; }

    @Override
    public String toString() {
        return "\nCurrent Configuration:" +
                "\nTotal Tickets: " + totalTickets +
                "\nTicket Release Rate: " + ticketReleaseRate + " per minute" +
                "\nCustomer Retrieval Rate: " + customerRetrievalRate + " per minute" +
                "\nMaximum Ticket Capacity: " + maxTicketCapacity;
    }
}
