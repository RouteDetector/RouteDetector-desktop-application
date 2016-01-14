package com.routedetector.Client;

import java.io.Serializable;

public class LoginInfo implements Serializable{
	/**
	 * Class holds username, company's email, and user's password in fields.
	 *
	 * @author  Danijel Sudimac
	 */
	private static final long serialVersionUID = 1L;
	private String user, email;
	private char[] password;
	
	public LoginInfo(String user, String email, char[] password) {
		this.user = user;
		this.email = email;
		this.password = password;
	}
	public char[] getPassword() {
		return password;
	}
	public void setPassword(char[] password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
}
