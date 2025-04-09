package com.example.serverside_coursework.repository;

import com.example.serverside_coursework.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    // Additional custom queries can be added here
    @Query("SELECT t FROM Ticket t WHERE t.ticketUuid = :uuid")
    Ticket findByTicketUuidCustom(@Param("uuid") String uuid);

    @Query("SELECT t FROM Ticket t WHERE t.customer.id = :customerId")
    List<Ticket> findTicketsByCustomerId(@Param("customerId") Integer customerId);


}
