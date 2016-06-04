package com.ra_bbvet.app.repository;

import com.ra_bbvet.app.domain.Suboutput;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Suboutput entity.
 */
@SuppressWarnings("unused")
public interface SuboutputRepository extends JpaRepository<Suboutput,Long> {

}
