package com.example.pidevinvesti.Service;

import com.example.pidevinvesti.Entity.Demand;
import com.example.pidevinvesti.Entity.Pack;

import java.util.List;
import java.util.Optional;

public interface IPackService {
    Pack AddPack(Pack pack);
    List<Pack> getAllPacks();
    Optional<Pack> getPackById(Long PackId);
    Pack UpdatePack (Long PackId,Pack pack );
    void DeletePack (Long PackId);
}
