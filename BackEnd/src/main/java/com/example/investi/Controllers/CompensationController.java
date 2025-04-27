package com.example.investi.Controllers;

import com.example.investi.Services.ICompensationServices;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.investi.Entities.Compensation;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compensations")
@RequiredArgsConstructor
@AllArgsConstructor

public class CompensationController {
@Autowired
    private ICompensationServices compensationService;

    // 🔹 Create Compensation
    @PostMapping("/add")
    public ResponseEntity<Compensation> createCompensation(@RequestBody Compensation compensation) {
        Compensation savedCompensation = compensationService.createCompensation(compensation);
        return ResponseEntity.ok(savedCompensation);
    }

    // 🔹 Get All Compensations
    @GetMapping
    public ResponseEntity<List<Compensation>> getAllCompensations() {
        return ResponseEntity.ok(compensationService.getAllCompensations());
    }

    // 🔹 Get Compensation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Compensation> getCompensationById(@PathVariable Long id) {
        Optional<Compensation> compensation = compensationService.getCompensationById(id);
        return compensation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Update Compensation
    @PutMapping("/update")
    public ResponseEntity<Compensation> updateCompensation( @RequestBody Compensation compensation) {
        return ResponseEntity.ok(compensationService.updateCompensation( compensation));
    }

    // 🔹 Delete Compensation
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCompensation(@PathVariable Long id) {
        compensationService.deleteCompensation(id);
        return ResponseEntity.noContent().build();
    }
}
