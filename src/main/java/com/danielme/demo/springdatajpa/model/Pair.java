package com.danielme.demo.springdatajpa.model;

public class Pair {

    private Long id;
    private String value;

    public Pair(Long id, String name) {
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
