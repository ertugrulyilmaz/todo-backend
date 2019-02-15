package io.todo.todo.security;

import io.todo.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private static final boolean ACCOUNT_NON_EXPIRED = true;
	private static final boolean CREDENTIALS_NON_EXPIRED = true;
	private static final boolean ACCOUNT_NON_LOCKED = true;

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(final String userId) throws UsernameNotFoundException {


		final Optional<io.todo.todo.domain.entity.User> maybeUser = userService.getUserById(userId).join();

		return maybeUser.map(user -> new User(
				user.getId(),
				"{noop}" + user.getPassword(),
				true,
				ACCOUNT_NON_EXPIRED,
				CREDENTIALS_NON_EXPIRED,
				ACCOUNT_NON_LOCKED,
				Collections.emptyList()))
				.orElse(null);
	}
}