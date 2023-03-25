package com.danielme.demo.springdatajpa.test;

import com.danielme.demo.springdatajpa.ApplicationContext;
import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.model.Pair;
import com.danielme.demo.springdatajpa.model.PairProjection;
import com.danielme.demo.springdatajpa.repository.CountryRepository;
import com.danielme.demo.springdatajpa.repository.specifications.CountrySpecifications;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.Tuple;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("file:src/main/resources/applicationContext.xml")
@ContextConfiguration(classes = {ApplicationContext.class})
@Sql(scripts = {"/test.sql"})
public class CountryRepositoryTest {

    private static final Long SPAIN_ID = 2L;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testExists() {
        assertTrue(countryRepository.exists("Spain"));
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
        Optional<Country> optCountry = countryRepository.findByName("Norway");
        assertTrue(optCountry.isPresent());
        assertEquals("Norway", optCountry.get().getName());
    }

    @Test
    public void testFindByConfederation() {
        List<Country> countries = countryRepository.findByConfederationName("CONMEBOL");

        assertEquals(1, countries.size());
        assertEquals("Colombia", countries.get(0).getName());
    }

    @Test
    public void testNoName() {
        assertFalse(countryRepository.findByName("France").isPresent());
    }

    @Test
    public void testNamedQuery() {
        assertTrue(countryRepository.byPopulationNamedQuery(115296767).isPresent());
    }

    @Test
    public void testNamedNativeQuery() {
        assertEquals(countryRepository.byPopulationNamedNativeQuery(115296767).getValue(), "Mexico");
    }

    @Test
    public void testQuerysSortingAndPaging() {
        Page<Country> page0 = countryRepository.findByNameWithQuery("%i%",
                PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name")));
        assertEquals(4, page0.getTotalElements());
        assertEquals(2, page0.getTotalPages());
        assertEquals("Colombia", page0.getContent().get(0).getName());
    }

    @Test
    public void testUpdate() {
        Calendar creation = countryRepository.findByName("Norway").get().getCreation();
        assertEquals(5, countryRepository.updateCreation(Calendar.getInstance()));
        assertTrue(countryRepository.findByName("Norway").get().getCreation().after(creation));
    }

    @Test
    public void testDeleteByName() {
        assertEquals(1, countryRepository.deleteByName("Norway"));
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
    public void testJpaCriteria() {
        assertEquals("Mexico", countryRepository
                .findOne(CountrySpecifications.searchByName("Mexico")).get().getName());
    }

    @Test
    public void testProjectionConstructor() {
        Pair pair = countryRepository.getPairById(SPAIN_ID);
        assertEquals(SPAIN_ID, pair.getId());
        assertEquals("Spain", pair.getValue());
    }

    @Test
    public void testProjectionTuple() {
        Tuple tuple = countryRepository.getTupleById(SPAIN_ID);
        assertEquals(SPAIN_ID, tuple.get("ID"));
        assertEquals("Spain", tuple.get("value"));
    }

    @Test
    public void testProjectionInterface() {
        PairProjection pair = countryRepository.getPairByIdInterface(SPAIN_ID);
        assertEquals(SPAIN_ID, pair.getId());
        assertEquals("Spain", pair.getValue());
    }

    @Test
    public void testFindAllListNative() {
        List<Country> countries = countryRepository.findAllNative();
        assertEquals(5, countries.size());
    }

    @Test
    public void testFindAllPageNative() {
        Page<Country> page = countryRepository.findAllNative(PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name")));
        assertEquals(5, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

}
