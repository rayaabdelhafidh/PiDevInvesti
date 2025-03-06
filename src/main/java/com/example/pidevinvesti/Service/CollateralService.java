package com.example.pidevinvesti.Service;

import com.example.pidevinvesti.Entity.Collateral;
import com.example.pidevinvesti.Entity.Demand;
import com.example.pidevinvesti.Entity.User;
import com.example.pidevinvesti.Repository.ICollateralRepo;
import com.example.pidevinvesti.Repository.IDemandRepo;
import com.example.pidevinvesti.Repository.UserRepo;
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
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private IDemandRepo demandRepo;


    @Override
    public Collateral addCollateral(Collateral collateral ,Long UserId)
    {
        User user=userRepo.findById(UserId).get();
        collateral.setUser(user);
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
