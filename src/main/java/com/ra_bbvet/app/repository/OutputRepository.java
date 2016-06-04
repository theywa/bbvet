package com.ra_bbvet.app.repository;

import com.ra_bbvet.app.domain.Output;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Output entity.
 */
@SuppressWarnings("unused")
public interface OutputRepository extends JpaRepository<Output,Long> {

}
