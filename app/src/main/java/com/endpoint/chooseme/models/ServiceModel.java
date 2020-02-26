package com.endpoint.chooseme.models;

import java.io.Serializable;

public class ServiceModel implements Serializable {

    private int id;
    private String name;
    private int image_resource;

    public ServiceModel() {
    }

    public ServiceModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage_resource() {
        return image_resource;
    }

    public void setImage_resource(int image_resource) {
        this.image_resource = image_resource;
    }
}
