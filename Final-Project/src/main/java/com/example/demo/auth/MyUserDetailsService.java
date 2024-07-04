package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.user.MemberDao;
import com.example.demo.user.Member;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private MemberDao dao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Member u = dao.findById(username).orElseThrow(
				() -> new UsernameNotFoundException("not found id:" + username));
		System.out.println("security service: " + u);
		return new MyUserDetails(u);
	}

}
