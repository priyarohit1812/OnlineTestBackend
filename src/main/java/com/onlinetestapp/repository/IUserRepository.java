package com.onlinetestapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlinetestapp.model.User;

public interface IUserRepository extends JpaRepository<User, Long>  {
	Optional<User> findByEmailAndPassword(String email, String password);
}
