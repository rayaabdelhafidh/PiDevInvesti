package com.example.pidevinvesti.Controller;

import com.example.pidevinvesti.Entity.Demand;
import com.example.pidevinvesti.Service.IDemandService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DemandController {
    @Autowired
    IDemandService DemandService;
    @PostMapping("add")
    public Demand ajouterDemand(@RequestBody Demand demand) {
        return DemandService.AddDemand(demand);
    }
}
