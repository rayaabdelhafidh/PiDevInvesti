package com.example.pidevinvesti.Service;

import com.example.pidevinvesti.Entity.Demand;

import java.util.List;
import java.util.Optional;

public interface IDemandService {
    Demand AddDemand(Demand demand , Long packId);
    List<Demand> getAllDemands();
    Optional<Demand> getDemandById(Long DemandId);
    Demand UpdateDemand (Long DemandId,Demand demand );
    void DeleteDemand (Long DemandId);
    void TraiterDemande(Long demandId, String status);
}
