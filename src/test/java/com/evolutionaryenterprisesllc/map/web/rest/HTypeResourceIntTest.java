package com.evolutionaryenterprisesllc.map.web.rest;

import com.evolutionaryenterprisesllc.map.MapApp;

import com.evolutionaryenterprisesllc.map.domain.HType;
import com.evolutionaryenterprisesllc.map.repository.HTypeRepository;

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
 * Test class for the HTypeResource REST controller.
 *
 * @see HTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MapApp.class)
public class HTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private HTypeRepository hTypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHTypeMockMvc;

    private HType hType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HTypeResource hTypeResource = new HTypeResource();
        ReflectionTestUtils.setField(hTypeResource, "hTypeRepository", hTypeRepository);
        this.restHTypeMockMvc = MockMvcBuilders.standaloneSetup(hTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HType createEntity(EntityManager em) {
        HType hType = new HType()
                .name(DEFAULT_NAME);
        return hType;
    }

    @Before
    public void initTest() {
        hType = createEntity(em);
    }

    @Test
    @Transactional
    public void createHType() throws Exception {
        int databaseSizeBeforeCreate = hTypeRepository.findAll().size();

        // Create the HType

        restHTypeMockMvc.perform(post("/api/h-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hType)))
            .andExpect(status().isCreated());

        // Validate the HType in the database
        List<HType> hTypeList = hTypeRepository.findAll();
        assertThat(hTypeList).hasSize(databaseSizeBeforeCreate + 1);
        HType testHType = hTypeList.get(hTypeList.size() - 1);
        assertThat(testHType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createHTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hTypeRepository.findAll().size();

        // Create the HType with an existing ID
        HType existingHType = new HType();
        existingHType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHTypeMockMvc.perform(post("/api/h-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingHType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HType> hTypeList = hTypeRepository.findAll();
        assertThat(hTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHTypes() throws Exception {
        // Initialize the database
        hTypeRepository.saveAndFlush(hType);

        // Get all the hTypeList
        restHTypeMockMvc.perform(get("/api/h-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getHType() throws Exception {
        // Initialize the database
        hTypeRepository.saveAndFlush(hType);

        // Get the hType
        restHTypeMockMvc.perform(get("/api/h-types/{id}", hType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHType() throws Exception {
        // Get the hType
        restHTypeMockMvc.perform(get("/api/h-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHType() throws Exception {
        // Initialize the database
        hTypeRepository.saveAndFlush(hType);
        int databaseSizeBeforeUpdate = hTypeRepository.findAll().size();

        // Update the hType
        HType updatedHType = hTypeRepository.findOne(hType.getId());
        updatedHType
                .name(UPDATED_NAME);

        restHTypeMockMvc.perform(put("/api/h-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHType)))
            .andExpect(status().isOk());

        // Validate the HType in the database
        List<HType> hTypeList = hTypeRepository.findAll();
        assertThat(hTypeList).hasSize(databaseSizeBeforeUpdate);
        HType testHType = hTypeList.get(hTypeList.size() - 1);
        assertThat(testHType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingHType() throws Exception {
        int databaseSizeBeforeUpdate = hTypeRepository.findAll().size();

        // Create the HType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHTypeMockMvc.perform(put("/api/h-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hType)))
            .andExpect(status().isCreated());

        // Validate the HType in the database
        List<HType> hTypeList = hTypeRepository.findAll();
        assertThat(hTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHType() throws Exception {
        // Initialize the database
        hTypeRepository.saveAndFlush(hType);
        int databaseSizeBeforeDelete = hTypeRepository.findAll().size();

        // Get the hType
        restHTypeMockMvc.perform(delete("/api/h-types/{id}", hType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HType> hTypeList = hTypeRepository.findAll();
        assertThat(hTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
