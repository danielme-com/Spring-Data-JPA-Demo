package com.danielme.demo.springdatajpa.repository.base;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class BaseRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable> extends JpaRepositoryFactoryBean<T, S, ID>
{

	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager)
	{
		return new BaseRepositoryFactory(entityManager);
	}

	private static class BaseRepositoryFactory extends JpaRepositoryFactory
	{		
		public BaseRepositoryFactory(EntityManager entityManager)
		{
			super(entityManager);			
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata, EntityManager entityManager)
		{
			return new BaseRepositoryImpl(metadata.getDomainType(), entityManager);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata)
		{
			return BaseRepositoryImpl.class;
		}
	}
}
