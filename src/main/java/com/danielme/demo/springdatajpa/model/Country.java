package com.danielme.demo.springdatajpa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@NamedQuery(name = "Country.byPopulationNamedQuery", query = "FROM Country WHERE population = ?1")
@SqlResultSetMapping(
        name = "idNameConstructor",
        classes = @ConstructorResult(targetClass = IdNameDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class)}))
@NamedNativeQuery(
        name = "Country.byPopulationNamedNativeQuery",
        query = "select id, name FROM countries WHERE population = ?1",
        resultSetMapping = "idNameConstructor")
@NamedStoredProcedureQuery(name = "Country.findCountriesByConfederationIdAsNamed",
        procedureName = "countries_by_confederation_id",
        resultClasses = Country.class,
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN,
                        type = Long.class)
        })
@NamedStoredProcedureQuery(name = "Country.findCountriesIdNameByConfederationAsDTO",
        procedureName = "countries_by_confederation_id",
        resultSetMappings = "idNameConstructor",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN,
                        name = "param_conf_id", type = Long.class)
        })
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer population;

    @Column(updatable = false, nullable = false)
    private LocalDateTime creation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "confederation_id")
    private Confederation confederation;

    public Country() {
        super();
    }

    public Country(String name, Integer population, Confederation confederation) {
        super();
        this.name = name;
        this.population = population;
        this.confederation = confederation;
    }

    @PrePersist
    public void onPersist() {
        creation = LocalDateTime.now();
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

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public Confederation getConfederation() {
        return confederation;
    }

    public void setConfederation(Confederation confederation) {
        this.confederation = confederation;
    }
}