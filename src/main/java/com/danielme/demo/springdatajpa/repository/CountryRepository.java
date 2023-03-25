package com.danielme.demo.springdatajpa.repository;

import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.model.IdValueDTO;
import com.danielme.demo.springdatajpa.model.IdValueProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface CountryRepository extends Repository<Country, Long> {

    Optional<Country> findByName(String name);

    @QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable",
            value = "true")})
    List<Country> findByPopulationGreaterThan(Integer population);

    int countByPopulationGreaterThan(Integer population);

    List<Country> findByConfederationName(String confederationName);

    List<Country> findByNameContainingIgnoreCase(String name);

    @Query("from Country c where lower(c.name) like lower(:name)")
    Page<Country> findByNameWithQuery(@Param("name")String name, Pageable page);

    //@Query("from Country c where lower(c.name) like lower(?1)")
    //Page<Country> findByNameWithQuery(String name, Pageable page);

    @Query(nativeQuery = true)
    IdValueDTO byPopulationNamedNativeQuery(Integer population);

    @Query(value = "select id, name as value FROM countries WHERE population = :population", nativeQuery = true)
    IdValueProjection byPopulationProjectionNativeQuery(@Param("population") Integer population);

    @Query(value = "select * from countries", nativeQuery = true)
    List<Country> findAllNative();

    @Query(value = "select * from countries order by name",
            nativeQuery = true,
            countQuery = "select count(*) from countries")
    Page<Country> findAllNative(Pageable pageable);

    Optional<Country> byPopulationNamedQuery(Integer population);

    Optional<Country> findByName(String name, Sort sort);

    List<Country> findByPopulationGreaterThanOrderByPopulationAsc(Integer population);

    @Query("select new com.danielme.demo.springdatajpa.model.IdValueDTO(c.id, c.name) from Country c where c.id = ?1")
    IdValueDTO getAsIdValueDtoById(Long id);

    @Query("select c.id as id, c.name as value from Country c where c.id = ?1")
    IdValueProjection getAsIdValueInterfaceById(Long id);

    @Query("select case when (count(c) > 0)  then true else false end from Country c where c.name = ?1")
    boolean exists(String name);

    @Transactional
    @Modifying
    @Query("UPDATE Country set creation = (?1)")
    int updateCreation(Calendar creation);

    @Transactional
    int deleteByName(String name);

    @Transactional
    int removeById(Long id);

    @Transactional
    @Modifying
    @Query("delete from Country where id=:id")
    int deleteCountryById(@Param("id") Long id);

}
