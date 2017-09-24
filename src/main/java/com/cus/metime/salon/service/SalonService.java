package com.cus.metime.salon.service;

import com.cus.metime.salon.domain.Salon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Salon.
 */
public interface SalonService {

    /**
     * Save a salon.
     *
     * @param salon the entity to save
     * @return the persisted entity
     */
    Salon save(Salon salon);

    /**
     *  Get all the salons.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Salon> findAll(Pageable pageable);

    /**
     *  Get the "id" salon.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Salon findOne(Long id);

    /**
     *  Delete the "id" salon.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
