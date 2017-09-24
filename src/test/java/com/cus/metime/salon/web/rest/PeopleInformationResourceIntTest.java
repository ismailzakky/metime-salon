package com.cus.metime.salon.web.rest;

import com.cus.metime.salon.SalonApp;

import com.cus.metime.salon.config.SecurityBeanOverrideConfiguration;

import com.cus.metime.salon.domain.PeopleInformation;
import com.cus.metime.salon.repository.PeopleInformationRepository;
import com.cus.metime.salon.service.PeopleInformationService;
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

import com.cus.metime.salon.domain.enumeration.Gender;
import com.cus.metime.salon.domain.enumeration.IdentityType;
/**
 * Test class for the PeopleInformationResource REST controller.
 *
 * @see PeopleInformationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SalonApp.class, SecurityBeanOverrideConfiguration.class})
public class PeopleInformationResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Long DEFAULT_CONTACT_NUMBER = 1L;
    private static final Long UPDATED_CONTACT_NUMBER = 2L;

    private static final String DEFAULT_IDENTITY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTITY_NUMBER = "BBBBBBBBBB";

    private static final IdentityType DEFAULT_IDENTITY_TYPE = IdentityType.KTP;
    private static final IdentityType UPDATED_IDENTITY_TYPE = IdentityType.SIM;

    @Autowired
    private PeopleInformationRepository peopleInformationRepository;

    @Autowired
    private PeopleInformationService peopleInformationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeopleInformationMockMvc;

    private PeopleInformation peopleInformation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PeopleInformationResource peopleInformationResource = new PeopleInformationResource(peopleInformationService);
        this.restPeopleInformationMockMvc = MockMvcBuilders.standaloneSetup(peopleInformationResource)
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
    public static PeopleInformation createEntity(EntityManager em) {
        PeopleInformation peopleInformation = new PeopleInformation()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .gender(DEFAULT_GENDER)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .identityNumber(DEFAULT_IDENTITY_NUMBER)
            .identityType(DEFAULT_IDENTITY_TYPE);
        return peopleInformation;
    }

    @Before
    public void initTest() {
        peopleInformation = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeopleInformation() throws Exception {
        int databaseSizeBeforeCreate = peopleInformationRepository.findAll().size();

        // Create the PeopleInformation
        restPeopleInformationMockMvc.perform(post("/api/people-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peopleInformation)))
            .andExpect(status().isCreated());

        // Validate the PeopleInformation in the database
        List<PeopleInformation> peopleInformationList = peopleInformationRepository.findAll();
        assertThat(peopleInformationList).hasSize(databaseSizeBeforeCreate + 1);
        PeopleInformation testPeopleInformation = peopleInformationList.get(peopleInformationList.size() - 1);
        assertThat(testPeopleInformation.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPeopleInformation.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPeopleInformation.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPeopleInformation.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testPeopleInformation.getIdentityNumber()).isEqualTo(DEFAULT_IDENTITY_NUMBER);
        assertThat(testPeopleInformation.getIdentityType()).isEqualTo(DEFAULT_IDENTITY_TYPE);
    }

    @Test
    @Transactional
    public void createPeopleInformationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = peopleInformationRepository.findAll().size();

        // Create the PeopleInformation with an existing ID
        peopleInformation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeopleInformationMockMvc.perform(post("/api/people-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peopleInformation)))
            .andExpect(status().isBadRequest());

        // Validate the PeopleInformation in the database
        List<PeopleInformation> peopleInformationList = peopleInformationRepository.findAll();
        assertThat(peopleInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPeopleInformations() throws Exception {
        // Initialize the database
        peopleInformationRepository.saveAndFlush(peopleInformation);

        // Get all the peopleInformationList
        restPeopleInformationMockMvc.perform(get("/api/people-informations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peopleInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].identityNumber").value(hasItem(DEFAULT_IDENTITY_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].identityType").value(hasItem(DEFAULT_IDENTITY_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPeopleInformation() throws Exception {
        // Initialize the database
        peopleInformationRepository.saveAndFlush(peopleInformation);

        // Get the peopleInformation
        restPeopleInformationMockMvc.perform(get("/api/people-informations/{id}", peopleInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(peopleInformation.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER.intValue()))
            .andExpect(jsonPath("$.identityNumber").value(DEFAULT_IDENTITY_NUMBER.toString()))
            .andExpect(jsonPath("$.identityType").value(DEFAULT_IDENTITY_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPeopleInformation() throws Exception {
        // Get the peopleInformation
        restPeopleInformationMockMvc.perform(get("/api/people-informations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeopleInformation() throws Exception {
        // Initialize the database
        peopleInformationService.save(peopleInformation);

        int databaseSizeBeforeUpdate = peopleInformationRepository.findAll().size();

        // Update the peopleInformation
        PeopleInformation updatedPeopleInformation = peopleInformationRepository.findOne(peopleInformation.getId());
        updatedPeopleInformation
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .identityNumber(UPDATED_IDENTITY_NUMBER)
            .identityType(UPDATED_IDENTITY_TYPE);

        restPeopleInformationMockMvc.perform(put("/api/people-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeopleInformation)))
            .andExpect(status().isOk());

        // Validate the PeopleInformation in the database
        List<PeopleInformation> peopleInformationList = peopleInformationRepository.findAll();
        assertThat(peopleInformationList).hasSize(databaseSizeBeforeUpdate);
        PeopleInformation testPeopleInformation = peopleInformationList.get(peopleInformationList.size() - 1);
        assertThat(testPeopleInformation.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPeopleInformation.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPeopleInformation.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPeopleInformation.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testPeopleInformation.getIdentityNumber()).isEqualTo(UPDATED_IDENTITY_NUMBER);
        assertThat(testPeopleInformation.getIdentityType()).isEqualTo(UPDATED_IDENTITY_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPeopleInformation() throws Exception {
        int databaseSizeBeforeUpdate = peopleInformationRepository.findAll().size();

        // Create the PeopleInformation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPeopleInformationMockMvc.perform(put("/api/people-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peopleInformation)))
            .andExpect(status().isCreated());

        // Validate the PeopleInformation in the database
        List<PeopleInformation> peopleInformationList = peopleInformationRepository.findAll();
        assertThat(peopleInformationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePeopleInformation() throws Exception {
        // Initialize the database
        peopleInformationService.save(peopleInformation);

        int databaseSizeBeforeDelete = peopleInformationRepository.findAll().size();

        // Get the peopleInformation
        restPeopleInformationMockMvc.perform(delete("/api/people-informations/{id}", peopleInformation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PeopleInformation> peopleInformationList = peopleInformationRepository.findAll();
        assertThat(peopleInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeopleInformation.class);
        PeopleInformation peopleInformation1 = new PeopleInformation();
        peopleInformation1.setId(1L);
        PeopleInformation peopleInformation2 = new PeopleInformation();
        peopleInformation2.setId(peopleInformation1.getId());
        assertThat(peopleInformation1).isEqualTo(peopleInformation2);
        peopleInformation2.setId(2L);
        assertThat(peopleInformation1).isNotEqualTo(peopleInformation2);
        peopleInformation1.setId(null);
        assertThat(peopleInformation1).isNotEqualTo(peopleInformation2);
    }
}
