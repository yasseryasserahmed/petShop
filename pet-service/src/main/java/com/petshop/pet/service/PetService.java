package com.petshop.pet.service;

import com.petshop.pet.dto.PetRequestDTO;
import com.petshop.pet.dto.PetResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PetService {

    PetResponseDTO createPet(PetRequestDTO requestDTO);

    PetResponseDTO getPetById(Long id);

    Page<PetResponseDTO> getAllPets(Pageable pageable);

    Page<PetResponseDTO> getPetsByOwnerId(Long ownerId, Pageable pageable);

    PetResponseDTO updatePet(Long id, PetRequestDTO requestDTO);

    void deletePet(Long id);
}
