package com.danielme.demo.springdatajpa.model;

public class IdNameDTO {

    private Long id;
    private String name;

    public IdNameDTO(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
