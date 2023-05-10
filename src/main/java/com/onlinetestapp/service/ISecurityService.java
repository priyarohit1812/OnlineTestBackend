package com.onlinetestapp.service;

public interface ISecurityService {
	public String generateHash(String input);
	public String generateJWTToken(Long userId);
	public boolean isJWTTokenExpired(String token);
	public Long parseJWTToken(String token);
}
