package com.example.demo.Model;

import java.sql.Date;

public class StaffRole {
	private int id_staff_role;
	private String role;
	private Date created_at;
	private Date updated_at; 
	private int delete;
	
	public StaffRole() {
		super();
	}
	
	public StaffRole(int id_staff_role, String role) {
		super();
		this.id_staff_role = id_staff_role;
		this.role = role;
	}

	public StaffRole(int id_staff_role, String role, Date created_at, Date updated_at) {
		super();
		this.id_staff_role = id_staff_role;
		this.role = role;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public StaffRole(int id_staff_role, String role, Date created_at, Date updated_at, int delete) {
		super();
		this.id_staff_role = id_staff_role;
		this.role = role;
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

	public int getId_staff_role() {
		return id_staff_role;
	}
	public void setId_staff_role(int id_staff_role) {
		this.id_staff_role = id_staff_role;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	} 
	
	
}
