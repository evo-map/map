package com.evolutionaryenterprisesllc.map.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evolutionaryenterprisesllc.map.domain.HVisualizer;

import com.evolutionaryenterprisesllc.map.repository.HVisualizerRepository;
import com.evolutionaryenterprisesllc.map.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing HVisualizer.
 */
@RestController
@RequestMapping("/api")
public class HVisualizerResource {

    private final Logger log = LoggerFactory.getLogger(HVisualizerResource.class);
        
    @Inject
    private HVisualizerRepository hVisualizerRepository;

    /**
     * POST  /h-visualizers : Create a new hVisualizer.
     *
     * @param hVisualizer the hVisualizer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hVisualizer, or with status 400 (Bad Request) if the hVisualizer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/h-visualizers")
    @Timed
    public ResponseEntity<HVisualizer> createHVisualizer(@RequestBody HVisualizer hVisualizer) throws URISyntaxException {
        log.debug("REST request to save HVisualizer : {}", hVisualizer);
        if (hVisualizer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hVisualizer", "idexists", "A new hVisualizer cannot already have an ID")).body(null);
        }
        HVisualizer result = hVisualizerRepository.save(hVisualizer);
        return ResponseEntity.created(new URI("/api/h-visualizers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hVisualizer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /h-visualizers : Updates an existing hVisualizer.
     *
     * @param hVisualizer the hVisualizer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hVisualizer,
     * or with status 400 (Bad Request) if the hVisualizer is not valid,
     * or with status 500 (Internal Server Error) if the hVisualizer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/h-visualizers")
    @Timed
    public ResponseEntity<HVisualizer> updateHVisualizer(@RequestBody HVisualizer hVisualizer) throws URISyntaxException {
        log.debug("REST request to update HVisualizer : {}", hVisualizer);
        if (hVisualizer.getId() == null) {
            return createHVisualizer(hVisualizer);
        }
        HVisualizer result = hVisualizerRepository.save(hVisualizer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hVisualizer", hVisualizer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /h-visualizers : get all the hVisualizers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hVisualizers in body
     */
    @GetMapping("/h-visualizers")
    @Timed
    public List<HVisualizer> getAllHVisualizers() {
        log.debug("REST request to get all HVisualizers");
        List<HVisualizer> hVisualizers = hVisualizerRepository.findAll();
        return hVisualizers;
    }

    /**
     * GET  /h-visualizers/:id : get the "id" hVisualizer.
     *
     * @param id the id of the hVisualizer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hVisualizer, or with status 404 (Not Found)
     */
    @GetMapping("/h-visualizers/{id}")
    @Timed
    public ResponseEntity<HVisualizer> getHVisualizer(@PathVariable Long id) {
        log.debug("REST request to get HVisualizer : {}", id);
        HVisualizer hVisualizer = hVisualizerRepository.findOne(id);
        return Optional.ofNullable(hVisualizer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /h-visualizers/:id : delete the "id" hVisualizer.
     *
     * @param id the id of the hVisualizer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/h-visualizers/{id}")
    @Timed
    public ResponseEntity<Void> deleteHVisualizer(@PathVariable Long id) {
        log.debug("REST request to delete HVisualizer : {}", id);
        hVisualizerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hVisualizer", id.toString())).build();
    }

}
