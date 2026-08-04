package com.petshop.owner.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petshop.owner.dto.OwnerRequestDTO;
import com.petshop.owner.dto.OwnerResponseDTO;
import com.petshop.owner.entity.Owner;
import com.petshop.owner.exception.DuplicateEmailException;
import com.petshop.owner.exception.OwnerNotFoundException;
import com.petshop.owner.mapper.OwnerMapper;
import com.petshop.owner.repository.OwnerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper ;

    @Override
    @Transactional
    public OwnerResponseDTO createOwner(OwnerRequestDTO data) {
        if (ownerRepository.existsByEmail(data.getEmail())) {
            throw new DuplicateKeyException(data.getEmail());
        }

        Owner owner = ownerMapper.toEntity(data);
        Owner saved = ownerRepository.save(owner);

        log.info("Created owner with id: {}", saved.getId());
        return ownerMapper.toResponseDTO(saved);
    }
 @Override
    @Transactional(readOnly = true)
    public OwnerResponseDTO getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));

        return ownerMapper.toResponseDTO(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OwnerResponseDTO> getAllOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable)
                .map(ownerMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public OwnerResponseDTO updateOwner(Long id, OwnerRequestDTO requestDTO) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));

        boolean emailChanged = !owner.getEmail().equalsIgnoreCase(requestDTO.getEmail());
        if (emailChanged && ownerRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateEmailException(requestDTO.getEmail());
        }

        ownerMapper.updateEntityFromDto(requestDTO, owner);
        Owner updated = ownerRepository.save(owner);

        log.info("Updated owner with id: {}", updated.getId());
        return ownerMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteOwner(Long id) {
        if (!ownerRepository.existsById(id)) {
            throw new OwnerNotFoundException(id);
        }

        ownerRepository.deleteById(id);
        log.info("Deleted owner with id: {}", id);
    }
    
}
