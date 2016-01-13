package com.benlinus92.synchronize.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="PROFILE")
public class Profile {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_id")
	private int userId;
	@NotNull
	@Column(name="login", nullable=false)
	private String login;
	@NotNull
	@Column(name="password", nullable=false)
	private String password;
	@NotNull
	@Column(name="email")
	private String email;
	@NotNull
	@Transient
	private String passwordConfirm;
	@OneToMany(mappedBy="userId", fetch = FetchType.LAZY)
	private List<Room> roomsList;
	public Profile() { }
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getLogin() {
		return this.login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Room> getRoomsList() {
		return roomsList;
	}
	public void setRoomsList(List<Room> roomsList) {
		this.roomsList = roomsList;
	}
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
}
