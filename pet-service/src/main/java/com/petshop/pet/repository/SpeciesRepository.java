package com.petshop.pet.repository;

import com.petshop.pet.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface SpeciesRepository extends JpaRepository<Species, Long> {

    Optional<Species> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
