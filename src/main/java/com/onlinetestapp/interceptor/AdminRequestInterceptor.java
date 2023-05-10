package com.onlinetestapp.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.onlinetestapp.model.Admin;
import com.onlinetestapp.service.IAdminService;
import com.onlinetestapp.service.ISecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminRequestInterceptor implements HandlerInterceptor {

	@Autowired
	private IAdminService adminService;
	
	@Autowired
	private ISecurityService securityService;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	throws Exception{
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
		
		Long adminId = this.securityService.parseJWTToken(token);
		if (adminId==0) {
			response.addHeader("Interceptor", "Invalid token");
			return false;
		}
		
		Admin admin = this.adminService.getAdminById(adminId);
		if (admin==null) {
			response.addHeader("Interceptor", "Invalid token");
			return false;
		}
		
		request.setAttribute("adminId", adminId);
		return true;
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
