package com.cus.metime.salon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.metime.salon.domain.PlaceInformation;
import com.cus.metime.salon.service.PlaceInformationService;
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
 * REST controller for managing PlaceInformation.
 */
@RestController
@RequestMapping("/api")
public class PlaceInformationResource {

    private final Logger log = LoggerFactory.getLogger(PlaceInformationResource.class);

    private static final String ENTITY_NAME = "placeInformation";

    private final PlaceInformationService placeInformationService;

    public PlaceInformationResource(PlaceInformationService placeInformationService) {
        this.placeInformationService = placeInformationService;
    }

    /**
     * POST  /place-informations : Create a new placeInformation.
     *
     * @param placeInformation the placeInformation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new placeInformation, or with status 400 (Bad Request) if the placeInformation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/place-informations")
    @Timed
    public ResponseEntity<PlaceInformation> createPlaceInformation(@RequestBody PlaceInformation placeInformation) throws URISyntaxException {
        log.debug("REST request to save PlaceInformation : {}", placeInformation);
        if (placeInformation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new placeInformation cannot already have an ID")).body(null);
        }
        PlaceInformation result = placeInformationService.save(placeInformation);
        return ResponseEntity.created(new URI("/api/place-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /place-informations : Updates an existing placeInformation.
     *
     * @param placeInformation the placeInformation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated placeInformation,
     * or with status 400 (Bad Request) if the placeInformation is not valid,
     * or with status 500 (Internal Server Error) if the placeInformation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/place-informations")
    @Timed
    public ResponseEntity<PlaceInformation> updatePlaceInformation(@RequestBody PlaceInformation placeInformation) throws URISyntaxException {
        log.debug("REST request to update PlaceInformation : {}", placeInformation);
        if (placeInformation.getId() == null) {
            return createPlaceInformation(placeInformation);
        }
        PlaceInformation result = placeInformationService.save(placeInformation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, placeInformation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /place-informations : get all the placeInformations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of placeInformations in body
     */
    @GetMapping("/place-informations")
    @Timed
    public List<PlaceInformation> getAllPlaceInformations() {
        log.debug("REST request to get all PlaceInformations");
        return placeInformationService.findAll();
        }

    /**
     * GET  /place-informations/:id : get the "id" placeInformation.
     *
     * @param id the id of the placeInformation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the placeInformation, or with status 404 (Not Found)
     */
    @GetMapping("/place-informations/{id}")
    @Timed
    public ResponseEntity<PlaceInformation> getPlaceInformation(@PathVariable Long id) {
        log.debug("REST request to get PlaceInformation : {}", id);
        PlaceInformation placeInformation = placeInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placeInformation));
    }

    /**
     * DELETE  /place-informations/:id : delete the "id" placeInformation.
     *
     * @param id the id of the placeInformation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/place-informations/{id}")
    @Timed
    public ResponseEntity<Void> deletePlaceInformation(@PathVariable Long id) {
        log.debug("REST request to delete PlaceInformation : {}", id);
        placeInformationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
