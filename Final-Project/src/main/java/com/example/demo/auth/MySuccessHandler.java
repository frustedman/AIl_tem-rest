package com.example.demo.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//인증 성공시 실행될 핸들러
public class MySuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String loginId = (String) session.getAttribute("loginId");
		
		
		
		
		String type = "";
		if (loginId == null) {
			session.setAttribute("loginId", authentication.getName());// 인증한 사람 id

			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
				type = "admin";
			} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MEMBER"))) {
				type = "member";
			}
			session.setAttribute("type", type);
		}
		
		//인증 후 클라이언트가 요청한 페이지로 이동
		RequestCache requestCache=new HttpSessionRequestCache();
		SavedRequest saveRequest=requestCache.getRequest(request, response);
		String path=saveRequest.getRedirectUrl().split("8081")[1].split("&")[0];
		if(path.startsWith("/auth/index_")) {
			path="/auth/index_"+type;
		}
		request.getRequestDispatcher(path).forward(request, response);
	}

}
