package com.evolutionaryenterprisesllc.map.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evolutionaryenterprisesllc.map.domain.HRel;

import com.evolutionaryenterprisesllc.map.repository.HRelRepository;
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
 * REST controller for managing HRel.
 */
@RestController
@RequestMapping("/api")
public class HRelResource {

    private final Logger log = LoggerFactory.getLogger(HRelResource.class);
        
    @Inject
    private HRelRepository hRelRepository;

    /**
     * POST  /h-rels : Create a new hRel.
     *
     * @param hRel the hRel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hRel, or with status 400 (Bad Request) if the hRel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/h-rels")
    @Timed
    public ResponseEntity<HRel> createHRel(@RequestBody HRel hRel) throws URISyntaxException {
        log.debug("REST request to save HRel : {}", hRel);
        if (hRel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hRel", "idexists", "A new hRel cannot already have an ID")).body(null);
        }
        HRel result = hRelRepository.save(hRel);
        return ResponseEntity.created(new URI("/api/h-rels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hRel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /h-rels : Updates an existing hRel.
     *
     * @param hRel the hRel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hRel,
     * or with status 400 (Bad Request) if the hRel is not valid,
     * or with status 500 (Internal Server Error) if the hRel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/h-rels")
    @Timed
    public ResponseEntity<HRel> updateHRel(@RequestBody HRel hRel) throws URISyntaxException {
        log.debug("REST request to update HRel : {}", hRel);
        if (hRel.getId() == null) {
            return createHRel(hRel);
        }
        HRel result = hRelRepository.save(hRel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hRel", hRel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /h-rels : get all the hRels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hRels in body
     */
    @GetMapping("/h-rels")
    @Timed
    public List<HRel> getAllHRels() {
        log.debug("REST request to get all HRels");
        List<HRel> hRels = hRelRepository.findAll();
        return hRels;
    }

    /**
     * GET  /h-rels/:id : get the "id" hRel.
     *
     * @param id the id of the hRel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hRel, or with status 404 (Not Found)
     */
    @GetMapping("/h-rels/{id}")
    @Timed
    public ResponseEntity<HRel> getHRel(@PathVariable Long id) {
        log.debug("REST request to get HRel : {}", id);
        HRel hRel = hRelRepository.findOne(id);
        return Optional.ofNullable(hRel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /h-rels/:id : delete the "id" hRel.
     *
     * @param id the id of the hRel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/h-rels/{id}")
    @Timed
    public ResponseEntity<Void> deleteHRel(@PathVariable Long id) {
        log.debug("REST request to delete HRel : {}", id);
        hRelRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hRel", id.toString())).build();
    }

}
