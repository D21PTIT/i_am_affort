package com.example.demo.Model;

import java.sql.Date;

public class Tenant {
	private int id_tenant;
	private String username;
	private int id_user;
	private String name;
	private String email;
	private String phone_number;
	private String id_card;
	private int gender;
	private Date dob;
	private String note;
	private int rate;
	private Date created_at;
	private Date updated_at;
	private int id_owner;

	public String getId_card() {
		return id_card;
	}

	public void setId_card(String id_card) {
		this.id_card = id_card;
	}

	public int getId_owner() {
		return id_owner;
	}

	public void setId_owner(int id_owner) {
		this.id_owner = id_owner;
	}

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

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
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

	public Tenant() {
		super();
	}

	public Tenant(int id_tenant, String username, int id_user, String name, String email, String phone_number,String id_card,
			int gender, Date dob, String note, int rate, Date created_at, Date updated_at, int id_owner) {
		super();
		this.id_tenant = id_tenant;
		this.username = username;
		this.id_user = id_user;
		this.name = name;
		this.email = email;
		this.phone_number = phone_number;
		this.id_card = id_card;
		this.gender = gender;
		this.dob = dob;
		this.note = note;
		this.rate = rate;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.id_owner = id_owner;
	}

	public Tenant(int id_tenant, String username, String name, String email, String phone_number, String id_card,
			int gender, Date dob) {
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

}
