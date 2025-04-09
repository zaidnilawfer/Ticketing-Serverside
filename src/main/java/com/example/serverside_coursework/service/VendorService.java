package com.example.serverside_coursework.service;

import com.example.serverside_coursework.concurrency.TicketPool;
import com.example.serverside_coursework.concurrency.VendorThread;
import com.example.serverside_coursework.config.AppConfigProperties;
import com.example.serverside_coursework.model.Ticket;
import com.example.serverside_coursework.model.Vendor;
import com.example.serverside_coursework.repository.TicketRepository;
import com.example.serverside_coursework.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VendorService {
    private final int batchSize;
    private final ExecutorService executorService;
    private final VendorRepository vendorRepository;
    private final TicketPool ticketPool;
    private final TicketService ticketService;

    @Autowired
    public VendorService(
            VendorRepository vendorRepository,
            TicketRepository ticketRepository,
            TicketPool ticketPool,
            TicketService ticketService,
            AppConfigProperties configProperties
    ) {
        this.vendorRepository = vendorRepository;
        this.ticketPool = ticketPool;
        this.ticketService = ticketService;

        int threadPoolSize = configProperties.getThreadPoolSize();
        this.batchSize = configProperties.getBatchSize();

        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    public List<Ticket> addTickets(Vendor vendor, List<Ticket> tickets) {
        List<List<Ticket>> batches = createBatches(tickets);

        for (List<Ticket> batch : batches) {
            VendorThread vendorThread = new VendorThread(ticketPool, ticketService, vendor, batch);
            executorService.submit(vendorThread);
        }
        return tickets;
    }

    private List<List<Ticket>> createBatches(List<Ticket> tickets) {
        List<List<Ticket>> batches = new ArrayList<>();
        for (int i = 0; i < tickets.size(); i += batchSize) {
            int end = Math.min(i + batchSize, tickets.size());
            batches.add(new ArrayList<>(tickets.subList(i, end)));
        }
        return batches;
    }
    public boolean validateLogin(String email, String password) {
        return vendorRepository.findByEmailAndPassword(email, password) != null;
    }
    public Vendor saveVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public Optional<Vendor> findVendorById(Integer vendorId) {
        return vendorRepository.findById(vendorId);
    }


}