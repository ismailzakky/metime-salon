package com.cus.metime.salon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.metime.salon.domain.MasterPiece;
import com.cus.metime.salon.service.MasterPieceService;
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
 * REST controller for managing MasterPiece.
 */
@RestController
@RequestMapping("/api")
public class MasterPieceResource {

    private final Logger log = LoggerFactory.getLogger(MasterPieceResource.class);

    private static final String ENTITY_NAME = "masterPiece";

    private final MasterPieceService masterPieceService;

    public MasterPieceResource(MasterPieceService masterPieceService) {
        this.masterPieceService = masterPieceService;
    }

    /**
     * POST  /master-pieces : Create a new masterPiece.
     *
     * @param masterPiece the masterPiece to create
     * @return the ResponseEntity with status 201 (Created) and with body the new masterPiece, or with status 400 (Bad Request) if the masterPiece has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/master-pieces")
    @Timed
    public ResponseEntity<MasterPiece> createMasterPiece(@RequestBody MasterPiece masterPiece) throws URISyntaxException {
        log.debug("REST request to save MasterPiece : {}", masterPiece);
        if (masterPiece.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new masterPiece cannot already have an ID")).body(null);
        }
        MasterPiece result = masterPieceService.save(masterPiece);
        return ResponseEntity.created(new URI("/api/master-pieces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /master-pieces : Updates an existing masterPiece.
     *
     * @param masterPiece the masterPiece to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated masterPiece,
     * or with status 400 (Bad Request) if the masterPiece is not valid,
     * or with status 500 (Internal Server Error) if the masterPiece couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/master-pieces")
    @Timed
    public ResponseEntity<MasterPiece> updateMasterPiece(@RequestBody MasterPiece masterPiece) throws URISyntaxException {
        log.debug("REST request to update MasterPiece : {}", masterPiece);
        if (masterPiece.getId() == null) {
            return createMasterPiece(masterPiece);
        }
        MasterPiece result = masterPieceService.save(masterPiece);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, masterPiece.getId().toString()))
            .body(result);
    }

    /**
     * GET  /master-pieces : get all the masterPieces.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of masterPieces in body
     */
    @GetMapping("/master-pieces")
    @Timed
    public List<MasterPiece> getAllMasterPieces() {
        log.debug("REST request to get all MasterPieces");
        return masterPieceService.findAll();
        }

    /**
     * GET  /master-pieces/:id : get the "id" masterPiece.
     *
     * @param id the id of the masterPiece to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the masterPiece, or with status 404 (Not Found)
     */
    @GetMapping("/master-pieces/{id}")
    @Timed
    public ResponseEntity<MasterPiece> getMasterPiece(@PathVariable Long id) {
        log.debug("REST request to get MasterPiece : {}", id);
        MasterPiece masterPiece = masterPieceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(masterPiece));
    }

    /**
     * DELETE  /master-pieces/:id : delete the "id" masterPiece.
     *
     * @param id the id of the masterPiece to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/master-pieces/{id}")
    @Timed
    public ResponseEntity<Void> deleteMasterPiece(@PathVariable Long id) {
        log.debug("REST request to delete MasterPiece : {}", id);
        masterPieceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
