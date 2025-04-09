package com.example.serverside_coursework.repository;

import com.example.serverside_coursework.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByEmailAndPassword(String email, String password);

}
