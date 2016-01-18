package com.benlinus92.synchronize.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.benlinus92.synchronize.dao.SynchronizeDao;
import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.model.Room;

@Service("synchronizedService")
@Transactional
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
	public Profile findUserByLogin(String login, boolean lazyInitialize) {
		return dao.findUserByLogin(login, lazyInitialize);
	}
	@Override
	public void editUserProfile(Profile editedUser, String login) {
		Profile oldUser = dao.findUserByLogin(login, false);
		if(oldUser != null) {
			oldUser.setPassword(BCrypt.hashpw(editedUser.getPassword(), BCrypt.gensalt()));
			oldUser.setEmail(editedUser.getEmail());
		}
	}
	@Override
	public void saveRoom(Room room, String userName) {
		Profile user = dao.findUserByLogin(userName, false);
		room.setUserId(user);
		dao.saveRoom(room);
	}

	@Override
	public List<Room> getAllRooms() {
		return dao.getAllRooms();
	}
	@Override
	public boolean deleteRoomById(int id, String userName) {
		if(dao.findRoomById(id).getUserId().getLogin().equals(userName)) {
			dao.deleteRoomById(id);
			return true;
		}
		return false;
	}
}
