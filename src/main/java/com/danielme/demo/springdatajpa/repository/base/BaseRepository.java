package com.danielme.demo.springdatajpa.repository.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> 
{
	void clearHibenateCache();

	List<T> getAllUsingCache(Pageable page);
}