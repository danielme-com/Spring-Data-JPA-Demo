package com.danielme.demo.springdatajpa.test;

import com.danielme.demo.springdatajpa.ApplicationContext;
import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.model.IdNameDTO;
import com.danielme.demo.springdatajpa.model.IdNameProjection;
import com.danielme.demo.springdatajpa.repository.CountryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("file:src/main/resources/applicationContext.xml")
@ContextConfiguration(classes = ApplicationContext.class)
@Sql("/test.sql")
public class CountryRepositoryTest {

    private static final Long SPAIN_ID = 2L;
    private static final String SPAIN = "Spain";
    private static final String NORWAY = "Norway";
    private static final String COLOMBIA = "Colombia";
    public static final int MEXICO_POPULATION = 115296767;
    public static final String MEXICO = "Mexico";

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testExists() {
        assertTrue(countryRepository.exists(SPAIN));
    }

    @Test
    public void testFindByName() {
        assertEquals(SPAIN, countryRepository.findNameById(SPAIN_ID).get());
    }

    @Test
    public void testNotExists() {
        assertFalse(countryRepository.exists("Italy"));
    }

    @Test
    public void testPopulation() {
        assertEquals(3, countryRepository.countByPopulationGreaterThan(45000000));
        assertEquals(3, countryRepository.findByPopulationGreaterThan(45000000).size());
    }

    @Test
    public void testName() {
        Optional<Country> optCountry = countryRepository.findByName(NORWAY);
        assertTrue(optCountry.isPresent());
        assertEquals(NORWAY, optCountry.get().getName());
    }

    @Test
    public void testFindByConfederation() {
        List<Country> countries = countryRepository.findByConfederationName("CONMEBOL");

        assertEquals(1, countries.size());
        assertEquals(COLOMBIA, countries.get(0).getName());
    }

    @Test
    public void testNoName() {
        assertFalse(countryRepository.findByName("France").isPresent());
    }

    @Test
    public void testNamedQuery() {
        assertTrue(countryRepository.byPopulationNamedQuery(MEXICO_POPULATION).isPresent());
    }

    @Test
    public void testNamedNativeQuery() {
        String value = countryRepository.byPopulationNamedNativeQuery(MEXICO_POPULATION).getName();

        assertEquals(MEXICO, value);
    }

    @Test
    public void testProjectionNativeQuery() {
        String value = countryRepository.byPopulationProjectionNativeQuery(MEXICO_POPULATION).getName();

        assertEquals(MEXICO, value);
    }

    @Test
    public void testQuerysSortingAndPaging() {
        Page<Country> page0 = countryRepository.findByNameWithQuery("%i%",
                PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name")));

        assertEquals(4, page0.getTotalElements());
        assertEquals(2, page0.getTotalPages());
        assertEquals(COLOMBIA, page0.getContent().get(0).getName());
    }

    @Test
    public void testUpdate() {
        Calendar creation = countryRepository.findByName(NORWAY).get().getCreation();

        assertEquals(5, countryRepository.updateCreation(Calendar.getInstance()));
        assertTrue(countryRepository.findByName(NORWAY).get().getCreation().after(creation));
    }

    @Test
    public void testDeleteByName() {
        assertEquals(1, countryRepository.deleteByName(NORWAY));
    }

    @Test
    public void testRemoveById() {
        assertEquals(1, countryRepository.removeById(SPAIN_ID));
    }

    @Test
    public void testRemoveByIdWithQuery() {
        assertEquals(1, countryRepository.deleteCountryById(SPAIN_ID));
    }

    @Test
    public void testProjectionConstructor() {
        IdNameDTO idNameDTO = countryRepository.getAsIdNameDtoById(SPAIN_ID);

        assertEquals(SPAIN_ID, idNameDTO.getId());
        assertEquals(SPAIN, idNameDTO.getName());
    }

    @Test
    public void testProjectionInterface() {
        IdNameProjection idName = countryRepository.getAsIdNameInterfaceById(SPAIN_ID);

        assertEquals(SPAIN_ID, idName.getId());
        assertEquals(SPAIN, idName.getName());
    }

    @Test
    public void testFindAllListNative() {
        List<Country> countries = countryRepository.findAllNative();

        assertEquals(5, countries.size());
    }

    @Test
    public void testFindAllPageNative() {
        Page<Country> page = countryRepository.findAllNative(PageRequest.of(0, 3));

        assertEquals(5, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

}
