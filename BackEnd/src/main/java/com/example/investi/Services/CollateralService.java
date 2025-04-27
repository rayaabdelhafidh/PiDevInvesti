package com.example.investi.Services;


import com.example.investi.Entities.Client;
import com.example.investi.Entities.Collateral;
import com.example.investi.Repositories.ClientRepository;
import com.example.investi.Repositories.ICollateralRepo;
import com.example.investi.Repositories.IDemandRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CollateralService implements ICollateralService {

    @Autowired
    private ICollateralRepo collateralRepository;
    @Autowired
    private ClientRepository userRepo;
    @Autowired
    private IDemandRepo demandRepo;


    @Override
    public Collateral addCollateral(Collateral collateral , Long UserId)
    {
        Client user=userRepo.findById(UserId).get();
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
