package com.petshop.pet.exception;

public class DuplicateSpeciesException extends RuntimeException {

    public DuplicateSpeciesException(String name) {
        super("Species already exists with name: " + name);
    }
}
