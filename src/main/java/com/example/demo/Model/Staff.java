package com.example.demo.Model;

import java.sql.Date;

public class Staff {
	private int id_staff;
	private String username;
	private String password;
	private int status;
	private String name; 
	private int role;
	private String email;
	private String phone_number; 
	private Date dob;
	private String address;
	private String url_ava_img;
	private Date created_at; 
	private Date updated_at;
	private int delete;
	
	public Staff() {
		super();
	}
	

	public Staff(int id_staff, String username, String password, int status, String name, int role, String email,
			String phone_number, Date dob, String address, String url_ava_img, Date created_at, Date updated_at) {
		super();
		this.id_staff = id_staff;
		this.username = username;
		this.password = password;
		this.status = status;
		this.name = name;
		this.role = role;
		this.email = email;
		this.phone_number = phone_number;
		this.dob = dob;
		this.address = address;
		this.url_ava_img = url_ava_img;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	

	public Staff(int id_staff, String username, String password, int status, String name, int role, String email,
			String phone_number, Date dob, String address, String url_ava_img) {
		super();
		this.id_staff = id_staff;
		this.username = username;
		this.password = password;
		this.status = status;
		this.name = name;
		this.role = role;
		this.email = email;
		this.phone_number = phone_number;
		this.dob = dob;
		this.address = address;
		this.url_ava_img = url_ava_img;
	}


	public Staff(int id_staff, String username, String password, int status, String name, int role, String email,
			String phone_number, Date dob, String address, String url_ava_img, Date created_at, Date updated_at,
			int delete) {
		super();
		this.id_staff = id_staff;
		this.username = username;
		this.password = password;
		this.status = status;
		this.name = name;
		this.role = role;
		this.email = email;
		this.phone_number = phone_number;
		this.dob = dob;
		this.address = address;
		this.url_ava_img = url_ava_img;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.delete = delete;
	}


	public int getDelete() {
		return delete;
	}


	public void setDelete(int delete) {
		this.delete = delete;
	}


	public String getUrl_ava_img() {
		return url_ava_img;
	}


	public void setUrl_ava_img(String url_ava_img) {
		this.url_ava_img = url_ava_img;
	}

	public String getPhone_number() {
		return phone_number;
	}


	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getUpdated_at() {
		return updated_at;
	}


	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}


	public int getId_staff() {
		return id_staff;
	}
	public void setId_staff(int id_staff) {
		this.id_staff = id_staff;
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
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	} 
	
}
