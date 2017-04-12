package com.benlinus92.synchronize.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="WAITING_USER")
public class WaitingUser {
	@Id
	@Column(name="session_id")
	private String sessionId;
	@Column(name="login")
	private String login;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="room_id")
	private Room room;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="video_id")
	private Playlist video;
	
	public WaitingUser() {
		
	}
	public WaitingUser(String sessionId, String login, Room room, Playlist video) {
		this.sessionId = sessionId;
		this.login = login;
		this.room = room;
		this.video = video;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public Playlist getVideo() {
		return video;
	}
	public void setVideo(Playlist video_id) {
		this.video = video_id;
	}
}
