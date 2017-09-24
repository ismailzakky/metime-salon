package com.cus.metime.salon.service;

import com.cus.metime.salon.domain.BusinessInformation;
import java.util.List;

/**
 * Service Interface for managing BusinessInformation.
 */
public interface BusinessInformationService {

    /**
     * Save a businessInformation.
     *
     * @param businessInformation the entity to save
     * @return the persisted entity
     */
    BusinessInformation save(BusinessInformation businessInformation);

    /**
     *  Get all the businessInformations.
     *
     *  @return the list of entities
     */
    List<BusinessInformation> findAll();

    /**
     *  Get the "id" businessInformation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BusinessInformation findOne(Long id);

    /**
     *  Delete the "id" businessInformation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
