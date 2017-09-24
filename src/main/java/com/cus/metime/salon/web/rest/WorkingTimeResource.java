package com.cus.metime.salon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.metime.salon.domain.WorkingTime;
import com.cus.metime.salon.service.WorkingTimeService;
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
 * REST controller for managing WorkingTime.
 */
@RestController
@RequestMapping("/api")
public class WorkingTimeResource {

    private final Logger log = LoggerFactory.getLogger(WorkingTimeResource.class);

    private static final String ENTITY_NAME = "workingTime";

    private final WorkingTimeService workingTimeService;

    public WorkingTimeResource(WorkingTimeService workingTimeService) {
        this.workingTimeService = workingTimeService;
    }

    /**
     * POST  /working-times : Create a new workingTime.
     *
     * @param workingTime the workingTime to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workingTime, or with status 400 (Bad Request) if the workingTime has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/working-times")
    @Timed
    public ResponseEntity<WorkingTime> createWorkingTime(@RequestBody WorkingTime workingTime) throws URISyntaxException {
        log.debug("REST request to save WorkingTime : {}", workingTime);
        if (workingTime.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new workingTime cannot already have an ID")).body(null);
        }
        WorkingTime result = workingTimeService.save(workingTime);
        return ResponseEntity.created(new URI("/api/working-times/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /working-times : Updates an existing workingTime.
     *
     * @param workingTime the workingTime to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workingTime,
     * or with status 400 (Bad Request) if the workingTime is not valid,
     * or with status 500 (Internal Server Error) if the workingTime couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/working-times")
    @Timed
    public ResponseEntity<WorkingTime> updateWorkingTime(@RequestBody WorkingTime workingTime) throws URISyntaxException {
        log.debug("REST request to update WorkingTime : {}", workingTime);
        if (workingTime.getId() == null) {
            return createWorkingTime(workingTime);
        }
        WorkingTime result = workingTimeService.save(workingTime);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workingTime.getId().toString()))
            .body(result);
    }

    /**
     * GET  /working-times : get all the workingTimes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workingTimes in body
     */
    @GetMapping("/working-times")
    @Timed
    public List<WorkingTime> getAllWorkingTimes() {
        log.debug("REST request to get all WorkingTimes");
        return workingTimeService.findAll();
        }

    /**
     * GET  /working-times/:id : get the "id" workingTime.
     *
     * @param id the id of the workingTime to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workingTime, or with status 404 (Not Found)
     */
    @GetMapping("/working-times/{id}")
    @Timed
    public ResponseEntity<WorkingTime> getWorkingTime(@PathVariable Long id) {
        log.debug("REST request to get WorkingTime : {}", id);
        WorkingTime workingTime = workingTimeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(workingTime));
    }

    /**
     * DELETE  /working-times/:id : delete the "id" workingTime.
     *
     * @param id the id of the workingTime to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/working-times/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkingTime(@PathVariable Long id) {
        log.debug("REST request to delete WorkingTime : {}", id);
        workingTimeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
