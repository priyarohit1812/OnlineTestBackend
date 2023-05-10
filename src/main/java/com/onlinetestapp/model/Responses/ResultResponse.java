package com.onlinetestapp.model.Responses;

public class ResultResponse {
	private Long resultId;
	private Long quizId;
	private int marksObtained;
	private int maxMarks;
	private boolean isPass;
	
	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public Long getQuizId() {
		return quizId;
	}

	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}

	public int getMarksObtained() {
		return marksObtained;
	}

	public void setMarksObtained(int marksObtained) {
		this.marksObtained = marksObtained;
	}

	public int getMaxMarks() {
		return maxMarks;
	}

	public void setMaxMarks(int maxMarks) {
		this.maxMarks = maxMarks;
	}

	public boolean getIsPass() {
		return isPass;
	}

	public void setIsPass(boolean isPass) {
		this.isPass = isPass;
	}

	public ResultResponse(Long resultId, Long quizId, int marksObtained, int maxMarks, boolean isPass) {
		this.resultId = resultId;
		this.quizId = quizId;
		this.marksObtained = marksObtained;
		this.maxMarks = maxMarks;
		this.isPass = isPass;
	}

	public ResultResponse() {
		this(0L,0L,0,0,false);
	}
}
