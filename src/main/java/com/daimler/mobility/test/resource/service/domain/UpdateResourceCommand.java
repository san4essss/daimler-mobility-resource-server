package com.daimler.mobility.test.resource.service.domain;

public class UpdateResourceCommand {

    private Long id;

    private String name;

    private String description;

    private byte[] content;

    private Boolean active;

    private String contentType;

    public static UpdateResourceCommand of(
            Long id,
            String name,
            String description,
            byte[] content,
            Boolean active,
            String contentType) {
        var updateResourceCommand = new UpdateResourceCommand();

        updateResourceCommand.id = id;
        updateResourceCommand.name = name;
        updateResourceCommand.content = content;
        updateResourceCommand.description = description;
        updateResourceCommand.active = active;
        updateResourceCommand.contentType = contentType;

        return updateResourceCommand;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isActive() {
        return active;
    }
}
