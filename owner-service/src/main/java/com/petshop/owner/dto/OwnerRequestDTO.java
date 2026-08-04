package com.petshop.owner.dto;

import lombok.Data;

@Data
public class OwnerRequestDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;
}