package com.benlinus92.synchronize.dao;

import com.benlinus92.synchronize.model.Profile;

public interface SynchronizeDao {
	void saveUser(Profile user);
	boolean isUserUnique(Profile user);
	Profile findUserByLogin(String login);
}
