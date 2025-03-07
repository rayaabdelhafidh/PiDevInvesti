package com.example.investi.Repositories;

import com.example.investi.Entities.Investor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface InvestorRepository  extends JpaRepository<Investor,Long> {


}
