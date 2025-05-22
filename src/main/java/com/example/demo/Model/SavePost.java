package com.example.demo.Model;

import java.sql.Date;

public class SavePost {
	private int id_save_post;
	private int id_tenant;
	private int id_post;
	private Date created_date;
	private String header;
	private String province;
	private String district;
	private float other_service;

	public int getId_save_post() {
		return id_save_post;
	}

	public void setId_save_post(int id_save_post) {
		this.id_save_post = id_save_post;
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

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public float getOther_service() {
		return other_service;
	}

	public void setOther_service(float other_service) {
		this.other_service = other_service;
	}

	public SavePost(int id_save_post, int id_tenant, int id_post, Date created_date, String header, String province,
			String district, float other_service) {
		super();
		this.id_save_post = id_save_post;
		this.id_tenant = id_tenant;
		this.id_post = id_post;
		this.created_date = created_date;
		this.header = header;
		this.province = province;
		this.district = district;
		this.other_service = other_service;
	}

	public SavePost() {
		super();
	}

}
