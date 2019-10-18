package com.legendApi.models;

public class AddGroup {
    private String name;
    private String description;
    private String[] tags;

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    public String getDescription() {return description;}
    public void setDescription(String value) {description = value;}

    public String[] getTags() {return tags;}
    public void setTags(String[] value) {tags = value;}
}
