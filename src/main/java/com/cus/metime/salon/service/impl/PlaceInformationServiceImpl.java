package com.cus.metime.salon.service.impl;

import com.cus.metime.salon.service.PlaceInformationService;
import com.cus.metime.salon.domain.PlaceInformation;
import com.cus.metime.salon.repository.PlaceInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing PlaceInformation.
 */
@Service
@Transactional
public class PlaceInformationServiceImpl implements PlaceInformationService{

    private final Logger log = LoggerFactory.getLogger(PlaceInformationServiceImpl.class);

    private final PlaceInformationRepository placeInformationRepository;

    public PlaceInformationServiceImpl(PlaceInformationRepository placeInformationRepository) {
        this.placeInformationRepository = placeInformationRepository;
    }

    /**
     * Save a placeInformation.
     *
     * @param placeInformation the entity to save
     * @return the persisted entity
     */
    @Override
    public PlaceInformation save(PlaceInformation placeInformation) {
        log.debug("Request to save PlaceInformation : {}", placeInformation);
        return placeInformationRepository.save(placeInformation);
    }

    /**
     *  Get all the placeInformations.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PlaceInformation> findAll() {
        log.debug("Request to get all PlaceInformations");
        return placeInformationRepository.findAll();
    }

    /**
     *  Get one placeInformation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PlaceInformation findOne(Long id) {
        log.debug("Request to get PlaceInformation : {}", id);
        return placeInformationRepository.findOne(id);
    }

    /**
     *  Delete the  placeInformation by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlaceInformation : {}", id);
        placeInformationRepository.delete(id);
    }
}
