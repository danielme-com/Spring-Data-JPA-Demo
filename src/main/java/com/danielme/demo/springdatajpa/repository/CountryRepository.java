package com.danielme.demo.springdatajpa.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.repository.base.BaseRepository;

public interface CountryRepository extends BaseRepository<Country, Long>, CountryRepositoryCustom, JpaSpecificationExecutor<Country>
{
	Country findByName(String name);
	
	@Query("from Country c where lower(c.name) like lower(?1)")
	List<Country> findByNameWithQuery(String name, Sort sort);
	
	Country findByPopulation(Integer population);
	
	@Transactional
	@Modifying
    @Query("UPDATE Country set creation = (?1)")
	int updateCreation(Calendar creation);
		
}
