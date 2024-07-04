package com.example.demo.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
public class SecurityConfiguration {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.httpBasic(HttpBasicConfigurer::disable)
        .csrf(CsrfConfigurer::disable)  //post, put, delete 요청 안먹음
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests((authz)-> authz
				.requestMatchers("/index_admin").hasRole("ADMIN")
				.requestMatchers("/index_member").hasRole("MEMBER")
				.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()//forward 요청은 모두 허용
				.requestMatchers("/auth/**").authenticated()  //url이 /auth/로 시작하면 인증을 요구
				.requestMatchers("/","/all/**", "/join", "/error", "/login" , "/idcheck","/app.js","/favicon.ico","/price.js","/images/**", "/css/**", "/images/**", "/js/**", "/style.css","/notice.js","/webjars/**").permitAll()
						.requestMatchers("/ws/**").permitAll()
				)
		.formLogin((login)->login
				.loginPage("/loginform")  //로그인 폼 url 설정
				.loginProcessingUrl("/login")  //로그인 처리 url
				.usernameParameter("id")		//로그인 페이지에서 id 입력 양식의 이름
				.passwordParameter("pwd")       //로그인 페이지에서 pwd 입력 양식의 이름
				.defaultSuccessUrl("/", true).permitAll()
				.successHandler(new MySuccessHandler())
				.failureHandler(new MyFailureHandler())
		);
		return http.build();
	}
}








