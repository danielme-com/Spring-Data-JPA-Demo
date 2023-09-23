package com.danielme.demo.springdatajpa.model;

import jakarta.persistence.*;

@Entity
@Table(name = "confederations")
public class Confederation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false, unique = true)
    private  String name;

    public Confederation() {
        super();
    }

    public Confederation(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
