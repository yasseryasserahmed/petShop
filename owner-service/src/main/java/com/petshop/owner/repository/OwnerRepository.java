package com.petshop.owner.repository;

import com.petshop.owner.entity.Owner;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Owner> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
}
