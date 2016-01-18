package com.benlinus92.synchronize.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.benlinus92.synchronize.model.Profile;

/**
 * Service that implement UserDetailsService 
 * for only secure user authentification
 * **/
@Service("userAuthSecurityService")
public class UserAuthSecurityService implements UserDetailsService {

	@Autowired
	private SynchronizeService service;
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String login)
			throws UsernameNotFoundException {
		Profile user = service.findUserByLogin(login, false);
		if(user == null)
			throw new UsernameNotFoundException("Username not found");
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		UserDetails userDetails = (UserDetails) new User(user.getLogin(), user.getPassword(), Arrays.asList(authority));
		return userDetails;
	}
}
