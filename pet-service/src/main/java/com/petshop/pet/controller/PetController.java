package com.petshop.pet.controller;

import com.petshop.pet.dto.PetRequestDTO;
import com.petshop.pet.dto.PetResponseDTO;
import com.petshop.pet.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetResponseDTO> createPet(
            @Valid @RequestBody PetRequestDTO requestDTO) {

        PetResponseDTO created = petService.createPet(requestDTO);
        return ResponseEntity
                .created(URI.create("/pets/" + created.getId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PetResponseDTO>> getAllPets(
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            @RequestParam(required = false) Long ownerId) {

        if (ownerId != null) {
            return ResponseEntity.ok(petService.getPetsByOwnerId(ownerId, pageable));
        }
        return ResponseEntity.ok(petService.getAllPets(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(
            @PathVariable Long id,
            @Valid @RequestBody PetRequestDTO requestDTO) {

        return ResponseEntity.ok(petService.updatePet(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable Long id) {
        petService.deletePet(id);
    }
}
