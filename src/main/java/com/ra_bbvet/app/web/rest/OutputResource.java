package com.ra_bbvet.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ra_bbvet.app.domain.Output;
import com.ra_bbvet.app.repository.OutputRepository;
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
 * REST controller for managing Output.
 */
@RestController
@RequestMapping("/api")
public class OutputResource {

    private final Logger log = LoggerFactory.getLogger(OutputResource.class);
        
    @Inject
    private OutputRepository outputRepository;
    
    /**
     * POST  /outputs : Create a new output.
     *
     * @param output the output to create
     * @return the ResponseEntity with status 201 (Created) and with body the new output, or with status 400 (Bad Request) if the output has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/outputs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Output> createOutput(@RequestBody Output output) throws URISyntaxException {
        log.debug("REST request to save Output : {}", output);
        if (output.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("output", "idexists", "A new output cannot already have an ID")).body(null);
        }
        Output result = outputRepository.save(output);
        return ResponseEntity.created(new URI("/api/outputs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("output", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /outputs : Updates an existing output.
     *
     * @param output the output to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated output,
     * or with status 400 (Bad Request) if the output is not valid,
     * or with status 500 (Internal Server Error) if the output couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/outputs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Output> updateOutput(@RequestBody Output output) throws URISyntaxException {
        log.debug("REST request to update Output : {}", output);
        if (output.getId() == null) {
            return createOutput(output);
        }
        Output result = outputRepository.save(output);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("output", output.getId().toString()))
            .body(result);
    }

    /**
     * GET  /outputs : get all the outputs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of outputs in body
     */
    @RequestMapping(value = "/outputs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Output> getAllOutputs() {
        log.debug("REST request to get all Outputs");
        List<Output> outputs = outputRepository.findAll();
        return outputs;
    }

    /**
     * GET  /outputs/:id : get the "id" output.
     *
     * @param id the id of the output to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the output, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/outputs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Output> getOutput(@PathVariable Long id) {
        log.debug("REST request to get Output : {}", id);
        Output output = outputRepository.findOne(id);
        return Optional.ofNullable(output)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /outputs/:id : delete the "id" output.
     *
     * @param id the id of the output to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/outputs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOutput(@PathVariable Long id) {
        log.debug("REST request to delete Output : {}", id);
        outputRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("output", id.toString())).build();
    }

}
