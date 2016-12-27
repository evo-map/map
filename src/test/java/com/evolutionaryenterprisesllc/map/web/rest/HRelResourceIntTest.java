package com.evolutionaryenterprisesllc.map.web.rest;

import com.evolutionaryenterprisesllc.map.MapApp;

import com.evolutionaryenterprisesllc.map.domain.HRel;
import com.evolutionaryenterprisesllc.map.repository.HRelRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evolutionaryenterprisesllc.map.domain.enumeration.RelationshipSemantic;
import com.evolutionaryenterprisesllc.map.domain.enumeration.DeletionSemantic;
/**
 * Test class for the HRelResource REST controller.
 *
 * @see HRelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MapApp.class)
public class HRelResourceIntTest {

    private static final RelationshipSemantic DEFAULT_RELATIONSHIP_SEMANTIC = RelationshipSemantic.AKO;
    private static final RelationshipSemantic UPDATED_RELATIONSHIP_SEMANTIC = RelationshipSemantic.APO;

    private static final DeletionSemantic DEFAULT_DELETION_SEMANTIC = DeletionSemantic.BLOCK;
    private static final DeletionSemantic UPDATED_DELETION_SEMANTIC = DeletionSemantic.PROP;

    private static final Long DEFAULT_MAX_TO_CARDINALITY = 1L;
    private static final Long UPDATED_MAX_TO_CARDINALITY = 2L;

    private static final Long DEFAULT_MAX_FROM_CARDINALITY = 1L;
    private static final Long UPDATED_MAX_FROM_CARDINALITY = 2L;

    private static final Boolean DEFAULT_REQUIRES_TO_HOLON = false;
    private static final Boolean UPDATED_REQUIRES_TO_HOLON = true;

    private static final Boolean DEFAULT_REQUIRES_FROM_HOLON = false;
    private static final Boolean UPDATED_REQUIRES_FROM_HOLON = true;

    @Inject
    private HRelRepository hRelRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHRelMockMvc;

    private HRel hRel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HRelResource hRelResource = new HRelResource();
        ReflectionTestUtils.setField(hRelResource, "hRelRepository", hRelRepository);
        this.restHRelMockMvc = MockMvcBuilders.standaloneSetup(hRelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HRel createEntity(EntityManager em) {
        HRel hRel = new HRel()
                .relationshipSemantic(DEFAULT_RELATIONSHIP_SEMANTIC)
                .deletionSemantic(DEFAULT_DELETION_SEMANTIC)
                .maxToCardinality(DEFAULT_MAX_TO_CARDINALITY)
                .maxFromCardinality(DEFAULT_MAX_FROM_CARDINALITY)
                .requiresToHolon(DEFAULT_REQUIRES_TO_HOLON)
                .requiresFromHolon(DEFAULT_REQUIRES_FROM_HOLON);
        return hRel;
    }

    @Before
    public void initTest() {
        hRel = createEntity(em);
    }

    @Test
    @Transactional
    public void createHRel() throws Exception {
        int databaseSizeBeforeCreate = hRelRepository.findAll().size();

        // Create the HRel

        restHRelMockMvc.perform(post("/api/h-rels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hRel)))
            .andExpect(status().isCreated());

        // Validate the HRel in the database
        List<HRel> hRelList = hRelRepository.findAll();
        assertThat(hRelList).hasSize(databaseSizeBeforeCreate + 1);
        HRel testHRel = hRelList.get(hRelList.size() - 1);
        assertThat(testHRel.getRelationshipSemantic()).isEqualTo(DEFAULT_RELATIONSHIP_SEMANTIC);
        assertThat(testHRel.getDeletionSemantic()).isEqualTo(DEFAULT_DELETION_SEMANTIC);
        assertThat(testHRel.getMaxToCardinality()).isEqualTo(DEFAULT_MAX_TO_CARDINALITY);
        assertThat(testHRel.getMaxFromCardinality()).isEqualTo(DEFAULT_MAX_FROM_CARDINALITY);
        assertThat(testHRel.isRequiresToHolon()).isEqualTo(DEFAULT_REQUIRES_TO_HOLON);
        assertThat(testHRel.isRequiresFromHolon()).isEqualTo(DEFAULT_REQUIRES_FROM_HOLON);
    }

    @Test
    @Transactional
    public void createHRelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hRelRepository.findAll().size();

        // Create the HRel with an existing ID
        HRel existingHRel = new HRel();
        existingHRel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHRelMockMvc.perform(post("/api/h-rels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingHRel)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HRel> hRelList = hRelRepository.findAll();
        assertThat(hRelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHRels() throws Exception {
        // Initialize the database
        hRelRepository.saveAndFlush(hRel);

        // Get all the hRelList
        restHRelMockMvc.perform(get("/api/h-rels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hRel.getId().intValue())))
            .andExpect(jsonPath("$.[*].relationshipSemantic").value(hasItem(DEFAULT_RELATIONSHIP_SEMANTIC.toString())))
            .andExpect(jsonPath("$.[*].deletionSemantic").value(hasItem(DEFAULT_DELETION_SEMANTIC.toString())))
            .andExpect(jsonPath("$.[*].maxToCardinality").value(hasItem(DEFAULT_MAX_TO_CARDINALITY.intValue())))
            .andExpect(jsonPath("$.[*].maxFromCardinality").value(hasItem(DEFAULT_MAX_FROM_CARDINALITY.intValue())))
            .andExpect(jsonPath("$.[*].requiresToHolon").value(hasItem(DEFAULT_REQUIRES_TO_HOLON.booleanValue())))
            .andExpect(jsonPath("$.[*].requiresFromHolon").value(hasItem(DEFAULT_REQUIRES_FROM_HOLON.booleanValue())));
    }

    @Test
    @Transactional
    public void getHRel() throws Exception {
        // Initialize the database
        hRelRepository.saveAndFlush(hRel);

        // Get the hRel
        restHRelMockMvc.perform(get("/api/h-rels/{id}", hRel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hRel.getId().intValue()))
            .andExpect(jsonPath("$.relationshipSemantic").value(DEFAULT_RELATIONSHIP_SEMANTIC.toString()))
            .andExpect(jsonPath("$.deletionSemantic").value(DEFAULT_DELETION_SEMANTIC.toString()))
            .andExpect(jsonPath("$.maxToCardinality").value(DEFAULT_MAX_TO_CARDINALITY.intValue()))
            .andExpect(jsonPath("$.maxFromCardinality").value(DEFAULT_MAX_FROM_CARDINALITY.intValue()))
            .andExpect(jsonPath("$.requiresToHolon").value(DEFAULT_REQUIRES_TO_HOLON.booleanValue()))
            .andExpect(jsonPath("$.requiresFromHolon").value(DEFAULT_REQUIRES_FROM_HOLON.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHRel() throws Exception {
        // Get the hRel
        restHRelMockMvc.perform(get("/api/h-rels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHRel() throws Exception {
        // Initialize the database
        hRelRepository.saveAndFlush(hRel);
        int databaseSizeBeforeUpdate = hRelRepository.findAll().size();

        // Update the hRel
        HRel updatedHRel = hRelRepository.findOne(hRel.getId());
        updatedHRel
                .relationshipSemantic(UPDATED_RELATIONSHIP_SEMANTIC)
                .deletionSemantic(UPDATED_DELETION_SEMANTIC)
                .maxToCardinality(UPDATED_MAX_TO_CARDINALITY)
                .maxFromCardinality(UPDATED_MAX_FROM_CARDINALITY)
                .requiresToHolon(UPDATED_REQUIRES_TO_HOLON)
                .requiresFromHolon(UPDATED_REQUIRES_FROM_HOLON);

        restHRelMockMvc.perform(put("/api/h-rels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHRel)))
            .andExpect(status().isOk());

        // Validate the HRel in the database
        List<HRel> hRelList = hRelRepository.findAll();
        assertThat(hRelList).hasSize(databaseSizeBeforeUpdate);
        HRel testHRel = hRelList.get(hRelList.size() - 1);
        assertThat(testHRel.getRelationshipSemantic()).isEqualTo(UPDATED_RELATIONSHIP_SEMANTIC);
        assertThat(testHRel.getDeletionSemantic()).isEqualTo(UPDATED_DELETION_SEMANTIC);
        assertThat(testHRel.getMaxToCardinality()).isEqualTo(UPDATED_MAX_TO_CARDINALITY);
        assertThat(testHRel.getMaxFromCardinality()).isEqualTo(UPDATED_MAX_FROM_CARDINALITY);
        assertThat(testHRel.isRequiresToHolon()).isEqualTo(UPDATED_REQUIRES_TO_HOLON);
        assertThat(testHRel.isRequiresFromHolon()).isEqualTo(UPDATED_REQUIRES_FROM_HOLON);
    }

    @Test
    @Transactional
    public void updateNonExistingHRel() throws Exception {
        int databaseSizeBeforeUpdate = hRelRepository.findAll().size();

        // Create the HRel

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHRelMockMvc.perform(put("/api/h-rels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hRel)))
            .andExpect(status().isCreated());

        // Validate the HRel in the database
        List<HRel> hRelList = hRelRepository.findAll();
        assertThat(hRelList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHRel() throws Exception {
        // Initialize the database
        hRelRepository.saveAndFlush(hRel);
        int databaseSizeBeforeDelete = hRelRepository.findAll().size();

        // Get the hRel
        restHRelMockMvc.perform(delete("/api/h-rels/{id}", hRel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HRel> hRelList = hRelRepository.findAll();
        assertThat(hRelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
