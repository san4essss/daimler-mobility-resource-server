package com.daimler.mobility.test.resource.rest.dto.mappers;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import com.daimler.mobility.test.resource.rest.dto.ResourceDto;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceMapperTest {

    @Test
    void toDto() {
        ResourceEntity resource1 = new ResourceEntity();
        resource1.setId(1L);
        resource1.setActive(true);
        resource1.setName("1.docx");
        resource1.setDescription("dfg");
        resource1.setContent("bla-bla".getBytes());
        resource1.setContentType("application/docx");

        ResourceMapper resourceMapper = new ResourceMapper();

        // run
        ResourceDto dto = resourceMapper.toDto(resource1);

        assertNotNull(dto);
        assertEquals(resource1.getId(), dto.getId());
        assertEquals(resource1.getName(), dto.getName());
        assertEquals(resource1.getDescription(), dto.getDescription());
        assertEquals(resource1.getContentType(), dto.getContentType());
        assertEquals(7, dto.getSize());
        assertEquals(resource1.getCreatedDate(), dto.getCreatedDate());
        assertEquals(resource1.getCreatedBy(), dto.getCreatedBy());
        assertEquals(resource1.getModifiedDate(), dto.getModifiedDate());
        assertEquals(resource1.getModifiedBy(), dto.getModifiedBy());
    }

    @Test
    void testToDto() {
        ResourceEntity resource1 = new ResourceEntity();
        resource1.setId(1L);
        resource1.setActive(true);
        resource1.setName("1.docx");
        resource1.setDescription("dfg");
        resource1.setContent("bla-bla".getBytes());
        resource1.setContentType("application/docx");

        ResourceEntity resource2 = new ResourceEntity();
        resource2.setId(2L);
        resource2.setActive(true);
        resource2.setName("2.docx");
        resource2.setDescription("dfg");
        resource2.setContent("bla-bla".getBytes());
        resource2.setContentType("application/docx");

        ResourceMapper resourceMapper = new ResourceMapper();

        // run
        Collection<ResourceDto> dtoList = resourceMapper.toDto(List.of(resource1, resource2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(Set.of(2L, 1L), dtoList.stream().map(ResourceDto::getId).collect(Collectors.toSet()));
    }
}