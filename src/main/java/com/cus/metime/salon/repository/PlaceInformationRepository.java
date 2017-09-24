package com.cus.metime.salon.repository;

import com.cus.metime.salon.domain.PlaceInformation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PlaceInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaceInformationRepository extends JpaRepository<PlaceInformation, Long> {

}
