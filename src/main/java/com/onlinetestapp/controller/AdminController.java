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

import com.onlinetestapp.model.Admin;
import com.onlinetestapp.model.Requests.LoginRequest;
import com.onlinetestapp.model.Responses.BaseResponse;
import com.onlinetestapp.service.AdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/list")
	public ResponseEntity<BaseResponse<List<Admin>>> getAllAdmins() {
		BaseResponse<List<Admin>> response = new BaseResponse<List<Admin>>();
		try {
			List<Admin> allAdmins = this.adminService.fetchAdminList();
			if (allAdmins == null || allAdmins.isEmpty()) {
				response.setMessage("No admin found");
			}
			response.setError(false);
			response.setResponse(allAdmins);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.setError(true);
			response.setMessage("Could not fetch admins. " + e.getMessage());
			response.setResponse(null);
			return ResponseEntity.internalServerError().body(response);
		}
	}
	
	@PostMapping("/save")
	public ResponseEntity<BaseResponse<Admin>> saveAdmin(@RequestBody Admin admin) {
		BaseResponse<Admin> response = new BaseResponse<Admin>();
		if (admin == null) {
			response.setError(true);
			response.setMessage("Request object is null");
			return ResponseEntity.badRequest().body(response);
		}
		Admin savedAdmin = this.adminService.saveAdmin(admin);
		if (savedAdmin == null) {
			response.setError(true);
			response.setMessage("Could not save the requested admin");
			return ResponseEntity.internalServerError().body(response);
		} 
		response.setError(false);
		response.setMessage("Admin saved successfully!");
		response.setResponse(savedAdmin);
		return  ResponseEntity.ok(response);		
	}
	
	@PostMapping("/login")
	public ResponseEntity<BaseResponse<Admin>> saveAdmin(@RequestBody LoginRequest request) {
		BaseResponse<Admin> response = new BaseResponse<Admin>();
		if (request == null) {
			response.setError(true);
			response.setMessage("Request object is null");
			return ResponseEntity.badRequest().body(response);
		}
		Admin admin = this.adminService.getAdmin(request.getUsername(), request.getPassword());
		if (admin == null) {
			response.setError(true);
			response.setMessage("Invalid username or password");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		} 
		response.setError(false);
		response.setMessage("Admin logged in successfully!");
		response.setResponse(admin);
		return  ResponseEntity.ok(response);		
	}
	
	@GetMapping("/{adminId}")
	public ResponseEntity<BaseResponse<Admin>> getAdmin(@PathVariable Long adminId) {
		BaseResponse<Admin> response = new BaseResponse<Admin>();
		if (adminId <= 0) {
			response.setError(true);
			response.setMessage("Please provide admin id to fetch admin");
			return ResponseEntity.badRequest().body(response);
		}
		Admin admin = this.adminService.getAdminById(adminId);
		if (admin == null) {
			response.setError(true);
			response.setMessage("Could not fetch the requested admin");
			return ResponseEntity.internalServerError().body(response);
		}
		
		response.setError(false);
		response.setMessage("Admin found!");
		response.setResponse(admin);
		return  ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{adminId}")
	public ResponseEntity<BaseResponse<?>> deleteAdmin(@PathVariable Long adminId) {
		BaseResponse<?> response = new BaseResponse<>();
		if (adminId <= 0) {
			response.setError(true);
			response.setMessage("Please provide admin id to delete admin");
			return ResponseEntity.badRequest().body(response);
		}
		boolean isDeleted = this.adminService.deleteAdmin(adminId);
		if (isDeleted) {
			response.setError(false);
			response.setMessage("Admin deleted Succcessfully!");
			return  ResponseEntity.ok(response);
		} else {
			response.setError(true);
			response.setMessage("Could not delete the requested admin");
			return ResponseEntity.internalServerError().body(response);
		}
	}
	
}
