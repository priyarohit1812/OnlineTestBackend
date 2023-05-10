package com.onlinetestapp.service;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service(value = "securityService")
public class SecurityService implements ISecurityService, Serializable {
	private static final long serialVersionUID = -7424929841781943903L;

	public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60; // 24 hours

	@Value("${com.onlinetestapp.salt}")
	private String salt;

	@Value("${jwt.secret}")
	private String secret;

	@Override
	public String generateHash(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.update(this.salt.getBytes(StandardCharsets.UTF_8));
			byte[] hashedInput = Base64.getEncoder().encode(digest.digest(input.getBytes()));
			return new String(hashedInput);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	// generate token for user
	@Override
	public String generateJWTToken(Long userId) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userId.toString());
	}

	// check if the token has expired
	@Override
	public boolean isJWTTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// Get User ID from JWT Token
	@Override
	public Long parseJWTToken(String token) {
		if (!isJWTTokenExpired(token)) {
			String strUserId = getUserIdFromToken(token);
			if (strUserId != null && !strUserId.isBlank())
				return Long.parseLong(strUserId);
		}
		return 0L;
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, this.secret).compact();
	}

	// for retrieving any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// retrieve expiration date from JWT token
	private Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	// retrieve user id from JWT token
	private String getUserIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
}
