package com.example.demo.Model;

import java.sql.Date;

public class Contact {
	private int id_contact;
	private String username;
	private String user_email;
	private String content;
	private Date created_at;
	private Date updated_at;
	private int delete;
	private int status;
	private int id_owner;
	private int id_tenant;
	private int id_post;
	private String header;
	private String phone_number;

	public Contact() {
		super();
	}

	public Contact(int id_contact, String username, String user_email, String content, Date created_at, Date updated_at,
			int delete, int status, int id_owner, int id_tenant, int id_post, String header, String phone_number) {
		super();
		this.id_contact = id_contact;
		this.username = username;
		this.user_email = user_email;
		this.content = content;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.delete = delete;
		this.status = status;
		this.id_owner = id_owner;
		this.id_tenant = id_tenant;
		this.id_post = id_post;
		this.header = header;
		this.phone_number = phone_number;
	}

	public int getId_contact() {
		return id_contact;
	}

	public void setId_contact(int id_contact) {
		this.id_contact = id_contact;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public int getDelete() {
		return delete;
	}

	public void setDelete(int delete) {
		this.delete = delete;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getId_post() {
		return id_post;
	}

	public void setId_post(int id_post) {
		this.id_post = id_post;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

}
