package com.cus.metime.salon.repository;

import com.cus.metime.salon.domain.MasterPiece;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MasterPiece entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MasterPieceRepository extends JpaRepository<MasterPiece, Long> {

}
