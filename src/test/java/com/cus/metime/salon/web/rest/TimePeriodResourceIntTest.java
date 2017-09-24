package com.cus.metime.salon.web.rest;

import com.cus.metime.salon.SalonApp;

import com.cus.metime.salon.config.SecurityBeanOverrideConfiguration;

import com.cus.metime.salon.domain.TimePeriod;
import com.cus.metime.salon.repository.TimePeriodRepository;
import com.cus.metime.salon.service.TimePeriodService;
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
 * Test class for the TimePeriodResource REST controller.
 *
 * @see TimePeriodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SalonApp.class, SecurityBeanOverrideConfiguration.class})
public class TimePeriodResourceIntTest {

    private static final LocalDate DEFAULT_END_HOUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_HOUR = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_START_HOUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_HOUR = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TimePeriodRepository timePeriodRepository;

    @Autowired
    private TimePeriodService timePeriodService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTimePeriodMockMvc;

    private TimePeriod timePeriod;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TimePeriodResource timePeriodResource = new TimePeriodResource(timePeriodService);
        this.restTimePeriodMockMvc = MockMvcBuilders.standaloneSetup(timePeriodResource)
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
    public static TimePeriod createEntity(EntityManager em) {
        TimePeriod timePeriod = new TimePeriod()
            .endHour(DEFAULT_END_HOUR)
            .startHour(DEFAULT_START_HOUR);
        return timePeriod;
    }

    @Before
    public void initTest() {
        timePeriod = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimePeriod() throws Exception {
        int databaseSizeBeforeCreate = timePeriodRepository.findAll().size();

        // Create the TimePeriod
        restTimePeriodMockMvc.perform(post("/api/time-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timePeriod)))
            .andExpect(status().isCreated());

        // Validate the TimePeriod in the database
        List<TimePeriod> timePeriodList = timePeriodRepository.findAll();
        assertThat(timePeriodList).hasSize(databaseSizeBeforeCreate + 1);
        TimePeriod testTimePeriod = timePeriodList.get(timePeriodList.size() - 1);
        assertThat(testTimePeriod.getEndHour()).isEqualTo(DEFAULT_END_HOUR);
        assertThat(testTimePeriod.getStartHour()).isEqualTo(DEFAULT_START_HOUR);
    }

    @Test
    @Transactional
    public void createTimePeriodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timePeriodRepository.findAll().size();

        // Create the TimePeriod with an existing ID
        timePeriod.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimePeriodMockMvc.perform(post("/api/time-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timePeriod)))
            .andExpect(status().isBadRequest());

        // Validate the TimePeriod in the database
        List<TimePeriod> timePeriodList = timePeriodRepository.findAll();
        assertThat(timePeriodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTimePeriods() throws Exception {
        // Initialize the database
        timePeriodRepository.saveAndFlush(timePeriod);

        // Get all the timePeriodList
        restTimePeriodMockMvc.perform(get("/api/time-periods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timePeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR.toString())))
            .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR.toString())));
    }

    @Test
    @Transactional
    public void getTimePeriod() throws Exception {
        // Initialize the database
        timePeriodRepository.saveAndFlush(timePeriod);

        // Get the timePeriod
        restTimePeriodMockMvc.perform(get("/api/time-periods/{id}", timePeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timePeriod.getId().intValue()))
            .andExpect(jsonPath("$.endHour").value(DEFAULT_END_HOUR.toString()))
            .andExpect(jsonPath("$.startHour").value(DEFAULT_START_HOUR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTimePeriod() throws Exception {
        // Get the timePeriod
        restTimePeriodMockMvc.perform(get("/api/time-periods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimePeriod() throws Exception {
        // Initialize the database
        timePeriodService.save(timePeriod);

        int databaseSizeBeforeUpdate = timePeriodRepository.findAll().size();

        // Update the timePeriod
        TimePeriod updatedTimePeriod = timePeriodRepository.findOne(timePeriod.getId());
        updatedTimePeriod
            .endHour(UPDATED_END_HOUR)
            .startHour(UPDATED_START_HOUR);

        restTimePeriodMockMvc.perform(put("/api/time-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTimePeriod)))
            .andExpect(status().isOk());

        // Validate the TimePeriod in the database
        List<TimePeriod> timePeriodList = timePeriodRepository.findAll();
        assertThat(timePeriodList).hasSize(databaseSizeBeforeUpdate);
        TimePeriod testTimePeriod = timePeriodList.get(timePeriodList.size() - 1);
        assertThat(testTimePeriod.getEndHour()).isEqualTo(UPDATED_END_HOUR);
        assertThat(testTimePeriod.getStartHour()).isEqualTo(UPDATED_START_HOUR);
    }

    @Test
    @Transactional
    public void updateNonExistingTimePeriod() throws Exception {
        int databaseSizeBeforeUpdate = timePeriodRepository.findAll().size();

        // Create the TimePeriod

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimePeriodMockMvc.perform(put("/api/time-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timePeriod)))
            .andExpect(status().isCreated());

        // Validate the TimePeriod in the database
        List<TimePeriod> timePeriodList = timePeriodRepository.findAll();
        assertThat(timePeriodList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTimePeriod() throws Exception {
        // Initialize the database
        timePeriodService.save(timePeriod);

        int databaseSizeBeforeDelete = timePeriodRepository.findAll().size();

        // Get the timePeriod
        restTimePeriodMockMvc.perform(delete("/api/time-periods/{id}", timePeriod.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TimePeriod> timePeriodList = timePeriodRepository.findAll();
        assertThat(timePeriodList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimePeriod.class);
        TimePeriod timePeriod1 = new TimePeriod();
        timePeriod1.setId(1L);
        TimePeriod timePeriod2 = new TimePeriod();
        timePeriod2.setId(timePeriod1.getId());
        assertThat(timePeriod1).isEqualTo(timePeriod2);
        timePeriod2.setId(2L);
        assertThat(timePeriod1).isNotEqualTo(timePeriod2);
        timePeriod1.setId(null);
        assertThat(timePeriod1).isNotEqualTo(timePeriod2);
    }
}
