package com.cus.metime.salon.service;

import com.cus.metime.salon.domain.CreationalDate;
import java.util.List;

/**
 * Service Interface for managing CreationalDate.
 */
public interface CreationalDateService {

    /**
     * Save a creationalDate.
     *
     * @param creationalDate the entity to save
     * @return the persisted entity
     */
    CreationalDate save(CreationalDate creationalDate);

    /**
     *  Get all the creationalDates.
     *
     *  @return the list of entities
     */
    List<CreationalDate> findAll();

    /**
     *  Get the "id" creationalDate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CreationalDate findOne(Long id);

    /**
     *  Delete the "id" creationalDate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
