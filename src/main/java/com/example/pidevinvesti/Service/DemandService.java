package com.example.pidevinvesti.Service;

import com.example.pidevinvesti.Entity.Demand;
import com.example.pidevinvesti.Repository.IDemandRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DemandService implements IDemandService {
    @Autowired
  private  IDemandRepo iDemandRepository;
    @Override
    public Demand AddDemand(Demand demand) {
        iDemandRepository.save(demand);
        return demand ;
    }
}
