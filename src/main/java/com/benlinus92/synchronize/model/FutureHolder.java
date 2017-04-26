package com.benlinus92.synchronize.model;

import java.util.concurrent.Future;

public class FutureHolder {
	private volatile Future<?> future = null;
	private volatile String roomId = "";
	private volatile String videoId = "";
	public FutureHolder() {
		
	}
	public FutureHolder(Future<?> future, String roomId, String videoId) {
		this.future = future;
		this.roomId = roomId;
		this.videoId = videoId;
	}
	public Future<?> getFuture() {
		return future;
	}
	public synchronized void setFuture(Future<?> future) {
		this.future = future;
	}
	public String getRoomId() {
		return roomId;
	}
	public synchronized void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getVideoId() {
		return videoId;
	}
	public synchronized void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
}
