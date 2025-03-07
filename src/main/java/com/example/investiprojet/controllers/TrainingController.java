package com.example.investiprojet.controllers;

import com.example.investiprojet.entities.Training;
import com.example.investiprojet.entities.TrainingCategory;
import com.example.investiprojet.entities.TrainingLevel;
import com.example.investiprojet.services.ExcelExportService;
import com.example.investiprojet.services.ITrainingService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/trainings")
public class TrainingController {
    @Autowired
    private ITrainingService iTrainingService ;
    @Autowired
    private ExcelExportService excelExportService;

    @PostMapping("create")
    public Training creatingTraining(@RequestBody Training training){
        iTrainingService.createTraining(training);
        return training;

    }

    @GetMapping("all")
     public List<Training> getAllTrainings(){
        return iTrainingService.getAllTrainings();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Training> getTrainingById(@PathVariable Long id) {
        Optional<Training> training = iTrainingService.getTrainingById(id);
        return training.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    /*@GetMapping("/category/{category}")
    public List<Training> getTrainingsByCategory(@PathVariable String category) {
        return iTrainingService.getTrainingsByCategory(category);
    }*/

    @PutMapping("/update/{id}")
    public ResponseEntity<Training> updateTraining(@PathVariable Long id, @RequestBody Training training) {

            Training updatedTraining = iTrainingService.updateTraining(id, training);
            return ResponseEntity.ok(updatedTraining);
        }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {

        iTrainingService.deleteTraining(id);
        return ResponseEntity.noContent().build(); //
    }

    @GetMapping("/category/{category}")
    public List<Training> getTrainingsByCategory(@PathVariable String category) {
        return iTrainingService.getTrainingsByCategory(category);
    }

    @GetMapping("/level/{level}")
    public List<Training> getTrainingsByLevel(@PathVariable String level) {
        return iTrainingService.getTrainingsByLevel(level);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Training>> searchTrainings(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) TrainingCategory category,
            @RequestParam(required = false) TrainingLevel level,
            @RequestParam(required = false) Integer maxDuration) {

        List<Training> trainings = iTrainingService.searchTrainings(title, category, level, maxDuration);
        return ResponseEntity.ok(trainings);
    }
    @GetMapping("/generateExcel")
    public void generateExcel(HttpServletResponse response) throws IOException {
        excelExportService.generateFormationHistoryExcel( (HttpServletResponse) response);

    }
}



