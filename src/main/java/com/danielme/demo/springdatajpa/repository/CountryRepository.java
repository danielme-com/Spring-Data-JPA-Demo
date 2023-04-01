package com.danielme.demo.springdatajpa.repository;

import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.model.IdNameDTO;
import com.danielme.demo.springdatajpa.model.IdNameProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface CountryRepository extends Repository<Country, Long> {

    Optional<Country> findByName(String name);

    List<Country> findByPopulationGreaterThan(Integer population);

    int countByPopulationGreaterThan(Integer population);

    List<Country> findByConfederationName(String confederationName);

    List<Country> findByNameContainingIgnoreCase(String name);

    List<Country> findByNameContainingIgnoreCaseAndConfederationId(String name, Long confId);

    List<Country> findByCreationBetween(LocalDateTime start, LocalDateTime end);

    List<Country> findByNameStartingWithIgnoreCase(String name);

    List<Country> findByConfederationIdNotIn(Long... ids);

    @Query("select c.name from Country c where c.id = :id")
    Optional<String> findNameById(Long id);

    @Query("select c from Country c where lower(c.name) like lower(:name)")
    List<Country> findByNameWithQuery(@Param("name") String name, Sort sort);

    @Query("select c from Country c where lower(c.name) like lower(:name)")
    Page<Country> findByNameWithQuery(@Param("name")String name, Pageable page);

    //@Query("from Country c where lower(c.name) like lower(?1)")
    //Page<Country> findByNameWithQuery(String name, Pageable page);

    @Query(nativeQuery = true)
    IdNameDTO byPopulationNamedNativeQuery(Integer population);

    @Query(value = "select id, name FROM countries WHERE population = :population", nativeQuery = true)
    IdNameProjection byPopulationProjectionNativeQuery(@Param("population") Integer population);

    @Query(value = "select * from countries WHERE confederation_id = :confId", nativeQuery = true)
    List<Country> findAllByConfederationNative(@Param("confId") Long confId);

    @Query(value = "select * from countries",
            nativeQuery = true,
            countQuery = "select count(*) from countries")
    Page<Country> findAllNative(Pageable pageable);

    Optional<Country> byPopulationNamedQuery(Integer population);

    Optional<Country> findByName(String name, Sort sort);

    List<Country> findByPopulationGreaterThanOrderByCreationAscName(Integer population);

    @Query("select new com.danielme.demo.springdatajpa.model.IdNameDTO(c.id, c.name) from Country c")
    List<IdNameDTO> getAsIdNameDto();

    List<IdNameDTO> findAsIdNameDtoBy();

    List<IdNameProjection> findAsIdNameProjectionBy();

    @Query("select c.id as id, c.name as name from Country c")
    List<IdNameProjection> getAsIdNameInterface();

    boolean existsByPopulationGreaterThan(int population);

    @Transactional
    @Modifying
    @Query("UPDATE Country set creation = (?1)")
    int updateCreation(LocalDateTime creation);

    @Transactional
    int deleteByName(String name);

    @Transactional
    int removeById(Long id);

    @Transactional
    @Modifying
    @Query("delete from Country where id=:id")
    int deleteCountryById(@Param("id") Long id);

}
