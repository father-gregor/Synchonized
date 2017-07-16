package com.benlinus92.synchronize.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserPerRoomTrackerService {
	@Autowired
	private SynchronizeService mainService;
	@Autowired
	private SimpMessagingTemplate simp;
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
			mainService.stopVideoTimeCountingThread(roomId);
		}
	}
	public void markUserLastAccess(String roomId, String sessionId) {
		List<ActiveRoomUser> usersPerRoom = null;
		if((usersPerRoom = roomUsersMap.get(roomId)) == null) {
			addActiveUser(roomId, sessionId);
			usersPerRoom = roomUsersMap.get(roomId);
		}
		for(int i = 0; i < usersPerRoom.size(); i++) {
			if(usersPerRoom.get(i).getSessionId().equals(sessionId)) {
				long prevAccess = usersPerRoom.get(i).getLastAccess();
				if((usersPerRoom.get(i).setLastAccess() - prevAccess) > 8000)
					removeUser(roomId, sessionId);
				break;
			}
		}
	}
	public void checkActiveUsers() {
		for(Entry<String, List<ActiveRoomUser>> roomEntry: roomUsersMap.entrySet()) {
			List<ActiveRoomUser> userList = roomEntry.getValue();
			for(int i = 0; i < userList.size(); i++) {
				long now = convertToMillis(System.nanoTime());
				if((now - userList.get(i).getLastAccess()) > 8000) {
					removeUser(roomEntry.getKey(), userList.get(i).getSessionId());
				}
			}
		}
	}
	@Scheduled(fixedDelay=5000)
	private void checkUsers() {
		checkActiveUsers();
		simp.convertAndSend("/topic/alive", "");
	}
	private static long convertToMillis(long nanos) {
		return nanos / 1000000;
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
