package com.daimler.mobility.test.resource.service;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import com.daimler.mobility.test.resource.dao.repo.ResourceRepository;
import com.daimler.mobility.test.resource.exceptions.ResourceNotFoundException;
import com.daimler.mobility.test.resource.service.domain.AddResourceCommand;
import com.daimler.mobility.test.resource.service.domain.UpdateResourceCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    @Transactional
    public ResourceEntity addResource(AddResourceCommand addResourceCommand) {
        var resourceEntity = new ResourceEntity();
        resourceEntity.setName(addResourceCommand.getName());
        resourceEntity.setDescription(addResourceCommand.getDescription());
        resourceEntity.setContent(addResourceCommand.getContent());
        resourceEntity.setContentType(addResourceCommand.getContentType());
        resourceEntity.setActive(true);

        resourceEntity = resourceRepository.saveAndFlush(resourceEntity);

        return resourceEntity;
    }

    @Override
    @Transactional
    public ResourceEntity updateResource(UpdateResourceCommand command) {
        var entity = getResourceEntity(command.getId());

        entity.setName(command.getName() != null ? command.getName() : entity.getName());
        entity.setDescription(command.getDescription() != null ? command.getDescription() : entity.getDescription());
        entity.setContent(command.getContent() != null ? command.getContent() : entity.getContent());
        entity.setContentType(command.getContentType() != null ? command.getContentType() : entity.getContentType());
        entity.setActive(command.isActive() != null ? command.isActive() : entity.isActive());

        entity = resourceRepository.saveAndFlush(entity);

        return entity;
    }

    @Override
    @Transactional
    public ResourceEntity getResource(Long id) {
        return getResourceEntity(id);
    }

    @Override
    public List<ResourceEntity> getResources(boolean includeActive, boolean includeInactive) {

        List<ResourceEntity> resources;

        if (!includeActive && !includeInactive) {
            resources = Collections.emptyList();
        } else if (includeActive && includeInactive) {
            resources = resourceRepository.findAll();
        } else {
            resources = resourceRepository.findByActive(includeActive);
        }

        return resources;
    }

    @Override
    @Transactional
    public void deleteResource(Long id) {
        getResourceEntity(id);
        resourceRepository.deleteById(id);
    }

    private ResourceEntity getResourceEntity(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString()));
    }
}
