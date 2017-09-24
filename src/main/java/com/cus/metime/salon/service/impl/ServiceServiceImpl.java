package com.cus.metime.salon.service.impl;

import com.cus.metime.salon.service.ServiceService;
import com.cus.metime.salon.domain.Service;
import com.cus.metime.salon.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Service.
 */
@Service
@Transactional
public class ServiceServiceImpl implements ServiceService{

    private final Logger log = LoggerFactory.getLogger(ServiceServiceImpl.class);

    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * Save a service.
     *
     * @param service the entity to save
     * @return the persisted entity
     */
    @Override
    public Service save(Service service) {
        log.debug("Request to save Service : {}", service);
        return serviceRepository.save(service);
    }

    /**
     *  Get all the services.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Service> findAll() {
        log.debug("Request to get all Services");
        return serviceRepository.findAll();
    }

    /**
     *  Get one service by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Service findOne(Long id) {
        log.debug("Request to get Service : {}", id);
        return serviceRepository.findOne(id);
    }

    /**
     *  Delete the  service by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Service : {}", id);
        serviceRepository.delete(id);
    }
}
