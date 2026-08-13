package com.petshop.owner.service;

import com.petshop.owner.dto.OwnerResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.petshop.owner.dto.OwnerRequestDTO;;

public interface OwnerService {
     // create a new owner
    OwnerResponseDTO createOwner(OwnerRequestDTO data );
    //retrieve owner by its id 
    OwnerResponseDTO getOwnerById(Long id);
    // get a page 'interface impl iterable' of all owners 
    Page<OwnerResponseDTO> getAllOwners(Pageable page); 
    // update owner data using id for identification 
    OwnerResponseDTO updateOwner(Long id, OwnerRequestDTO data);
    //delete owner by id 
    void deleteOwner(Long id);
    
}