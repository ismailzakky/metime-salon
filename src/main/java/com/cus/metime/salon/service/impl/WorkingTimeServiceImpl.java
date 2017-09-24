package com.cus.metime.salon.service.impl;

import com.cus.metime.salon.service.WorkingTimeService;
import com.cus.metime.salon.domain.WorkingTime;
import com.cus.metime.salon.repository.WorkingTimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing WorkingTime.
 */
@Service
@Transactional
public class WorkingTimeServiceImpl implements WorkingTimeService{

    private final Logger log = LoggerFactory.getLogger(WorkingTimeServiceImpl.class);

    private final WorkingTimeRepository workingTimeRepository;

    public WorkingTimeServiceImpl(WorkingTimeRepository workingTimeRepository) {
        this.workingTimeRepository = workingTimeRepository;
    }

    /**
     * Save a workingTime.
     *
     * @param workingTime the entity to save
     * @return the persisted entity
     */
    @Override
    public WorkingTime save(WorkingTime workingTime) {
        log.debug("Request to save WorkingTime : {}", workingTime);
        return workingTimeRepository.save(workingTime);
    }

    /**
     *  Get all the workingTimes.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkingTime> findAll() {
        log.debug("Request to get all WorkingTimes");
        return workingTimeRepository.findAll();
    }

    /**
     *  Get one workingTime by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WorkingTime findOne(Long id) {
        log.debug("Request to get WorkingTime : {}", id);
        return workingTimeRepository.findOne(id);
    }

    /**
     *  Delete the  workingTime by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkingTime : {}", id);
        workingTimeRepository.delete(id);
    }
}
