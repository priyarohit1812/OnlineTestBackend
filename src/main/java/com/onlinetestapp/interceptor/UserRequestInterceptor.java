package com.onlinetestapp.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.onlinetestapp.model.User;
import com.onlinetestapp.service.ISecurityService;
import com.onlinetestapp.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserRequestInterceptor implements HandlerInterceptor {
	@Autowired
	IUserService userService;
	@Autowired
	ISecurityService securityService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || authHeader.isBlank()) {
			response.addHeader("Interceptor", "Authorization not sent");
			return false;
		}

		String token = authHeader.replace("Bearer", "").replace("bearer", "").trim();
		if (token.isEmpty()) {
			response.addHeader("Interceptor", "Token not sent");
			return false;
		}

		if (this.securityService.isJWTTokenExpired(token)) {
			response.addHeader("Interceptor", "Token expired");
			return false;
		}

		Long userId = this.securityService.parseJWTToken(token);
		if (userId == 0) {
			response.addHeader("Interceptor", "Invalid Token");
			return false;
		}
		
		User user = this.userService.getUserById(userId);
		if (user == null) {
			response.addHeader("Interceptor", "Invalid Token");
			return false;
		}
		request.setAttribute("userId", userId);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
