package com.daimler.mobility.test.resource.dao.repo;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ResourceRepositoryTest {

    @Autowired
    private ResourceRepository resourceRepository;

    @Test
    void saveResource() {
        ResourceEntity resource1 = new ResourceEntity();
        resource1.setActive(true);
        resource1.setName("1.pdf");
        resource1.setContent("dafmgal".getBytes(StandardCharsets.UTF_8));
        resource1.setContentType("application/pdf");

        // run
        ResourceEntity savedEntity = resourceRepository.saveAndFlush(resource1);

        assertNotNull(savedEntity);
        assertNotNull(resource1.getCreatedDate());
        assertNull(resource1.getModifiedBy());
        assertEquals(resource1.getCreatedDate(), resource1.getModifiedDate());
    }

    @Sql("findResources.sql")
    @Test
    void testFindActiveResources() {
        // run
        List<ResourceEntity> resources = resourceRepository.findByActive(true);

        assertNotNull(resources);
        assertEquals(2, resources.size());
        assertEquals(Set.of(3L, 1L), resources.stream().map(r -> r.getId()).collect(Collectors.toSet()));
    }

    @Sql("findResources.sql")
    @Test
    void testDeleteResource() {
        // run
        resourceRepository.deleteById(3L);

        List<ResourceEntity> resources = resourceRepository.findAll();

        assertNotNull(resources);
        assertEquals(2, resources.size());
        assertEquals(Set.of(2L, 1L), resources.stream().map(r -> r.getId()).collect(Collectors.toSet()));
    }

}