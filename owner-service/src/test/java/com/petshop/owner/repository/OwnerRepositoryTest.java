package com.petshop.owner.repository;

import com.petshop.owner.entity.Owner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class OwnerRepositoryTest {
        /* 

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void shouldSaveAndFindOwnerByEmail() {
        Owner owner = Owner.builder()
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .phone("123456789")
                .build();

        ownerRepository.save(owner);

        assertThat(ownerRepository.findByEmail("yasser.ahmed@example.com")).isPresent();
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        Owner owner = Owner.builder()
                .firstName("Achraf")
                .lastName("Hakimi")
                .email("achraf.hakimi@example.com")
                .build();

        ownerRepository.save(owner);

        assertThat(ownerRepository.existsByEmail("achraf.hakimi@example.com")).isTrue();
        assertThat(ownerRepository.existsByEmail("deosntexist@example.com")).isFalse();
    }

    @Test
    void shouldEnforceUniqueEmailConstraintAtDatabaseLevel() {
        // This proves the Flyway UNIQUE constraint itself works, independent
        // of the app-level existsByEmail() check, which has a race-condition
        // gap under concurrent requests. The DB constraint is the real backstop.
        Owner first = Owner.builder()
                .firstName("Hakim")
                .lastName("Ziyach")
                .email("duplicate@example.com")
                .build();
        ownerRepository.saveAndFlush(first);

        Owner second = Owner.builder()
                .firstName("Noussayr")
                .lastName("Mazraoui")
                .email("duplicate@example.com")
                .build();

        assertThatThrownBy(() -> ownerRepository.saveAndFlush(second))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldFindOwnersByLastNameCaseInsensitive() {
        ownerRepository.save(Owner.builder()
                .firstName("Ibrahim")
                .lastName("Diaz")
                .email("ibrahim.diaz@example.com")
                .build());
        ownerRepository.save(Owner.builder()
                .firstName("Achraf")
                .lastName("Hakimi")
                .email("achraf.hakimi@example.com")
                .build());

        Page<Owner> results = ownerRepository.findByLastNameContainingIgnoreCase(
                "hakimi", PageRequest.of(0, 10));

        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent().get(0).getEmail()).isEqualTo("achraf.hakimi@example.com");
    }
    */
}
