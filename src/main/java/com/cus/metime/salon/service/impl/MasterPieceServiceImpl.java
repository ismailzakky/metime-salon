package com.cus.metime.salon.service.impl;

import com.cus.metime.salon.service.MasterPieceService;
import com.cus.metime.salon.domain.MasterPiece;
import com.cus.metime.salon.repository.MasterPieceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing MasterPiece.
 */
@Service
@Transactional
public class MasterPieceServiceImpl implements MasterPieceService{

    private final Logger log = LoggerFactory.getLogger(MasterPieceServiceImpl.class);

    private final MasterPieceRepository masterPieceRepository;

    public MasterPieceServiceImpl(MasterPieceRepository masterPieceRepository) {
        this.masterPieceRepository = masterPieceRepository;
    }

    /**
     * Save a masterPiece.
     *
     * @param masterPiece the entity to save
     * @return the persisted entity
     */
    @Override
    public MasterPiece save(MasterPiece masterPiece) {
        log.debug("Request to save MasterPiece : {}", masterPiece);
        return masterPieceRepository.save(masterPiece);
    }

    /**
     *  Get all the masterPieces.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MasterPiece> findAll() {
        log.debug("Request to get all MasterPieces");
        return masterPieceRepository.findAll();
    }

    /**
     *  Get one masterPiece by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MasterPiece findOne(Long id) {
        log.debug("Request to get MasterPiece : {}", id);
        return masterPieceRepository.findOne(id);
    }

    /**
     *  Delete the  masterPiece by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MasterPiece : {}", id);
        masterPieceRepository.delete(id);
    }
}
