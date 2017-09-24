package com.cus.metime.salon.repository;

import com.cus.metime.salon.domain.PeopleInformation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PeopleInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeopleInformationRepository extends JpaRepository<PeopleInformation, Long> {

}
