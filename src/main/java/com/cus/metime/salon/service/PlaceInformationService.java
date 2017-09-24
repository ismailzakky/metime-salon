package com.cus.metime.salon.service;

import com.cus.metime.salon.domain.PlaceInformation;
import java.util.List;

/**
 * Service Interface for managing PlaceInformation.
 */
public interface PlaceInformationService {

    /**
     * Save a placeInformation.
     *
     * @param placeInformation the entity to save
     * @return the persisted entity
     */
    PlaceInformation save(PlaceInformation placeInformation);

    /**
     *  Get all the placeInformations.
     *
     *  @return the list of entities
     */
    List<PlaceInformation> findAll();

    /**
     *  Get the "id" placeInformation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PlaceInformation findOne(Long id);

    /**
     *  Delete the "id" placeInformation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
