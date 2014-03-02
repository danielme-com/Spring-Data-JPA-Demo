package com.danielme.demo.springdatajpa;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.repository.CountryRepository;
import com.danielme.demo.springdatajpa.repository.specifications.CountrySpecifications;


/**
 * 
 * @author danielme.com
 * 
 */
public class Main
{
	
    private static final Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) 
	{			
  		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
  		CountryRepository countryRepository = (CountryRepository) applicationContext.getBean(CountryRepository.class);

  		//empty repository
  		countryRepository.deleteAllInBatch();
  		
  		//insert
  		countryRepository.save(new Country("Spain", 47265321));
  		countryRepository.save(new Country("Mexico", 115296767));
  		countryRepository.save(new Country("Germany", 81799600));
  		
  		//get
  		List<Country> countries = countryRepository.findAll();
  		for(Country country : countries)
  		{
  			logger.info(country.getName() + ":" + country.getPopulation());
  		}
  		//pagination
  		Page<Country> pageCountry = countryRepository.findAll(new PageRequest(0, 1));
  		for(Country country : pageCountry)
  		{
  			logger.info(country.getName() + ":" + country.getPopulation());
  		}
  		
  		//query by method name
  		logger.info(countryRepository.findByName("Germany").getName());
  		
  		//query by annotation
  		countries = countryRepository.findByNameWithQuery("%i%", new Sort( new Sort.Order(Sort.Direction.ASC,"name")));
  		logger.info(countries.size());  		
  		
  		//query by name query
  		logger.info(countryRepository.findByPopulation(115296767).getName());
  		
  		System.out.println("updated: " + countryRepository.updateCreation(Calendar.getInstance()));
  		
  		//debug and test EHCache for the second call ;)
  		countryRepository.getAllUsingCache(null);
  		countryRepository.getAllUsingCache(null);
  		
  		Country country = countryRepository.findOne(CountrySpecifications.searchByName("Mexico"));
  		logger.info(country.getName());
  		
  		countryRepository.clearEntityCache();
  		
  		countryRepository.clearHibenateCache();
  		
  		//empty repository
  		countryRepository.deleteAllInBatch();	  		
  		
 	}		

}