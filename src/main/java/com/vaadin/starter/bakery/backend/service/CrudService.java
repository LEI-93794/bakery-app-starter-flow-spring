package com.vaadin.starter.bakery.backend.service;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.User;

/**
 * Generic CRUD (Create, Read, Update, Delete) service interface for entities.
 * Provides common operations for managing entities that extend {@link AbstractEntity}.
 * 
 * @param <T> the type of entity this service manages, must extend AbstractEntity
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
public interface CrudService<T extends AbstractEntity> {

	/**
	 * Returns the JPA repository instance for this service.
	 * 
	 * @return the repository for the entity type T
	 */
	JpaRepository<T, Long> getRepository();

	/**
	 * Saves an entity to the database.
	 * 
	 * @param currentUser the user performing the save operation
	 * @param entity the entity to save
	 * @return the saved entity
	 */
	default T save(User currentUser, T entity) {
		return getRepository().saveAndFlush(entity);
	}

	/**
	 * Deletes an entity from the database.
	 * 
	 * @param currentUser the user performing the delete operation
	 * @param entity the entity to delete
	 * @throws EntityNotFoundException if the entity is null
	 */
	default void delete(User currentUser, T entity) {
		if (entity == null) {
			throw new EntityNotFoundException();
		}
		getRepository().delete(entity);
	}

	/**
	 * Deletes an entity by its ID.
	 * 
	 * @param currentUser the user performing the delete operation
	 * @param id the ID of the entity to delete
	 * @throws EntityNotFoundException if no entity with the given ID exists
	 */
	default void delete(User currentUser, long id) {
		delete(currentUser, load(id));
	}

	/**
	 * Returns the total count of entities in the database.
	 * 
	 * @return the number of entities
	 */
	default long count() {
		return getRepository().count();
	}

	/**
	 * Loads an entity by its ID.
	 * 
	 * @param id the ID of the entity to load
	 * @return the loaded entity
	 * @throws EntityNotFoundException if no entity with the given ID exists
	 */
	default T load(long id) {
		T entity = getRepository().findById(id).orElse(null);
		if (entity == null) {
			throw new EntityNotFoundException();
		}
		return entity;
	}

	/**
	 * Creates a new instance of the entity type.
	 * 
	 * @param currentUser the user creating the new entity
	 * @return a new entity instance
	 */
	T createNew(User currentUser);
}
