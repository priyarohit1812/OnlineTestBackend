package com.onlinetestapp.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinetestapp.model.User;
import com.onlinetestapp.model.Requests.LoginRequest;
import com.onlinetestapp.model.Responses.BaseResponse;
import com.onlinetestapp.service.ISecurityService;
import com.onlinetestapp.service.IUserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ISecurityService securityService;
	
	@GetMapping("/list")
	public ResponseEntity<BaseResponse<List<User>>> getAllUsers() {
		BaseResponse<List<User>> response = new BaseResponse<List<User>>();
		try {
			List<User> allUsers = this.userService.fetchUserList();
			if (allUsers == null || allUsers.isEmpty()) {
				response.setMessage("No user found");
			}
			response.setError(false);
			response.setResponse(allUsers);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.setError(true);
			response.setMessage("Could not fetch users. " + e.getMessage());
			response.setResponse(null);
			return ResponseEntity.internalServerError().body(response);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<BaseResponse<String>> registerUser(@RequestBody User user) {
		BaseResponse<String> response = new BaseResponse<String>();
		if (user == null) {
			response.setError(true);
			response.setMessage("Request object is null");
			return ResponseEntity.badRequest().body(response);
		}

		User savedUser = this.userService.saveUser(user);
		if (savedUser == null) {
			response.setError(true);
			response.setMessage("Could not register the requested user");
			return ResponseEntity.internalServerError().body(response);

		}

		response.setError(false);
		response.setMessage("User registered successfully!");
		response.setResponse(this.securityService.generateJWTToken(savedUser.getUserId()));
		return ResponseEntity.ok(response);

	}

	@PostMapping("/update")
	public ResponseEntity<BaseResponse<User>> updateUser(@RequestBody User user) {
		BaseResponse<User> response = new BaseResponse<User>();
		if (user == null) {
			response.setError(true);
			response.setMessage("Request object is null");
			return ResponseEntity.badRequest().body(response);
		} else if (user.getUserId() <= 0) {
			response.setError(true);
			response.setMessage("Please provide user id to update");
			return ResponseEntity.badRequest().body(response);
		}

		User savedUser = this.userService.saveUser(user);
		if (savedUser == null) {
			response.setError(true);
			response.setMessage("Could not update the requested user");
			return ResponseEntity.internalServerError().body(response);

		}

		response.setError(false);
		response.setMessage("User updated successfully!");
		response.setResponse(savedUser);
		return ResponseEntity.ok(response);

	}
	
	@PostMapping("/login")
	public ResponseEntity<BaseResponse<String>> saveUser(@RequestBody LoginRequest request) {
		BaseResponse<String> response = new BaseResponse<String>();
		if (request == null) {
			response.setError(true);
			response.setMessage("Request object is null");
			return ResponseEntity.badRequest().body(response);
		}
		User user = this.userService.getUser(request.getUsername(), request.getPassword());
		if (user == null) {
			response.setError(true);
			response.setMessage("Invalid username or password");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		} 
		response.setError(false);
		response.setMessage("User logged in successfully!");
		response.setResponse(this.securityService.generateJWTToken(user.getUserId()));
		return  ResponseEntity.ok(response);		
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<BaseResponse<User>> getUser(@PathVariable Long userId) {
		BaseResponse<User> response = new BaseResponse<User>();
		if (userId <= 0) {
			response.setError(true);
			response.setMessage("Please provide user id to fetch user");
			return ResponseEntity.badRequest().body(response);
		}
		User user = this.userService.getUserById(userId);
		if (user == null) {
			response.setError(true);
			response.setMessage("Could not fetch the requested user");
			return ResponseEntity.internalServerError().body(response);
		}
		
		response.setError(false);
		response.setMessage("User found!");
		response.setResponse(user);
		return  ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<BaseResponse<?>> deleteUser(@PathVariable Long userId) {
		BaseResponse<?> response = new BaseResponse<>();
		if (userId <= 0) {
			response.setError(true);
			response.setMessage("Please provide user id to delete user");
			return ResponseEntity.badRequest().body(response);
		}
		boolean isDeleted = this.userService.deleteUser(userId);
		if (isDeleted) {
			response.setError(false);
			response.setMessage("User deleted Succcessfully!");
			return  ResponseEntity.ok(response);
		} else {
			response.setError(true);
			response.setMessage("Could not delete the requested user");
			return ResponseEntity.internalServerError().body(response);
		}
	}
	
}
