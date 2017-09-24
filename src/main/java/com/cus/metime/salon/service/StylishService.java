package com.cus.metime.salon.service;

import com.cus.metime.salon.domain.Stylish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Stylish.
 */
public interface StylishService {

    /**
     * Save a stylish.
     *
     * @param stylish the entity to save
     * @return the persisted entity
     */
    Stylish save(Stylish stylish);

    /**
     *  Get all the stylishes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Stylish> findAll(Pageable pageable);

    /**
     *  Get the "id" stylish.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Stylish findOne(Long id);

    /**
     *  Delete the "id" stylish.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
