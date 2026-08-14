package com.petshop.pet.mapper;

import com.petshop.pet.dto.SpeciesRequestDTO;
import com.petshop.pet.dto.SpeciesResponseDTO;
import com.petshop.pet.entity.Species;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SpeciesMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Species toEntity(SpeciesRequestDTO dto);

    SpeciesResponseDTO toResponseDTO(Species entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(SpeciesRequestDTO dto, @MappingTarget Species entity);
}
