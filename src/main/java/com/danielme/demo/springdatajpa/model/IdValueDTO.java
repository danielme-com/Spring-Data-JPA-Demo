package com.danielme.demo.springdatajpa.model;

public class IdValueDTO {

    private Long id;
    private String value;

    public IdValueDTO(Long id, String name) {
        super();
        this.id = id;
        this.value = name;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

}
