package com.ra_bbvet.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ra_bbvet.app.domain.Suboutput;
import com.ra_bbvet.app.repository.SuboutputRepository;
import com.ra_bbvet.app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Suboutput.
 */
@RestController
@RequestMapping("/api")
public class SuboutputResource {

    private final Logger log = LoggerFactory.getLogger(SuboutputResource.class);
        
    @Inject
    private SuboutputRepository suboutputRepository;
    
    /**
     * POST  /suboutputs : Create a new suboutput.
     *
     * @param suboutput the suboutput to create
     * @return the ResponseEntity with status 201 (Created) and with body the new suboutput, or with status 400 (Bad Request) if the suboutput has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/suboutputs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Suboutput> createSuboutput(@RequestBody Suboutput suboutput) throws URISyntaxException {
        log.debug("REST request to save Suboutput : {}", suboutput);
        if (suboutput.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("suboutput", "idexists", "A new suboutput cannot already have an ID")).body(null);
        }
        Suboutput result = suboutputRepository.save(suboutput);
        return ResponseEntity.created(new URI("/api/suboutputs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("suboutput", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /suboutputs : Updates an existing suboutput.
     *
     * @param suboutput the suboutput to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated suboutput,
     * or with status 400 (Bad Request) if the suboutput is not valid,
     * or with status 500 (Internal Server Error) if the suboutput couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/suboutputs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Suboutput> updateSuboutput(@RequestBody Suboutput suboutput) throws URISyntaxException {
        log.debug("REST request to update Suboutput : {}", suboutput);
        if (suboutput.getId() == null) {
            return createSuboutput(suboutput);
        }
        Suboutput result = suboutputRepository.save(suboutput);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("suboutput", suboutput.getId().toString()))
            .body(result);
    }

    /**
     * GET  /suboutputs : get all the suboutputs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of suboutputs in body
     */
    @RequestMapping(value = "/suboutputs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Suboutput> getAllSuboutputs() {
        log.debug("REST request to get all Suboutputs");
        List<Suboutput> suboutputs = suboutputRepository.findAll();
        return suboutputs;
    }

    /**
     * GET  /suboutputs/:id : get the "id" suboutput.
     *
     * @param id the id of the suboutput to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the suboutput, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/suboutputs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Suboutput> getSuboutput(@PathVariable Long id) {
        log.debug("REST request to get Suboutput : {}", id);
        Suboutput suboutput = suboutputRepository.findOne(id);
        return Optional.ofNullable(suboutput)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /suboutputs/:id : delete the "id" suboutput.
     *
     * @param id the id of the suboutput to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/suboutputs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSuboutput(@PathVariable Long id) {
        log.debug("REST request to delete Suboutput : {}", id);
        suboutputRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("suboutput", id.toString())).build();
    }

}
