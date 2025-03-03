package com.example.pidevinvesti.Service;

import com.example.pidevinvesti.Entity.Collateral;
import com.example.pidevinvesti.Repository.ICollateralRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CollateralService implements ICollateralService {

    @Autowired
    private ICollateralRepo collateralRepository;


    @Override
    public Collateral addCollateral(Collateral collateral) {
        return collateralRepository.save(collateral);
    }

    @Override
    public List<Collateral> getAllCollaterals() {
        return collateralRepository.findAll();
    }

    @Override
    public Optional<Collateral> getCollateralById(Long collateralId) {
        return collateralRepository.findById(collateralId);
    }

    @Override
    public Collateral updateCollateral(Long collateralId, Collateral collateral) {
        Optional<Collateral> existingCollateralOpt = collateralRepository.findById(collateralId);

        if (existingCollateralOpt.isPresent()) {
            Collateral existingCollateral = existingCollateralOpt.get();
            existingCollateral.setType(collateral.getType());
            existingCollateral.setDescription(collateral.getDescription());
            return collateralRepository.save(existingCollateral);
        }
        return null;
    }

    @Override
    public void deleteCollateral(Long collateralId) {
        collateralRepository.deleteById(collateralId);
    }


}
