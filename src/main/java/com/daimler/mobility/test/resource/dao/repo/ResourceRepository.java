package com.daimler.mobility.test.resource.dao.repo;

import com.daimler.mobility.test.resource.dao.entity.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {
    List<ResourceEntity> findByActive(boolean active);
}
