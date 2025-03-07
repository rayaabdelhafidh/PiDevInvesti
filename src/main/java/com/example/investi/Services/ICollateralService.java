package com.example.investi.Services;


import com.example.investi.Entities.Collateral;

import java.util.List;
import java.util.Optional;

public interface ICollateralService {
    Collateral addCollateral(Collateral collateral ,Long UserId);
    List<Collateral> getAllCollaterals();
    Optional<Collateral> getCollateralById(Long CollateralId);
    Collateral updateCollateral(Long CollateralId, Collateral collateral);
    void deleteCollateral(Long CollateralId);

}
