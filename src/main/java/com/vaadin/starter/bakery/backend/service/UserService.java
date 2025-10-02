package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;

/**
 * Service class for managing CRUD (Create, Read, Update, Delete) operations on User entities.
 * 
 * <p>This service implements specific business validations such as preventing modification
 * of locked users and self-deletion protection.</p>
 * 
 * <p>The service provides comprehensive user management functionality including:
 * <ul>
 *   <li>Multi-field search across user properties (email, names, role)</li>
 *   <li>User account security validations</li>
 *   <li>Prevention of unauthorized operations</li>
 *   <li>Pagination and filtering support</li>
 * </ul>
 * </p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
@Service
public class UserService implements FilterableCrudService<User> {

	/** Error messages for user operations. */
	public static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";
	private static final String DELETING_SELF_NOT_PERMITTED = "You cannot delete your own account";
	
	/**
	 * Repository for accessing user data.
	 */
	private final UserRepository userRepository;

	/**
	 * Constructs a new UserService with the specified user repository.
	 * 
	 * @param userRepository the JPA repository for accessing user data
	 */
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Finds a page of users whose fields (email, first name, last name, or role)
	 * match the provided filter (case-insensitive search).
	 *
	 * @param filter optional value to be used as search filter
	 * @param pageable pagination information (page number, size, and sorting)
	 * @return a {@code Page} of matching {@code User} entities
	 */	
	public Page<User> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository()
					.findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
							repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	/**
	 * Counts the total number of users whose fields (email, first name, last name, or role)
	 * match the provided filter.
	 *
	 * @param filter optional value to be used as search filter
	 * @return the total number of matching users
	 */	
	@Override
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return userRepository.countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
					repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter);
		} else {
			return count();
		}
	}

	@Override
	public UserRepository getRepository() {
		return userRepository;
	}

	/**
	 * Retrieves all users with pagination support.
	 * 
	 * @param pageable pagination information
	 * @return a page of users
	 */
	public Page<User> find(Pageable pageable) {
		return getRepository().findBy(pageable);
	}

	/**
	 * Saves the provided {@code User} entity.
	 * Throws an exception if the user is locked.
	 *
	 * @param currentUser the user performing the operation (for security validations)
	 * @param entity the user to be saved or updated
	 * @return the saved user
	 * @throws UserFriendlyDataException if the user is locked
	 */	
	@Override
	public User save(User currentUser, User entity) {
		throwIfUserLocked(entity);
		return getRepository().saveAndFlush(entity);
	}

	/**
	 * Deletes the specified user.
	 * Throws an exception if the user to delete is the current user or if the user
	 * to delete is locked.
	 *
	 * @param currentUser the user performing the operation (admin, etc.)
	 * @param userToDelete the user to be deleted
	 * @throws UserFriendlyDataException if attempting self-deletion or if the user is locked
	 */	
	@Override
	@Transactional
	public void delete(User currentUser, User userToDelete) {
		throwIfDeletingSelf(currentUser, userToDelete);
		throwIfUserLocked(userToDelete);
		FilterableCrudService.super.delete(currentUser, userToDelete);
	}

	/**
	 * Checks if the current user is attempting to delete their own account.
	 *
	 * @param currentUser the user making the request
	 * @param user the target user for the deletion operation
	 * @throws UserFriendlyDataException if both users are the same
	 */	
	private void throwIfDeletingSelf(User currentUser, User user) {
		if (currentUser.equals(user)) {
			throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
		}
	}

	/**
	 * Checks if the provided {@code User} entity is marked as locked.
	 *
	 * @param entity the target user for the modification operation
	 * @throws UserFriendlyDataException if the user is locked
	 */	
	private void throwIfUserLocked(User entity) {
		if (entity != null && entity.isLocked()) {
			throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
		}
	}

	/**
	 * Creates a new user instance.
	 * 
	 * @param currentUser the user creating the new entity
	 * @return a new user instance
	 */
	@Override
	public User createNew(User currentUser) {
		return new User();
	}

}
