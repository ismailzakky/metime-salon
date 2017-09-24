package com.cus.metime.salon.web.rest;

import com.cus.metime.salon.SalonApp;

import com.cus.metime.salon.config.SecurityBeanOverrideConfiguration;

import com.cus.metime.salon.domain.PlaceInformation;
import com.cus.metime.salon.repository.PlaceInformationRepository;
import com.cus.metime.salon.service.PlaceInformationService;
import com.cus.metime.salon.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlaceInformationResource REST controller.
 *
 * @see PlaceInformationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SalonApp.class, SecurityBeanOverrideConfiguration.class})
public class PlaceInformationResourceIntTest {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE = 1L;
    private static final Long UPDATED_PHONE = 2L;

    private static final Integer DEFAULT_ZIP_CODE = 1;
    private static final Integer UPDATED_ZIP_CODE = 2;

    @Autowired
    private PlaceInformationRepository placeInformationRepository;

    @Autowired
    private PlaceInformationService placeInformationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPlaceInformationMockMvc;

    private PlaceInformation placeInformation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlaceInformationResource placeInformationResource = new PlaceInformationResource(placeInformationService);
        this.restPlaceInformationMockMvc = MockMvcBuilders.standaloneSetup(placeInformationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaceInformation createEntity(EntityManager em) {
        PlaceInformation placeInformation = new PlaceInformation()
            .address(DEFAULT_ADDRESS)
            .location(DEFAULT_LOCATION)
            .city(DEFAULT_CITY)
            .phone(DEFAULT_PHONE)
            .zipCode(DEFAULT_ZIP_CODE);
        return placeInformation;
    }

    @Before
    public void initTest() {
        placeInformation = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlaceInformation() throws Exception {
        int databaseSizeBeforeCreate = placeInformationRepository.findAll().size();

        // Create the PlaceInformation
        restPlaceInformationMockMvc.perform(post("/api/place-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeInformation)))
            .andExpect(status().isCreated());

        // Validate the PlaceInformation in the database
        List<PlaceInformation> placeInformationList = placeInformationRepository.findAll();
        assertThat(placeInformationList).hasSize(databaseSizeBeforeCreate + 1);
        PlaceInformation testPlaceInformation = placeInformationList.get(placeInformationList.size() - 1);
        assertThat(testPlaceInformation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPlaceInformation.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testPlaceInformation.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPlaceInformation.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPlaceInformation.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
    }

    @Test
    @Transactional
    public void createPlaceInformationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = placeInformationRepository.findAll().size();

        // Create the PlaceInformation with an existing ID
        placeInformation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceInformationMockMvc.perform(post("/api/place-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeInformation)))
            .andExpect(status().isBadRequest());

        // Validate the PlaceInformation in the database
        List<PlaceInformation> placeInformationList = placeInformationRepository.findAll();
        assertThat(placeInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPlaceInformations() throws Exception {
        // Initialize the database
        placeInformationRepository.saveAndFlush(placeInformation);

        // Get all the placeInformationList
        restPlaceInformationMockMvc.perform(get("/api/place-informations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(placeInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.intValue())))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)));
    }

    @Test
    @Transactional
    public void getPlaceInformation() throws Exception {
        // Initialize the database
        placeInformationRepository.saveAndFlush(placeInformation);

        // Get the placeInformation
        restPlaceInformationMockMvc.perform(get("/api/place-informations/{id}", placeInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(placeInformation.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.intValue()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingPlaceInformation() throws Exception {
        // Get the placeInformation
        restPlaceInformationMockMvc.perform(get("/api/place-informations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlaceInformation() throws Exception {
        // Initialize the database
        placeInformationService.save(placeInformation);

        int databaseSizeBeforeUpdate = placeInformationRepository.findAll().size();

        // Update the placeInformation
        PlaceInformation updatedPlaceInformation = placeInformationRepository.findOne(placeInformation.getId());
        updatedPlaceInformation
            .address(UPDATED_ADDRESS)
            .location(UPDATED_LOCATION)
            .city(UPDATED_CITY)
            .phone(UPDATED_PHONE)
            .zipCode(UPDATED_ZIP_CODE);

        restPlaceInformationMockMvc.perform(put("/api/place-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlaceInformation)))
            .andExpect(status().isOk());

        // Validate the PlaceInformation in the database
        List<PlaceInformation> placeInformationList = placeInformationRepository.findAll();
        assertThat(placeInformationList).hasSize(databaseSizeBeforeUpdate);
        PlaceInformation testPlaceInformation = placeInformationList.get(placeInformationList.size() - 1);
        assertThat(testPlaceInformation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPlaceInformation.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testPlaceInformation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPlaceInformation.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPlaceInformation.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingPlaceInformation() throws Exception {
        int databaseSizeBeforeUpdate = placeInformationRepository.findAll().size();

        // Create the PlaceInformation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPlaceInformationMockMvc.perform(put("/api/place-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(placeInformation)))
            .andExpect(status().isCreated());

        // Validate the PlaceInformation in the database
        List<PlaceInformation> placeInformationList = placeInformationRepository.findAll();
        assertThat(placeInformationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePlaceInformation() throws Exception {
        // Initialize the database
        placeInformationService.save(placeInformation);

        int databaseSizeBeforeDelete = placeInformationRepository.findAll().size();

        // Get the placeInformation
        restPlaceInformationMockMvc.perform(delete("/api/place-informations/{id}", placeInformation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PlaceInformation> placeInformationList = placeInformationRepository.findAll();
        assertThat(placeInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaceInformation.class);
        PlaceInformation placeInformation1 = new PlaceInformation();
        placeInformation1.setId(1L);
        PlaceInformation placeInformation2 = new PlaceInformation();
        placeInformation2.setId(placeInformation1.getId());
        assertThat(placeInformation1).isEqualTo(placeInformation2);
        placeInformation2.setId(2L);
        assertThat(placeInformation1).isNotEqualTo(placeInformation2);
        placeInformation1.setId(null);
        assertThat(placeInformation1).isNotEqualTo(placeInformation2);
    }
}
