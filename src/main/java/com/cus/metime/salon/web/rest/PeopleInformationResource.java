package com.cus.metime.salon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.metime.salon.domain.PeopleInformation;
import com.cus.metime.salon.service.PeopleInformationService;
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
 * REST controller for managing PeopleInformation.
 */
@RestController
@RequestMapping("/api")
public class PeopleInformationResource {

    private final Logger log = LoggerFactory.getLogger(PeopleInformationResource.class);

    private static final String ENTITY_NAME = "peopleInformation";

    private final PeopleInformationService peopleInformationService;

    public PeopleInformationResource(PeopleInformationService peopleInformationService) {
        this.peopleInformationService = peopleInformationService;
    }

    /**
     * POST  /people-informations : Create a new peopleInformation.
     *
     * @param peopleInformation the peopleInformation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new peopleInformation, or with status 400 (Bad Request) if the peopleInformation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/people-informations")
    @Timed
    public ResponseEntity<PeopleInformation> createPeopleInformation(@RequestBody PeopleInformation peopleInformation) throws URISyntaxException {
        log.debug("REST request to save PeopleInformation : {}", peopleInformation);
        if (peopleInformation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new peopleInformation cannot already have an ID")).body(null);
        }
        PeopleInformation result = peopleInformationService.save(peopleInformation);
        return ResponseEntity.created(new URI("/api/people-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /people-informations : Updates an existing peopleInformation.
     *
     * @param peopleInformation the peopleInformation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated peopleInformation,
     * or with status 400 (Bad Request) if the peopleInformation is not valid,
     * or with status 500 (Internal Server Error) if the peopleInformation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/people-informations")
    @Timed
    public ResponseEntity<PeopleInformation> updatePeopleInformation(@RequestBody PeopleInformation peopleInformation) throws URISyntaxException {
        log.debug("REST request to update PeopleInformation : {}", peopleInformation);
        if (peopleInformation.getId() == null) {
            return createPeopleInformation(peopleInformation);
        }
        PeopleInformation result = peopleInformationService.save(peopleInformation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, peopleInformation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /people-informations : get all the peopleInformations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of peopleInformations in body
     */
    @GetMapping("/people-informations")
    @Timed
    public List<PeopleInformation> getAllPeopleInformations() {
        log.debug("REST request to get all PeopleInformations");
        return peopleInformationService.findAll();
        }

    /**
     * GET  /people-informations/:id : get the "id" peopleInformation.
     *
     * @param id the id of the peopleInformation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the peopleInformation, or with status 404 (Not Found)
     */
    @GetMapping("/people-informations/{id}")
    @Timed
    public ResponseEntity<PeopleInformation> getPeopleInformation(@PathVariable Long id) {
        log.debug("REST request to get PeopleInformation : {}", id);
        PeopleInformation peopleInformation = peopleInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(peopleInformation));
    }

    /**
     * DELETE  /people-informations/:id : delete the "id" peopleInformation.
     *
     * @param id the id of the peopleInformation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/people-informations/{id}")
    @Timed
    public ResponseEntity<Void> deletePeopleInformation(@PathVariable Long id) {
        log.debug("REST request to delete PeopleInformation : {}", id);
        peopleInformationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
