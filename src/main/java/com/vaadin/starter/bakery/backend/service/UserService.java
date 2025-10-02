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
 * Serviço de aplicação para gerir a CRUD (Criar, Ler, Atualizar, Apagar) de 
 * entidades de Utilizador (User).
 * <p>
 * Implementa validações específicas de negócio, como a prevenção de modificação
 * de utilizadores bloqueados ou a auto-exclusão.
 */
@Service
public class UserService implements FilterableCrudService<User> {

	/** Mensagem de erro para quando se tenta modificar ou apagar um utilizador bloqueado. */
	public static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";
	private static final String DELETING_SELF_NOT_PERMITTED = "You cannot delete your own account";
	private final UserRepository userRepository;

	/**
	 * Construtor injetado por dependência.
	 * * @param userRepository o repositório JPA para aceder aos dados do utilizador.
	 */
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Encontra uma página de utilizadores cujos campos (e-mail, nome, apelido ou role)
	 * correspondam ao filtro fornecido (sem distinção entre maiúsculas e minúsculas).
	 *
	 * @param filter O valor opcional a ser usado como filtro de pesquisa.
	 * @param pageable A informação de paginação (número da página, tamanho e ordenação).
	 * @return Uma {@code Page} de entidades {@code User} correspondentes.
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
	 * Conta o número total de utilizadores cujos campos (e-mail, nome, apelido ou role)
	 * correspondam ao filtro fornecido.
	 *
	 * @param filter O valor opcional a ser usado como filtro de pesquisa.
	 * @return O número total de utilizadores correspondentes.
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

	public Page<User> find(Pageable pageable) {
		return getRepository().findBy(pageable);
	}

	/**
	 * Guarda a entidade {@code User} fornecida.
	 * Lança uma exceção se o utilizador estiver bloqueado (locked).
	 *
	 * @param currentUser O utilizador que está a efetuar a operação (para validações de segurança).
	 * @param entity O utilizador a ser guardado ou atualizado.
	 * @return O utilizador guardado.
	 * @throws com.vaadin.starter.bakery.backend.data.UserFriendlyDataException se o utilizador estiver bloqueado.
	 */	
	@Override
	public User save(User currentUser, User entity) {
		throwIfUserLocked(entity);
		return getRepository().saveAndFlush(entity);
	}

	/**
	 * Apaga o utilizador especificado.
	 * Lança uma exceção se o utilizador a apagar for o utilizador atual ou se o utilizador
	 * a apagar estiver bloqueado.
	 *
	 * @param currentUser O utilizador que está a efetuar a operação (admin, etc.).
	 * @param userToDelete O utilizador que se pretende apagar.
	 * @throws com.vaadin.starter.bakery.backend.data.UserFriendlyDataException se for auto-exclusão ou se o utilizador estiver bloqueado.
	 */	
	@Override
	@Transactional
	public void delete(User currentUser, User userToDelete) {
		throwIfDeletingSelf(currentUser, userToDelete);
		throwIfUserLocked(userToDelete);
		FilterableCrudService.super.delete(currentUser, userToDelete);
	}

	/**
	 * Verifica se o utilizador atual está a tentar apagar a sua própria conta.
	 *
	 * @param currentUser O utilizador que está a fazer o pedido.
	 * @param user O utilizador alvo da operação de eliminação.
	 * @throws com.vaadin.starter.bakery.backend.data.UserFriendlyDataException se os dois utilizadores forem os mesmos.
	 */	
	private void throwIfDeletingSelf(User currentUser, User user) {
		if (currentUser.equals(user)) {
			throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
		}
	}

	/**
	 * Verifica se a entidade {@code User} fornecida está marcada como bloqueada.
	 *
	 * @param entity O utilizador alvo da operação de modificação.
	 * @throws com.vaadin.starter.bakery.backend.data.UserFriendlyDataException se o utilizador estiver bloqueado.
	 */	
	private void throwIfUserLocked(User entity) {
		if (entity != null && entity.isLocked()) {
			throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
		}
	}

	@Override
	public User createNew(User currentUser) {
		return new User();
	}

}
