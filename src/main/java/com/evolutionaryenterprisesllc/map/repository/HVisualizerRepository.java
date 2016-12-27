package com.evolutionaryenterprisesllc.map.repository;

import com.evolutionaryenterprisesllc.map.domain.HVisualizer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HVisualizer entity.
 */
@SuppressWarnings("unused")
public interface HVisualizerRepository extends JpaRepository<HVisualizer,Long> {

}
