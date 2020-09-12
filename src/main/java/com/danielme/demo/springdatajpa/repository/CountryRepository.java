package com.danielme.demo.springdatajpa.repository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;
import javax.persistence.Tuple;

import com.danielme.demo.springdatajpa.model.PairProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.model.Pair;
import com.danielme.demo.springdatajpa.repository.base.CustomBaseRepository;

public interface CountryRepository extends CustomBaseRepository<Country, Long>,
        CountryRepositoryCustom, JpaSpecificationExecutor<Country> {
    Optional<Country> findByName(String name);

    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable",
            value = "true") })
    List<Country> findByPopulationGreaterThan(Integer population);

    int countByPopulationGreaterThan(Integer population);

    @Query("from Country c where lower(c.name) like lower(?1)")
    Page<Country> findByNameWithQuery(String name, Pageable page);

    @Query(nativeQuery = true)
    Pair byPopulationNamedNativeQuery(Integer population);

    @Query(value = "select * from countries", nativeQuery = true)
    List<Country> findAllNative();

    @Query(value = "select * from countries", nativeQuery = true)
    Page<Country> findAllNative(Pageable pageable);

    Optional<Country> byPopulationNamedQuery(Integer population);

    Optional<Country> findByName(String name, Sort sort);

    List<Country> findByPopulationGreaterThanOrderByPopulationAsc(Integer population);

    @Query("select new com.danielme.demo.springdatajpa.model.Pair(c.id, c.name) from Country c where c.id = ?1")
    Pair getPairById(Long id);

    @Query("select c.id as id, c.name as value from Country c where c.id = ?1")
    PairProjection getPairByIdInterface(Long id);

    @Query("select c.id as ID, c.name As value from Country c where c.id = ?1")
    Tuple getTupleById(Long id);

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
