package com.petshop.pet.exception;

public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException(Long id) {
        super("Pet not found with id: " + id);
    }

    public PetNotFoundException(String message) {
        super(message);
    }
}
