package com.cus.metime.salon.service;

import com.cus.metime.salon.domain.Service;
import java.util.List;

/**
 * Service Interface for managing Service.
 */
public interface ServiceService {

    /**
     * Save a service.
     *
     * @param service the entity to save
     * @return the persisted entity
     */
    Service save(Service service);

    /**
     *  Get all the services.
     *
     *  @return the list of entities
     */
    List<Service> findAll();

    /**
     *  Get the "id" service.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Service findOne(Long id);

    /**
     *  Delete the "id" service.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
