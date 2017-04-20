package com.benlinus92.synchronize.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.benlinus92.synchronize.dao.SynchronizeDao;
import com.benlinus92.synchronize.model.Playlist;
import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.model.Room;

@Service("synchronizedService")
@Transactional
public class SynchronizeServiceImpl implements SynchronizeService {
	@Autowired
	SynchronizeDao dao;
	@Autowired
	TaskScheduler taskScheduler;
	
	private ConcurrentHashMap<String, List<String>> roomClientsMap = new ConcurrentHashMap<String, List<String>>();
	
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
	public Room findRoomById(int id) {
		return dao.findRoomById(id);
	}
	@Override
	public boolean deleteRoomById(int id, String userName) {
		Room room = dao.findRoomById(id);
		if(room.getUserId().getLogin().equals(userName)) {
			List<Playlist> videos = room.getVideosList();
			for(Playlist video: videos)
				dao.deleteVideoById(video.getVideoId());
			dao.deleteRoomById(id);
			return true;
		}
		return false;
	}
	@Override
	public void saveVideo(Playlist video, int roomId) {
		Room room = dao.findRoomById(roomId);
		video.setRoom(room);
		dao.saveVideo(video);
	}
	@Override
	public List<Playlist> getAllVideos() {
		return dao.getAllVideos();
	}
	@Override
	public Playlist findVideoById(String videoId) {
		return dao.findVideoById(videoId);
	}
	@Override
	public void deleteVideo(int videoId) {
		dao.deleteVideoById(videoId);
	}
	@Override
	public void updateVideo(int videoId, String currTime) {
		dao.updateVideoTime(videoId, currTime);
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Playlist> getVideoListFromRoom(int roomId) {
		List<Playlist> list = dao.findRoomById(roomId).getVideosList();;
		return list;
	}

	@Override
	public boolean isRoomOpened(String roomId, String simpSessionId, String videoId) {
		if(!roomClientsMap.containsKey(roomId)) {
			dao.findVideoById(videoId);
			startVideoTimeCountingThread();
			return false;
		}
		return true;
	}
	private void startVideoTimeCountingThread() {
		//da
		taskScheduler.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Task Executed in " + Thread.currentThread().getName());
			}
		}, 5000);
	}
	@Override
	public void addUserToRoomMap(String roomId, String simpSessionId) {
		if(roomClientsMap.containsKey(roomId)) {
			roomClientsMap.get(roomId).add(simpSessionId);
		} else {
			roomClientsMap.put(roomId, new ArrayList<String>());
			roomClientsMap.get(roomId).add(simpSessionId);
		}
		System.out.println("ROOM " + roomId + " size: " + roomClientsMap.get(roomId).size());
	}
	@Override
	public void removeUserFromRoomMap(String simpSessionId) {
		for(Entry<String, List<String>> entry: roomClientsMap.entrySet()) {
			if(entry.getValue().contains(simpSessionId)) {
				String roomId = entry.getKey();
				int clients = roomClientsMap.get(roomId).size();
				System.out.println("ROOM " + roomId + " size: " + clients);
				if(clients - 1 <= 0)
					roomClientsMap.remove(roomId);
				else
					roomClientsMap.get(roomId).remove(simpSessionId);
				break;
			}
		}
	}
}
