package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Extension of {@link CrudService} that adds filtering and pagination capabilities.
 * This interface provides methods to search and count entities based on filter criteria
 * with pagination support.
 * 
 * @param <T> the type of entity this service manages, must extend AbstractEntity
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
public interface FilterableCrudService<T extends AbstractEntity> extends CrudService<T> {

	/**
	 * Finds entities that match the given filter criteria with pagination support.
	 * 
	 * @param filter optional filter criteria to apply when searching entities
	 * @param pageable pagination information including page number, size, and sorting
	 * @return a page of entities matching the filter criteria
	 */
	Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);

	/**
	 * Counts the number of entities that match the given filter criteria.
	 * 
	 * @param filter optional filter criteria to apply when counting entities
	 * @return the number of entities matching the filter criteria
	 */
	long countAnyMatching(Optional<String> filter);
}
