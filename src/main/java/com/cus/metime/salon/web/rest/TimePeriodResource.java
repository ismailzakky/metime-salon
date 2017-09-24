package com.cus.metime.salon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.metime.salon.domain.TimePeriod;
import com.cus.metime.salon.service.TimePeriodService;
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
 * REST controller for managing TimePeriod.
 */
@RestController
@RequestMapping("/api")
public class TimePeriodResource {

    private final Logger log = LoggerFactory.getLogger(TimePeriodResource.class);

    private static final String ENTITY_NAME = "timePeriod";

    private final TimePeriodService timePeriodService;

    public TimePeriodResource(TimePeriodService timePeriodService) {
        this.timePeriodService = timePeriodService;
    }

    /**
     * POST  /time-periods : Create a new timePeriod.
     *
     * @param timePeriod the timePeriod to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timePeriod, or with status 400 (Bad Request) if the timePeriod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/time-periods")
    @Timed
    public ResponseEntity<TimePeriod> createTimePeriod(@RequestBody TimePeriod timePeriod) throws URISyntaxException {
        log.debug("REST request to save TimePeriod : {}", timePeriod);
        if (timePeriod.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new timePeriod cannot already have an ID")).body(null);
        }
        TimePeriod result = timePeriodService.save(timePeriod);
        return ResponseEntity.created(new URI("/api/time-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /time-periods : Updates an existing timePeriod.
     *
     * @param timePeriod the timePeriod to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timePeriod,
     * or with status 400 (Bad Request) if the timePeriod is not valid,
     * or with status 500 (Internal Server Error) if the timePeriod couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/time-periods")
    @Timed
    public ResponseEntity<TimePeriod> updateTimePeriod(@RequestBody TimePeriod timePeriod) throws URISyntaxException {
        log.debug("REST request to update TimePeriod : {}", timePeriod);
        if (timePeriod.getId() == null) {
            return createTimePeriod(timePeriod);
        }
        TimePeriod result = timePeriodService.save(timePeriod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timePeriod.getId().toString()))
            .body(result);
    }

    /**
     * GET  /time-periods : get all the timePeriods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of timePeriods in body
     */
    @GetMapping("/time-periods")
    @Timed
    public List<TimePeriod> getAllTimePeriods() {
        log.debug("REST request to get all TimePeriods");
        return timePeriodService.findAll();
        }

    /**
     * GET  /time-periods/:id : get the "id" timePeriod.
     *
     * @param id the id of the timePeriod to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timePeriod, or with status 404 (Not Found)
     */
    @GetMapping("/time-periods/{id}")
    @Timed
    public ResponseEntity<TimePeriod> getTimePeriod(@PathVariable Long id) {
        log.debug("REST request to get TimePeriod : {}", id);
        TimePeriod timePeriod = timePeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timePeriod));
    }

    /**
     * DELETE  /time-periods/:id : delete the "id" timePeriod.
     *
     * @param id the id of the timePeriod to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/time-periods/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimePeriod(@PathVariable Long id) {
        log.debug("REST request to delete TimePeriod : {}", id);
        timePeriodService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
