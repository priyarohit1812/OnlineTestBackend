package com.onlinetestapp.service;

import java.util.List;

import com.onlinetestapp.model.Result;

public interface IResultService {
	public List<Result> fetchResultList();
	public Result saveResult(Result result);
	public boolean deleteResult(Long resultId);
	public Result getResultById(Long resultId);
	public Result getResultByUserAndQuiz(Long userId, Long quizId);
	public List<Result> getResultByUser(Long userId);
}
