package com.petshop.owner.exception;

public class OwnerNotFoundException extends RuntimeException {

    public OwnerNotFoundException(Long id) {
        super("Owner not found with id: " + id);
    }

    public OwnerNotFoundException(String message) {
        super(message);
    }
}

