package com.example.serverside_coursework.repository;

import com.example.serverside_coursework.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Vendor findByEmailAndPassword(String email, String password);

}
