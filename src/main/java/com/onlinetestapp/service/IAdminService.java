package com.onlinetestapp.service;

import java.util.List;

import com.onlinetestapp.model.Admin;

public interface IAdminService {
	public List<Admin> fetchAdminList();
	public Admin saveAdmin(Admin admin);
	public boolean deleteAdmin(Long adminId);
	public Admin getAdmin(String email, String password);
	public Admin getAdminById(Long adminId);
}
