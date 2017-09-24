package com.cus.metime.salon.web.rest;

import com.cus.metime.salon.SalonApp;

import com.cus.metime.salon.config.SecurityBeanOverrideConfiguration;

import com.cus.metime.salon.domain.WorkingTime;
import com.cus.metime.salon.repository.WorkingTimeRepository;
import com.cus.metime.salon.service.WorkingTimeService;
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

import com.cus.metime.salon.domain.enumeration.WorkingDay;
/**
 * Test class for the WorkingTimeResource REST controller.
 *
 * @see WorkingTimeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SalonApp.class, SecurityBeanOverrideConfiguration.class})
public class WorkingTimeResourceIntTest {

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final WorkingDay DEFAULT_WORKING_DAY = WorkingDay.SUNDAY;
    private static final WorkingDay UPDATED_WORKING_DAY = WorkingDay.MONDAY;

    @Autowired
    private WorkingTimeRepository workingTimeRepository;

    @Autowired
    private WorkingTimeService workingTimeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkingTimeMockMvc;

    private WorkingTime workingTime;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkingTimeResource workingTimeResource = new WorkingTimeResource(workingTimeService);
        this.restWorkingTimeMockMvc = MockMvcBuilders.standaloneSetup(workingTimeResource)
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
    public static WorkingTime createEntity(EntityManager em) {
        WorkingTime workingTime = new WorkingTime()
            .isActive(DEFAULT_IS_ACTIVE)
            .workingDay(DEFAULT_WORKING_DAY);
        return workingTime;
    }

    @Before
    public void initTest() {
        workingTime = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkingTime() throws Exception {
        int databaseSizeBeforeCreate = workingTimeRepository.findAll().size();

        // Create the WorkingTime
        restWorkingTimeMockMvc.perform(post("/api/working-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workingTime)))
            .andExpect(status().isCreated());

        // Validate the WorkingTime in the database
        List<WorkingTime> workingTimeList = workingTimeRepository.findAll();
        assertThat(workingTimeList).hasSize(databaseSizeBeforeCreate + 1);
        WorkingTime testWorkingTime = workingTimeList.get(workingTimeList.size() - 1);
        assertThat(testWorkingTime.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testWorkingTime.getWorkingDay()).isEqualTo(DEFAULT_WORKING_DAY);
    }

    @Test
    @Transactional
    public void createWorkingTimeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workingTimeRepository.findAll().size();

        // Create the WorkingTime with an existing ID
        workingTime.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkingTimeMockMvc.perform(post("/api/working-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workingTime)))
            .andExpect(status().isBadRequest());

        // Validate the WorkingTime in the database
        List<WorkingTime> workingTimeList = workingTimeRepository.findAll();
        assertThat(workingTimeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWorkingTimes() throws Exception {
        // Initialize the database
        workingTimeRepository.saveAndFlush(workingTime);

        // Get all the workingTimeList
        restWorkingTimeMockMvc.perform(get("/api/working-times?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workingTime.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].workingDay").value(hasItem(DEFAULT_WORKING_DAY.toString())));
    }

    @Test
    @Transactional
    public void getWorkingTime() throws Exception {
        // Initialize the database
        workingTimeRepository.saveAndFlush(workingTime);

        // Get the workingTime
        restWorkingTimeMockMvc.perform(get("/api/working-times/{id}", workingTime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workingTime.getId().intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.workingDay").value(DEFAULT_WORKING_DAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkingTime() throws Exception {
        // Get the workingTime
        restWorkingTimeMockMvc.perform(get("/api/working-times/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkingTime() throws Exception {
        // Initialize the database
        workingTimeService.save(workingTime);

        int databaseSizeBeforeUpdate = workingTimeRepository.findAll().size();

        // Update the workingTime
        WorkingTime updatedWorkingTime = workingTimeRepository.findOne(workingTime.getId());
        updatedWorkingTime
            .isActive(UPDATED_IS_ACTIVE)
            .workingDay(UPDATED_WORKING_DAY);

        restWorkingTimeMockMvc.perform(put("/api/working-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkingTime)))
            .andExpect(status().isOk());

        // Validate the WorkingTime in the database
        List<WorkingTime> workingTimeList = workingTimeRepository.findAll();
        assertThat(workingTimeList).hasSize(databaseSizeBeforeUpdate);
        WorkingTime testWorkingTime = workingTimeList.get(workingTimeList.size() - 1);
        assertThat(testWorkingTime.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testWorkingTime.getWorkingDay()).isEqualTo(UPDATED_WORKING_DAY);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkingTime() throws Exception {
        int databaseSizeBeforeUpdate = workingTimeRepository.findAll().size();

        // Create the WorkingTime

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWorkingTimeMockMvc.perform(put("/api/working-times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workingTime)))
            .andExpect(status().isCreated());

        // Validate the WorkingTime in the database
        List<WorkingTime> workingTimeList = workingTimeRepository.findAll();
        assertThat(workingTimeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWorkingTime() throws Exception {
        // Initialize the database
        workingTimeService.save(workingTime);

        int databaseSizeBeforeDelete = workingTimeRepository.findAll().size();

        // Get the workingTime
        restWorkingTimeMockMvc.perform(delete("/api/working-times/{id}", workingTime.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkingTime> workingTimeList = workingTimeRepository.findAll();
        assertThat(workingTimeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkingTime.class);
        WorkingTime workingTime1 = new WorkingTime();
        workingTime1.setId(1L);
        WorkingTime workingTime2 = new WorkingTime();
        workingTime2.setId(workingTime1.getId());
        assertThat(workingTime1).isEqualTo(workingTime2);
        workingTime2.setId(2L);
        assertThat(workingTime1).isNotEqualTo(workingTime2);
        workingTime1.setId(null);
        assertThat(workingTime1).isNotEqualTo(workingTime2);
    }
}
