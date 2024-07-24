package com.example.dishapp.dto;

public class DishDTO {

    private Long id;
    private String name;
    private String photo; // Base64 encoded string

    // Constructors, getters, and setters

    public DishDTO() {}

    public DishDTO(Long id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
