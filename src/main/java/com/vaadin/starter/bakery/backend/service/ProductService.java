package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.ProductRepository;

/**
 * Service class for managing {@link Product} entities.
 * Provides CRUD operations and filtering capabilities for products in the bakery application.
 * This service implements {@link FilterableCrudService} to support paginated and filtered queries.
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
@Service
public class ProductService implements FilterableCrudService<Product> {

	/**
	 * The repository for accessing product data.
	 */
	private final ProductRepository productRepository;

	/**
	 * Constructs a new ProductService with the specified product repository.
	 * 
	 * @param productRepository the repository for managing product entities
	 */
	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Finds products that match the given filter criteria with pagination support.
	 * If a filter is provided, searches for products with names containing the filter text (case-insensitive).
	 * If no filter is provided, returns all products.
	 * 
	 * @param filter optional filter string to search for in product names
	 * @param pageable pagination information including page number, size, and sorting
	 * @return a page of products matching the filter criteria
	 */
	@Override
	public Page<Product> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return productRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	/**
	 * Counts the number of products that match the given filter criteria.
	 * If a filter is provided, counts products with names containing the filter text (case-insensitive).
	 * If no filter is provided, returns the total count of all products.
	 * 
	 * @param filter optional filter string to search for in product names
	 * @return the number of products matching the filter criteria
	 */
	@Override
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return productRepository.countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return count();
		}
	}

	/**
	 * Retrieves a page of products with pagination support.
	 * 
	 * @param pageable pagination information including page number, size, and sorting
	 * @return a page of products
	 */
	public Page<Product> find(Pageable pageable) {
		return productRepository.findBy(pageable);
	}

	/**
	 * Returns the underlying JPA repository for this service.
	 * 
	 * @return the product repository instance
	 */
	@Override
	public JpaRepository<Product, Long> getRepository() {
		return productRepository;
	}

	/**
	 * Creates a new product instance.
	 * This method is called when creating new products in the application.
	 * 
	 * @param currentUser the user creating the product (not used in this implementation)
	 * @return a new empty product instance
	 */
	@Override
	public Product createNew(User currentUser) {
		return new Product();
	}

	/**
	 * Saves a product entity to the database.
	 * Handles data integrity violations by throwing a user-friendly exception
	 * when attempting to save a product with a duplicate name.
	 * 
	 * @param currentUser the user performing the save operation
	 * @param entity the product entity to save
	 * @return the saved product entity
	 * @throws UserFriendlyDataException if a product with the same name already exists
	 */
	@Override
	public Product save(User currentUser, Product entity) {
		try {
			return FilterableCrudService.super.save(currentUser, entity);
		} catch (DataIntegrityViolationException e) {
			throw new UserFriendlyDataException(
					"There is already a product with that name. Please select a unique name for the product.");
		}
	}

}
