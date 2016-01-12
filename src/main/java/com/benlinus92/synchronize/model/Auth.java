package com.benlinus92.synchronize.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="AUTH")
public class Auth {
	public String login;
	public String password;
	
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
}
