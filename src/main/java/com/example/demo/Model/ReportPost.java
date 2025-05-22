package com.example.demo.Model;

import java.sql.Date;

public class ReportPost {
	private int id_report_peroperty;
	private int id_property;
	private int id_user;
	private String content;
	private String[] file;
	private Date created_at; 
	private Date updated_at;
	private int delete;
	
	public ReportPost() {
		super();
	}

	public ReportPost(int id_report_peroperty, int id_property, int id_user, String content, String[] file,
			Date created_at, Date updated_at) {
		super();
		this.id_report_peroperty = id_report_peroperty;
		this.id_property = id_property;
		this.id_user = id_user;
		this.content = content;
		this.file = file;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}
	
	public ReportPost(int id_report_peroperty, int id_property, int id_user, String content, String[] file) {
		super();
		this.id_report_peroperty = id_report_peroperty;
		this.id_property = id_property;
		this.id_user = id_user;
		this.content = content;
		this.file = file;
	}


	public ReportPost(int id_report_peroperty, int id_property, int id_user, String content, String[] file,
			Date created_at, Date updated_at, int delete) {
		super();
		this.id_report_peroperty = id_report_peroperty;
		this.id_property = id_property;
		this.id_user = id_user;
		this.content = content;
		this.file = file;
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

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public int getId_report_peroperty() {
		return id_report_peroperty;
	}

	public void setId_report_peroperty(int id_report_peroperty) {
		this.id_report_peroperty = id_report_peroperty;
	}

	public int getId_property() {
		return id_property;
	}

	public void setId_property(int id_property) {
		this.id_property = id_property;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getFile() {
		return file;
	}

	public void setFile(String[] file) {
		this.file = file;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}
