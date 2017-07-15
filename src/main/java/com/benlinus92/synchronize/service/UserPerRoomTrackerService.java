package com.benlinus92.synchronize.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class UserPerRoomTrackerService {
	private ConcurrentHashMap<String, List<ActiveRoomUser>> roomUsersMap = new ConcurrentHashMap<String, List<ActiveRoomUser>>();
	
	public void addActiveUser(String roomId, String sessionId) {
		List<ActiveRoomUser> usersList = null;
		if((usersList = roomUsersMap.get(roomId)) == null) {
			usersList = new ArrayList<ActiveRoomUser>();
		}
		usersList.add(new ActiveRoomUser(sessionId));
		roomUsersMap.put(roomId, usersList);
	}
	public void removeUser(String roomId, String sessionId) {
		roomUsersMap.get(roomId).remove(new ActiveRoomUser(sessionId));
		if(roomUsersMap.get(roomId).size() == 0) {
			roomUsersMap.remove(roomId);
		}
	}
	public void markUserAccess(String roomId, String sessionId) {
		List<ActiveRoomUser> usersPerRoom = null;
		if((usersPerRoom = roomUsersMap.get(roomId)) == null) {
			addActiveUser(roomId, sessionId);
			usersPerRoom = roomUsersMap.get(roomId);
		}
		for(int i = 0; i < usersPerRoom.size(); i++) {
			if(usersPerRoom.get(i).getSessionId().equals(sessionId)) {
				long prevAccess = usersPerRoom.get(i).getLastAccess();
				System.out.println("ACCESS SUB " + (usersPerRoom.get(i).setLastAccess() - prevAccess));
				if((usersPerRoom.get(i).setLastAccess() - prevAccess) > 8000)
					removeUser(roomId, sessionId);
				break;
			}
		}
	}
	private class ActiveRoomUser {
		private String sessionId = new String();
		private AtomicLong lastAccess = new AtomicLong(convertToMillis(System.nanoTime()));
		public ActiveRoomUser(String sessionId) {
			this.sessionId = sessionId;
		}
		private long setLastAccess() {
			lastAccess = new AtomicLong(convertToMillis(System.nanoTime()));
			System.out.println("LAST ACCESS FROM " + sessionId + " WAS AT " + lastAccess);
			return lastAccess.get();
		}
		private long getLastAccess() {
			return lastAccess.get();
		}
		private String getSessionId() {
			return sessionId;
		}
		private long convertToMillis(long nanos) {
			return nanos / 1000000;
		}
		@Override
		public boolean equals(Object obj) {
			if(this == obj)
				return true;
			if(!(obj instanceof ActiveRoomUser))
				return false;
			final ActiveRoomUser user = (ActiveRoomUser) obj;
			if((this.sessionId == null) ? (user.sessionId != null) : !this.sessionId.equals(user.sessionId)) {
				return false;
			}
			System.out.println("Deleted - " + this.sessionId);//_________optional
			return true;
		}
	}
}
