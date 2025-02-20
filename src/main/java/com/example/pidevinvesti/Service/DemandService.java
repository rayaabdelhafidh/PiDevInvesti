package com.example.pidevinvesti.Service;

import com.example.pidevinvesti.Entity.Demand;
import com.example.pidevinvesti.Repository.IDemandRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Demand> getAllDemands() {
        return iDemandRepository.findAll();
    }

    @Override
    public Optional<Demand> getDemandById(Long DemandId) {
        return iDemandRepository.findById(DemandId);
    }

    @Override
    public Demand UpdateDemand(Long DemandId, Demand demand) {
// Find the existing training by ID
        Optional<Demand> existingDemandOpt = iDemandRepository.findById(DemandId);

        if (existingDemandOpt.isPresent()) {
            // Retrieve the existing training entity
            Demand existingDemand = existingDemandOpt.get();

            // Update the fields of the existing training entity
            existingDemand.setDemandDate(demand.getDemandDate());
            existingDemand.setAmount(demand.getAmount());
            existingDemand.setDuration(demand.getDuration()); // Enums will work fine
            existingDemand.setStatus(demand.getStatus());
            existingDemand.setIntrestRate(demand.getIntrestRate());

            // Save and return the updated training entity
            return iDemandRepository.save(existingDemand);
        }
        return null;    }


    @Override
    public void DeleteDemand(Long DemandId) {
        iDemandRepository.deleteById(DemandId);
    }
}
