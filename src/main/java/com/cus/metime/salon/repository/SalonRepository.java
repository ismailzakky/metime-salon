package com.cus.metime.salon.repository;

import com.cus.metime.salon.domain.Salon;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Salon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {

}
