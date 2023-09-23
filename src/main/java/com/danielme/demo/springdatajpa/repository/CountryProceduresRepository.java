package com.danielme.demo.springdatajpa.repository;

import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.model.IdNameDTO;
import com.danielme.demo.springdatajpa.model.IdNameProjection;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountryProceduresRepository extends Repository<Country, Long> {

    @Procedure
    List<Country> findCountriesByConfederationIdAsNamed(@Param("param_conf_id") Long confId);

    @Procedure(procedureName = "countries_by_confederation_id")
    List<Country> findCountriesByConfederationFromAnnotation(@Param("param_conf_id") Long confId);

    @Procedure(procedureName = "countries_by_confederation_id")
    List<Object[]> findCountriesByConfederationAsObject(@Param("param_conf_id") Long confId);

    @Procedure(procedureName = "countries_id_name_by_confederation_id")
    List<IdNameProjection> findCountriesByConfederationAsInterface(@Param("param_conf_id") Long confId);

    @Procedure
    List<IdNameDTO> findCountriesIdNameByConfederationAsDTO(@Param("param_conf_id") Long confId);

}
