package com.petshop.pet.exception;

public class SpeciesInUseException extends RuntimeException {

    public SpeciesInUseException(Long id) {
        super("Cannot delete species with id " + id + ": one or more pets still reference it");
    }
}
