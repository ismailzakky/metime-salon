package com.cus.metime.salon.service.impl;

import com.cus.metime.salon.service.BusinessInformationService;
import com.cus.metime.salon.domain.BusinessInformation;
import com.cus.metime.salon.repository.BusinessInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing BusinessInformation.
 */
@Service
@Transactional
public class BusinessInformationServiceImpl implements BusinessInformationService{

    private final Logger log = LoggerFactory.getLogger(BusinessInformationServiceImpl.class);

    private final BusinessInformationRepository businessInformationRepository;

    public BusinessInformationServiceImpl(BusinessInformationRepository businessInformationRepository) {
        this.businessInformationRepository = businessInformationRepository;
    }

    /**
     * Save a businessInformation.
     *
     * @param businessInformation the entity to save
     * @return the persisted entity
     */
    @Override
    public BusinessInformation save(BusinessInformation businessInformation) {
        log.debug("Request to save BusinessInformation : {}", businessInformation);
        return businessInformationRepository.save(businessInformation);
    }

    /**
     *  Get all the businessInformations.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<BusinessInformation> findAll() {
        log.debug("Request to get all BusinessInformations");
        return businessInformationRepository.findAll();
    }

    /**
     *  Get one businessInformation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BusinessInformation findOne(Long id) {
        log.debug("Request to get BusinessInformation : {}", id);
        return businessInformationRepository.findOne(id);
    }

    /**
     *  Delete the  businessInformation by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BusinessInformation : {}", id);
        businessInformationRepository.delete(id);
    }
}
