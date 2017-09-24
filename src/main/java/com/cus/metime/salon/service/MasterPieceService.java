package com.cus.metime.salon.service;

import com.cus.metime.salon.domain.MasterPiece;
import java.util.List;

/**
 * Service Interface for managing MasterPiece.
 */
public interface MasterPieceService {

    /**
     * Save a masterPiece.
     *
     * @param masterPiece the entity to save
     * @return the persisted entity
     */
    MasterPiece save(MasterPiece masterPiece);

    /**
     *  Get all the masterPieces.
     *
     *  @return the list of entities
     */
    List<MasterPiece> findAll();

    /**
     *  Get the "id" masterPiece.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MasterPiece findOne(Long id);

    /**
     *  Delete the "id" masterPiece.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
