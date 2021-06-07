package com.daimler.mobility.test.resource.service;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import com.daimler.mobility.test.resource.service.domain.AddResourceCommand;
import com.daimler.mobility.test.resource.service.domain.UpdateResourceCommand;

import java.util.List;

/**
 * Operations to manage file resources
 */
public interface ResourceService {

    /**
     * Creates new resource
     * @param addResourceCommand resource data
     * @return created resource entity object
     */
    ResourceEntity addResource(AddResourceCommand addResourceCommand);

    /**
     * Updates resource with given id and data
     * @param command
     * @return updated resource entity object
     * @throws com.daimler.mobility.test.resource.exceptions.ResourceNotFoundException
     */
    ResourceEntity updateResource(UpdateResourceCommand command);

    /**
     * Returns resource by id
     * @param id resource identifier
     * @return resource entity object
     * @throws com.daimler.mobility.test.resource.exceptions.ResourceNotFoundException
     */
    ResourceEntity getResource(Long id);

    /**
     * Returns resources
     * @param includeActive if include active resources in result
     * @param includeInactive if include inactive resources in result
     * @return a list of all available resources if active=null, active resources if active=true, not active resources if active=false
     */
    List<ResourceEntity> getResources(boolean includeActive, boolean includeInactive);

    /**
     * Delete resource by id
     * @param id resource identifier
     */
    void deleteResource(Long id);
}
