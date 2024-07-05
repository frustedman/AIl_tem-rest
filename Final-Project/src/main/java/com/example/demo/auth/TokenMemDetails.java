package com.example.demo.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.user.Member;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TokenMemDetails implements UserDetails {

	/**
	 * 인증에 사용될 정보 vo
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private final Member mem; // 인증에 사용될 id/pwd/type 등의 정보를 갖는 entity를 멤버로 갖는다
	
	public TokenMemDetails(Member mem) { // 생성자로 의존성 주입
		this.mem=mem;
	}
	
	// 인증 객체의 권한 목록 저장 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		
		List<GrantedAuthority> list =new ArrayList<>();
		list.add(new SimpleGrantedAuthority(mem.getType()));
		return list;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return mem.getPwd();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return mem.getId();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
