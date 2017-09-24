package com.cus.metime.salon.service.impl;

import com.cus.metime.salon.service.PeopleInformationService;
import com.cus.metime.salon.domain.PeopleInformation;
import com.cus.metime.salon.repository.PeopleInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing PeopleInformation.
 */
@Service
@Transactional
public class PeopleInformationServiceImpl implements PeopleInformationService{

    private final Logger log = LoggerFactory.getLogger(PeopleInformationServiceImpl.class);

    private final PeopleInformationRepository peopleInformationRepository;

    public PeopleInformationServiceImpl(PeopleInformationRepository peopleInformationRepository) {
        this.peopleInformationRepository = peopleInformationRepository;
    }

    /**
     * Save a peopleInformation.
     *
     * @param peopleInformation the entity to save
     * @return the persisted entity
     */
    @Override
    public PeopleInformation save(PeopleInformation peopleInformation) {
        log.debug("Request to save PeopleInformation : {}", peopleInformation);
        return peopleInformationRepository.save(peopleInformation);
    }

    /**
     *  Get all the peopleInformations.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PeopleInformation> findAll() {
        log.debug("Request to get all PeopleInformations");
        return peopleInformationRepository.findAll();
    }

    /**
     *  Get one peopleInformation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PeopleInformation findOne(Long id) {
        log.debug("Request to get PeopleInformation : {}", id);
        return peopleInformationRepository.findOne(id);
    }

    /**
     *  Delete the  peopleInformation by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PeopleInformation : {}", id);
        peopleInformationRepository.delete(id);
    }
}
