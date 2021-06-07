package com.daimler.mobility.test.resource.service;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import com.daimler.mobility.test.resource.dao.repo.ResourceRepository;
import com.daimler.mobility.test.resource.exceptions.ResourceNotFoundException;
import com.daimler.mobility.test.resource.service.domain.UpdateResourceCommand;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResourceUpdateServiceTest {

    @Test
    void updateResource() {
        ResourceEntity resource = createResourceEntity();

        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        UpdateResourceCommand command = UpdateResourceCommand.of(
                1L, "2.pdf", "bla-bla", "abcd".getBytes(), true,"application/pdf");

        when(resourceRepository.findById(eq(1L))).thenReturn(Optional.of(resource));
        when(resourceRepository.saveAndFlush(any(ResourceEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        ResourceEntity resourceUpdated = resourceService.updateResource(command);

        assertEquals(resourceUpdated.getName(), command.getName());
        assertEquals(resourceUpdated.getDescription(), command.getDescription());
        assertEquals(resourceUpdated.getContent(), command.getContent());
        assertEquals(resourceUpdated.getContentType(), command.getContentType());
        assertEquals(resourceUpdated.isActive(), command.isActive());
    }

    @Test
    void updateResource_onlyActiveProperty() {
        ResourceEntity resource = createResourceEntity();

        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        UpdateResourceCommand command = UpdateResourceCommand.of(
                1L, resource.getName(), resource.getDescription(), resource.getContent(), true, resource.getContentType());

        when(resourceRepository.findById(eq(1L))).thenReturn(Optional.of(resource));
        when(resourceRepository.saveAndFlush(any(ResourceEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        ResourceEntity resourceUpdated = resourceService.updateResource(command);

        assertEquals(resourceUpdated.getName(), resource.getName());
        assertEquals(resourceUpdated.getDescription(), resource.getDescription());
        assertEquals(resourceUpdated.getContent(), resource.getContent());
        assertEquals(resourceUpdated.getContentType(), resource.getContentType());
        assertEquals(resourceUpdated.isActive(), command.isActive());
    }

    @Test
    void updateResource_onlyName() {
        ResourceEntity resource = createResourceEntity();

        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        UpdateResourceCommand command = UpdateResourceCommand.of(
                1L, "new.docx", resource.getDescription(), resource.getContent(), resource.isActive(), resource.getContentType());

        when(resourceRepository.findById(eq(1L))).thenReturn(Optional.of(resource));
        when(resourceRepository.saveAndFlush(any(ResourceEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        ResourceEntity resourceUpdated = resourceService.updateResource(command);

        assertEquals(resourceUpdated.getName(), command.getName());
        assertEquals(resourceUpdated.getDescription(), resource.getDescription());
        assertEquals(resourceUpdated.getContent(), resource.getContent());
        assertEquals(resourceUpdated.getContentType(), resource.getContentType());
        assertEquals(resourceUpdated.isActive(), resource.isActive());
    }

    @Test
    void updateResource_onlyDescription() {
        ResourceEntity resource = createResourceEntity();

        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        UpdateResourceCommand command = UpdateResourceCommand.of(
                1L, resource.getName(), "new description", resource.getContent(), resource.isActive(), resource.getContentType());

        when(resourceRepository.findById(eq(1L))).thenReturn(Optional.of(resource));
        when(resourceRepository.saveAndFlush(any(ResourceEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        ResourceEntity resourceUpdated = resourceService.updateResource(command);

        assertEquals(resourceUpdated.getName(), resource.getName());
        assertEquals(resourceUpdated.getDescription(), command.getDescription());
        assertEquals(resourceUpdated.getContent(), resource.getContent());
        assertEquals(resourceUpdated.getContentType(), resource.getContentType());
        assertEquals(resourceUpdated.isActive(), resource.isActive());
    }

    @Test
    void updateResourceOnlyActiveProperty() {
        ResourceEntity resource = createResourceEntity();

        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        UpdateResourceCommand command = UpdateResourceCommand.of(
                1L, resource.getName(), resource.getDescription(), "foo".getBytes(), resource.isActive(), resource.getContentType());

        when(resourceRepository.findById(eq(1L))).thenReturn(Optional.of(resource));
        when(resourceRepository.saveAndFlush(any(ResourceEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        ResourceEntity resourceUpdated = resourceService.updateResource(command);

        assertEquals(resourceUpdated.getName(), resource.getName());
        assertEquals(resourceUpdated.getDescription(), resource.getDescription());
        assertEquals(resourceUpdated.getContent(), command.getContent());
        assertEquals(resourceUpdated.getContentType(), resource.getContentType());
        assertEquals(resourceUpdated.isActive(), resource.isActive());
    }

    @Test
    void updateResource_onlyContentType() {
        ResourceEntity resource = createResourceEntity();

        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        UpdateResourceCommand command = UpdateResourceCommand.of(
                1L, resource.getName(), resource.getDescription(), resource.getContent(), resource.isActive(), "application/pdf");

        when(resourceRepository.findById(eq(1L))).thenReturn(Optional.of(resource));
        when(resourceRepository.saveAndFlush(any(ResourceEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // run
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);
        ResourceEntity resourceUpdated = resourceService.updateResource(command);

        assertEquals(resourceUpdated.getName(), resource.getName());
        assertEquals(resourceUpdated.getDescription(), resource.getDescription());
        assertEquals(resourceUpdated.getContent(), resource.getContent());
        assertEquals(resourceUpdated.getContentType(), command.getContentType());
        assertEquals(resourceUpdated.isActive(), resource.isActive());
    }

    @Test
    void updateNotExistingResource() {
        UpdateResourceCommand command = UpdateResourceCommand.of(
                1L, "asd", "ASfd", "sdf".getBytes(), true, "application/pdf");

        ResourceRepository resourceRepository = mock(ResourceRepository.class);
        when(resourceRepository.findById(any())).thenReturn(Optional.empty());
        ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceRepository);

        // run
        assertThrows(ResourceNotFoundException.class, () -> resourceService.updateResource(command));
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