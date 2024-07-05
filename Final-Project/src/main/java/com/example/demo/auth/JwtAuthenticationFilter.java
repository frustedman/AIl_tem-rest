package com.example.demo.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// 요청이 올 때마다 헤더에서 토큰을 꺼내 인증 확인
@RequiredArgsConstructor // 파람있는 생성자 자동으로 만들어서 의존성 자동 주입
public class JwtAuthenticationFilter extends GenericFilterBean {
	
	private final TokenProvider provider;
	
	@Override  //filter가 할일 구현
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletResponse hres=(HttpServletResponse) response;
		hres.setHeader("Access-Control-Allow-Origin", "*");
		hres.setHeader("Access-Control-Allow-Credentials", "*");
		hres.setHeader("Access-Control-Allow-Methods", "*");
		hres.setHeader("Access-Control-Allow-Max-Age", "3600");
		hres.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type,Aceept,Authorization");
		hres.setStatus(HttpServletResponse.SC_OK);
		
		String token=provider.resolveToken((HttpServletRequest)request);
		if(token!=null && provider.validateToken(token))
		{
			// 토큰으로 인증하고 그 결과인 authentication 객체 반환
			Authentication authentication=provider.getAuthentication(token);
			
			// 반환된 Authentication 객체를 context에 저장 
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}

}