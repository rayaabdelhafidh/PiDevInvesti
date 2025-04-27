package com.example.investi.Repositories;

import com.example.investi.Entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpaymentRepository extends JpaRepository<Payment,Long> {
}
