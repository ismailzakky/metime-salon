package com.cus.metime.salon.service.impl;

import com.cus.metime.salon.service.TimePeriodService;
import com.cus.metime.salon.domain.TimePeriod;
import com.cus.metime.salon.repository.TimePeriodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing TimePeriod.
 */
@Service
@Transactional
public class TimePeriodServiceImpl implements TimePeriodService{

    private final Logger log = LoggerFactory.getLogger(TimePeriodServiceImpl.class);

    private final TimePeriodRepository timePeriodRepository;

    public TimePeriodServiceImpl(TimePeriodRepository timePeriodRepository) {
        this.timePeriodRepository = timePeriodRepository;
    }

    /**
     * Save a timePeriod.
     *
     * @param timePeriod the entity to save
     * @return the persisted entity
     */
    @Override
    public TimePeriod save(TimePeriod timePeriod) {
        log.debug("Request to save TimePeriod : {}", timePeriod);
        return timePeriodRepository.save(timePeriod);
    }

    /**
     *  Get all the timePeriods.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TimePeriod> findAll() {
        log.debug("Request to get all TimePeriods");
        return timePeriodRepository.findAll();
    }

    /**
     *  Get one timePeriod by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TimePeriod findOne(Long id) {
        log.debug("Request to get TimePeriod : {}", id);
        return timePeriodRepository.findOne(id);
    }

    /**
     *  Delete the  timePeriod by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimePeriod : {}", id);
        timePeriodRepository.delete(id);
    }
}
