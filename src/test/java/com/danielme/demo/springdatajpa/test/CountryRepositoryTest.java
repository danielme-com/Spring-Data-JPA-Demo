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
@Sql("/dataset.sql")
public class CountryRepositoryTest {

    private static final Long SPAIN_ID = 2L;
    private static final String SPAIN = "Spain";
    private static final String NORWAY = "Norway";
    private static final String COLOMBIA = "Colombia";
    private static final int MEXICO_POPULATION = 115296767;
    private static final String MEXICO = "Mexico";
    private static final int ALL_COUNTRIES = 5;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testExistsByPopulationGreaterThan() {
        assertFalse(countryRepository.existsByPopulationGreaterThan(150000000));
    }

    @Test
    public void testFindByName() {
        assertEquals(SPAIN, countryRepository.findNameById(SPAIN_ID).get());
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        List<Country> countriesWithP = countryRepository.findByNameContainingIgnoreCase("p");

        assertEquals(1, countriesWithP.size());
        assertEquals(SPAIN_ID, countriesWithP.get(0).getId());
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
        String name = countryRepository.byPopulationNamedNativeQuery(MEXICO_POPULATION).getName();

        assertEquals(MEXICO, name);
    }

    @Test
    public void testProjectionNativeQuery() {
        String name = countryRepository.byPopulationProjectionNativeQuery(MEXICO_POPULATION).getName();

        assertEquals(MEXICO, name);
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
        List<IdNameDTO> countries = countryRepository.findAsIdNameDtoBy();

        assertEquals(5, countries.size());
    }

    @Test
    public void testProjectionInterface() {
        List<IdNameProjection> countries = countryRepository.getAsIdNameInterface();

        assertEquals(5, countries.size());
    }

    @Test
    public void testFindAllListNative() {
        List<Country> countries = countryRepository.findAllNative();

        assertEquals(5, countries.size());
    }

    @Test
    public void testFindAllPageNative() {
        Page<Country> page = countryRepository.findAllNative(PageRequest.of(0, 3));

        assertEquals(ALL_COUNTRIES, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

}
