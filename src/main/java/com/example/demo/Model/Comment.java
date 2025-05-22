package com.example.demo.Model;

import java.sql.Date;

public class Comment {
	private int id_comment;
	private int id_user;
	private String user_name;
	private int id_post;
	private String content;
	private Date created_at;
	private Date updated_at;
	private int delete;

	public Comment() {
		super();
	}

	public Comment(int id_comment, int id_user, String user_name, int id_post, String content, Date created_at,
			Date updated_at) {
		super();
		this.id_comment = id_comment;
		this.id_user = id_user;
		this.user_name = user_name;
		this.id_post = id_post;
		this.content = content;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public Comment(int id_comment, int id_user, String user_name, int id_post, String content, Date created_at,
			Date updated_at, int delete) {
		super();
		this.id_comment = id_comment;
		this.id_user = id_user;
		this.user_name = user_name;
		this.id_post = id_post;
		this.content = content;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.delete = delete;
	}

	public Comment(int id_comment, int id_user, String user_name, int id_post, String content) {
		super();
		this.id_comment = id_comment;
		this.id_user = id_user;
		this.user_name = user_name;
		this.id_post = id_post;
		this.content = content;
	}

	public Comment(int id_comment, int id_user, String user_name, String content, Date created_at, Date updated_at) {
		super();
		this.id_comment = id_comment;
		this.id_user = id_user;
		this.user_name = user_name;
		this.content = content;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public Comment(int id_comment, int id_post, String content) {
		super();
		this.id_comment = id_comment;
		this.id_post = id_post;
		this.content = content;
	}

	public int getDelete() {
		return delete;
	}

	public void setDelete(int delete) {
		this.delete = delete;
	}

	public int getId_comment() {
		return id_comment;
	}

	public void setId_comment(int id_comment) {
		this.id_comment = id_comment;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public int getId_post() {
		return id_post;
	}

	public void setId_post(int id_post) {
		this.id_post = id_post;
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

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

}
