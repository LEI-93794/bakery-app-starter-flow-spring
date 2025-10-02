package com.vaadin.starter.bakery.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.User;

/**
 * Repository interface for managing {@link User} entities.
 * 
 * <p>This repository provides methods for:
 * <ul>
 *   <li>Basic CRUD operations through JpaRepository</li>
 *   <li>User authentication and lookup by email</li>
 *   <li>Multi-field search across user properties</li>
 *   <li>Paginated user retrieval and counting</li>
 * </ul>
 * </p>
 * 
 * @author Bakery Application
 * @version 1.0
 * @since 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Finds a user by email address (case-insensitive).
	 * Used for user authentication and lookup.
	 * 
	 * @param email the email address to search for
	 * @return the user with the specified email, or null if not found
	 */
	User findByEmailIgnoreCase(String email);

	/**
	 * Retrieves all users with pagination support.
	 * 
	 * @param pageable pagination information including page number, size, and sorting
	 * @return a page of users
	 */
	Page<User> findBy(Pageable pageable);

	/**
	 * Finds users matching the search criteria across multiple fields (case-insensitive).
	 * Searches in email, first name, last name, and role fields using LIKE matching.
	 * 
	 * @param emailLike pattern to match against email field
	 * @param firstNameLike pattern to match against first name field
	 * @param lastNameLike pattern to match against last name field
	 * @param roleLike pattern to match against role field
	 * @param pageable pagination information
	 * @return a page of users matching any of the search criteria
	 */
	Page<User> findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
			String emailLike, String firstNameLike, String lastNameLike, String roleLike, Pageable pageable);

	/**
	 * Counts users matching the search criteria across multiple fields (case-insensitive).
	 * 
	 * @param emailLike pattern to match against email field
	 * @param firstNameLike pattern to match against first name field
	 * @param lastNameLike pattern to match against last name field
	 * @param roleLike pattern to match against role field
	 * @return the number of users matching any of the search criteria
	 */
	long countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
			String emailLike, String firstNameLike, String lastNameLike, String roleLike);
}
