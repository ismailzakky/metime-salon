package com.cus.metime.salon.repository;

import com.cus.metime.salon.domain.Stylish;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Stylish entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StylishRepository extends JpaRepository<Stylish, Long> {

}
