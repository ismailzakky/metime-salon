package com.cus.metime.salon.web.rest;

import com.cus.metime.salon.SalonApp;

import com.cus.metime.salon.config.SecurityBeanOverrideConfiguration;

import com.cus.metime.salon.domain.Salon;
import com.cus.metime.salon.repository.SalonRepository;
import com.cus.metime.salon.service.SalonService;
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
 * Test class for the SalonResource REST controller.
 *
 * @see SalonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SalonApp.class, SecurityBeanOverrideConfiguration.class})
public class SalonResourceIntTest {

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIA_FILE = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_FILE = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private SalonService salonService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalonMockMvc;

    private Salon salon;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalonResource salonResource = new SalonResource(salonService);
        this.restSalonMockMvc = MockMvcBuilders.standaloneSetup(salonResource)
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
    public static Salon createEntity(EntityManager em) {
        Salon salon = new Salon()
            .creationDate(DEFAULT_CREATION_DATE)
            .isActive(DEFAULT_IS_ACTIVE)
            .manager(DEFAULT_MANAGER)
            .mediaFile(DEFAULT_MEDIA_FILE)
            .owner(DEFAULT_OWNER);
        return salon;
    }

    @Before
    public void initTest() {
        salon = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalon() throws Exception {
        int databaseSizeBeforeCreate = salonRepository.findAll().size();

        // Create the Salon
        restSalonMockMvc.perform(post("/api/salons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salon)))
            .andExpect(status().isCreated());

        // Validate the Salon in the database
        List<Salon> salonList = salonRepository.findAll();
        assertThat(salonList).hasSize(databaseSizeBeforeCreate + 1);
        Salon testSalon = salonList.get(salonList.size() - 1);
        assertThat(testSalon.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testSalon.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testSalon.getManager()).isEqualTo(DEFAULT_MANAGER);
        assertThat(testSalon.getMediaFile()).isEqualTo(DEFAULT_MEDIA_FILE);
        assertThat(testSalon.getOwner()).isEqualTo(DEFAULT_OWNER);
    }

    @Test
    @Transactional
    public void createSalonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salonRepository.findAll().size();

        // Create the Salon with an existing ID
        salon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalonMockMvc.perform(post("/api/salons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salon)))
            .andExpect(status().isBadRequest());

        // Validate the Salon in the database
        List<Salon> salonList = salonRepository.findAll();
        assertThat(salonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSalons() throws Exception {
        // Initialize the database
        salonRepository.saveAndFlush(salon);

        // Get all the salonList
        restSalonMockMvc.perform(get("/api/salons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salon.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].manager").value(hasItem(DEFAULT_MANAGER.toString())))
            .andExpect(jsonPath("$.[*].mediaFile").value(hasItem(DEFAULT_MEDIA_FILE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())));
    }

    @Test
    @Transactional
    public void getSalon() throws Exception {
        // Initialize the database
        salonRepository.saveAndFlush(salon);

        // Get the salon
        restSalonMockMvc.perform(get("/api/salons/{id}", salon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salon.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.manager").value(DEFAULT_MANAGER.toString()))
            .andExpect(jsonPath("$.mediaFile").value(DEFAULT_MEDIA_FILE.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSalon() throws Exception {
        // Get the salon
        restSalonMockMvc.perform(get("/api/salons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalon() throws Exception {
        // Initialize the database
        salonService.save(salon);

        int databaseSizeBeforeUpdate = salonRepository.findAll().size();

        // Update the salon
        Salon updatedSalon = salonRepository.findOne(salon.getId());
        updatedSalon
            .creationDate(UPDATED_CREATION_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .manager(UPDATED_MANAGER)
            .mediaFile(UPDATED_MEDIA_FILE)
            .owner(UPDATED_OWNER);

        restSalonMockMvc.perform(put("/api/salons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalon)))
            .andExpect(status().isOk());

        // Validate the Salon in the database
        List<Salon> salonList = salonRepository.findAll();
        assertThat(salonList).hasSize(databaseSizeBeforeUpdate);
        Salon testSalon = salonList.get(salonList.size() - 1);
        assertThat(testSalon.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSalon.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testSalon.getManager()).isEqualTo(UPDATED_MANAGER);
        assertThat(testSalon.getMediaFile()).isEqualTo(UPDATED_MEDIA_FILE);
        assertThat(testSalon.getOwner()).isEqualTo(UPDATED_OWNER);
    }

    @Test
    @Transactional
    public void updateNonExistingSalon() throws Exception {
        int databaseSizeBeforeUpdate = salonRepository.findAll().size();

        // Create the Salon

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalonMockMvc.perform(put("/api/salons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salon)))
            .andExpect(status().isCreated());

        // Validate the Salon in the database
        List<Salon> salonList = salonRepository.findAll();
        assertThat(salonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSalon() throws Exception {
        // Initialize the database
        salonService.save(salon);

        int databaseSizeBeforeDelete = salonRepository.findAll().size();

        // Get the salon
        restSalonMockMvc.perform(delete("/api/salons/{id}", salon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Salon> salonList = salonRepository.findAll();
        assertThat(salonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Salon.class);
        Salon salon1 = new Salon();
        salon1.setId(1L);
        Salon salon2 = new Salon();
        salon2.setId(salon1.getId());
        assertThat(salon1).isEqualTo(salon2);
        salon2.setId(2L);
        assertThat(salon1).isNotEqualTo(salon2);
        salon1.setId(null);
        assertThat(salon1).isNotEqualTo(salon2);
    }
}
