package com.example.investi.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.investi.Services.DeviseService;

@AllArgsConstructor
@RestController
@RequestMapping("/devise")
public class DeviseController {

    private DeviseService deviseService;

    @GetMapping("/convertir/{montant}/{deviseSource}/{deviseDestination}")
    public ResponseEntity<Double> convertir(@PathVariable("montant") double montant, @PathVariable("deviseSource") String deviseSource, @PathVariable("deviseDestination") String deviseDestination) {
        double montantConverti = deviseService.convertisseur(montant, deviseSource, deviseDestination);
        return ResponseEntity.ok(montantConverti);
    }
}
