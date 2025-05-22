package com.example.demo.Model;

import java.sql.Date;

public class User {
	private int id_user;
	private String username;
	private String password;
	private int status;
	private String name;
	private int gender;
	private Date dob;
	private String email;
	private int user_type;
	private String phone_number;
	private String url_ava_img;
	private String company_name;
	private Date created_at;
	private Date updated_at;
	private int delete;

	public User() {
		super();
	}

	public User(int id_user, String username, String password, int status, String name, int gender, Date dob,
			String email, int user_type, String phone_number, String url_ava_img, String company_name, Date created_at,
			Date updated_at, int delete) {
		super();
		this.id_user = id_user;
		this.username = username;
		this.password = password;
		this.status = status;
		this.name = name;
		this.gender = gender;
		this.dob = dob;
		this.email = email;
		this.user_type = user_type;
		this.phone_number = phone_number;
		this.url_ava_img = url_ava_img;
		this.company_name = company_name;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.delete = delete;
	}

	public User(int id_user, String username, String password, int status, String name, int gender, Date dob,
			String email, int user_type, String phone_number, String url_ava_img, String company_name, Date created_at,
			Date updated_at) {
		super();
		this.id_user = id_user;
		this.username = username;
		this.password = password;
		this.status = status;
		this.name = name;
		this.gender = gender;
		this.dob = dob;
		this.email = email;
		this.user_type = user_type;
		this.phone_number = phone_number;
		this.url_ava_img = url_ava_img;
		this.company_name = company_name;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}



	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public int getDelete() {
		return delete;
	}

	public void setDelete(int delete) {
		this.delete = delete;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUser_type() {
		return user_type;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getUrl_ava_img() {
		return url_ava_img;
	}

	public void setUrl_ava_img(String url_ava_img) {
		this.url_ava_img = url_ava_img;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

}
