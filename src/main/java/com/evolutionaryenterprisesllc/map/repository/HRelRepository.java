package com.evolutionaryenterprisesllc.map.repository;

import com.evolutionaryenterprisesllc.map.domain.HRel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HRel entity.
 */
@SuppressWarnings("unused")
public interface HRelRepository extends JpaRepository<HRel,Long> {

}
