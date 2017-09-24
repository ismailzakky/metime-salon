package com.cus.metime.salon.repository;

import com.cus.metime.salon.domain.CreationalDate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CreationalDate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CreationalDateRepository extends JpaRepository<CreationalDate, Long> {

}
