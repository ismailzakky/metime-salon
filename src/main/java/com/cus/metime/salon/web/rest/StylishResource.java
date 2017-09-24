package com.cus.metime.salon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.metime.salon.domain.Stylish;
import com.cus.metime.salon.service.StylishService;
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
 * REST controller for managing Stylish.
 */
@RestController
@RequestMapping("/api")
public class StylishResource {

    private final Logger log = LoggerFactory.getLogger(StylishResource.class);

    private static final String ENTITY_NAME = "stylish";

    private final StylishService stylishService;

    public StylishResource(StylishService stylishService) {
        this.stylishService = stylishService;
    }

    /**
     * POST  /stylishes : Create a new stylish.
     *
     * @param stylish the stylish to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stylish, or with status 400 (Bad Request) if the stylish has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stylishes")
    @Timed
    public ResponseEntity<Stylish> createStylish(@RequestBody Stylish stylish) throws URISyntaxException {
        log.debug("REST request to save Stylish : {}", stylish);
        if (stylish.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stylish cannot already have an ID")).body(null);
        }
        Stylish result = stylishService.save(stylish);
        return ResponseEntity.created(new URI("/api/stylishes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stylishes : Updates an existing stylish.
     *
     * @param stylish the stylish to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stylish,
     * or with status 400 (Bad Request) if the stylish is not valid,
     * or with status 500 (Internal Server Error) if the stylish couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stylishes")
    @Timed
    public ResponseEntity<Stylish> updateStylish(@RequestBody Stylish stylish) throws URISyntaxException {
        log.debug("REST request to update Stylish : {}", stylish);
        if (stylish.getId() == null) {
            return createStylish(stylish);
        }
        Stylish result = stylishService.save(stylish);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stylish.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stylishes : get all the stylishes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stylishes in body
     */
    @GetMapping("/stylishes")
    @Timed
    public ResponseEntity<List<Stylish>> getAllStylishes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Stylishes");
        Page<Stylish> page = stylishService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stylishes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stylishes/:id : get the "id" stylish.
     *
     * @param id the id of the stylish to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stylish, or with status 404 (Not Found)
     */
    @GetMapping("/stylishes/{id}")
    @Timed
    public ResponseEntity<Stylish> getStylish(@PathVariable Long id) {
        log.debug("REST request to get Stylish : {}", id);
        Stylish stylish = stylishService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stylish));
    }

    /**
     * DELETE  /stylishes/:id : delete the "id" stylish.
     *
     * @param id the id of the stylish to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stylishes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStylish(@PathVariable Long id) {
        log.debug("REST request to delete Stylish : {}", id);
        stylishService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
