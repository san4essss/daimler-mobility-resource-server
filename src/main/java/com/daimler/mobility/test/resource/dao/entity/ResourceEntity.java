package com.daimler.mobility.test.resource.dao.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "resources")
@EntityListeners(AuditingEntityListener.class)
public class ResourceEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Long createdDate;

    @Column
    @LastModifiedDate
    private Long modifiedDate;

    @Column
    @CreatedBy
    private String createdBy;

    @Column
    @LastModifiedBy
    private String modifiedBy;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private boolean active;

    @Lob
    @Column(nullable = false)
    private byte[] content;

    @Column
    private String contentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
