package com.benlinus92.synchronize.service;

import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.model.Room;

public interface SynchronizeService {
	boolean saveUser(Profile user);
	Profile findUserByLogin(String login);
	void saveRoom(Room room, String userName);
}
