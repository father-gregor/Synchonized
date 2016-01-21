package com.benlinus92.synchronize.model;

public class AjaxVideoTime {
	private String roomId;
	private String videoId;
	private Double currTime;
	
	public AjaxVideoTime() {
		
	}
	public AjaxVideoTime(String roomId, String videoId, Double currTime) {
		this.roomId = roomId;
		this.videoId = videoId;
		this.currTime = currTime;
	}
	
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public Double getCurrTime() {
		return currTime;
	}
	public void setCurrTime(Double currTime) {
		this.currTime = currTime;
	}
}
