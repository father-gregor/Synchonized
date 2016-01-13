package com.benlinus92.synchronize.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.benlinus92.synchronize.dao.SynchronizeDao;
import com.benlinus92.synchronize.model.Profile;

@Service("synchronizedService")
public class SynchronizeServiceImpl implements SynchronizeService {
	@Autowired
	SynchronizeDao dao;
	
	@Override
	public void saveUser(Profile user) {
		dao.saveUser(user);
	}
}
