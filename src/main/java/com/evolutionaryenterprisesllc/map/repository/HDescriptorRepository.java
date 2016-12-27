package com.evolutionaryenterprisesllc.map.repository;

import com.evolutionaryenterprisesllc.map.domain.HDescriptor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HDescriptor entity.
 */
@SuppressWarnings("unused")
public interface HDescriptorRepository extends JpaRepository<HDescriptor,Long> {

}
