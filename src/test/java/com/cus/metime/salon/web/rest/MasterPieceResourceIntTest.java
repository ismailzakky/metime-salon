package com.cus.metime.salon.web.rest;

import com.cus.metime.salon.SalonApp;

import com.cus.metime.salon.config.SecurityBeanOverrideConfiguration;

import com.cus.metime.salon.domain.MasterPiece;
import com.cus.metime.salon.repository.MasterPieceRepository;
import com.cus.metime.salon.service.MasterPieceService;
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
/**
 * Test class for the MasterPieceResource REST controller.
 *
 * @see MasterPieceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SalonApp.class, SecurityBeanOverrideConfiguration.class})
public class MasterPieceResourceIntTest {

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_MEDIA_FILE = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_FILE = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_NAME = "BBBBBBBBBB";

    @Autowired
    private MasterPieceRepository masterPieceRepository;

    @Autowired
    private MasterPieceService masterPieceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMasterPieceMockMvc;

    private MasterPiece masterPiece;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MasterPieceResource masterPieceResource = new MasterPieceResource(masterPieceService);
        this.restMasterPieceMockMvc = MockMvcBuilders.standaloneSetup(masterPieceResource)
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
    public static MasterPiece createEntity(EntityManager em) {
        MasterPiece masterPiece = new MasterPiece()
            .gender(DEFAULT_GENDER)
            .mediaFile(DEFAULT_MEDIA_FILE)
            .propertyName(DEFAULT_PROPERTY_NAME);
        return masterPiece;
    }

    @Before
    public void initTest() {
        masterPiece = createEntity(em);
    }

    @Test
    @Transactional
    public void createMasterPiece() throws Exception {
        int databaseSizeBeforeCreate = masterPieceRepository.findAll().size();

        // Create the MasterPiece
        restMasterPieceMockMvc.perform(post("/api/master-pieces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterPiece)))
            .andExpect(status().isCreated());

        // Validate the MasterPiece in the database
        List<MasterPiece> masterPieceList = masterPieceRepository.findAll();
        assertThat(masterPieceList).hasSize(databaseSizeBeforeCreate + 1);
        MasterPiece testMasterPiece = masterPieceList.get(masterPieceList.size() - 1);
        assertThat(testMasterPiece.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testMasterPiece.getMediaFile()).isEqualTo(DEFAULT_MEDIA_FILE);
        assertThat(testMasterPiece.getPropertyName()).isEqualTo(DEFAULT_PROPERTY_NAME);
    }

    @Test
    @Transactional
    public void createMasterPieceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = masterPieceRepository.findAll().size();

        // Create the MasterPiece with an existing ID
        masterPiece.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterPieceMockMvc.perform(post("/api/master-pieces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterPiece)))
            .andExpect(status().isBadRequest());

        // Validate the MasterPiece in the database
        List<MasterPiece> masterPieceList = masterPieceRepository.findAll();
        assertThat(masterPieceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMasterPieces() throws Exception {
        // Initialize the database
        masterPieceRepository.saveAndFlush(masterPiece);

        // Get all the masterPieceList
        restMasterPieceMockMvc.perform(get("/api/master-pieces?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterPiece.getId().intValue())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].mediaFile").value(hasItem(DEFAULT_MEDIA_FILE.toString())))
            .andExpect(jsonPath("$.[*].propertyName").value(hasItem(DEFAULT_PROPERTY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMasterPiece() throws Exception {
        // Initialize the database
        masterPieceRepository.saveAndFlush(masterPiece);

        // Get the masterPiece
        restMasterPieceMockMvc.perform(get("/api/master-pieces/{id}", masterPiece.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(masterPiece.getId().intValue()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.mediaFile").value(DEFAULT_MEDIA_FILE.toString()))
            .andExpect(jsonPath("$.propertyName").value(DEFAULT_PROPERTY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMasterPiece() throws Exception {
        // Get the masterPiece
        restMasterPieceMockMvc.perform(get("/api/master-pieces/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMasterPiece() throws Exception {
        // Initialize the database
        masterPieceService.save(masterPiece);

        int databaseSizeBeforeUpdate = masterPieceRepository.findAll().size();

        // Update the masterPiece
        MasterPiece updatedMasterPiece = masterPieceRepository.findOne(masterPiece.getId());
        updatedMasterPiece
            .gender(UPDATED_GENDER)
            .mediaFile(UPDATED_MEDIA_FILE)
            .propertyName(UPDATED_PROPERTY_NAME);

        restMasterPieceMockMvc.perform(put("/api/master-pieces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMasterPiece)))
            .andExpect(status().isOk());

        // Validate the MasterPiece in the database
        List<MasterPiece> masterPieceList = masterPieceRepository.findAll();
        assertThat(masterPieceList).hasSize(databaseSizeBeforeUpdate);
        MasterPiece testMasterPiece = masterPieceList.get(masterPieceList.size() - 1);
        assertThat(testMasterPiece.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testMasterPiece.getMediaFile()).isEqualTo(UPDATED_MEDIA_FILE);
        assertThat(testMasterPiece.getPropertyName()).isEqualTo(UPDATED_PROPERTY_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMasterPiece() throws Exception {
        int databaseSizeBeforeUpdate = masterPieceRepository.findAll().size();

        // Create the MasterPiece

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMasterPieceMockMvc.perform(put("/api/master-pieces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterPiece)))
            .andExpect(status().isCreated());

        // Validate the MasterPiece in the database
        List<MasterPiece> masterPieceList = masterPieceRepository.findAll();
        assertThat(masterPieceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMasterPiece() throws Exception {
        // Initialize the database
        masterPieceService.save(masterPiece);

        int databaseSizeBeforeDelete = masterPieceRepository.findAll().size();

        // Get the masterPiece
        restMasterPieceMockMvc.perform(delete("/api/master-pieces/{id}", masterPiece.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MasterPiece> masterPieceList = masterPieceRepository.findAll();
        assertThat(masterPieceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterPiece.class);
        MasterPiece masterPiece1 = new MasterPiece();
        masterPiece1.setId(1L);
        MasterPiece masterPiece2 = new MasterPiece();
        masterPiece2.setId(masterPiece1.getId());
        assertThat(masterPiece1).isEqualTo(masterPiece2);
        masterPiece2.setId(2L);
        assertThat(masterPiece1).isNotEqualTo(masterPiece2);
        masterPiece1.setId(null);
        assertThat(masterPiece1).isNotEqualTo(masterPiece2);
    }
}
