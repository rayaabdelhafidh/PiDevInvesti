package com.example.investi.Services;

import com.example.investi.Entities.Pack;
import com.example.investi.Repositories.IPackRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PackService implements IPackService {
    @Autowired
    private IPackRepo iPackRepo;


    @Override
    public Pack AddPack(Pack pack) {
        iPackRepo.save(pack);
        return pack ;    }

    @Override
    public List<Pack> getAllPacks() {
        return iPackRepo.findAll();
    }

    @Override
    public Optional<Pack> getPackById(Long PackId) {
        return iPackRepo.findById(PackId);
    }

    @Override
    public Pack UpdatePack(Long PackId, Pack pack) {
        Optional<Pack> existingPackOpt = iPackRepo.findById(PackId);
        if (existingPackOpt.isPresent()) {
            Pack existingPack = existingPackOpt.get();
            existingPack.setNom(pack.getNom());
            existingPack.setCategorie(pack.getCategorie());
            existingPack.setDescription(pack.getDescription());
            existingPack.setIntervalle(pack.getIntervalle());
            return iPackRepo.save(existingPack);
        }    return null;
    }

    @Override
    public void DeletePack(Long PackId) {
        iPackRepo.deleteById(PackId);
    }
}
