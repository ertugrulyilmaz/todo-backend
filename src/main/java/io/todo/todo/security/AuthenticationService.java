package io.todo.todo.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.todo.todo.domain.dto.response.LoginResponse;
import io.todo.todo.domain.dto.response.Profile;
import io.todo.todo.domain.entity.User;
import io.todo.todo.service.UserService;
import io.todo.todo.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Component
@Slf4j
public class AuthenticationService {

	private static final String TOKEN_PREFIX = "Bearer";
	private static final String AUTHORIZATION_HEADER = "Authorization";

	@Value("${auth.jwt.exp-minute}")
	private int jwtExpMinute;

	@Value("${auth.jwt.secret}")
	private String jwtSecret;

	private final UserService userService;
	private final CryptoUtil cryptoUtil;
	private final ObjectMapper mapper;

	public AuthenticationService(final UserService userService, final CryptoUtil cryptoUtil, final ObjectMapper mapper) {
		this.userService = userService;
		this.cryptoUtil = cryptoUtil;
		this.mapper = mapper;
	}

	public void addAuthentication(final HttpServletResponse res, final String userId) throws IOException {
		final PrintWriter out = res.getWriter();
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		final Optional<User> maybeUser = userService.getUserById(userId).join();

		maybeUser.ifPresent(user -> {
			final String JWT = cryptoUtil.generateJWT(jwtSecret, jwtExpMinute, user.getId());

			final Profile profile = new Profile(user.getEmail(), user.getFirstName(), user.getLastName());
			final LoginResponse loginResponse = new LoginResponse(profile, TOKEN_PREFIX + " " + JWT);

			try {
				userService.saveToken(user.getId(), JWT);

				out.print(mapper.writeValueAsString(loginResponse));
			} catch (JsonProcessingException e) {
				log.error("{}", e);
			} finally {
				out.flush();
			}
		});
	}


	public Authentication getAuthentication(final HttpServletRequest request) {
		final String header = request.getHeader(AUTHORIZATION_HEADER);

		if (header != null && header.startsWith(TOKEN_PREFIX)) {
			final String token = header.replace(TOKEN_PREFIX, "").trim();

			try {
				final String userId = cryptoUtil.parseJWT(jwtSecret, token);

				if (!StringUtils.isEmpty(userId)) {
					final Optional<User> user = userService.findTokenBy(userId, token).join();

					if (user.isPresent()) {
						return new UsernamePasswordAuthenticationToken(user.get().getId(), null, emptyList());
					}
				}
			} catch (ExpiredJwtException e) {
				log.error("{}", e);

				return null;
			}
		}

		return null;
	}

}
