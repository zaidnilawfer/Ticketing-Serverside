package com.example.serverside_coursework.service;

import com.example.serverside_coursework.model.Ticket;
import com.example.serverside_coursework.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Optional<Ticket> findById(Integer id) {
        return ticketRepository.findById(id);
    }

    @Transactional
    public List<Ticket> saveTickets(List<Ticket> tickets) {
        try {
            // Ensure each ticket has a UUID
            tickets.forEach(ticket -> {
                if (ticket.getTicketUuid() == null) {
                    ticket.setTicketUuid(UUID.randomUUID().toString());
                }
                ticket.setSold(false); // Ensure tickets are not marked as sold
            });

            // Save and return the saved tickets
            return ticketRepository.saveAll(tickets);
        } catch (Exception e) {
            logger.error("Error saving tickets", e);
            throw new RuntimeException("Failed to save tickets", e);
        }
    }

    public List<Ticket> getAvailableTickets() {
        return ticketRepository.findAll().stream()
                .filter(ticket -> !ticket.isSold())
                .collect(Collectors.toList());
    }

    public List<Ticket> getAvailableTicketsByEventName(String eventName) {
        return ticketRepository.findAll().stream()
                .filter(ticket -> !ticket.isSold() && ticket.getEventName().equals(eventName))
                .collect(Collectors.toList());
    }
    public List<Ticket> getTicketsByCustomerId(Integer customerId) {
        return ticketRepository.findTicketsByCustomerId(customerId);
    }
    @Transactional
    public Ticket purchaseTicket(Ticket ticket) {
        try {
            ticket.setSold(true);
            return ticketRepository.save(ticket);
        } catch (Exception e) {
            logger.error("Error purchasing ticket", e);
            throw new RuntimeException("Failed to purchase ticket", e);
        }
    }
}