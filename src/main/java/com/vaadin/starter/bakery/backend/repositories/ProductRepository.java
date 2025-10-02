package com.vaadin.starter.bakery.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.Product;

/**
 * Repository interface for managing {@link Product} entities.
 * 
 * <p>This repository provides methods for:
 * <ul>
 *   <li>Basic CRUD operations through JpaRepository</li>
 *   <li>Paginated retrieval of all products</li>
 *   <li>Case-insensitive name-based searching with pagination</li>
 *   <li>Counting products by name criteria</li>
 * </ul>
 * </p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * Retrieves all products with pagination support.
	 * 
	 * @param page pagination information including page number, size, and sorting
	 * @return a page of products
	 */
	Page<Product> findBy(Pageable page);

	/**
	 * Finds products with names matching the specified pattern (case-insensitive).
	 * Uses SQL LIKE pattern matching with the provided name parameter.
	 * 
	 * @param name the name pattern to search for (supports SQL wildcards like %)
	 * @param page pagination information
	 * @return a page of products matching the name pattern
	 */
	Page<Product> findByNameLikeIgnoreCase(String name, Pageable page);

	/**
	 * Counts products with names matching the specified pattern (case-insensitive).
	 * 
	 * @param name the name pattern to search for (supports SQL wildcards like %)
	 * @return the number of products matching the name pattern
	 */
	int countByNameLikeIgnoreCase(String name);

}
