package com.cus.metime.salon.repository;

import com.cus.metime.salon.domain.BusinessInformation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BusinessInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessInformationRepository extends JpaRepository<BusinessInformation, Long> {

}
