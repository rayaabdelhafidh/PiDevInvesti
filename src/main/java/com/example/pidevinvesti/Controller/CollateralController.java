package com.example.pidevinvesti.Controller;

import com.example.pidevinvesti.Entity.Collateral;
import com.example.pidevinvesti.Service.ICollateralService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/collaterals")
public class CollateralController {

    @Autowired
    private ICollateralService collateralService;

    @PostMapping("/add")
    public Collateral addCollateral(@RequestBody Collateral collateral) {
        return collateralService.addCollateral(collateral);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Collateral>> getAllCollaterals() {
        List<Collateral> collaterals = collateralService.getAllCollaterals();
        return ResponseEntity.ok(collaterals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collateral> getCollateralById(@PathVariable Long id) {
        Optional<Collateral> collateral = collateralService.getCollateralById(id);
        return collateral.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Collateral> updateCollateral(@PathVariable Long id, @RequestBody Collateral collateral) {
        Collateral updatedCollateral = collateralService.updateCollateral(id, collateral);
        return ResponseEntity.ok(updatedCollateral);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCollateral(@PathVariable Long id) {
        collateralService.deleteCollateral(id);
        return ResponseEntity.noContent().build();
    }


}
