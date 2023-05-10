package com.onlinetestapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.onlinetestapp.model.Result;

public interface IResultRepository extends JpaRepository<Result, Long> {
	@Query(nativeQuery = true, value = "SELECT res.* FROM ota_result res WHERE res.user_id_fk = :userId")
	List<Result> getResultByUser(@Param("userId") Long userId);
	
	@Query(nativeQuery = true, value = "SELECT res.* FROM ota_result res WHERE res.user_id_fk = :userId AND res.quiz_id = :quizId")
	Optional<Result> getResultByUserAndQuiz(@Param("userId") Long userId, @Param("quizId") Long quizId);
}
