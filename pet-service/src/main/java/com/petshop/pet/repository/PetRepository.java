package com.petshop.pet.repository;

import com.petshop.pet.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Page<Pet> findByOwnerId(Long ownerId, Pageable pageable);

    Page<Pet> findBySpecies_NameIgnoreCase(String speciesName, Pageable pageable);

    boolean existsBySpecies_Id(Long speciesId);
}
