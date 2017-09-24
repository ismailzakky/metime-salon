package com.cus.metime.salon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.metime.salon.domain.BusinessInformation;
import com.cus.metime.salon.service.BusinessInformationService;
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
 * REST controller for managing BusinessInformation.
 */
@RestController
@RequestMapping("/api")
public class BusinessInformationResource {

    private final Logger log = LoggerFactory.getLogger(BusinessInformationResource.class);

    private static final String ENTITY_NAME = "businessInformation";

    private final BusinessInformationService businessInformationService;

    public BusinessInformationResource(BusinessInformationService businessInformationService) {
        this.businessInformationService = businessInformationService;
    }

    /**
     * POST  /business-informations : Create a new businessInformation.
     *
     * @param businessInformation the businessInformation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new businessInformation, or with status 400 (Bad Request) if the businessInformation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/business-informations")
    @Timed
    public ResponseEntity<BusinessInformation> createBusinessInformation(@RequestBody BusinessInformation businessInformation) throws URISyntaxException {
        log.debug("REST request to save BusinessInformation : {}", businessInformation);
        if (businessInformation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new businessInformation cannot already have an ID")).body(null);
        }
        BusinessInformation result = businessInformationService.save(businessInformation);
        return ResponseEntity.created(new URI("/api/business-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /business-informations : Updates an existing businessInformation.
     *
     * @param businessInformation the businessInformation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated businessInformation,
     * or with status 400 (Bad Request) if the businessInformation is not valid,
     * or with status 500 (Internal Server Error) if the businessInformation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/business-informations")
    @Timed
    public ResponseEntity<BusinessInformation> updateBusinessInformation(@RequestBody BusinessInformation businessInformation) throws URISyntaxException {
        log.debug("REST request to update BusinessInformation : {}", businessInformation);
        if (businessInformation.getId() == null) {
            return createBusinessInformation(businessInformation);
        }
        BusinessInformation result = businessInformationService.save(businessInformation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, businessInformation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /business-informations : get all the businessInformations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of businessInformations in body
     */
    @GetMapping("/business-informations")
    @Timed
    public List<BusinessInformation> getAllBusinessInformations() {
        log.debug("REST request to get all BusinessInformations");
        return businessInformationService.findAll();
        }

    /**
     * GET  /business-informations/:id : get the "id" businessInformation.
     *
     * @param id the id of the businessInformation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the businessInformation, or with status 404 (Not Found)
     */
    @GetMapping("/business-informations/{id}")
    @Timed
    public ResponseEntity<BusinessInformation> getBusinessInformation(@PathVariable Long id) {
        log.debug("REST request to get BusinessInformation : {}", id);
        BusinessInformation businessInformation = businessInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(businessInformation));
    }

    /**
     * DELETE  /business-informations/:id : delete the "id" businessInformation.
     *
     * @param id the id of the businessInformation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/business-informations/{id}")
    @Timed
    public ResponseEntity<Void> deleteBusinessInformation(@PathVariable Long id) {
        log.debug("REST request to delete BusinessInformation : {}", id);
        businessInformationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
