package com.petshop.owner.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pure Bean Validation tests - no Spring context, no database, no MockMvc.
 * These run in milliseconds and pin down exactly what OwnerRequestDTO
 * accepts and rejects, independent of the HTTP layer.
 */
class OwnerRequestDTOValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    private OwnerRequestDTO validDto() {
        return OwnerRequestDTO.builder()
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .phone("123456789")
                .build();
    }

    @Test
    void shouldHaveNoViolationsForValidDto() {
        Set<ConstraintViolation<OwnerRequestDTO>> violations = validator.validate(validDto());
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldAllowEmptyPhone() {
        OwnerRequestDTO dto = validDto();
        dto.setPhone("");

        Set<ConstraintViolation<OwnerRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldRejectBlankFirstName() {
        OwnerRequestDTO dto = validDto();
        dto.setFirstName("  ");

        Set<ConstraintViolation<OwnerRequestDTO>> violations = validator.validate(dto);
        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("firstName");
    }

    @Test
    void shouldRejectFirstNameOver100Characters() {
        OwnerRequestDTO dto = validDto();
        dto.setFirstName("A".repeat(101));

        Set<ConstraintViolation<OwnerRequestDTO>> violations = validator.validate(dto);
        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("firstName");
    }

    @Test
    void shouldAllowFirstNameAtExactly100Characters() {
        OwnerRequestDTO dto = validDto();
        dto.setFirstName("A".repeat(100));

        Set<ConstraintViolation<OwnerRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "not-an-email",
            "missing-at-sign.com",
            "@no-local-part.com",
            "no-domain@",
            "spaces in@email.com"
    })
    void shouldRejectInvalidEmailFormats(String invalidEmail) {
        OwnerRequestDTO dto = validDto();
        dto.setEmail(invalidEmail);

        Set<ConstraintViolation<OwnerRequestDTO>> violations = validator.validate(dto);
        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("email");
    }

    @Test
    void shouldRejectBlankEmail() {
        OwnerRequestDTO dto = validDto();
        dto.setEmail("");

        Set<ConstraintViolation<OwnerRequestDTO>> violations = validator.validate(dto);
        // Blank triggers BOTH @NotBlank and @Email, so at least one violation on "email"
        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("email");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc-not-digits",
            "123-abc-4567",
            "12345"
    })
    void shouldRejectInvalidPhoneFormats(String invalidPhone) {
        OwnerRequestDTO dto = validDto();
        dto.setPhone(invalidPhone);

        Set<ConstraintViolation<OwnerRequestDTO>> violations = validator.validate(dto);
        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("phone");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123-456-7890",
            "+1 234 567 8900",
            "(123) 456-7890",
            "1234567"
    })
    void shouldAcceptValidPhoneFormats(String validPhone) {
        OwnerRequestDTO dto = validDto();
        dto.setPhone(validPhone);

        Set<ConstraintViolation<OwnerRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }
}
