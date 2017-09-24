package com.cus.metime.salon.service;

import com.cus.metime.salon.domain.WorkingTime;
import java.util.List;

/**
 * Service Interface for managing WorkingTime.
 */
public interface WorkingTimeService {

    /**
     * Save a workingTime.
     *
     * @param workingTime the entity to save
     * @return the persisted entity
     */
    WorkingTime save(WorkingTime workingTime);

    /**
     *  Get all the workingTimes.
     *
     *  @return the list of entities
     */
    List<WorkingTime> findAll();

    /**
     *  Get the "id" workingTime.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WorkingTime findOne(Long id);

    /**
     *  Delete the "id" workingTime.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
