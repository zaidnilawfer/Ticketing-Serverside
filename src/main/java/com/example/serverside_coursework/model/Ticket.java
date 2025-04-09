package com.example.serverside_coursework.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,nullable = false, length = 36)
    private String ticketUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean isSold;

    // Default constructor
    public Ticket() {
        this.isSold = false;
    }
    public void initializeUuid() {
        if (this.ticketUuid == null) {
            this.ticketUuid = UUID.randomUUID().toString();
        }
    }

    public Ticket(Ticket templateTicket) {
        this.eventName = templateTicket.eventName;
        this.price = templateTicket.price;
        this.vendor = templateTicket.vendor;
        this.isSold = false;
        initializeUuid(); // Ensure a new UUID is generated
    }
    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public String getTicketUuid() {
        return ticketUuid;
    }

    public void setTicketUuid(String ticketUuid) {
        this.ticketUuid = ticketUuid;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }
}
