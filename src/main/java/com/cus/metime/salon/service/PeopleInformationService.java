package com.cus.metime.salon.service;

import com.cus.metime.salon.domain.PeopleInformation;
import java.util.List;

/**
 * Service Interface for managing PeopleInformation.
 */
public interface PeopleInformationService {

    /**
     * Save a peopleInformation.
     *
     * @param peopleInformation the entity to save
     * @return the persisted entity
     */
    PeopleInformation save(PeopleInformation peopleInformation);

    /**
     *  Get all the peopleInformations.
     *
     *  @return the list of entities
     */
    List<PeopleInformation> findAll();

    /**
     *  Get the "id" peopleInformation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PeopleInformation findOne(Long id);

    /**
     *  Delete the "id" peopleInformation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
