package com.example.demo.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Notification {
	private int id_notification;
	private String label;
	private String content;
	private String path;
	private int id_owner;
	private List<TenantInContract> tenant_list = new ArrayList<TenantInContract>();
	private int status;
	private Date created_date;

	public int getId_notification() {
		return id_notification;
	}

	public void setId_notification(int id_notification) {
		this.id_notification = id_notification;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getId_owner() {
		return id_owner;
	}

	public void setId_owner(int id_owner) {
		this.id_owner = id_owner;
	}

	public List<TenantInContract> getTenant_list() {
		return tenant_list;
	}

	public void setTenant_list(List<TenantInContract> tenant_list) {
		this.tenant_list = tenant_list;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Notification(int id_notification, String label, String content, String path, int id_owner,
			List<TenantInContract> tenant_list, int status, Date created_date) {
		super();
		this.id_notification = id_notification;
		this.label = label;
		this.content = content;
		this.path = path;
		this.id_owner = id_owner;
		this.tenant_list = tenant_list;
		this.status = status;
		this.created_date = created_date;
	}

	public Notification() {
		super();
	}

}
