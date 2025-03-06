package com.example.pidevinvesti.Controller;

import com.example.pidevinvesti.Entity.Demand;
import com.example.pidevinvesti.Entity.Loan;
import com.example.pidevinvesti.Repository.IDemandRepo;
import com.example.pidevinvesti.Repository.ILoanRepo;
import com.example.pidevinvesti.Service.IDemandService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class DemandController {
    @Autowired
    IDemandService DemandService;
    @Autowired
    IDemandRepo demandRepo;

    @Autowired
    ILoanRepo loanRepo;
    @PostMapping("add")
    public Demand ajouterDemand(@RequestBody Demand demand, @RequestParam(required = true) Long packId,@RequestParam(required = true) Long UserId) {
        return DemandService.AddDemand(demand , packId,UserId);
    }

    @GetMapping("all")
    public List<Demand> getAllDemands (){
        return DemandService.getAllDemands();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demand> getDemandById(@PathVariable Long id) {
        Optional<Demand> demand = DemandService.getDemandById(id);
        return demand.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Demand> updateDemand(@PathVariable Long id, @RequestBody Demand demand) {

        Demand updatedDemand= DemandService.UpdateDemand(id, demand);
        return ResponseEntity.ok(updatedDemand);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDemand(@PathVariable Long id) {

        DemandService.DeleteDemand(id);
        return ResponseEntity.noContent().build(); //
    }

    @PutMapping("/{demandId}/traiter")
    public ResponseEntity<String> traiterDemande(
            @PathVariable Long demandId) {

        try {

            DemandService.TraiterDemande(demandId);

            return ResponseEntity.ok("Demande traitée avec succès !");


        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }


    }
}
