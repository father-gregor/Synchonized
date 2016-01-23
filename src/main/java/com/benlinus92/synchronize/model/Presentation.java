package com.benlinus92.synchronize.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name="PRESENTATION")
public class Presentation {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="present_id")
	private int presentId;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="room_id")
	private Room room;
	@Column(name="title")
	private String title;
	@Column(name="url")
	private String url;
	@Column(name="curr_slide")
	private String currSlide;
	
	public int getPresentId() {
		return presentId;
	}
	public void setPresentId(int presentId) {
		this.presentId = presentId;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoomId(Room room) {
		this.room = room;
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
	public String getCurrSlide() {
		return currSlide;
	}
	public void setCurrSlide(String currSlide) {
		this.currSlide = currSlide;
	}
	
	
}
