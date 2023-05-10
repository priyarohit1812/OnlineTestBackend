package com.onlinetestapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinetestapp.model.Result;
import com.onlinetestapp.model.User;
import com.onlinetestapp.model.Responses.BaseResponse;
import com.onlinetestapp.model.Responses.ResultResponse;
import com.onlinetestapp.service.IResultService;
import com.onlinetestapp.service.IUserService;

@RestController
@RequestMapping("/user/result")
@CrossOrigin(origins = "http://localhost:4200")
public class ResultController {
	@Autowired
	private IResultService resultService;
	@Autowired
	private IUserService userService;

	@GetMapping("/list")
	public ResponseEntity<BaseResponse<List<ResultResponse>>> getAllResults(@RequestAttribute("userId") Long userId) {
		BaseResponse<List<ResultResponse>> response = new BaseResponse<List<ResultResponse>>();
		List<ResultResponse> resultList = new ArrayList<>();
		try {
			List<Result> allResults = this.resultService.getResultByUser(userId);
			if (allResults == null || allResults.isEmpty()) {
				response.setMessage("No result found");
			}
			for (Result result : allResults) {
				resultList.add(new ResultResponse(result.getResultId(), result.getQuizId(), result.getMarksObtained(),
						result.getMaxMarks(), result.getIsPass()));
			}
			response.setError(false);
			response.setResponse(resultList);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.setError(true);
			response.setMessage("Could not fetch results. " + e.getMessage());
			response.setResponse(null);
			return ResponseEntity.internalServerError().body(response);
		}
	}	

	@PostMapping("/save")
	public ResponseEntity<BaseResponse<ResultResponse>> saveResult(@RequestAttribute("userId") Long userId,
			@RequestBody Result result) {
		BaseResponse<ResultResponse> response = new BaseResponse<ResultResponse>();
		if (result == null) {
			response.setError(true);
			response.setMessage("Request object is null");
			return ResponseEntity.badRequest().body(response);
		}
		User user = this.userService.getUserById(userId);
		result.setUser(user);
		Result savedResult = this.resultService.saveResult(result);
		if (savedResult == null) {
			response.setError(true);
			response.setMessage("Could not save the requested result");
			return ResponseEntity.internalServerError().body(response);
		}
		response.setError(false);
		response.setMessage("Result saved successfully!");
		response.setResponse(new ResultResponse(savedResult.getResultId(), savedResult.getQuizId(),
				savedResult.getMarksObtained(), savedResult.getMaxMarks(), savedResult.getIsPass()));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{resultId}")
	public ResponseEntity<BaseResponse<ResultResponse>> getResult(@PathVariable Long resultId) {
		BaseResponse<ResultResponse> response = new BaseResponse<ResultResponse>();
		if (resultId <= 0) {
			response.setError(true);
			response.setMessage("Please provide result id to fetch result");
			return ResponseEntity.badRequest().body(response);
		}
		Result result = this.resultService.getResultById(resultId);
		if (result == null) {
			response.setError(true);
			response.setMessage("Could not fetch the requested result");
			return ResponseEntity.internalServerError().body(response);
		}

		response.setError(false);
		response.setMessage("Result found!");
		response.setResponse(new ResultResponse(result.getResultId(), result.getQuizId(), result.getMarksObtained(),
				result.getMaxMarks(), result.getIsPass()));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{userId}/{quizId}")
	public ResponseEntity<BaseResponse<ResultResponse>> getResult(@PathVariable Long userId,
			@PathVariable Long quizId) {
		BaseResponse<ResultResponse> response = new BaseResponse<ResultResponse>();
		if (userId <= 0 || quizId <= 0) {
			response.setError(true);
			response.setMessage("Please provide user id and quiz id to fetch result");
			return ResponseEntity.badRequest().body(response);
		}
		Result result = this.resultService.getResultByUserAndQuiz(userId, quizId);
		if (result == null) {
			response.setError(true);
			response.setMessage("Could not fetch the requested result");
			return ResponseEntity.internalServerError().body(response);
		}

		response.setError(false);
		response.setMessage("Result found!");
		response.setResponse(new ResultResponse(result.getResultId(), result.getQuizId(), result.getMarksObtained(),
				result.getMaxMarks(), result.getIsPass()));
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{resultId}")
	public ResponseEntity<BaseResponse<?>> deleteResult(@PathVariable Long resultId) {
		BaseResponse<?> response = new BaseResponse<>();
		if (resultId <= 0) {
			response.setError(true);
			response.setMessage("Please provide result id to delete result");
			return ResponseEntity.badRequest().body(response);
		}
		boolean isDeleted = this.resultService.deleteResult(resultId);
		if (isDeleted) {
			response.setError(false);
			response.setMessage("Result deleted Succcessfully!");
			return ResponseEntity.ok(response);
		} else {
			response.setError(true);
			response.setMessage("Could not delete the requested result");
			return ResponseEntity.internalServerError().body(response);
		}
	}

}
