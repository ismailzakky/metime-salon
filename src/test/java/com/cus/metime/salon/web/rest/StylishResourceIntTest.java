package com.cus.metime.salon.web.rest;

import com.cus.metime.salon.SalonApp;

import com.cus.metime.salon.config.SecurityBeanOverrideConfiguration;

import com.cus.metime.salon.domain.Stylish;
import com.cus.metime.salon.repository.StylishRepository;
import com.cus.metime.salon.service.StylishService;
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
 * Test class for the StylishResource REST controller.
 *
 * @see StylishResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SalonApp.class, SecurityBeanOverrideConfiguration.class})
public class StylishResourceIntTest {

    private static final String DEFAULT_MEDIA_FILE = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_FILE = "BBBBBBBBBB";

    @Autowired
    private StylishRepository stylishRepository;

    @Autowired
    private StylishService stylishService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStylishMockMvc;

    private Stylish stylish;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StylishResource stylishResource = new StylishResource(stylishService);
        this.restStylishMockMvc = MockMvcBuilders.standaloneSetup(stylishResource)
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
    public static Stylish createEntity(EntityManager em) {
        Stylish stylish = new Stylish()
            .mediaFile(DEFAULT_MEDIA_FILE);
        return stylish;
    }

    @Before
    public void initTest() {
        stylish = createEntity(em);
    }

    @Test
    @Transactional
    public void createStylish() throws Exception {
        int databaseSizeBeforeCreate = stylishRepository.findAll().size();

        // Create the Stylish
        restStylishMockMvc.perform(post("/api/stylishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stylish)))
            .andExpect(status().isCreated());

        // Validate the Stylish in the database
        List<Stylish> stylishList = stylishRepository.findAll();
        assertThat(stylishList).hasSize(databaseSizeBeforeCreate + 1);
        Stylish testStylish = stylishList.get(stylishList.size() - 1);
        assertThat(testStylish.getMediaFile()).isEqualTo(DEFAULT_MEDIA_FILE);
    }

    @Test
    @Transactional
    public void createStylishWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stylishRepository.findAll().size();

        // Create the Stylish with an existing ID
        stylish.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStylishMockMvc.perform(post("/api/stylishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stylish)))
            .andExpect(status().isBadRequest());

        // Validate the Stylish in the database
        List<Stylish> stylishList = stylishRepository.findAll();
        assertThat(stylishList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStylishes() throws Exception {
        // Initialize the database
        stylishRepository.saveAndFlush(stylish);

        // Get all the stylishList
        restStylishMockMvc.perform(get("/api/stylishes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stylish.getId().intValue())))
            .andExpect(jsonPath("$.[*].mediaFile").value(hasItem(DEFAULT_MEDIA_FILE.toString())));
    }

    @Test
    @Transactional
    public void getStylish() throws Exception {
        // Initialize the database
        stylishRepository.saveAndFlush(stylish);

        // Get the stylish
        restStylishMockMvc.perform(get("/api/stylishes/{id}", stylish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stylish.getId().intValue()))
            .andExpect(jsonPath("$.mediaFile").value(DEFAULT_MEDIA_FILE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStylish() throws Exception {
        // Get the stylish
        restStylishMockMvc.perform(get("/api/stylishes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStylish() throws Exception {
        // Initialize the database
        stylishService.save(stylish);

        int databaseSizeBeforeUpdate = stylishRepository.findAll().size();

        // Update the stylish
        Stylish updatedStylish = stylishRepository.findOne(stylish.getId());
        updatedStylish
            .mediaFile(UPDATED_MEDIA_FILE);

        restStylishMockMvc.perform(put("/api/stylishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStylish)))
            .andExpect(status().isOk());

        // Validate the Stylish in the database
        List<Stylish> stylishList = stylishRepository.findAll();
        assertThat(stylishList).hasSize(databaseSizeBeforeUpdate);
        Stylish testStylish = stylishList.get(stylishList.size() - 1);
        assertThat(testStylish.getMediaFile()).isEqualTo(UPDATED_MEDIA_FILE);
    }

    @Test
    @Transactional
    public void updateNonExistingStylish() throws Exception {
        int databaseSizeBeforeUpdate = stylishRepository.findAll().size();

        // Create the Stylish

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStylishMockMvc.perform(put("/api/stylishes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stylish)))
            .andExpect(status().isCreated());

        // Validate the Stylish in the database
        List<Stylish> stylishList = stylishRepository.findAll();
        assertThat(stylishList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStylish() throws Exception {
        // Initialize the database
        stylishService.save(stylish);

        int databaseSizeBeforeDelete = stylishRepository.findAll().size();

        // Get the stylish
        restStylishMockMvc.perform(delete("/api/stylishes/{id}", stylish.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Stylish> stylishList = stylishRepository.findAll();
        assertThat(stylishList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stylish.class);
        Stylish stylish1 = new Stylish();
        stylish1.setId(1L);
        Stylish stylish2 = new Stylish();
        stylish2.setId(stylish1.getId());
        assertThat(stylish1).isEqualTo(stylish2);
        stylish2.setId(2L);
        assertThat(stylish1).isNotEqualTo(stylish2);
        stylish1.setId(null);
        assertThat(stylish1).isNotEqualTo(stylish2);
    }
}
