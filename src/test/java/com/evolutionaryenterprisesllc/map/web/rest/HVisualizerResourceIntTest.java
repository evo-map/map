package com.evolutionaryenterprisesllc.map.web.rest;

import com.evolutionaryenterprisesllc.map.MapApp;

import com.evolutionaryenterprisesllc.map.domain.HVisualizer;
import com.evolutionaryenterprisesllc.map.repository.HVisualizerRepository;

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

/**
 * Test class for the HVisualizerResource REST controller.
 *
 * @see HVisualizerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MapApp.class)
public class HVisualizerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private HVisualizerRepository hVisualizerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHVisualizerMockMvc;

    private HVisualizer hVisualizer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HVisualizerResource hVisualizerResource = new HVisualizerResource();
        ReflectionTestUtils.setField(hVisualizerResource, "hVisualizerRepository", hVisualizerRepository);
        this.restHVisualizerMockMvc = MockMvcBuilders.standaloneSetup(hVisualizerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HVisualizer createEntity(EntityManager em) {
        HVisualizer hVisualizer = new HVisualizer()
                .name(DEFAULT_NAME);
        return hVisualizer;
    }

    @Before
    public void initTest() {
        hVisualizer = createEntity(em);
    }

    @Test
    @Transactional
    public void createHVisualizer() throws Exception {
        int databaseSizeBeforeCreate = hVisualizerRepository.findAll().size();

        // Create the HVisualizer

        restHVisualizerMockMvc.perform(post("/api/h-visualizers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hVisualizer)))
            .andExpect(status().isCreated());

        // Validate the HVisualizer in the database
        List<HVisualizer> hVisualizerList = hVisualizerRepository.findAll();
        assertThat(hVisualizerList).hasSize(databaseSizeBeforeCreate + 1);
        HVisualizer testHVisualizer = hVisualizerList.get(hVisualizerList.size() - 1);
        assertThat(testHVisualizer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createHVisualizerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hVisualizerRepository.findAll().size();

        // Create the HVisualizer with an existing ID
        HVisualizer existingHVisualizer = new HVisualizer();
        existingHVisualizer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHVisualizerMockMvc.perform(post("/api/h-visualizers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingHVisualizer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HVisualizer> hVisualizerList = hVisualizerRepository.findAll();
        assertThat(hVisualizerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHVisualizers() throws Exception {
        // Initialize the database
        hVisualizerRepository.saveAndFlush(hVisualizer);

        // Get all the hVisualizerList
        restHVisualizerMockMvc.perform(get("/api/h-visualizers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hVisualizer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getHVisualizer() throws Exception {
        // Initialize the database
        hVisualizerRepository.saveAndFlush(hVisualizer);

        // Get the hVisualizer
        restHVisualizerMockMvc.perform(get("/api/h-visualizers/{id}", hVisualizer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hVisualizer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHVisualizer() throws Exception {
        // Get the hVisualizer
        restHVisualizerMockMvc.perform(get("/api/h-visualizers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHVisualizer() throws Exception {
        // Initialize the database
        hVisualizerRepository.saveAndFlush(hVisualizer);
        int databaseSizeBeforeUpdate = hVisualizerRepository.findAll().size();

        // Update the hVisualizer
        HVisualizer updatedHVisualizer = hVisualizerRepository.findOne(hVisualizer.getId());
        updatedHVisualizer
                .name(UPDATED_NAME);

        restHVisualizerMockMvc.perform(put("/api/h-visualizers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHVisualizer)))
            .andExpect(status().isOk());

        // Validate the HVisualizer in the database
        List<HVisualizer> hVisualizerList = hVisualizerRepository.findAll();
        assertThat(hVisualizerList).hasSize(databaseSizeBeforeUpdate);
        HVisualizer testHVisualizer = hVisualizerList.get(hVisualizerList.size() - 1);
        assertThat(testHVisualizer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingHVisualizer() throws Exception {
        int databaseSizeBeforeUpdate = hVisualizerRepository.findAll().size();

        // Create the HVisualizer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHVisualizerMockMvc.perform(put("/api/h-visualizers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hVisualizer)))
            .andExpect(status().isCreated());

        // Validate the HVisualizer in the database
        List<HVisualizer> hVisualizerList = hVisualizerRepository.findAll();
        assertThat(hVisualizerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHVisualizer() throws Exception {
        // Initialize the database
        hVisualizerRepository.saveAndFlush(hVisualizer);
        int databaseSizeBeforeDelete = hVisualizerRepository.findAll().size();

        // Get the hVisualizer
        restHVisualizerMockMvc.perform(delete("/api/h-visualizers/{id}", hVisualizer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HVisualizer> hVisualizerList = hVisualizerRepository.findAll();
        assertThat(hVisualizerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
