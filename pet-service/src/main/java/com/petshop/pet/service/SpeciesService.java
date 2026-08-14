package com.petshop.pet.service;

import com.petshop.pet.dto.SpeciesRequestDTO;
import com.petshop.pet.dto.SpeciesResponseDTO;

import java.util.List;

public interface SpeciesService {

    SpeciesResponseDTO createSpecies(SpeciesRequestDTO requestDTO);

    SpeciesResponseDTO getSpeciesById(Long id);

    List<SpeciesResponseDTO> getAllSpecies();

    SpeciesResponseDTO updateSpecies(Long id, SpeciesRequestDTO requestDTO);

    void deleteSpecies(Long id);
}
