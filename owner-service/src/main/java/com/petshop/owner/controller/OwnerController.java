package com.petshop.owner.controller;

import com.petshop.owner.dto.OwnerRequestDTO;
import com.petshop.owner.dto.OwnerResponseDTO;
import com.petshop.owner.service.OwnerService;
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
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<OwnerResponseDTO> createOwner(
            @Valid @RequestBody OwnerRequestDTO requestDTO) {

        OwnerResponseDTO created = ownerService.createOwner(requestDTO);
        return ResponseEntity
                .created(URI.create("/owners/" + created.getId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponseDTO> getOwnerById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    @GetMapping
    public ResponseEntity<Page<OwnerResponseDTO>> getAllOwners(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {

        return ResponseEntity.ok(ownerService.getAllOwners(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerResponseDTO> updateOwner(
            @PathVariable Long id,
            @Valid @RequestBody OwnerRequestDTO requestDTO) {

        return ResponseEntity.ok(ownerService.updateOwner(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
    }
}