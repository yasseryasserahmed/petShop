package com.petshop.pet.controller;

import com.petshop.pet.dto.SpeciesRequestDTO;
import com.petshop.pet.dto.SpeciesResponseDTO;
import com.petshop.pet.service.SpeciesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/species")
@RequiredArgsConstructor
public class SpeciesController {

    private final SpeciesService speciesService;

    @PostMapping
    public ResponseEntity<SpeciesResponseDTO> createSpecies(
            @Valid @RequestBody SpeciesRequestDTO requestDTO) {

        SpeciesResponseDTO created = speciesService.createSpecies(requestDTO);
        return ResponseEntity
                .created(URI.create("/species/" + created.getId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpeciesResponseDTO> getSpeciesById(@PathVariable Long id) {
        return ResponseEntity.ok(speciesService.getSpeciesById(id));
    }

    @GetMapping
    public ResponseEntity<List<SpeciesResponseDTO>> getAllSpecies() {
        return ResponseEntity.ok(speciesService.getAllSpecies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpeciesResponseDTO> updateSpecies(
            @PathVariable Long id,
            @Valid @RequestBody SpeciesRequestDTO requestDTO) {

        return ResponseEntity.ok(speciesService.updateSpecies(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSpecies(@PathVariable Long id) {
        speciesService.deleteSpecies(id);
    }
}
