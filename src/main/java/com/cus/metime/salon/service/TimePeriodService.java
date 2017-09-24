package com.cus.metime.salon.service;

import com.cus.metime.salon.domain.TimePeriod;
import java.util.List;

/**
 * Service Interface for managing TimePeriod.
 */
public interface TimePeriodService {

    /**
     * Save a timePeriod.
     *
     * @param timePeriod the entity to save
     * @return the persisted entity
     */
    TimePeriod save(TimePeriod timePeriod);

    /**
     *  Get all the timePeriods.
     *
     *  @return the list of entities
     */
    List<TimePeriod> findAll();

    /**
     *  Get the "id" timePeriod.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TimePeriod findOne(Long id);

    /**
     *  Delete the "id" timePeriod.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
