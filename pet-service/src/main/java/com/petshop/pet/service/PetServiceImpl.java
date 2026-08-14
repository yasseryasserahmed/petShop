package com.petshop.pet.service;

import com.petshop.pet.dto.PetRequestDTO;
import com.petshop.pet.dto.PetResponseDTO;
import com.petshop.pet.entity.Pet;
import com.petshop.pet.entity.Species;
import com.petshop.pet.exception.PetNotFoundException;
import com.petshop.pet.exception.SpeciesNotFoundException;
import com.petshop.pet.mapper.PetMapper;
import com.petshop.pet.repository.PetRepository;
import com.petshop.pet.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final SpeciesRepository speciesRepository;
    private final PetMapper petMapper;

    @Override
    @Transactional
    public PetResponseDTO createPet(PetRequestDTO requestDTO) {
        
        Species species = speciesRepository.findById(requestDTO.getSpeciesId())
                .orElseThrow(() -> new SpeciesNotFoundException(requestDTO.getSpeciesId()));

        Pet pet = petMapper.toEntity(requestDTO);
        pet.setSpecies(species);
        Pet saved = petRepository.save(pet);

        log.info("Created pet with id: {}", saved.getId());
        return petMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PetResponseDTO getPetById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        return petMapper.toResponseDTO(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PetResponseDTO> getAllPets(Pageable pageable) {
        return petRepository.findAll(pageable)
                .map(petMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PetResponseDTO> getPetsByOwnerId(Long ownerId, Pageable pageable) {
        return petRepository.findByOwnerId(ownerId, pageable)
                .map(petMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public PetResponseDTO updatePet(Long id, PetRequestDTO requestDTO) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        Species species = speciesRepository.findById(requestDTO.getSpeciesId())
                .orElseThrow(() -> new SpeciesNotFoundException(requestDTO.getSpeciesId()));

        petMapper.updateEntityFromDto(requestDTO, pet);
        pet.setSpecies(species);
        Pet updated = petRepository.save(pet);

        log.info("Updated pet with id: {}", updated.getId());
        return petMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new PetNotFoundException(id);
        }

        petRepository.deleteById(id);
        log.info("Deleted pet with id: {}", id);
    }
}
