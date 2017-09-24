package com.cus.metime.salon.service.impl;

import com.cus.metime.salon.service.CreationalDateService;
import com.cus.metime.salon.domain.CreationalDate;
import com.cus.metime.salon.repository.CreationalDateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing CreationalDate.
 */
@Service
@Transactional
public class CreationalDateServiceImpl implements CreationalDateService{

    private final Logger log = LoggerFactory.getLogger(CreationalDateServiceImpl.class);

    private final CreationalDateRepository creationalDateRepository;

    public CreationalDateServiceImpl(CreationalDateRepository creationalDateRepository) {
        this.creationalDateRepository = creationalDateRepository;
    }

    /**
     * Save a creationalDate.
     *
     * @param creationalDate the entity to save
     * @return the persisted entity
     */
    @Override
    public CreationalDate save(CreationalDate creationalDate) {
        log.debug("Request to save CreationalDate : {}", creationalDate);
        return creationalDateRepository.save(creationalDate);
    }

    /**
     *  Get all the creationalDates.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CreationalDate> findAll() {
        log.debug("Request to get all CreationalDates");
        return creationalDateRepository.findAll();
    }

    /**
     *  Get one creationalDate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CreationalDate findOne(Long id) {
        log.debug("Request to get CreationalDate : {}", id);
        return creationalDateRepository.findOne(id);
    }

    /**
     *  Delete the  creationalDate by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CreationalDate : {}", id);
        creationalDateRepository.delete(id);
    }
}
