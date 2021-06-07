package com.daimler.mobility.test.resource.rest;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import com.daimler.mobility.test.resource.rest.dto.ResourceDto;
import com.daimler.mobility.test.resource.rest.dto.mappers.ResourceMapper;
import com.daimler.mobility.test.resource.service.ResourceService;
import com.daimler.mobility.test.resource.service.domain.AddResourceCommand;
import com.daimler.mobility.test.resource.service.domain.UpdateResourceCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/resources")
public class ResourceRestController implements ResourceRestApi {

    private final ResourceService resourceService;

    private final ResourceMapper resourceMapper;

    public ResourceRestController(ResourceService resourceService, ResourceMapper resourceMapper) {
        this.resourceService = resourceService;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public ResponseEntity addNewResource(MultipartFile file, String description)
            throws IOException {
        var command =
                AddResourceCommand.of(file.getOriginalFilename(), description, file.getBytes(), file.getContentType());

        var resourceEntity = resourceService.addResource(command);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/resources/" + resourceEntity.getId()))
                .build();
    }

    @Override
    public ResponseEntity changeResource(Long id,
                                         MultipartFile file,
                                         String description,
                                         Boolean active)
            throws IOException {
        var command = UpdateResourceCommand.of(
                id,
                file !=null ? file.getOriginalFilename() : null,
                description,
                file != null ? file.getBytes() : null,
                active,
                file != null ? file.getContentType() : null
        );
        resourceService.updateResource(command);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity deleteResource(Long id) throws IOException {
        resourceService.deleteResource(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<ResourceDto> getResource(Long id) {
        ResourceEntity resource = resourceService.getResource(id);
        return ResponseEntity.ok(resourceMapper.toDto(resource));
    }

    @Override
    public ResponseEntity<Collection<ResourceDto>> getResources(boolean includeActive, boolean includeInactive) {
        return ResponseEntity.ok(resourceMapper.toDto(resourceService.getResources(includeActive, includeInactive)));
    }

    @Override
    public void downloadResource(Long id, HttpServletResponse response) throws IOException {
        ResourceEntity resource = resourceService.getResource(id);

        response.setContentType(resource.getContentType());
        response.addHeader("Content-Disposition", "attachment; filename=" + resource.getName());
        response.getOutputStream().write(resource.getContent());
        response.getOutputStream().flush();
    }
}
