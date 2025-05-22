package com.example.demo.Model;

import java.sql.Date;

public class UserRentProp {
	private int id_user_rent_prop;
	private int id_tenant;
	private int id_property;
	private int id_user;
	private String username;
	private String tenant_name;
	private int id_room;
	private Date start_date;
	private Date end_date;
	private int status;
	private float price;
	private int price_type;
	private int rent_type;
	private Date updated_at;
	private Date created_at;

	public int getId_user_rent_prop() {
		return id_user_rent_prop;
	}

	public void setId_user_rent_prop(int id_user_rent_prop) {
		this.id_user_rent_prop = id_user_rent_prop;
	}

	public int getId_tenant() {
		return id_tenant;
	}

	public void setId_tenant(int id_tenant) {
		this.id_tenant = id_tenant;
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

	public int getId_room() {
		return id_room;
	}

	public void setId_room(int id_room) {
		this.id_room = id_room;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getPrice_type() {
		return price_type;
	}

	public void setPrice_type(int price_type) {
		this.price_type = price_type;
	}

	public int getRent_type() {
		return rent_type;
	}

	public void setRent_type(int rent_type) {
		this.rent_type = rent_type;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public UserRentProp() {
		super();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTenant_name() {
		return tenant_name;
	}
	public void setTenant_name(String tenant_name) {
		this.tenant_name = tenant_name;
	}
	


	public UserRentProp(int id_tenant, int id_property, int id_user, String username, String tenant_name, int id_room,
			Date start_date, Date end_date, int status, float price, int price_type, int rent_type) {
		super();
		this.id_tenant = id_tenant;
		this.id_property = id_property;
		this.id_user = id_user;
		this.username = username;
		this.tenant_name = tenant_name;
		this.id_room = id_room;
		this.start_date = start_date;
		this.end_date = end_date;
		this.status = status;
		this.price = price;
		this.price_type = price_type;
		this.rent_type = rent_type;
	}

	public UserRentProp(int id_user_rent_prop, int id_tenant, int id_property, int id_user, String username,
			String tenant_name, int id_room, Date start_date, Date end_date, int status, float price, int price_type,
			int rent_type, Date updated_at, Date created_at) {
		super();
		this.id_user_rent_prop = id_user_rent_prop;
		this.id_tenant = id_tenant;
		this.id_property = id_property;
		this.id_user = id_user;
		this.username = username;
		this.tenant_name = tenant_name;
		this.id_room = id_room;
		this.start_date = start_date;
		this.end_date = end_date;
		this.status = status;
		this.price = price;
		this.price_type = price_type;
		this.rent_type = rent_type;
		this.updated_at = updated_at;
		this.created_at = created_at;
	}

}
