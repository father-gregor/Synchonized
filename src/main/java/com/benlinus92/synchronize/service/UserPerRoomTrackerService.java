package com.benlinus92.synchronize.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class UserPerRoomTrackerService {
	private ConcurrentHashMap<String, List<ActiveRoomUser>> roomUsersMap = new ConcurrentHashMap<String, List<ActiveRoomUser>>();
	
	public void markUserAccess(String roomId, String sessionId) {
		List<ActiveRoomUser> usersPerRoom = roomUsersMap.get(roomId);
		for(int i = 0; i < usersPerRoom.size(); i++) {
			if(usersPerRoom.get(i).getSessionId().equals(sessionId)) {
				usersPerRoom.get(i).setLastAccess();
				break;
			}
		}
	}
	private class ActiveRoomUser {
		private String sessionId = new String();
		private AtomicLong lastAccess = new AtomicLong(System.nanoTime());
		public ActiveRoomUser(String sessionId) {
			this.sessionId = sessionId;
		}
		private void setLastAccess() {
			this.lastAccess = new AtomicLong(System.nanoTime());
		}
		private long getLastAccess() {
			return this.lastAccess.get();
		}
		private String getSessionId() {
			return sessionId;
		}
	}
}
