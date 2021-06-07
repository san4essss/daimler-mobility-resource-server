package com.daimler.mobility.test.resource.rest;

import com.daimler.mobility.test.resource.rest.dto.ResourceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public interface ResourceRestApi {

    @Tag(name = "UI to server")
    @Operation(summary = "Creates a new resource")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    ResponseEntity addNewResource(@RequestParam("file") MultipartFile file,
                                  @RequestParam(value = "description", required = false) String description)
            throws IOException;

    @Tag(name = "UI to server")
    @Operation(summary = "Change partially resource's data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}")
    ResponseEntity changeResource(@PathVariable Long id,
                                  @RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam(value = "description", required = false) String description,
                                  @RequestParam(value = "active", required = false) Boolean active)
            throws IOException;

    @Tag(name = "UI to server")
    @Operation(summary = "Deletes existing resource by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity deleteResource(@PathVariable Long id) throws IOException;

    @Tag(name = "Other systems")
    @Operation(summary = "Return all resources or only active/inactive depending on request parameters")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    ResponseEntity<Collection<ResourceDto>> getResources(
            @RequestParam(name = "active", required = false, defaultValue = "true") boolean includeActive,
            @RequestParam(name = "inactive", required = false, defaultValue = "false") boolean includeInactive);

    @Tag(name = "Other systems")
    @Operation(summary = "Return resource metadata")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    ResponseEntity<ResourceDto> getResource(@PathVariable Long id);

    @Tag(name = "Other systems")
    @Operation(summary = "Return resource content")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}/download")
    void downloadResource(@PathVariable Long id, HttpServletResponse response) throws IOException;
}
