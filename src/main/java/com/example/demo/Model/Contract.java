package com.example.demo.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Contract {
	private int id_contract;
	private int prop_owner_id;
	private String prop_owner_name;
	private int owner_gender;
	private String owner_email;
	private String owner_phone;
	private String owner_dob;
	private int prop_id;
	private String prop_name;
	private int room_id;
	private String room_code;
	private int max_pp;
	private List<TenantInContract> tenant_list = new ArrayList<TenantInContract>();
	private float price;
	private int price_type;
	private String rule;
	private int status;
	private Date start_date;
	private Date end_date;
	private Date created_date;
	private Date updated_date;
	private int delete;
	private float electric;
	private float water;
	private int water_type;
	private float internet;
	private float clean;
	private float elevator;
	private float other_service;
	private float deposit;

	public float getDeposit() {
		return deposit;
	}

	public void setDeposit(float deposit) {
		this.deposit = deposit;
	}

	public int getMax_pp() {
		return max_pp;
	}

	public void setMax_pp(int max_pp) {
		this.max_pp = max_pp;
	}

	public int getId_contract() {
		return id_contract;
	}

	public void setId_contract(int id_contract) {
		this.id_contract = id_contract;
	}

	public int getProp_owner_id() {
		return prop_owner_id;
	}

	public void setProp_owner_id(int prop_owner_id) {
		this.prop_owner_id = prop_owner_id;
	}

	public String getProp_owner_name() {
		return prop_owner_name;
	}

	public void setProp_owner_name(String prop_owner_name) {
		this.prop_owner_name = prop_owner_name;
	}

	public int getOwner_gender() {
		return owner_gender;
	}

	public void setOwner_gender(int owner_gender) {
		this.owner_gender = owner_gender;
	}

	public String getOwner_email() {
		return owner_email;
	}

	public void setOwner_email(String owner_email) {
		this.owner_email = owner_email;
	}

	public String getOwner_phone() {
		return owner_phone;
	}

	public void setOwner_phone(String owner_phone) {
		this.owner_phone = owner_phone;
	}

	public String getOwner_dob() {
		return owner_dob;
	}

	public void setOwner_dob(String owner_dob) {
		this.owner_dob = owner_dob;
	}

	public int getProp_id() {
		return prop_id;
	}

	public void setProp_id(int prop_id) {
		this.prop_id = prop_id;
	}

	public String getProp_name() {
		return prop_name;
	}

	public void setProp_name(String prop_name) {
		this.prop_name = prop_name;
	}

	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	public String getRoom_code() {
		return room_code;
	}

	public void setRoom_code(String room_code) {
		this.room_code = room_code;
	}

	public List<TenantInContract> getTenant_list() {
		return tenant_list;
	}

	public void setTenant_list(List<TenantInContract> tenant_list) {
		this.tenant_list = tenant_list;
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

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getDelete() {
		return delete;
	}

	public void setDelete(int delete) {
		this.delete = delete;
	}

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

	public Contract() {
		super();
	}

	public Contract(int id_contract, int prop_owner_id, String prop_owner_name, int owner_gender, String owner_email,
			String owner_phone, String owner_dob, int prop_id, String prop_name, int room_id, String room_code,
			int max_pp, List<TenantInContract> tenant_list, float price, int price_type, String rule, int status,
			Date start_date, Date end_date, Date created_date, Date updated_date, int delete, float electric,
			float water, int water_type, float internet, float clean, float elevator, float other_service, float deposit) {
		super();
		this.id_contract = id_contract;
		this.prop_owner_id = prop_owner_id;
		this.prop_owner_name = prop_owner_name;
		this.owner_gender = owner_gender;
		this.owner_email = owner_email;
		this.owner_phone = owner_phone;
		this.owner_dob = owner_dob;
		this.prop_id = prop_id;
		this.prop_name = prop_name;
		this.room_id = room_id;
		this.room_code = room_code;
		this.max_pp = max_pp;
		this.tenant_list = tenant_list;
		this.price = price;
		this.price_type = price_type;
		this.rule = rule;
		this.status = status;
		this.start_date = start_date;
		this.end_date = end_date;
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.delete = delete;
		this.electric = electric;
		this.water = water;
		this.water_type = water_type;
		this.internet = internet;
		this.clean = clean;
		this.elevator = elevator;
		this.other_service = other_service;
		this.deposit = deposit;
	}

	public Contract(int id_contract, int prop_owner_id, String prop_owner_name, int owner_gender, String owner_email,
			String owner_phone, String owner_dob, int prop_id, String prop_name, int room_id, String room_code,
			int max_pp, List<TenantInContract> tenant_list, float price, int price_type, String rule, int status,
			Date start_date, Date end_date, Date created_date, Date updated_date, float electric, float water,
			int water_type, float internet, float clean, float elevator, float other_service, float deposit) {
		super();
		this.id_contract = id_contract;
		this.prop_owner_id = prop_owner_id;
		this.prop_owner_name = prop_owner_name;
		this.owner_gender = owner_gender;
		this.owner_email = owner_email;
		this.owner_phone = owner_phone;
		this.owner_dob = owner_dob;
		this.prop_id = prop_id;
		this.prop_name = prop_name;
		this.room_id = room_id;
		this.room_code = room_code;
		this.max_pp = max_pp;
		this.tenant_list = tenant_list;
		this.price = price;
		this.price_type = price_type;
		this.rule = rule;
		this.status = status;
		this.start_date = start_date;
		this.end_date = end_date;
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.electric = electric;
		this.water = water;
		this.water_type = water_type;
		this.internet = internet;
		this.clean = clean;
		this.elevator = elevator;
		this.other_service = other_service;
		this.deposit = deposit;
	}

}
