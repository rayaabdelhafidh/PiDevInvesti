package com.example.pidevinvesti.Service;

import com.example.pidevinvesti.Entity.Collateral;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ICollateralService {
    Collateral addCollateral(Collateral collateral ,Long UserId);
    List<Collateral> getAllCollaterals();
    Optional<Collateral> getCollateralById(Long CollateralId);
    Collateral updateCollateral(Long CollateralId, Collateral collateral);
    void deleteCollateral(Long CollateralId);

}
