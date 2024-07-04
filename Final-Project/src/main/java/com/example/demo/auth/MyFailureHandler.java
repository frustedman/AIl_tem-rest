package com.example.demo.auth;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MyFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		session.setAttribute("msg", "gang");
		setDefaultFailureUrl("/loginform");
		request.setAttribute("msg", "wrong id or password");
		super.onAuthenticationFailure(request, response, exception);
	}
	
}
