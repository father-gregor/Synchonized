package com.benlinus92.synchronize.model;

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
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name="PLAYLIST")
public class Playlist {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="video_id")
	private int videoId;
	@Column(name="type")
	private String type;
	@Column(name="title")
	private String title;
	@Column(name="url")
	private String url;
	@Column(name="curr_time")
	private String currTime;
	@Transient
	public MultipartFile file;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name= "room_id")
	private Room room;
	
	public int getVideoId() {
		return videoId;
	}
	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCurrTime() {
		return currTime;
	}
	public void setCurrTime(String currTime) {
		this.currTime = currTime;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
