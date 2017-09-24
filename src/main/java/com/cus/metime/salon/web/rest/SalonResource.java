package com.cus.metime.salon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.metime.salon.domain.Salon;
import com.cus.metime.salon.service.SalonService;
import com.cus.metime.salon.web.rest.util.HeaderUtil;
import com.cus.metime.salon.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Salon.
 */
@RestController
@RequestMapping("/api")
public class SalonResource {

    private final Logger log = LoggerFactory.getLogger(SalonResource.class);

    private static final String ENTITY_NAME = "salon";

    private final SalonService salonService;

    public SalonResource(SalonService salonService) {
        this.salonService = salonService;
    }

    /**
     * POST  /salons : Create a new salon.
     *
     * @param salon the salon to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salon, or with status 400 (Bad Request) if the salon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/salons")
    @Timed
    public ResponseEntity<Salon> createSalon(@RequestBody Salon salon) throws URISyntaxException {
        log.debug("REST request to save Salon : {}", salon);
        if (salon.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new salon cannot already have an ID")).body(null);
        }
        Salon result = salonService.save(salon);
        return ResponseEntity.created(new URI("/api/salons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /salons : Updates an existing salon.
     *
     * @param salon the salon to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salon,
     * or with status 400 (Bad Request) if the salon is not valid,
     * or with status 500 (Internal Server Error) if the salon couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/salons")
    @Timed
    public ResponseEntity<Salon> updateSalon(@RequestBody Salon salon) throws URISyntaxException {
        log.debug("REST request to update Salon : {}", salon);
        if (salon.getId() == null) {
            return createSalon(salon);
        }
        Salon result = salonService.save(salon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salon.getId().toString()))
            .body(result);
    }

    /**
     * GET  /salons : get all the salons.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of salons in body
     */
    @GetMapping("/salons")
    @Timed
    public ResponseEntity<List<Salon>> getAllSalons(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Salons");
        Page<Salon> page = salonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/salons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /salons/:id : get the "id" salon.
     *
     * @param id the id of the salon to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salon, or with status 404 (Not Found)
     */
    @GetMapping("/salons/{id}")
    @Timed
    public ResponseEntity<Salon> getSalon(@PathVariable Long id) {
        log.debug("REST request to get Salon : {}", id);
        Salon salon = salonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salon));
    }

    /**
     * DELETE  /salons/:id : delete the "id" salon.
     *
     * @param id the id of the salon to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/salons/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalon(@PathVariable Long id) {
        log.debug("REST request to delete Salon : {}", id);
        salonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
