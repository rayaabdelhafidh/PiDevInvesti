package com.example.investi.Controllers;

import com.example.investi.Entities.Pack;
import com.example.investi.Services.IPackService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Pack")
@AllArgsConstructor
public class PackController {
    @Autowired
    IPackService PackService;
    @PostMapping("/add")
    public Pack ajouterPack(@RequestBody Pack pack) {
        return PackService.AddPack(pack);
    }

    @GetMapping("/all")
    public List<Pack> getAllPacks (){
        return PackService.getAllPacks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pack> getPackById(@PathVariable Long id) {
        Optional<Pack> pack = PackService.getPackById(id);
        return pack.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Pack> updatePack(@PathVariable Long id, @RequestBody Pack pack) {

        Pack updatedPack= PackService.UpdatePack(id, pack);
        return ResponseEntity.ok(updatedPack);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePack(@PathVariable Long id) {

        PackService.DeletePack(id);
        return ResponseEntity.noContent().build(); //
    }
}
