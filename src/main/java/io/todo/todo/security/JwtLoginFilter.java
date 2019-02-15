package io.todo.todo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.todo.todo.domain.dto.Credential;
import io.todo.todo.domain.entity.User;
import io.todo.todo.repository.UserRepository;
import io.todo.todo.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Optional;

@Component
@Slf4j
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private CryptoUtil cryptoUtil;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;


	public JwtLoginFilter() {
		super("/v1/auth/login");
	}

	@PostConstruct
	public void init() {
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res) throws AuthenticationException, IOException {
		try (final InputStream is = req.getInputStream()) {
			final Credential account = mapper.readValue(is, Credential.class);
			final AuthenticationManager manager = getAuthenticationManager();
			final String email = account.getEmail();
			final String password = account.getPassword();
			final Optional<User> maybeUser = userRepository.getUserByEmail(email).join();

			return maybeUser.map(user -> {
				final String newEncodedPassword = cryptoUtil.encodePassword(password, user.getSalt());
				final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getId(), newEncodedPassword);

				try {
					return manager.authenticate(token);
				} catch (Exception e) {
					try {
						final PrintWriter out = res.getWriter();
						res.setContentType("application/json");
						res.setCharacterEncoding("UTF-8");
						res.setStatus(400);

						out.print(mapper.writeValueAsString(ResponseEntity.badRequest().body("Login failed")));
						out.flush();

						log.error("Login Failed for {}", email);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return null;
				}
			}).orElse(null);
		}
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest req, final HttpServletResponse res,
																					final FilterChain chain, final Authentication auth) throws IOException {
		authenticationService.addAuthentication(res, auth.getName());
	}
}
