package com.cus.metime.salon.service.impl;

import com.cus.metime.salon.service.SalonService;
import com.cus.metime.salon.domain.Salon;
import com.cus.metime.salon.repository.SalonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Salon.
 */
@Service
@Transactional
public class SalonServiceImpl implements SalonService{

    private final Logger log = LoggerFactory.getLogger(SalonServiceImpl.class);

    private final SalonRepository salonRepository;

    public SalonServiceImpl(SalonRepository salonRepository) {
        this.salonRepository = salonRepository;
    }

    /**
     * Save a salon.
     *
     * @param salon the entity to save
     * @return the persisted entity
     */
    @Override
    public Salon save(Salon salon) {
        log.debug("Request to save Salon : {}", salon);
        return salonRepository.save(salon);
    }

    /**
     *  Get all the salons.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Salon> findAll(Pageable pageable) {
        log.debug("Request to get all Salons");
        return salonRepository.findAll(pageable);
    }

    /**
     *  Get one salon by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Salon findOne(Long id) {
        log.debug("Request to get Salon : {}", id);
        return salonRepository.findOne(id);
    }

    /**
     *  Delete the  salon by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Salon : {}", id);
        salonRepository.delete(id);
    }
}
