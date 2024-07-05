package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.user.Member;
import com.example.demo.user.MemberDao;



@Service  // 인증객체의 username으로 db에서 검색하여 유효한지 확인해주는 기능 제공
public class TokenmemDetailsService implements UserDetailsService {

	@Autowired
	private MemberDao dao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Member m=dao.findById(username).orElseThrow(
				()-> new UsernameNotFoundException("not found"+username)
				);
		
		return new TokenMemDetails(m);
	}

}
