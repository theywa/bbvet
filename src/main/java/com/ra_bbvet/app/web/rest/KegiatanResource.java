package com.ra_bbvet.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ra_bbvet.app.domain.Kegiatan;
import com.ra_bbvet.app.repository.KegiatanRepository;
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
 * REST controller for managing Kegiatan.
 */
@RestController
@RequestMapping("/api")
public class KegiatanResource {

    private final Logger log = LoggerFactory.getLogger(KegiatanResource.class);
        
    @Inject
    private KegiatanRepository kegiatanRepository;
    
    /**
     * POST  /kegiatans : Create a new kegiatan.
     *
     * @param kegiatan the kegiatan to create
     * @return the ResponseEntity with status 201 (Created) and with body the new kegiatan, or with status 400 (Bad Request) if the kegiatan has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/kegiatans",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Kegiatan> createKegiatan(@RequestBody Kegiatan kegiatan) throws URISyntaxException {
        log.debug("REST request to save Kegiatan : {}", kegiatan);
        if (kegiatan.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("kegiatan", "idexists", "A new kegiatan cannot already have an ID")).body(null);
        }
        Kegiatan result = kegiatanRepository.save(kegiatan);
        return ResponseEntity.created(new URI("/api/kegiatans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("kegiatan", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /kegiatans : Updates an existing kegiatan.
     *
     * @param kegiatan the kegiatan to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated kegiatan,
     * or with status 400 (Bad Request) if the kegiatan is not valid,
     * or with status 500 (Internal Server Error) if the kegiatan couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/kegiatans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Kegiatan> updateKegiatan(@RequestBody Kegiatan kegiatan) throws URISyntaxException {
        log.debug("REST request to update Kegiatan : {}", kegiatan);
        if (kegiatan.getId() == null) {
            return createKegiatan(kegiatan);
        }
        Kegiatan result = kegiatanRepository.save(kegiatan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("kegiatan", kegiatan.getId().toString()))
            .body(result);
    }

    /**
     * GET  /kegiatans : get all the kegiatans.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of kegiatans in body
     */
    @RequestMapping(value = "/kegiatans",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Kegiatan> getAllKegiatans() {
        log.debug("REST request to get all Kegiatans");
        List<Kegiatan> kegiatans = kegiatanRepository.findAll();
        return kegiatans;
    }

    /**
     * GET  /kegiatans/:id : get the "id" kegiatan.
     *
     * @param id the id of the kegiatan to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the kegiatan, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/kegiatans/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Kegiatan> getKegiatan(@PathVariable Long id) {
        log.debug("REST request to get Kegiatan : {}", id);
        Kegiatan kegiatan = kegiatanRepository.findOne(id);
        return Optional.ofNullable(kegiatan)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /kegiatans/:id : delete the "id" kegiatan.
     *
     * @param id the id of the kegiatan to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/kegiatans/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteKegiatan(@PathVariable Long id) {
        log.debug("REST request to delete Kegiatan : {}", id);
        kegiatanRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("kegiatan", id.toString())).build();
    }

}
