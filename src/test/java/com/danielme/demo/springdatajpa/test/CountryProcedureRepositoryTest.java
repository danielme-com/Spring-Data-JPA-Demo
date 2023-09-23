package com.danielme.demo.springdatajpa.test;

import com.danielme.demo.springdatajpa.ApplicationContext;
import com.danielme.demo.springdatajpa.model.Country;
import com.danielme.demo.springdatajpa.model.IdNameDTO;
import com.danielme.demo.springdatajpa.model.IdNameProjection;
import com.danielme.demo.springdatajpa.repository.CountryProceduresRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContext.class)
@Sql("/dataset.sql")
@Sql(value = "/procedures.sql", config = @SqlConfig(separator = "^;"))
@Transactional
public class CountryProcedureRepositoryTest {

    private static final Long UEFA_ID = 1L;
    private static final Long NORWAY_ID = 1L;
    private static final Long SPAIN_ID = 2L;

    @Autowired
    CountryProceduresRepository countryRepository;

    @Test
    public void testFindCountriesAsNamed() {
        List<Country> uefaCountries = countryRepository.findCountriesByConfederationIdAsNamed(UEFA_ID);

        assertUefaCountries(uefaCountries);
    }

    @Test
    public void testFindCountriesEntityFromAnnotation() {
        List<Country> uefaCountries = countryRepository.findCountriesByConfederationFromAnnotation(UEFA_ID);

        assertUefaCountries(uefaCountries);
    }

    @Test
    public void testFindCountriesEntityAsObject() {
        List<Object[]> uefaCountries = countryRepository.findCountriesByConfederationAsObject(UEFA_ID);

        assertEquals(2, uefaCountries.size());
        assertEquals(NORWAY_ID, uefaCountries.get(0)[0]);
        assertEquals(SPAIN_ID, uefaCountries.get(1)[0]);
    }

    @Test
    public void testFindCountriesEntityAsInterface() {
        List<IdNameProjection> uefaCountries = countryRepository.findCountriesByConfederationAsInterface(UEFA_ID);

        assertEquals(2, uefaCountries.size());
        assertEquals(NORWAY_ID, uefaCountries.get(0).getId());
        assertEquals(SPAIN_ID, uefaCountries.get(1).getId());
    }

    @Test
    public void testFindCountriesEntityAsDTO() {
        List<IdNameDTO> uefaCountries = countryRepository.findCountriesIdNameByConfederationAsDTO(UEFA_ID);

        assertEquals(2, uefaCountries.size());
        assertEquals(NORWAY_ID, uefaCountries.get(0).getId());
        assertEquals(SPAIN_ID, uefaCountries.get(1).getId());
    }


    private static void assertUefaCountries(List<Country> uefaCountries) {
        assertEquals(2, uefaCountries.size());
        assertEquals(NORWAY_ID, uefaCountries.get(0).getId());
        assertEquals(SPAIN_ID, uefaCountries.get(1).getId());
    }

}
