package com.danielme.demo.springdatajpa.repository.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;



public class BaseRepositoryImpl <T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID>
{
	private EntityManager entityManager;
	
	private Class<T> clazz;
	
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager)
	{
		super(domainClass, entityManager);
		this.entityManager = entityManager; 
		clazz = domainClass;
	}
	
	@Override
	public void clearHibenateCache() {
		SessionFactory sessionFactory = entityManager.unwrap(Session.class).getSessionFactory();
		sessionFactory.getCache().evictEntityRegions();
		sessionFactory.getCache().evictCollectionRegions();
		sessionFactory.getCache().evictDefaultQueryRegion();
		sessionFactory.getCache().evictQueryRegions();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAllUsingCache(Pageable page)
	{
		Query query = entityManager.createQuery("from " + clazz.getName());
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		if (page != null)
		{
			query.setFirstResult(page.getPageNumber()*page.getPageSize());
			query.setMaxResults(page.getPageSize());
		}
		return query.getResultList();
	}

}
