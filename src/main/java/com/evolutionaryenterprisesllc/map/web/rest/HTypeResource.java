package com.evolutionaryenterprisesllc.map.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evolutionaryenterprisesllc.map.domain.HType;

import com.evolutionaryenterprisesllc.map.repository.HTypeRepository;
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
 * REST controller for managing HType.
 */
@RestController
@RequestMapping("/api")
public class HTypeResource {

    private final Logger log = LoggerFactory.getLogger(HTypeResource.class);
        
    @Inject
    private HTypeRepository hTypeRepository;

    /**
     * POST  /h-types : Create a new hType.
     *
     * @param hType the hType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hType, or with status 400 (Bad Request) if the hType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/h-types")
    @Timed
    public ResponseEntity<HType> createHType(@RequestBody HType hType) throws URISyntaxException {
        log.debug("REST request to save HType : {}", hType);
        if (hType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hType", "idexists", "A new hType cannot already have an ID")).body(null);
        }
        HType result = hTypeRepository.save(hType);
        return ResponseEntity.created(new URI("/api/h-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /h-types : Updates an existing hType.
     *
     * @param hType the hType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hType,
     * or with status 400 (Bad Request) if the hType is not valid,
     * or with status 500 (Internal Server Error) if the hType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/h-types")
    @Timed
    public ResponseEntity<HType> updateHType(@RequestBody HType hType) throws URISyntaxException {
        log.debug("REST request to update HType : {}", hType);
        if (hType.getId() == null) {
            return createHType(hType);
        }
        HType result = hTypeRepository.save(hType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hType", hType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /h-types : get all the hTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hTypes in body
     */
    @GetMapping("/h-types")
    @Timed
    public List<HType> getAllHTypes() {
        log.debug("REST request to get all HTypes");
        List<HType> hTypes = hTypeRepository.findAll();
        return hTypes;
    }

    /**
     * GET  /h-types/:id : get the "id" hType.
     *
     * @param id the id of the hType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hType, or with status 404 (Not Found)
     */
    @GetMapping("/h-types/{id}")
    @Timed
    public ResponseEntity<HType> getHType(@PathVariable Long id) {
        log.debug("REST request to get HType : {}", id);
        HType hType = hTypeRepository.findOne(id);
        return Optional.ofNullable(hType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /h-types/:id : delete the "id" hType.
     *
     * @param id the id of the hType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/h-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteHType(@PathVariable Long id) {
        log.debug("REST request to delete HType : {}", id);
        hTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hType", id.toString())).build();
    }

}
