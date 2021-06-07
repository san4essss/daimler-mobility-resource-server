package com.daimler.mobility.test.resource.service;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import com.daimler.mobility.test.resource.dao.repo.ResourceRepository;
import com.daimler.mobility.test.resource.exceptions.ResourceNotFoundException;
import com.daimler.mobility.test.resource.service.domain.AddResourceCommand;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ResourceServiceTest {

    @Test
    void addResource() {
        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        AddResourceCommand command = AddResourceCommand.of(
                "1.pdf", "bla-bla", "abcd".getBytes(), "application/pdf");

        when(resourceRepository.saveAndFlush(any(ResourceEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);

        // run
        ResourceEntity resourceEntity = resourceService.addResource(command);

        assertEquals(resourceEntity.getName(), command.getName());
        assertEquals(resourceEntity.getDescription(), command.getDescription());
        assertEquals(resourceEntity.getContent(), command.getContent());
        assertEquals(resourceEntity.getContentType(), command.getContentType());
        assertTrue(resourceEntity.isActive());
    }

    @Test
    void getResource() {
        ResourceEntity resourceEntity = createResourceEntity();
        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        when(resourceRepository.findById(eq(resourceEntity.getId()))).thenReturn(Optional.of(resourceEntity));

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        ResourceEntity resourceEntityReturned = resourceService.getResource(resourceEntity.getId());

        assertEquals(resourceEntity, resourceEntityReturned);
    }

    @Test
    void getNotExistingResource() {
        Long resourceId = 1L;
        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        when(resourceRepository.findById(any())).thenReturn(Optional.empty());
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);

        // run
        assertThrows(ResourceNotFoundException.class, () -> resourceService.getResource(resourceId));
    }

    @Test
    void getActiveResources() {
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

        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        when(resourceRepository.findByActive(eq(true))).thenReturn(List.of(resource1, resource2));

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        List<ResourceEntity> activeResources = resourceService.getResources(true, false);

        assertNotNull(activeResources);
        assertEquals(2, activeResources.size());
        assertEquals(Set.of(resource1.getId(), resource2.getId()),
                activeResources.stream().map(ResourceEntity::getId).collect(Collectors.toSet()));
    }

    @Test
    void getActiveResources_noActiveResources() {
        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        when(resourceRepository.findByActive(eq(true))).thenReturn(Collections.emptyList());

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        List<ResourceEntity> activeResources = resourceService.getResources(true, false);

        assertNotNull(activeResources);
        assertEquals(0, activeResources.size());
    }

    @Test
    void getAllResources() {
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

        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        when(resourceRepository.findAll()).thenReturn(List.of(resource1, resource2));

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        List<ResourceEntity> activeResources = resourceService.getResources(true, true);

        assertNotNull(activeResources);
        assertEquals(2, activeResources.size());
        assertEquals(Set.of(resource1.getId(), resource2.getId()),
                activeResources.stream().map(ResourceEntity::getId).collect(Collectors.toSet()));
    }

    @Test
    void deleteResource() {
        ResourceEntity resourceEntity = createResourceEntity();
        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        when(resourceRepository.findById(eq(resourceEntity.getId()))).thenReturn(Optional.of(resourceEntity));

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        resourceService.deleteResource(resourceEntity.getId());

        verify(resourceRepository).deleteById(resourceEntity.getId());
    }

    @Test
    void deleteNotExistingResource() {
        Long resourceId = 1L;
        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        when(resourceRepository.findById(any())).thenReturn(Optional.empty());
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);

        // run
        assertThrows(ResourceNotFoundException.class, () -> resourceService.deleteResource(resourceId));
    }

    private ResourceEntity createResourceEntity() {
        ResourceEntity resource = new ResourceEntity();
        resource.setId(1L);
        resource.setActive(false);
        resource.setName("1.docx");
        resource.setDescription("dfg");
        resource.setContent("bla-bla".getBytes());
        resource.setContentType("application/docx");
        return resource;
    }
}