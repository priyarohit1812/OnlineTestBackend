package com.onlinetestapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.onlinetestapp.model.Admin;
import com.onlinetestapp.service.IAdminService;

@Component
public class DataLoader implements ApplicationRunner {
	@Autowired
	IAdminService adminService;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<Admin> allAdmins = this.adminService.fetchAdminList();
		if (allAdmins == null || allAdmins.isEmpty()) {
			Admin firstAdmin = new Admin(0L,"admin@onlinetest.com","Administrator","","9876543210","OnlineTest@2023");
			this.adminService.saveAdmin(firstAdmin);
		}

	}

}
