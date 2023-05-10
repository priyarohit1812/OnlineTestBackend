package com.onlinetestapp.model.Responses;

public class BaseResponse<T> {
	private boolean isError;
	private String message;
	private T response;
	
	public BaseResponse() {
		this(false, "", null);
	}

	public BaseResponse(boolean isError, String message, T response) {
		this.isError = isError;
		this.message = message;
		this.response = response;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "BaseResponse [isError=" + isError + ", message=" + message + ", response=" + response + "]";
	}
}
