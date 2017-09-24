package com.cus.metime.salon.web.rest;

import com.cus.metime.salon.SalonApp;

import com.cus.metime.salon.config.SecurityBeanOverrideConfiguration;

import com.cus.metime.salon.domain.CreationalDate;
import com.cus.metime.salon.repository.CreationalDateRepository;
import com.cus.metime.salon.service.CreationalDateService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CreationalDateResource REST controller.
 *
 * @see CreationalDateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SalonApp.class, SecurityBeanOverrideConfiguration.class})
public class CreationalDateResourceIntTest {

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MODIFIED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private CreationalDateRepository creationalDateRepository;

    @Autowired
    private CreationalDateService creationalDateService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCreationalDateMockMvc;

    private CreationalDate creationalDate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CreationalDateResource creationalDateResource = new CreationalDateResource(creationalDateService);
        this.restCreationalDateMockMvc = MockMvcBuilders.standaloneSetup(creationalDateResource)
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
    public static CreationalDate createEntity(EntityManager em) {
        CreationalDate creationalDate = new CreationalDate()
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .modifiedAt(DEFAULT_MODIFIED_AT)
            .modifiedBy(DEFAULT_MODIFIED_BY);
        return creationalDate;
    }

    @Before
    public void initTest() {
        creationalDate = createEntity(em);
    }

    @Test
    @Transactional
    public void createCreationalDate() throws Exception {
        int databaseSizeBeforeCreate = creationalDateRepository.findAll().size();

        // Create the CreationalDate
        restCreationalDateMockMvc.perform(post("/api/creational-dates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creationalDate)))
            .andExpect(status().isCreated());

        // Validate the CreationalDate in the database
        List<CreationalDate> creationalDateList = creationalDateRepository.findAll();
        assertThat(creationalDateList).hasSize(databaseSizeBeforeCreate + 1);
        CreationalDate testCreationalDate = creationalDateList.get(creationalDateList.size() - 1);
        assertThat(testCreationalDate.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCreationalDate.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCreationalDate.getModifiedAt()).isEqualTo(DEFAULT_MODIFIED_AT);
        assertThat(testCreationalDate.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void createCreationalDateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = creationalDateRepository.findAll().size();

        // Create the CreationalDate with an existing ID
        creationalDate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreationalDateMockMvc.perform(post("/api/creational-dates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creationalDate)))
            .andExpect(status().isBadRequest());

        // Validate the CreationalDate in the database
        List<CreationalDate> creationalDateList = creationalDateRepository.findAll();
        assertThat(creationalDateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCreationalDates() throws Exception {
        // Initialize the database
        creationalDateRepository.saveAndFlush(creationalDate);

        // Get all the creationalDateList
        restCreationalDateMockMvc.perform(get("/api/creational-dates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creationalDate.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.toString())));
    }

    @Test
    @Transactional
    public void getCreationalDate() throws Exception {
        // Initialize the database
        creationalDateRepository.saveAndFlush(creationalDate);

        // Get the creationalDate
        restCreationalDateMockMvc.perform(get("/api/creational-dates/{id}", creationalDate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(creationalDate.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.modifiedAt").value(DEFAULT_MODIFIED_AT.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCreationalDate() throws Exception {
        // Get the creationalDate
        restCreationalDateMockMvc.perform(get("/api/creational-dates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCreationalDate() throws Exception {
        // Initialize the database
        creationalDateService.save(creationalDate);

        int databaseSizeBeforeUpdate = creationalDateRepository.findAll().size();

        // Update the creationalDate
        CreationalDate updatedCreationalDate = creationalDateRepository.findOne(creationalDate.getId());
        updatedCreationalDate
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .modifiedBy(UPDATED_MODIFIED_BY);

        restCreationalDateMockMvc.perform(put("/api/creational-dates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCreationalDate)))
            .andExpect(status().isOk());

        // Validate the CreationalDate in the database
        List<CreationalDate> creationalDateList = creationalDateRepository.findAll();
        assertThat(creationalDateList).hasSize(databaseSizeBeforeUpdate);
        CreationalDate testCreationalDate = creationalDateList.get(creationalDateList.size() - 1);
        assertThat(testCreationalDate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCreationalDate.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCreationalDate.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
        assertThat(testCreationalDate.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingCreationalDate() throws Exception {
        int databaseSizeBeforeUpdate = creationalDateRepository.findAll().size();

        // Create the CreationalDate

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCreationalDateMockMvc.perform(put("/api/creational-dates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creationalDate)))
            .andExpect(status().isCreated());

        // Validate the CreationalDate in the database
        List<CreationalDate> creationalDateList = creationalDateRepository.findAll();
        assertThat(creationalDateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCreationalDate() throws Exception {
        // Initialize the database
        creationalDateService.save(creationalDate);

        int databaseSizeBeforeDelete = creationalDateRepository.findAll().size();

        // Get the creationalDate
        restCreationalDateMockMvc.perform(delete("/api/creational-dates/{id}", creationalDate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CreationalDate> creationalDateList = creationalDateRepository.findAll();
        assertThat(creationalDateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreationalDate.class);
        CreationalDate creationalDate1 = new CreationalDate();
        creationalDate1.setId(1L);
        CreationalDate creationalDate2 = new CreationalDate();
        creationalDate2.setId(creationalDate1.getId());
        assertThat(creationalDate1).isEqualTo(creationalDate2);
        creationalDate2.setId(2L);
        assertThat(creationalDate1).isNotEqualTo(creationalDate2);
        creationalDate1.setId(null);
        assertThat(creationalDate1).isNotEqualTo(creationalDate2);
    }
}
