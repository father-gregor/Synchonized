package com.benlinus92.synchronize.dao;

import java.util.List;

import com.benlinus92.synchronize.model.Playlist;
import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.model.Room;

public interface SynchronizeDao {
	void saveUser(Profile user);
	boolean isUserUnique(Profile user);
	Profile findUserByLogin(String login, boolean lazyInitialize);
	void saveRoom(Room room);
	List<Room> getAllRooms();
	void deleteRoomById(int id);
	Room findRoomById(int id);
	void saveVideo(Playlist video);
	List<Playlist> getAllVideos();
	void deleteVideoById(int videoId);
}
