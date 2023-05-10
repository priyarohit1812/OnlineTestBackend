package com.onlinetestapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinetestapp.model.Result;
import com.onlinetestapp.repository.IResultRepository;

@Service(value = "resultService")
public class ResultService implements IResultService {
	@Autowired
	private IResultRepository resultRepository;
	@Autowired
	private IUserService userService;
	
	@Override
	public List<Result> fetchResultList() {
		return this.resultRepository.findAll();
	}

	@Override
	public Result saveResult(Result result) {
		return this.resultRepository.save(result);
	}

	@Override
	public boolean deleteResult(Long resultId) {
		this.resultRepository.deleteById(resultId);
		return !this.resultRepository.existsById(resultId);
	}

	@Override
	public Result getResultById(Long resultId) {
		return this.resultRepository.findById(resultId).get();
	}

	@Override
	public Result getResultByUserAndQuiz(Long userId, Long quizId) {
		Optional<Result> optResult = this.resultRepository.getResultByUserAndQuiz(userId, quizId);
		if (optResult == null || optResult.isEmpty()) {
			return null;
		}
		Result result = optResult.get();
		if (result.getUser() == null) {
			result.setUser(this.userService.getUserById(userId));
		}
		return result;
	}

	@Override
	public List<Result> getResultByUser(Long userId) {
		return this.resultRepository.getResultByUser(userId);
	}

}
