package com.danielme.demo.springdatajpa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import java.util.Optional;

import javax.persistence.Tuple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.danielme.demo.springdatajpa.ApplicationContext;
import com.danielme.demo.springdatajpa.AuthenticationMockup;
import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.model.Pair;
import com.danielme.demo.springdatajpa.repository.CountryRepository;
import com.danielme.demo.springdatajpa.repository.specifications.CountrySpecifications;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration("file:src/main/resources/applicationContext.xml")
@ContextConfiguration(classes = { ApplicationContext.class })
@Sql(scripts = { "/test.sql" })
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
    public void testNoName() {
        assertFalse(countryRepository.findByName("France").isPresent());
    }

    @Test
    public void testNamedQuery() {
        assertTrue(countryRepository.byPopulationNamedQuery(115296767).isPresent());
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
    public void testUpdate() throws Exception {
        Calendar creation = countryRepository.findByName("Norway").get().getCreation();
        assertEquals(5, countryRepository.updateCreation(Calendar.getInstance()));
        assertTrue(countryRepository.findByName("Norway").get().getCreation().after(creation));
    }

    @Test
    public void testDeleteByName() throws Exception {
        assertEquals(1, countryRepository.deleteByName("Norway"));
    }

    @Test
    public void testRemoveById() throws Exception {
        assertEquals(1, countryRepository.removeById(SPAIN_ID));
    }

    @Test
    public void testRemoveByIdWithQuery() throws Exception {
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
    public void testAudit() {
        AuthenticationMockup.UserName = "dani";

        Country country = countryRepository.save(new Country("Bolivia", 10556105));

        assertTrue(country.getCreateBy().equals(country.getLastModifiedBy()));
        assertTrue(country.getCreatedDate().equals(country.getLastModifiedDate()));

        AuthenticationMockup.UserName = "update";
        country.setName("Estado Plurinacional de Bolivia");
        country = countryRepository.save(country);

        assertTrue(country.getLastModifiedBy().equals(AuthenticationMockup.UserName));
        assertTrue(country.getCreateBy().equals("dani"));
        assertFalse(country.getCreatedDate().equals(country.getLastModifiedDate()));
    }

}
