package com.evolutionaryenterprisesllc.map.web.rest;

import com.evolutionaryenterprisesllc.map.MapApp;

import com.evolutionaryenterprisesllc.map.domain.HDescriptor;
import com.evolutionaryenterprisesllc.map.repository.HDescriptorRepository;

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

import com.evolutionaryenterprisesllc.map.domain.enumeration.HDescriptorType;
/**
 * Test class for the HDescriptorResource REST controller.
 *
 * @see HDescriptorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MapApp.class)
public class HDescriptorResourceIntTest {

    private static final HDescriptorType DEFAULT_TYPE = HDescriptorType.JSON_SCHEMA;
    private static final HDescriptorType UPDATED_TYPE = HDescriptorType.RDF_TURTLE;

    private static final String DEFAULT_DECSCRIPTOR_BODY = "AAAAAAAAAA";
    private static final String UPDATED_DECSCRIPTOR_BODY = "BBBBBBBBBB";

    @Inject
    private HDescriptorRepository hDescriptorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHDescriptorMockMvc;

    private HDescriptor hDescriptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HDescriptorResource hDescriptorResource = new HDescriptorResource();
        ReflectionTestUtils.setField(hDescriptorResource, "hDescriptorRepository", hDescriptorRepository);
        this.restHDescriptorMockMvc = MockMvcBuilders.standaloneSetup(hDescriptorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HDescriptor createEntity(EntityManager em) {
        HDescriptor hDescriptor = new HDescriptor()
                .type(DEFAULT_TYPE)
                .decscriptorBody(DEFAULT_DECSCRIPTOR_BODY);
        return hDescriptor;
    }

    @Before
    public void initTest() {
        hDescriptor = createEntity(em);
    }

    @Test
    @Transactional
    public void createHDescriptor() throws Exception {
        int databaseSizeBeforeCreate = hDescriptorRepository.findAll().size();

        // Create the HDescriptor

        restHDescriptorMockMvc.perform(post("/api/h-descriptors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hDescriptor)))
            .andExpect(status().isCreated());

        // Validate the HDescriptor in the database
        List<HDescriptor> hDescriptorList = hDescriptorRepository.findAll();
        assertThat(hDescriptorList).hasSize(databaseSizeBeforeCreate + 1);
        HDescriptor testHDescriptor = hDescriptorList.get(hDescriptorList.size() - 1);
        assertThat(testHDescriptor.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHDescriptor.getDecscriptorBody()).isEqualTo(DEFAULT_DECSCRIPTOR_BODY);
    }

    @Test
    @Transactional
    public void createHDescriptorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hDescriptorRepository.findAll().size();

        // Create the HDescriptor with an existing ID
        HDescriptor existingHDescriptor = new HDescriptor();
        existingHDescriptor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHDescriptorMockMvc.perform(post("/api/h-descriptors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingHDescriptor)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HDescriptor> hDescriptorList = hDescriptorRepository.findAll();
        assertThat(hDescriptorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHDescriptors() throws Exception {
        // Initialize the database
        hDescriptorRepository.saveAndFlush(hDescriptor);

        // Get all the hDescriptorList
        restHDescriptorMockMvc.perform(get("/api/h-descriptors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hDescriptor.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].decscriptorBody").value(hasItem(DEFAULT_DECSCRIPTOR_BODY.toString())));
    }

    @Test
    @Transactional
    public void getHDescriptor() throws Exception {
        // Initialize the database
        hDescriptorRepository.saveAndFlush(hDescriptor);

        // Get the hDescriptor
        restHDescriptorMockMvc.perform(get("/api/h-descriptors/{id}", hDescriptor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hDescriptor.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.decscriptorBody").value(DEFAULT_DECSCRIPTOR_BODY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHDescriptor() throws Exception {
        // Get the hDescriptor
        restHDescriptorMockMvc.perform(get("/api/h-descriptors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHDescriptor() throws Exception {
        // Initialize the database
        hDescriptorRepository.saveAndFlush(hDescriptor);
        int databaseSizeBeforeUpdate = hDescriptorRepository.findAll().size();

        // Update the hDescriptor
        HDescriptor updatedHDescriptor = hDescriptorRepository.findOne(hDescriptor.getId());
        updatedHDescriptor
                .type(UPDATED_TYPE)
                .decscriptorBody(UPDATED_DECSCRIPTOR_BODY);

        restHDescriptorMockMvc.perform(put("/api/h-descriptors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHDescriptor)))
            .andExpect(status().isOk());

        // Validate the HDescriptor in the database
        List<HDescriptor> hDescriptorList = hDescriptorRepository.findAll();
        assertThat(hDescriptorList).hasSize(databaseSizeBeforeUpdate);
        HDescriptor testHDescriptor = hDescriptorList.get(hDescriptorList.size() - 1);
        assertThat(testHDescriptor.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHDescriptor.getDecscriptorBody()).isEqualTo(UPDATED_DECSCRIPTOR_BODY);
    }

    @Test
    @Transactional
    public void updateNonExistingHDescriptor() throws Exception {
        int databaseSizeBeforeUpdate = hDescriptorRepository.findAll().size();

        // Create the HDescriptor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHDescriptorMockMvc.perform(put("/api/h-descriptors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hDescriptor)))
            .andExpect(status().isCreated());

        // Validate the HDescriptor in the database
        List<HDescriptor> hDescriptorList = hDescriptorRepository.findAll();
        assertThat(hDescriptorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHDescriptor() throws Exception {
        // Initialize the database
        hDescriptorRepository.saveAndFlush(hDescriptor);
        int databaseSizeBeforeDelete = hDescriptorRepository.findAll().size();

        // Get the hDescriptor
        restHDescriptorMockMvc.perform(delete("/api/h-descriptors/{id}", hDescriptor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HDescriptor> hDescriptorList = hDescriptorRepository.findAll();
        assertThat(hDescriptorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
