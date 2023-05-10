package com.onlinetestapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinetestapp.model.Result;
import com.onlinetestapp.model.User;
import com.onlinetestapp.repository.IUserRepository;

@Service(value = "userService")
public class UserService implements IUserService {
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IResultService resultService;
	
	@Autowired
	private ISecurityService securityService;
	
	@Override
	public List<User> fetchUserList() {
		try {
			return this.userRepository.findAll();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public User saveUser(User user) {
		String hashedPassword = this.securityService.generateHash(user.getPassword());
		user.setPassword(hashedPassword);
		return this.userRepository.save(user);
	}

	@Override
	public boolean deleteUser(Long userId) {
		if (this.deleteResultsForUser(userId)) {
			this.userRepository.deleteById(userId);
		}
		return !this.userRepository.existsById(userId);
	}

	@Override
	public User getUser(String email, String password) {
		String hashedPassword = this.securityService.generateHash(password);
		Optional<User> optUser = this.userRepository.findByEmailAndPassword(email, hashedPassword);
		if(optUser!= null && optUser.isPresent()) {
			return optUser.get();
		}
		return null;
	}

	@Override
	public User getUserById(Long userId) {
		return this.userRepository.findById(userId).get();
	}
	
	private boolean deleteResultsForUser(Long userId) {
		boolean isDeleted = true;
		List<Result> results = this.resultService.getResultByUser(userId);
		
		if (results != null && !results.isEmpty()) {
			for (Result result : results) {
				isDeleted = isDeleted && this.resultService.deleteResult(result.getResultId());
			} 
		}
		
		return isDeleted;
	}
}
