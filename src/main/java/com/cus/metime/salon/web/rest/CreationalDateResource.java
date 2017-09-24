package com.cus.metime.salon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.metime.salon.domain.CreationalDate;
import com.cus.metime.salon.service.CreationalDateService;
import com.cus.metime.salon.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CreationalDate.
 */
@RestController
@RequestMapping("/api")
public class CreationalDateResource {

    private final Logger log = LoggerFactory.getLogger(CreationalDateResource.class);

    private static final String ENTITY_NAME = "creationalDate";

    private final CreationalDateService creationalDateService;

    public CreationalDateResource(CreationalDateService creationalDateService) {
        this.creationalDateService = creationalDateService;
    }

    /**
     * POST  /creational-dates : Create a new creationalDate.
     *
     * @param creationalDate the creationalDate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new creationalDate, or with status 400 (Bad Request) if the creationalDate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/creational-dates")
    @Timed
    public ResponseEntity<CreationalDate> createCreationalDate(@RequestBody CreationalDate creationalDate) throws URISyntaxException {
        log.debug("REST request to save CreationalDate : {}", creationalDate);
        if (creationalDate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new creationalDate cannot already have an ID")).body(null);
        }
        CreationalDate result = creationalDateService.save(creationalDate);
        return ResponseEntity.created(new URI("/api/creational-dates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /creational-dates : Updates an existing creationalDate.
     *
     * @param creationalDate the creationalDate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated creationalDate,
     * or with status 400 (Bad Request) if the creationalDate is not valid,
     * or with status 500 (Internal Server Error) if the creationalDate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/creational-dates")
    @Timed
    public ResponseEntity<CreationalDate> updateCreationalDate(@RequestBody CreationalDate creationalDate) throws URISyntaxException {
        log.debug("REST request to update CreationalDate : {}", creationalDate);
        if (creationalDate.getId() == null) {
            return createCreationalDate(creationalDate);
        }
        CreationalDate result = creationalDateService.save(creationalDate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, creationalDate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /creational-dates : get all the creationalDates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of creationalDates in body
     */
    @GetMapping("/creational-dates")
    @Timed
    public List<CreationalDate> getAllCreationalDates() {
        log.debug("REST request to get all CreationalDates");
        return creationalDateService.findAll();
        }

    /**
     * GET  /creational-dates/:id : get the "id" creationalDate.
     *
     * @param id the id of the creationalDate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the creationalDate, or with status 404 (Not Found)
     */
    @GetMapping("/creational-dates/{id}")
    @Timed
    public ResponseEntity<CreationalDate> getCreationalDate(@PathVariable Long id) {
        log.debug("REST request to get CreationalDate : {}", id);
        CreationalDate creationalDate = creationalDateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(creationalDate));
    }

    /**
     * DELETE  /creational-dates/:id : delete the "id" creationalDate.
     *
     * @param id the id of the creationalDate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/creational-dates/{id}")
    @Timed
    public ResponseEntity<Void> deleteCreationalDate(@PathVariable Long id) {
        log.debug("REST request to delete CreationalDate : {}", id);
        creationalDateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
