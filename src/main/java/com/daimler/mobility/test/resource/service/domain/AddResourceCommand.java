package com.daimler.mobility.test.resource.service.domain;

public class AddResourceCommand {

    private String name;

    private String description;

    private byte[] content;

    private String contentType;

    public static AddResourceCommand of(String name, String description, byte[] content, String contentType) {
        var addResourceCommand = new AddResourceCommand();

        addResourceCommand.name = name;
        addResourceCommand.content = content;
        addResourceCommand.description = description;
        addResourceCommand.contentType = contentType;

        return addResourceCommand;
    }

    public String getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public String getContentType() {
        return contentType;
    }
}
