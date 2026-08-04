package com.petshop.owner.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Owner already exists with email: " + email);
    }
}
