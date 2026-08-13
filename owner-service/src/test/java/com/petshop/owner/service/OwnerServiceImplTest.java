package com.petshop.owner.service;

import com.petshop.owner.dto.OwnerRequestDTO;
import com.petshop.owner.dto.OwnerResponseDTO;
import com.petshop.owner.entity.Owner;
import com.petshop.owner.exception.DuplicateEmailException;
import com.petshop.owner.exception.OwnerNotFoundException;
import com.petshop.owner.mapper.OwnerMapper;
import com.petshop.owner.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceImplTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private OwnerMapper ownerMapper;

    @InjectMocks
    private OwnerServiceImpl ownerService;

    private Owner owner;
    private OwnerRequestDTO requestDTO;
    private OwnerResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        owner = Owner.builder()
                .id(1L)
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .phone("123456789")
                .build();

        requestDTO = OwnerRequestDTO.builder()
                .firstName("Achraf")
                .lastName("Hakimi")
                .email("achraf.hakimi@example.com")
                .phone("123456789")
                .build();

        responseDTO = OwnerResponseDTO.builder()
                .id(1L)
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .phone("123456789")
                .build();
    }

    @Test
    void shouldCreateOwnerSuccessfully() {
        when(ownerRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(ownerMapper.toEntity(requestDTO)).thenReturn(owner);
        when(ownerRepository.save(owner)).thenReturn(owner);
        when(ownerMapper.toResponseDTO(owner)).thenReturn(responseDTO);

        OwnerResponseDTO result = ownerService.createOwner(requestDTO);

        assertThat(result.getEmail()).isEqualTo("yasser.ahmed@example.com");
        verify(ownerRepository).save(owner);
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        when(ownerRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> ownerService.createOwner(requestDTO))
                .isInstanceOf(DuplicateEmailException.class);

        verify(ownerRepository, never()).save(any());
    }

    @Test
    void shouldReturnOwnerWhenFound() {
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(ownerMapper.toResponseDTO(owner)).thenReturn(responseDTO);

        OwnerResponseDTO result = ownerService.getOwnerById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldThrowWhenOwnerNotFound() {
        when(ownerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ownerService.getOwnerById(99L))
                .isInstanceOf(OwnerNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldUpdateOwnerWhenEmailUnchanged() {
        OwnerRequestDTO updateRequest = OwnerRequestDTO.builder()
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com") // same email as existing owner
                .phone("999999999")
                .build();

        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(ownerRepository.save(owner)).thenReturn(owner);
        when(ownerMapper.toResponseDTO(owner)).thenReturn(responseDTO);

        ownerService.updateOwner(1L, updateRequest);

        // Same email as before -> existsByEmail should never even be checked
        verify(ownerRepository, never()).existsByEmail(anyString());
        verify(ownerMapper).updateEntityFromDto(updateRequest, owner);
        verify(ownerRepository).save(owner);
    }

    @Test
    void shouldUpdateOwnerWhenEmailChangedToAvailableOne() {
        OwnerRequestDTO updateRequest = OwnerRequestDTO.builder()
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("new.email@example.com")
                .phone("123456789")
                .build();

        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(ownerRepository.existsByEmail("new.email@example.com")).thenReturn(false);
        when(ownerRepository.save(owner)).thenReturn(owner);
        when(ownerMapper.toResponseDTO(owner)).thenReturn(responseDTO);

        ownerService.updateOwner(1L, updateRequest);

        verify(ownerRepository).existsByEmail("new.email@example.com");
        verify(ownerRepository).save(owner);
    }

    @Test
    void shouldThrowWhenUpdateEmailBelongsToAnotherOwner() {
        OwnerRequestDTO updateRequest = OwnerRequestDTO.builder()
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("taken@example.com")
                .phone("123456789")
                .build();

        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(ownerRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThatThrownBy(() -> ownerService.updateOwner(1L, updateRequest))
                .isInstanceOf(DuplicateEmailException.class);

        verify(ownerRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentOwner() {
        when(ownerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ownerService.updateOwner(99L, requestDTO))
                .isInstanceOf(OwnerNotFoundException.class);

        verify(ownerRepository, never()).save(any());
    }

    @Test
    void shouldReturnPagedOwners() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Owner> ownerPage = new PageImpl<>(List.of(owner), pageable, 1);

        when(ownerRepository.findAll(pageable)).thenReturn(ownerPage);
        when(ownerMapper.toResponseDTO(owner)).thenReturn(responseDTO);

        Page<OwnerResponseDTO> result = ownerService.getAllOwners(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("yasser.ahmed@example.com");
    }

    @Test
    void shouldDeleteOwnerWhenExists() {
        when(ownerRepository.existsById(1L)).thenReturn(true);

        ownerService.deleteOwner(1L);

        verify(ownerRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentOwner() {
        when(ownerRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> ownerService.deleteOwner(99L))
                .isInstanceOf(OwnerNotFoundException.class);

        verify(ownerRepository, never()).deleteById(99L);
    }
}
