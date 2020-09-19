package com.danielme.demo.springdatajpa.model;

import javax.persistence.*;
import java.util.Calendar;

@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "countries")
@NamedQuery(name = "Country.byPopulationNamedQuery", query = "FROM Country WHERE population = ?1")
@SqlResultSetMapping(
        name = "pairConstructor",
        classes = @ConstructorResult(targetClass = Pair.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class)}))
@NamedNativeQuery(
        name = "Country.byPopulationNamedNativeQuery",
        query = "select id, name FROM countries WHERE population = ?1",
        resultSetMapping = "pairConstructor")
public class Country extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer population;

    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creation;

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
        creation = Calendar.getInstance();
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

    public Calendar getCreation() {
        return creation;
    }

    public void setCreation(Calendar creation) {
        this.creation = creation;
    }

    public Confederation getConfederation() {
        return confederation;
    }

    public void setConfederation(Confederation confederation) {
        this.confederation = confederation;
    }
}