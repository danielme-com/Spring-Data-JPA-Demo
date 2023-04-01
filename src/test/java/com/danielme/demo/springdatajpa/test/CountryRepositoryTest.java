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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("file:src/main/resources/applicationContext.xml")
@ContextConfiguration(classes = ApplicationContext.class)
@Sql("/dataset.sql")
public class CountryRepositoryTest {

    private static final Long SPAIN_ID = 2L;
    private static final Long MEXICO_ID = 3L;
    private static final Long COLOMBIA_ID = 4L;
    private static final Long CONCACAF_ID = 2L;
    private static final String SPAIN = "Spain";
    private static final String NORWAY = "Norway";
    private static final String MEXICO = "Mexico";
    private static final int MEXICO_POPULATION = 115296767;
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
    public void testFindByNameContainingIgnoreCaseAndConfederationId() {
        List<Country> countries = countryRepository.findByNameContainingIgnoreCaseAndConfederationId("m", CONCACAF_ID);

        assertEquals(1, countries.size());
        assertEquals(MEXICO_ID, countries.get(0).getId());
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
        assertEquals(COLOMBIA_ID, countries.get(0).getId());
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
        IdNameDTO mexico = countryRepository.byPopulationNamedNativeQuery(MEXICO_POPULATION);

        assertEquals(MEXICO_ID, mexico.getId());
        assertEquals(MEXICO, mexico.getName());
    }

    @Test
    public void testProjectionNativeQuery() {
        IdNameProjection mexico = countryRepository.byPopulationProjectionNativeQuery(MEXICO_POPULATION);

        assertEquals(MEXICO_ID, mexico.getId());
        assertEquals(MEXICO, mexico.getName());
    }

    @Test
    public void testQuerysSortingAndPaging() {
        Page<Country> page0 = countryRepository.findByNameWithQuery("%i%",
                PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name")));

        assertEquals(4, page0.getTotalElements());
        assertEquals(2, page0.getTotalPages());
        assertEquals(COLOMBIA_ID, page0.getContent().get(0).getId());
    }


    @Test
    public void testQuerysMultipleSorting() {
        Sort sort = Sort.by(Sort.Direction.DESC, "creation")
                .and(Sort.by(Sort.Direction.ASC, "name"));

        List<Country> countries = countryRepository.findByNameWithQuery("%i%", sort);

        assertEquals(SPAIN_ID, countries.get(0).getId());
    }

    @Test
    public void testFindByPopulationGreaterThanOrderByPopulation() {
        List<Country> countries = countryRepository.findByPopulationGreaterThanOrderByCreationAscName(0);

        assertEquals(COLOMBIA_ID, countries.get(0).getId());
    }

    @Test
    public void testUpdate() {
        LocalDateTime creation = countryRepository.findByName(NORWAY).get().getCreation();

        assertEquals(ALL_COUNTRIES, countryRepository.updateCreation(LocalDateTime.now()));
        assertTrue(countryRepository.findByName(NORWAY).get().getCreation().isAfter(creation));
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

        assertEquals(ALL_COUNTRIES, countries.size());
    }

    @Test
    public void testProjectionInterface() {
        List<IdNameProjection> countries = countryRepository.getAsIdNameInterface();

        assertEquals(ALL_COUNTRIES, countries.size());
    }

    @Test
    public void testFindAllListNative() {
        List<Country> countriesConcacaf = countryRepository.findAllByConfederationNative(CONCACAF_ID);

        assertEquals(2, countriesConcacaf.size());
    }

    @Test
    public void testFindAllPageNative() {
        Page<Country> page0 = countryRepository.findAllNative(PageRequest.of(0, 3, Sort.by("name")));

        assertEquals(ALL_COUNTRIES, page0.getTotalElements());
        assertEquals(2, page0.getTotalPages());
    }

    @Test
    public void testFindByCreationBetween() {
        LocalDateTime startDate = LocalDateTime.of(2018, 10, 6, 18, 15);
        List<Country> countriesBetweenDates = countryRepository.findByCreationBetween(startDate, LocalDateTime.now());

        assertEquals(1, countriesBetweenDates.size());
        assertEquals(SPAIN_ID, countriesBetweenDates.get(0).getId());
    }

    @Test
    public void testFindByNameStartsWith() {
        List<Country> countriesStartingS = countryRepository.findByNameStartingWithIgnoreCase("s");

        assertEquals(1, countriesStartingS.size());
        assertEquals(SPAIN_ID, countriesStartingS.get(0).getId());
    }

    @Test
    public void testFindByNameNotStartsWith() {
        List<Country> countries = countryRepository.findByConfederationIdNotIn(1L, 2L);

        assertEquals(1, countries.size());
        assertEquals(COLOMBIA_ID, countries.get(0).getId());
    }

}
