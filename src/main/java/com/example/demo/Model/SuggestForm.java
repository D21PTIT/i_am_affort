package com.example.demo.Model;

import java.sql.Date;

public class SuggestForm {
	private int id_suggest_form;
	private int id_tenant;
	private int property_type;
	private String province;
	private String district;
	private String ward;
	private String detail_address;
	private float longitude;
	private float latitude;
	private float distance;
	private Date created_date;
	private Date updated_date;

	public int getId_suggest_form() {
		return id_suggest_form;
	}

	public void setId_suggest_form(int id_suggest_form) {
		this.id_suggest_form = id_suggest_form;
	}

	public int getId_tenant() {
		return id_tenant;
	}

	public void setId_tenant(int id_tenant) {
		this.id_tenant = id_tenant;
	}

	public int getProperty_type() {
		return property_type;
	}

	public void setProperty_type(int property_type) {
		this.property_type = property_type;
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

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getDetail_address() {
		return detail_address;
	}

	public void setDetail_address(String detail_address) {
		this.detail_address = detail_address;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	public SuggestForm(int id_suggest_form, int id_tenant, int property_type, String province, String district,
			String ward, String detail_address, float longitude, float latitude, float distance, Date created_date,
			Date updated_date) {
		super();
		this.id_suggest_form = id_suggest_form;
		this.id_tenant = id_tenant;
		this.property_type = property_type;
		this.province = province;
		this.district = district;
		this.ward = ward;
		this.detail_address = detail_address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.distance = distance;
		this.created_date = created_date;
		this.updated_date = updated_date;
	}

	public SuggestForm() {
		super();
	}

}
