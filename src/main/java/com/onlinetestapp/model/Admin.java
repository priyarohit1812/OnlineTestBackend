package com.onlinetestapp.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ota_user")
@DiscriminatorValue("Admin")
public class Admin extends User {

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(Long userId, String email, String firstName, String lastName, String mobile, String password) {
		super(userId, email, firstName, lastName, mobile, password);
		// TODO Auto-generated constructor stub
	}

}
