package com.benlinus92.synchronize.service;

import java.util.List;

import com.benlinus92.synchronize.model.Playlist;
import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.model.Room;

public interface SynchronizeService {
	boolean saveUser(Profile user);
	Profile findUserByLogin(String login, boolean lazyInitialize);
	void editUserProfile(Profile user, String login);
	void saveRoom(Room room, String userName);
	List<Room> getAllRooms();
	Room findRoomById(int id);
	boolean deleteRoomById(int id, String userName);
	void saveVideo(Playlist video, int roomId);
	List<Playlist> getAllVideos();
	void deleteVideo(int videoId);
}
