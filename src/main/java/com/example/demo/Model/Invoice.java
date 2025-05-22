package com.example.demo.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
	private int id_invoice;
	private String label;
	private int id_contract;
	private int id_property;
	private String property_name;
	private int id_room;
	private String room_code;
	private int id_owner;
	private List<TenantInContract> tenant_list = new ArrayList<TenantInContract>();
	private Date created_date;
	private Date updated_date;
	private int rent_month;
	private int people;
	private float deposit;
	private float rent_price;
	private int rent_price_type;
	private float electric;
	private int electric_num;
	private float water;
	private int water_type;
	private int water_num;
	private float internet;
	private float clean;
	private float elevator;
	private float other;
	private float add;
	private float deduct;
	private float total;
	private String note;
	private int delete;
	private int status;
	private int deposit_amount;
	

	public int getDeposit_amount() {
		return deposit_amount;
	}

	public void setDeposit_amount(int deposit_amount) {
		this.deposit_amount = deposit_amount;
	}

	public float getClean() {
		return clean;
	}

	public void setClean(float clean) {
		this.clean = clean;
	}

	public int getRent_price_type() {
		return rent_price_type;
	}

	public void setRent_price_type(int rent_price_type) {
		this.rent_price_type = rent_price_type;
	}

	public int getId_invoice() {
		return id_invoice;
	}

	public void setId_invoice(int id_invoice) {
		this.id_invoice = id_invoice;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getId_contract() {
		return id_contract;
	}

	public void setId_contract(int id_contract) {
		this.id_contract = id_contract;
	}

	public int getId_property() {
		return id_property;
	}

	public void setId_property(int id_property) {
		this.id_property = id_property;
	}

	public String getProperty_name() {
		return property_name;
	}

	public void setProperty_name(String property_name) {
		this.property_name = property_name;
	}

	public int getId_room() {
		return id_room;
	}

	public void setId_room(int id_room) {
		this.id_room = id_room;
	}

	public String getRoom_code() {
		return room_code;
	}

	public void setRoom_code(String room_code) {
		this.room_code = room_code;
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

	public int getRent_month() {
		return rent_month;
	}

	public void setRent_month(int rent_month) {
		this.rent_month = rent_month;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public float getDeposit() {
		return deposit;
	}

	public void setDeposit(float deposit) {
		this.deposit = deposit;
	}

	public float getRent_price() {
		return rent_price;
	}

	public void setRent_price(float rent_price) {
		this.rent_price = rent_price;
	}

	public float getElectric() {
		return electric;
	}

	public void setElectric(float electric) {
		this.electric = electric;
	}

	public int getElectric_num() {
		return electric_num;
	}

	public void setElectric_num(int electric_num) {
		this.electric_num = electric_num;
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

	public int getWater_num() {
		return water_num;
	}

	public void setWater_num(int water_num) {
		this.water_num = water_num;
	}

	public float getInternet() {
		return internet;
	}

	public void setInternet(float internet) {
		this.internet = internet;
	}

	public float getElevator() {
		return elevator;
	}

	public void setElevator(float elevator) {
		this.elevator = elevator;
	}

	public float getOther() {
		return other;
	}

	public void setOther(float other) {
		this.other = other;
	}

	public float getAdd() {
		return add;
	}

	public void setAdd(float add) {
		this.add = add;
	}

	public float getDeduct() {
		return deduct;
	}

	public void setDeduct(float deduct) {
		this.deduct = deduct;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public Invoice(int id_invoice, String label, int id_contract, int id_property, String property_name, int id_room,
			String room_code, int id_owner, List<TenantInContract> tenant_list, Date created_date, Date updated_date,
			int rent_month, int people, float deposit, float rent_price, int rent_price_type, float electric,
			int electric_num, float water, int water_type, int water_num, float internet, float clean, float elevator, float other,
			float add, float deduct, float total, String note, int delete, int status, int deposit_amount) {
		super();
		this.id_invoice = id_invoice;
		this.label = label;
		this.id_contract = id_contract;
		this.id_property = id_property;
		this.property_name = property_name;
		this.id_room = id_room;
		this.room_code = room_code;
		this.id_owner = id_owner;
		this.tenant_list = tenant_list;
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.rent_month = rent_month;
		this.people = people;
		this.deposit = deposit;
		this.rent_price = rent_price;
		this.rent_price_type = rent_price_type;
		this.electric = electric;
		this.electric_num = electric_num;
		this.water = water;
		this.water_type = water_type;
		this.water_num = water_num;
		this.internet = internet;
		this.clean = clean;
		this.elevator = elevator;
		this.other = other;
		this.add = add;
		this.deduct = deduct;
		this.total = total;
		this.note = note;
		this.delete = delete;
		this.status = status;
		this.deposit_amount = deposit_amount;
	}

	public Invoice() {
		super();
	}

}
