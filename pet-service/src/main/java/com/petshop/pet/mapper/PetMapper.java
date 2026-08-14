package com.petshop.pet.mapper;

import com.petshop.pet.dto.PetRequestDTO;
import com.petshop.pet.dto.PetResponseDTO;
import com.petshop.pet.entity.Pet;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = SpeciesMapper.class)
public interface PetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "species", ignore = true) // resolved in the service via SpeciesRepository - MapStruct can't do repository lookups
    Pet toEntity(PetRequestDTO dto);

    PetResponseDTO toResponseDTO(Pet entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "species", ignore = true) // same - updated explicitly in the service
    void updateEntityFromDto(PetRequestDTO dto, @MappingTarget Pet entity);
}
