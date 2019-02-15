package io.todo.todo.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;
import static java.lang.System.currentTimeMillis;

@Slf4j
@Profile("!test")
@Component
public class CryptoUtil {

	private final static char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f'};

	public String getSalt() {
		final Random r = new SecureRandom();
		byte[] salt = new byte[32];
		r.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	public String encodePassword(String rawPass, String salt) {
		String saltedPass = mergePasswordAndSalt(rawPass, salt);

		MessageDigest messageDigest = getMessageDigest();
		messageDigest.update(saltedPass.getBytes(StandardCharsets.UTF_8));

		byte[] digest = messageDigest.digest();

		return new String(encode(digest));
	}

	private String mergePasswordAndSalt(String password, String salt) {
		if (password == null) {
			password = "";
		}

		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + salt;
		}
	}

	private MessageDigest getMessageDigest() throws IllegalArgumentException {
		try {
			return MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm [SHA-256]");
		}
	}

	private char[] encode(byte[] bytes) {
		final int nBytes = bytes.length;
		char[] result = new char[2 * nBytes];

		int j = 0;
		for (byte aByte : bytes) {
			// Char for top 4 bits
			result[j++] = HEX[(0xF0 & aByte) >>> 4];
			// Bottom 4
			result[j++] = HEX[(0x0F & aByte)];
		}

		return result;
	}

	public String encodeUrl(String str) {
		try {
			if (!StringUtils.isEmpty(str)) {
				return URLEncoder.encode(str, "UTF-8");
			} else {
				throw new RuntimeException("str can not be null or empty.");
			}
		} catch (UnsupportedEncodingException ex) {
			log.error("{}", ex);
			throw new RuntimeException(ex);
		}
	}

	public boolean isValidToken(String token, String secretKey) {
		return !StringUtils.isEmpty(parseJWT(secretKey, token));
	}

	public String generateJWT(String secretKey, int expMinute, String userId) {
		Date expireTime = new Date(currentTimeMillis() + expMinute * 60 * 1000);
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		return builder().setSubject(userId).setExpiration(expireTime).signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String parseJWT(String secretKey, String jwtToken) {
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		return parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody().getSubject();
	}

}