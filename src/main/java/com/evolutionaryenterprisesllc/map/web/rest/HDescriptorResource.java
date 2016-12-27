package com.evolutionaryenterprisesllc.map.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evolutionaryenterprisesllc.map.domain.HDescriptor;

import com.evolutionaryenterprisesllc.map.repository.HDescriptorRepository;
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
 * REST controller for managing HDescriptor.
 */
@RestController
@RequestMapping("/api")
public class HDescriptorResource {

    private final Logger log = LoggerFactory.getLogger(HDescriptorResource.class);
        
    @Inject
    private HDescriptorRepository hDescriptorRepository;

    /**
     * POST  /h-descriptors : Create a new hDescriptor.
     *
     * @param hDescriptor the hDescriptor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hDescriptor, or with status 400 (Bad Request) if the hDescriptor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/h-descriptors")
    @Timed
    public ResponseEntity<HDescriptor> createHDescriptor(@RequestBody HDescriptor hDescriptor) throws URISyntaxException {
        log.debug("REST request to save HDescriptor : {}", hDescriptor);
        if (hDescriptor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hDescriptor", "idexists", "A new hDescriptor cannot already have an ID")).body(null);
        }
        HDescriptor result = hDescriptorRepository.save(hDescriptor);
        return ResponseEntity.created(new URI("/api/h-descriptors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hDescriptor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /h-descriptors : Updates an existing hDescriptor.
     *
     * @param hDescriptor the hDescriptor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hDescriptor,
     * or with status 400 (Bad Request) if the hDescriptor is not valid,
     * or with status 500 (Internal Server Error) if the hDescriptor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/h-descriptors")
    @Timed
    public ResponseEntity<HDescriptor> updateHDescriptor(@RequestBody HDescriptor hDescriptor) throws URISyntaxException {
        log.debug("REST request to update HDescriptor : {}", hDescriptor);
        if (hDescriptor.getId() == null) {
            return createHDescriptor(hDescriptor);
        }
        HDescriptor result = hDescriptorRepository.save(hDescriptor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hDescriptor", hDescriptor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /h-descriptors : get all the hDescriptors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hDescriptors in body
     */
    @GetMapping("/h-descriptors")
    @Timed
    public List<HDescriptor> getAllHDescriptors() {
        log.debug("REST request to get all HDescriptors");
        List<HDescriptor> hDescriptors = hDescriptorRepository.findAll();
        return hDescriptors;
    }

    /**
     * GET  /h-descriptors/:id : get the "id" hDescriptor.
     *
     * @param id the id of the hDescriptor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hDescriptor, or with status 404 (Not Found)
     */
    @GetMapping("/h-descriptors/{id}")
    @Timed
    public ResponseEntity<HDescriptor> getHDescriptor(@PathVariable Long id) {
        log.debug("REST request to get HDescriptor : {}", id);
        HDescriptor hDescriptor = hDescriptorRepository.findOne(id);
        return Optional.ofNullable(hDescriptor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /h-descriptors/:id : delete the "id" hDescriptor.
     *
     * @param id the id of the hDescriptor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/h-descriptors/{id}")
    @Timed
    public ResponseEntity<Void> deleteHDescriptor(@PathVariable Long id) {
        log.debug("REST request to delete HDescriptor : {}", id);
        hDescriptorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hDescriptor", id.toString())).build();
    }

}
