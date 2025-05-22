package com.example.demo.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class RentPrice {
	private int id;
	private int prop_id;
	private int prop_type;
	private String prop_name;
	private int room_id;
	private String room_code;
	private List<Price> price_month = new ArrayList<>();
	private List<Price> price_quater = new ArrayList<>();
	private List<Price> price_half_year = new ArrayList<>();
	private List<Price> price_year = new ArrayList<>();
	private Date validity_date;
	private Date created_date;
	private Date updated_date;
	private int id_owner;
	private float electric;
	private float water;
	private int water_type;
	private float internet;
	private float clean;
	private float elevator;
	private float other_service;
	
	
	public float getElectric() {
		return electric;
	}
	public void setElectric(float electric) {
		this.electric = electric;
	}
	public float getWater() {
		return water;
	}
	public void setWater(float water) {
		this.water = water;
	}
	public int getWater_type() {
		return water_type;
	}
	public void setWater_type(int water_type) {
		this.water_type = water_type;
	}
	public float getInternet() {
		return internet;
	}
	public void setInternet(float internet) {
		this.internet = internet;
	}
	public float getClean() {
		return clean;
	}
	public void setClean(float clean) {
		this.clean = clean;
	}
	public float getElevator() {
		return elevator;
	}
	public void setElevator(float elevator) {
		this.elevator = elevator;
	}
	public float getOther_service() {
		return other_service;
	}
	public void setOther_service(float other_service) {
		this.other_service = other_service;
	}
	public String getRoom_code() {
		return room_code;
	}
	public void setRoom_code(String room_code) {
		this.room_code = room_code;
	}
	public int getProp_type() {
		return prop_type;
	}
	public void setProp_type(int prop_type) {
		this.prop_type = prop_type;
	}
	public String getProp_name() {
		return prop_name;
	}
	public void setProp_name(String prop_name) {
		this.prop_name = prop_name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProp_id() {
		return prop_id;
	}
	public void setProp_id(int prop_id) {
		this.prop_id = prop_id;
	}
	public int getRoom_id() {
		return room_id;
	}
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	public List<Price> getPrice_month() {
		return price_month;
	}
	public void setPrice_month(List<Price> price_month) {
		this.price_month = price_month;
	}
	public List<Price> getPrice_quater() {
		return price_quater;
	}
	public void setPrice_quater(List<Price> price_quater) {
		this.price_quater = price_quater;
	}
	public List<Price> getPrice_half_year() {
		return price_half_year;
	}
	public void setPrice_half_year(List<Price> price_half_year) {
		this.price_half_year = price_half_year;
	}
	public List<Price> getPrice_year() {
		return price_year;
	}
	public void setPrice_year(List<Price> price_year) {
		this.price_year = price_year;
	}
	public Date getValidity_date() {
		return validity_date;
	}
	public void setValidity_date(Date validity_date) {
		this.validity_date = validity_date;
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
	public int getId_owner() {
		return id_owner;
	}
	public void setId_owner(int id_owner) {
		this.id_owner = id_owner;
	}
	public RentPrice() {
		super();
	}
	public RentPrice(int id, int prop_id, int prop_type, String prop_name, int room_id, String room_code,
			List<Price> price_month, List<Price> price_quater, List<Price> price_half_year, List<Price> price_year,
			Date validity_date, Date created_date, Date updated_date, int id_owner, float electric, float water,
			int water_type, float internet, float clean, float elevator, float other_service) {
		super();
		this.id = id;
		this.prop_id = prop_id;
		this.prop_type = prop_type;
		this.prop_name = prop_name;
		this.room_id = room_id;
		this.room_code = room_code;
		this.price_month = price_month;
		this.price_quater = price_quater;
		this.price_half_year = price_half_year;
		this.price_year = price_year;
		this.validity_date = validity_date;
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.id_owner = id_owner;
		this.electric = electric;
		this.water = water;
		this.water_type = water_type;
		this.internet = internet;
		this.clean = clean;
		this.elevator = elevator;
		this.other_service = other_service;
	}
	

	
}
