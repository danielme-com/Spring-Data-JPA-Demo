package com.danielme.demo.springdatajpa.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.repository.base.BaseRepository;

public interface CountryRepository extends BaseRepository<Country, Long>, CountryRepositoryCustom, JpaSpecificationExecutor<Country>
{
	Country findByName(String name);
	
	@Query("from Country c where lower(c.name) like lower(?1)")
	List<Country> findByNameWithQuery(String name, Sort sort);
	
	Country findByPopulation(Integer population);
		
}
