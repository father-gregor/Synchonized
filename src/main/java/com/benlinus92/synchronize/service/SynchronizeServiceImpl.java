package com.benlinus92.synchronize.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.benlinus92.synchronize.dao.SynchronizeDao;
import com.benlinus92.synchronize.model.Profile;

@Service("synchronizedService")
public class SynchronizeServiceImpl implements SynchronizeService {
	@Autowired
	SynchronizeDao dao;
	
	@Override
	public boolean saveUser(Profile user) {
		if(dao.isUserUnique(user)) {
			String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
			user.setPassword(hashedPassword);
			dao.saveUser(user);
		} else 
			return false;
		return true;
	}

	@Override
	public Profile findUserByLogin(String login) {
		return dao.findUserByLogin(login);
	}
}
