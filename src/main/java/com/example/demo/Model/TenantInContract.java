package com.example.demo.Model;

import java.sql.Date;

public class TenantInContract {
	private int id_tenant;
	private String username;
	private String name;
	private String email;
	private String phone_number;
	private String id_card;
	private int gender;
	private Date dob;
	public int getId_tenant() {
		return id_tenant;
	}
	public void setId_tenant(int id_tenant) {
		this.id_tenant = id_tenant;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
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
	public TenantInContract(int id_tenant, String username, String name, String email, String phone_number,
			String id_card, int gender, Date dob) {
		super();
		this.id_tenant = id_tenant;
		this.username = username;
		this.name = name;
		this.email = email;
		this.phone_number = phone_number;
		this.id_card = id_card;
		this.gender = gender;
		this.dob = dob;
	}
	public TenantInContract() {
		super();
	}
	
	
}
