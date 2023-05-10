package com.onlinetestapp.service;

import java.util.List;

import com.onlinetestapp.model.User;

public interface IUserService {
	public List<User> fetchUserList();
	public User saveUser(User user);
	public boolean deleteUser(Long userId);
	public User getUser(String email, String password);
	public User getUserById(Long userId);
	
}
