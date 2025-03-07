package com.example.investi.Services;

import com.example.investi.Entities.Demand;

import java.util.List;
import java.util.Optional;

public interface IDemandService {
    Demand AddDemand(Demand demand);
    List<Demand> getAllDemands();
    Optional<Demand> getDemandById(Long DemandId);
    Demand UpdateDemand (Long DemandId,Demand demand );
    void DeleteDemand (Long DemandId);

}
