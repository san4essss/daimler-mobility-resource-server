package com.daimler.mobility.test.resource.rest.dto.mappers;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import com.daimler.mobility.test.resource.rest.dto.ResourceDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class ResourceMapper {
    public ResourceDto toDto(ResourceEntity entity) {
        var dto = new ResourceDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSize(entity.getContent().length);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedDate(entity.getModifiedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setActive(entity.isActive());
        dto.setContentType(entity.getContentType());

        return dto;
    }

    public Collection<ResourceDto> toDto(Collection<ResourceEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
