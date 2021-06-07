package com.daimler.mobility.test.resource.rest;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import com.daimler.mobility.test.resource.rest.dto.mappers.ResourceMapper;
import com.daimler.mobility.test.resource.service.ResourceService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResourceRestController.class)
@Import(AopAutoConfiguration.class)
@WithMockUser(username = "user", roles={"USER"})
class ResourceRestApiWithUserRoleTest {

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
        mvc.perform(MockMvcRequestBuilders.multipart("/api/resources")
                        .file("file", "qwe".getBytes())
                        .param("description", "desc1")
                        .with(csrf())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void changeResource() throws Exception {
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
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void deleteResource() throws Exception {
        mvc.perform(delete("/api/resources/1").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllResource() throws Exception {
        mvc.perform(get("/api/resources?inactive=true"))
                .andExpect(status().isOk());
    }

    @Test
    void getResource() throws Exception {
        mvc.perform(get("/api/resources/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void getActiveResources() throws Exception {
        mvc.perform(get("/api/resources").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void downloadResource() throws Exception {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setId(1L);
        resourceEntity.setName("name.pdf");
        resourceEntity.setContentType("application/pdf");
        resourceEntity.setContent("content".getBytes());

        when(resourceService.getResource(any())).thenReturn(resourceEntity);
        mvc.perform(get("/api/resources/1/download").with(csrf()))
                .andExpect(status().isOk());
    }
}