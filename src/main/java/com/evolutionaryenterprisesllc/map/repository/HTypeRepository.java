package com.evolutionaryenterprisesllc.map.repository;

import com.evolutionaryenterprisesllc.map.domain.HType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HType entity.
 */
@SuppressWarnings("unused")
public interface HTypeRepository extends JpaRepository<HType,Long> {

}
