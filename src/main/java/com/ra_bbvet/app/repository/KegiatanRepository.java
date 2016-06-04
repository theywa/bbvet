package com.ra_bbvet.app.repository;

import com.ra_bbvet.app.domain.Kegiatan;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Kegiatan entity.
 */
@SuppressWarnings("unused")
public interface KegiatanRepository extends JpaRepository<Kegiatan,Long> {

}
