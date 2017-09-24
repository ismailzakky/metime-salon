package com.cus.metime.salon.web.rest;

import com.cus.metime.salon.SalonApp;

import com.cus.metime.salon.config.SecurityBeanOverrideConfiguration;

import com.cus.metime.salon.domain.BusinessInformation;
import com.cus.metime.salon.repository.BusinessInformationRepository;
import com.cus.metime.salon.service.BusinessInformationService;
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

import com.cus.metime.salon.domain.enumeration.ServiceType;
import com.cus.metime.salon.domain.enumeration.Speciality;
/**
 * Test class for the BusinessInformationResource REST controller.
 *
 * @see BusinessInformationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SalonApp.class, SecurityBeanOverrideConfiguration.class})
public class BusinessInformationResourceIntTest {

    private static final ServiceType DEFAULT_SERVICE_TYPE = ServiceType.BEAUTY_SALON;
    private static final ServiceType UPDATED_SERVICE_TYPE = ServiceType.NAIL_ART;

    private static final Speciality DEFAULT_SPECIALITY = Speciality.HAIR_COLORING;
    private static final Speciality UPDATED_SPECIALITY = Speciality.NAIL_COLORING;

    @Autowired
    private BusinessInformationRepository businessInformationRepository;

    @Autowired
    private BusinessInformationService businessInformationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBusinessInformationMockMvc;

    private BusinessInformation businessInformation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BusinessInformationResource businessInformationResource = new BusinessInformationResource(businessInformationService);
        this.restBusinessInformationMockMvc = MockMvcBuilders.standaloneSetup(businessInformationResource)
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
    public static BusinessInformation createEntity(EntityManager em) {
        BusinessInformation businessInformation = new BusinessInformation()
            .serviceType(DEFAULT_SERVICE_TYPE)
            .speciality(DEFAULT_SPECIALITY);
        return businessInformation;
    }

    @Before
    public void initTest() {
        businessInformation = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusinessInformation() throws Exception {
        int databaseSizeBeforeCreate = businessInformationRepository.findAll().size();

        // Create the BusinessInformation
        restBusinessInformationMockMvc.perform(post("/api/business-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessInformation)))
            .andExpect(status().isCreated());

        // Validate the BusinessInformation in the database
        List<BusinessInformation> businessInformationList = businessInformationRepository.findAll();
        assertThat(businessInformationList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessInformation testBusinessInformation = businessInformationList.get(businessInformationList.size() - 1);
        assertThat(testBusinessInformation.getServiceType()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testBusinessInformation.getSpeciality()).isEqualTo(DEFAULT_SPECIALITY);
    }

    @Test
    @Transactional
    public void createBusinessInformationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = businessInformationRepository.findAll().size();

        // Create the BusinessInformation with an existing ID
        businessInformation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessInformationMockMvc.perform(post("/api/business-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessInformation)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessInformation in the database
        List<BusinessInformation> businessInformationList = businessInformationRepository.findAll();
        assertThat(businessInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBusinessInformations() throws Exception {
        // Initialize the database
        businessInformationRepository.saveAndFlush(businessInformation);

        // Get all the businessInformationList
        restBusinessInformationMockMvc.perform(get("/api/business-informations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].speciality").value(hasItem(DEFAULT_SPECIALITY.toString())));
    }

    @Test
    @Transactional
    public void getBusinessInformation() throws Exception {
        // Initialize the database
        businessInformationRepository.saveAndFlush(businessInformation);

        // Get the businessInformation
        restBusinessInformationMockMvc.perform(get("/api/business-informations/{id}", businessInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(businessInformation.getId().intValue()))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE.toString()))
            .andExpect(jsonPath("$.speciality").value(DEFAULT_SPECIALITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBusinessInformation() throws Exception {
        // Get the businessInformation
        restBusinessInformationMockMvc.perform(get("/api/business-informations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessInformation() throws Exception {
        // Initialize the database
        businessInformationService.save(businessInformation);

        int databaseSizeBeforeUpdate = businessInformationRepository.findAll().size();

        // Update the businessInformation
        BusinessInformation updatedBusinessInformation = businessInformationRepository.findOne(businessInformation.getId());
        updatedBusinessInformation
            .serviceType(UPDATED_SERVICE_TYPE)
            .speciality(UPDATED_SPECIALITY);

        restBusinessInformationMockMvc.perform(put("/api/business-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBusinessInformation)))
            .andExpect(status().isOk());

        // Validate the BusinessInformation in the database
        List<BusinessInformation> businessInformationList = businessInformationRepository.findAll();
        assertThat(businessInformationList).hasSize(databaseSizeBeforeUpdate);
        BusinessInformation testBusinessInformation = businessInformationList.get(businessInformationList.size() - 1);
        assertThat(testBusinessInformation.getServiceType()).isEqualTo(UPDATED_SERVICE_TYPE);
        assertThat(testBusinessInformation.getSpeciality()).isEqualTo(UPDATED_SPECIALITY);
    }

    @Test
    @Transactional
    public void updateNonExistingBusinessInformation() throws Exception {
        int databaseSizeBeforeUpdate = businessInformationRepository.findAll().size();

        // Create the BusinessInformation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBusinessInformationMockMvc.perform(put("/api/business-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessInformation)))
            .andExpect(status().isCreated());

        // Validate the BusinessInformation in the database
        List<BusinessInformation> businessInformationList = businessInformationRepository.findAll();
        assertThat(businessInformationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBusinessInformation() throws Exception {
        // Initialize the database
        businessInformationService.save(businessInformation);

        int databaseSizeBeforeDelete = businessInformationRepository.findAll().size();

        // Get the businessInformation
        restBusinessInformationMockMvc.perform(delete("/api/business-informations/{id}", businessInformation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BusinessInformation> businessInformationList = businessInformationRepository.findAll();
        assertThat(businessInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessInformation.class);
        BusinessInformation businessInformation1 = new BusinessInformation();
        businessInformation1.setId(1L);
        BusinessInformation businessInformation2 = new BusinessInformation();
        businessInformation2.setId(businessInformation1.getId());
        assertThat(businessInformation1).isEqualTo(businessInformation2);
        businessInformation2.setId(2L);
        assertThat(businessInformation1).isNotEqualTo(businessInformation2);
        businessInformation1.setId(null);
        assertThat(businessInformation1).isNotEqualTo(businessInformation2);
    }
}
