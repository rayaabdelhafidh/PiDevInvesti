package com.example.investi.Controllers;


import com.example.investi.Services.ITrainingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/trainings")
public class AdminController {
    @Autowired
    private ITrainingService iTrainingService;

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingTrainings() {
        return ResponseEntity.ok(iTrainingService.getPendingTrainings());
    }

    // ✅ Approuver une formation
    @PutMapping("/{trainingId}/approve")
    public ResponseEntity<?> approveTraining(@PathVariable Long trainingId) {
        iTrainingService.approveTraining(trainingId);
        return ResponseEntity.ok("Formation approuvée avec succès !");
    }

    // ❌ Rejeter une formation
    @PutMapping("/{trainingId}/reject")
    public ResponseEntity<?> rejectTraining(@PathVariable Long trainingId) {
        iTrainingService.rejectTraining(trainingId);
        return ResponseEntity.ok("Formation rejetée !");
    }
}
