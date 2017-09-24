package com.cus.metime.salon.repository;

import com.cus.metime.salon.domain.WorkingTime;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WorkingTime entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkingTimeRepository extends JpaRepository<WorkingTime, Long> {

}
