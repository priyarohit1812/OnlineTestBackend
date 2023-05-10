package com.onlinetestapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinetestapp.model.Admin;
import com.onlinetestapp.repository.IAdminRepository;

@Service(value = "adminService")
public class AdminService implements IAdminService {
	@Autowired
	private IAdminRepository adminRepository;
	
	@Autowired
	private ISecurityService securityService;
	
	@Override
	public List<Admin> fetchAdminList() {
		return this.adminRepository.findAll();
	}

	@Override
	public Admin saveAdmin(Admin admin) {
		String hashedPassword = this.securityService.generateHash(admin.getPassword());
		admin.setPassword(hashedPassword);
		return this.adminRepository.save(admin);
	}

	@Override
	public boolean deleteAdmin(Long adminId) {
		this.adminRepository.deleteById(adminId);
		return !this.adminRepository.existsById(adminId);
	}

	@Override
	public Admin getAdmin(String email, String password) {
		String hashedPassword = this.securityService.generateHash(password);
		Optional<Admin> optAdmin = this.adminRepository.findByEmailAndPassword(email, hashedPassword);
		if(optAdmin!= null && optAdmin.isPresent()) {
			return optAdmin.get();
		}
		return null;
	}

	@Override
	public Admin getAdminById(Long adminId) {
		return this.adminRepository.findById(adminId).get();
	}
	
}
