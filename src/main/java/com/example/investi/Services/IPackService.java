package com.example.investi.Services;

import com.example.investi.Entities.Pack;

import java.util.List;
import java.util.Optional;

public interface IPackService {
    Pack AddPack(Pack pack);
    List<Pack> getAllPacks();
    Optional<Pack> getPackById(Long PackId);
    Pack UpdatePack (Long PackId,Pack pack );
    void DeletePack (Long PackId);
}
