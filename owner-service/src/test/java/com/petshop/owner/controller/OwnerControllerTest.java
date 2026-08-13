package com.petshop.owner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petshop.owner.dto.OwnerRequestDTO;
import com.petshop.owner.dto.OwnerResponseDTO;
import com.petshop.owner.exception.OwnerNotFoundException;
import com.petshop.owner.service.OwnerService;
import org.junit.jupiter.api.Test;
import com.petshop.owner.exception.DuplicateEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OwnerService ownerService;

    @Test
    void shouldCreateOwnerAndReturn201() throws Exception {
        OwnerRequestDTO request = OwnerRequestDTO.builder()
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .phone("123456789")
                .build();

        OwnerResponseDTO response = OwnerResponseDTO.builder()
                .id(1L)
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .phone("123456789")
                .build();

        when(ownerService.createOwner(any(OwnerRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("yasser.ahmed@example.com"));
    }

    @Test
    void shouldReturn400WhenValidationFails() throws Exception {
        OwnerRequestDTO invalidRequest = OwnerRequestDTO.builder()
                .firstName("")
                .lastName("Ahmed")
                .email("not-an-email")
                .build();

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOwnerById() throws Exception {
        OwnerResponseDTO response = OwnerResponseDTO.builder()
                .id(1L)
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .build();

        when(ownerService.getOwnerById(1L)).thenReturn(response);

        mockMvc.perform(get("/owners/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Yasser"));
    }

    @Test
    void shouldReturn404WhenOwnerNotFound() throws Exception {
        when(ownerService.getOwnerById(99L)).thenThrow(new OwnerNotFoundException(99L));

        mockMvc.perform(get("/owners/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldDeleteOwnerAndReturn204() throws Exception {
        mockMvc.perform(delete("/owners/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateOwnerAndReturn200() throws Exception {
        OwnerRequestDTO request = OwnerRequestDTO.builder()
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .phone("999999999")
                .build();

        OwnerResponseDTO response = OwnerResponseDTO.builder()
                .id(1L)
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .phone("999999999")
                .build();

        when(ownerService.updateOwner(eq(1L), any(OwnerRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/owners/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Yasser"))
                .andExpect(jsonPath("$.phone").value("999999999"));
    }

    @Test
    void shouldReturn409WhenUpdateEmailConflicts() throws Exception {
        OwnerRequestDTO request = OwnerRequestDTO.builder()
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("taken@example.com")
                .phone("123456789")
                .build();

        when(ownerService.updateOwner(eq(1L), any(OwnerRequestDTO.class)))
                .thenThrow(new DuplicateEmailException("taken@example.com"));

        mockMvc.perform(put("/owners/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnPagedOwnersList() throws Exception {
        OwnerResponseDTO response = OwnerResponseDTO.builder()
                .id(1L)
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .build();

        Page<OwnerResponseDTO> page = new PageImpl<>(List.of(response));

        when(ownerService.getAllOwners(any())).thenReturn(page);

        mockMvc.perform(get("/owners")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("yasser.ahmed@example.com"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldReturn400OnMalformedJson() throws Exception {
        String malformedJson = "{ \"firstName\": \"Yasser\", \"lastName\": ";

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn415WhenContentTypeMissing() throws Exception {
        OwnerRequestDTO request = OwnerRequestDTO.builder()
                .firstName("Yasser")
                .lastName("Ahmed")
                .email("yasser.ahmed@example.com")
                .build();

        mockMvc.perform(post("/owners")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnsupportedMediaType());
    }
}
