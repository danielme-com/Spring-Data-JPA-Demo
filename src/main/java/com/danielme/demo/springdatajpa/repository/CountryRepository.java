package com.danielme.demo.springdatajpa.repository;

import java.util.Calendar;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.repository.base.BaseRepository;

public interface CountryRepository extends BaseRepository<Country, Long>, CountryRepositoryCustom, JpaSpecificationExecutor<Country>
{
	Country findByName(String name);
	
	@QueryHints(value = { @QueryHint (name = "org.hibernate.cacheable", value = "true")})
	List<Country> findByPopulationGreaterThan(Integer population);
	
	int countByPopulationGreaterThan(Integer population);
	
	@Query("from Country c where lower(c.name) like lower(?1)")
	Page<Country> getByNameWithQuery(String name, Pageable page);
	
	Country getByPopulationNamedQuery(Integer population);	
	
	List<Country> findByPopulationGreaterThanOrderByPopulationAsc(Integer population);			
	
	@Query("select case when (count(c) > 0)  then true else false end from Country c where c.name = ?1)")
	boolean exists(String name);		
	
	@Transactional
	@Modifying
    @Query("UPDATE Country set creation = (?1)")
	int updateCreation(Calendar creation);
	
	@Transactional
	int deleteByName(String name);
		
}
