package com.petshop.owner.mapper;

import com.petshop.owner.dto.OwnerRequestDTO;
import com.petshop.owner.dto.OwnerResponseDTO;
import com.petshop.owner.entity.Owner;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    Owner toEntity(OwnerRequestDTO dto);

    OwnerResponseDTO toResponseDTO(Owner entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(OwnerRequestDTO dto, @MappingTarget Owner entity);
}