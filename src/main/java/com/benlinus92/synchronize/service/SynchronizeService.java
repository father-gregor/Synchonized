package com.benlinus92.synchronize.service;

import com.benlinus92.synchronize.model.Profile;

public interface SynchronizeService {
	boolean saveUser(Profile user);
	Profile findUserByLogin(String login);
}
