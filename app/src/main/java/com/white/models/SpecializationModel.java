package com.white.models;

import java.io.Serializable;

public class SpecializationModel implements Serializable {
    private int id;
    private String title;
    private String image;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
