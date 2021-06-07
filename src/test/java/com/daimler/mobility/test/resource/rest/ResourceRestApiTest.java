package com.daimler.mobility.test.resource.rest;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import com.daimler.mobility.test.resource.exceptions.ResourceNotFoundException;
import com.daimler.mobility.test.resource.rest.dto.ResourceDto;
import com.daimler.mobility.test.resource.rest.dto.mappers.ResourceMapper;
import com.daimler.mobility.test.resource.service.ResourceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResourceRestController.class)
@Import(AopAutoConfiguration.class)
@WithMockUser(username = "admin", roles={"ADMIN"})
class ResourceRestApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private ResourceMapper resourceMapper;

    @BeforeEach
    public void resetMocks() {
        Mockito.reset(resourceService, resourceMapper);
    }

    @Test
    void addNewResource() throws Exception {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setId(1L);
        when(resourceService.addResource(any())).thenReturn(resourceEntity);
        MockMultipartFile firstFile = new MockMultipartFile("file", "1.pdf", "application/pdf", "some pdf".getBytes());

        //run
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.multipart("/api/resources")
                        .file(firstFile)
                        .param("description", "desc1")
                        .with(csrf())
                )
                .andExpect(status().isCreated())
                .andReturn();

        String locationHeader = mvcResult.getResponse().getHeader("Location");

        Assertions.assertNotNull(locationHeader);
        assertEquals("/resources/" + resourceEntity.getId(), locationHeader);
    }

    @Test
    void changeResource() throws Exception {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setId(1L);
        when(resourceService.addResource(any())).thenReturn(resourceEntity);
        MockMultipartFile file = new MockMultipartFile("file", "1.pdf", "application/pdf", "some pdf".getBytes());

        MockHttpServletRequestBuilder requestBuilder = multipart("/api/resources/1")
                .file(file)
                .with(request -> {
                    request.setMethod("PATCH");
                    return request;
                });

        //run
        mvc.perform(requestBuilder
                .param("description", "desc1")
                .with(csrf()))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void changeNotExistingResource() throws Exception {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setId(1L);
        when(resourceService.updateResource(any())).thenThrow(new ResourceNotFoundException("1"));
        MockMultipartFile file = new MockMultipartFile("file", "1.pdf", "application/pdf", "some pdf".getBytes());
        MockHttpServletRequestBuilder requestBuilder = multipart("/api/resources/1")
                .file(file)
                .with(request -> {
                    request.setMethod("PATCH");
                    return request;
                });

        //run
        mvc.perform(requestBuilder
                .param("description", "desc1")
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void deleteResource() throws Exception {
        MvcResult mvcResult = mvc
                .perform(delete("/api/resources/1").with(csrf()))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void deleteNotExistingResource() throws Exception {
        doThrow(new ResourceNotFoundException("1")).when(resourceService).deleteResource(any(Long.class));
        MvcResult mvcResult = mvc
                .perform(delete("/api/resources/1").with(csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getResource() throws Exception {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setId(1L);
        ResourceDto dto = new ResourceDto();
        dto.setId(1L);

        when(resourceService.getResource(any())).thenReturn(resourceEntity);
        when(resourceMapper.toDto(any(ResourceEntity.class))).thenReturn(dto);

        MvcResult mvcResult = mvc
                .perform(get("/api/resources/1"))
                .andExpect(status().isOk())
                .andReturn();

        ResourceDto resourceDtoResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResourceDto.class);
        assertEquals(dto.getId(), resourceDtoResult.getId());
    }

    @Test
    void getNotExistingResource() throws Exception {
        when(resourceService.getResource(any())).thenThrow(new ResourceNotFoundException("1"));

        MvcResult mvcResult = mvc
                .perform(get("/api/resources/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getActiveResources() throws Exception {
        MvcResult mvcResult = mvc
                .perform(get("/api/resources"))
                .andExpect(status().isOk())
                .andReturn();

        verify(resourceService).getResources(true, false);
    }

    @Test
    void getAllResources() throws Exception {
        MvcResult mvcResult = mvc
                .perform(get("/api/resources?inactive=true&active=true"))
                .andExpect(status().isOk())
                .andReturn();
        verify(resourceService).getResources(true, true);
    }

    @Test
    void downloadResource() throws Exception {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setId(1L);
        resourceEntity.setName("name.pdf");
        resourceEntity.setContentType("application/pdf");
        resourceEntity.setContent("content".getBytes());

        when(resourceService.getResource(any())).thenReturn(resourceEntity);

        MvcResult mvcResult = mvc
                .perform(get("/api/resources/1/download"))
                .andExpect(status().isOk())
                .andReturn();

        String resourceContent = mvcResult.getResponse().getContentAsString();
        assertEquals(new String(resourceEntity.getContent()), resourceContent);
    }

    @Test
    void downloadNotExistingResource() throws Exception {
        when(resourceService.getResource(any())).thenThrow(new ResourceNotFoundException("1"));

        mvc.perform(get("/api/resources/1/download"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}