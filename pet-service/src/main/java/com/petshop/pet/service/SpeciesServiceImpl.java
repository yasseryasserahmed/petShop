package com.petshop.pet.service;

import com.petshop.pet.dto.SpeciesRequestDTO;
import com.petshop.pet.dto.SpeciesResponseDTO;
import com.petshop.pet.entity.Species;
import com.petshop.pet.exception.DuplicateSpeciesException;
import com.petshop.pet.exception.SpeciesInUseException;
import com.petshop.pet.exception.SpeciesNotFoundException;
import com.petshop.pet.mapper.SpeciesMapper;
import com.petshop.pet.repository.PetRepository;
import com.petshop.pet.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpeciesServiceImpl implements SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final PetRepository petRepository;
    private final SpeciesMapper speciesMapper;

    @Override
    @Transactional
    public SpeciesResponseDTO createSpecies(SpeciesRequestDTO requestDTO) {
        if (speciesRepository.existsByNameIgnoreCase(requestDTO.getName())) {
            throw new DuplicateSpeciesException(requestDTO.getName());
        }

        Species species = speciesMapper.toEntity(requestDTO);
        Species saved = speciesRepository.save(species);

        log.info("Created species with id: {}", saved.getId());
        return speciesMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SpeciesResponseDTO getSpeciesById(Long id) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));

        return speciesMapper.toResponseDTO(species);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpeciesResponseDTO> getAllSpecies() {
        // list is enugh for species pagination can be a push off
        return speciesRepository.findAll().stream()
                .map(speciesMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public SpeciesResponseDTO updateSpecies(Long id, SpeciesRequestDTO requestDTO) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));

        boolean nameChanged = !species.getName().equalsIgnoreCase(requestDTO.getName());
        if (nameChanged && speciesRepository.existsByNameIgnoreCase(requestDTO.getName())) {
            throw new DuplicateSpeciesException(requestDTO.getName());
        }

        speciesMapper.updateEntityFromDto(requestDTO, species);
        Species updated = speciesRepository.save(species);

        log.info("Updated species with id: {}", updated.getId());
        return speciesMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteSpecies(Long id) {
        if (!speciesRepository.existsById(id)) {
            throw new SpeciesNotFoundException(id);
        }

        // App-level guard for a clean 409 with a clear message. The FK
        // constraint in Flyway is still the real backstop underneath -
        // same "app check + DB constraint" pattern as owner-service's
        // email uniqueness.
        if (petRepository.existsBySpecies_Id(id)) {
            throw new SpeciesInUseException(id);
        }

        speciesRepository.deleteById(id);
        log.info("Deleted species with id: {}", id);
    }
}
