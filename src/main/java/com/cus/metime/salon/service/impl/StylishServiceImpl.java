package com.cus.metime.salon.service.impl;

import com.cus.metime.salon.service.StylishService;
import com.cus.metime.salon.domain.Stylish;
import com.cus.metime.salon.repository.StylishRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Stylish.
 */
@Service
@Transactional
public class StylishServiceImpl implements StylishService{

    private final Logger log = LoggerFactory.getLogger(StylishServiceImpl.class);

    private final StylishRepository stylishRepository;

    public StylishServiceImpl(StylishRepository stylishRepository) {
        this.stylishRepository = stylishRepository;
    }

    /**
     * Save a stylish.
     *
     * @param stylish the entity to save
     * @return the persisted entity
     */
    @Override
    public Stylish save(Stylish stylish) {
        log.debug("Request to save Stylish : {}", stylish);
        return stylishRepository.save(stylish);
    }

    /**
     *  Get all the stylishes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Stylish> findAll(Pageable pageable) {
        log.debug("Request to get all Stylishes");
        return stylishRepository.findAll(pageable);
    }

    /**
     *  Get one stylish by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Stylish findOne(Long id) {
        log.debug("Request to get Stylish : {}", id);
        return stylishRepository.findOne(id);
    }

    /**
     *  Delete the  stylish by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Stylish : {}", id);
        stylishRepository.delete(id);
    }
}
