package com.benlinus92.synchronize.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="ROOM")
public class Room {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="room_id")
	private int roomId;
	@Size(min=5, max=50)
	@Column(name="title", nullable=false)
	private String title;
	@OneToMany(mappedBy="room", fetch = FetchType.LAZY)
	private List<Playlist> videosList;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private Profile userId; 
	
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Playlist> getVideosList() {
		return videosList;
	}
	public void setVideosList(List<Playlist> videosList) {
		this.videosList = videosList;
	}
	public Profile getUserId() {
		return userId;
	}
	public void setCreatorId(Profile userId) {
		this.userId = userId;
	}
}
