package com.example.demo.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class HandOver {
	private int id_hand_over;
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
	private Date date;
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId_hand_over() {
		return id_hand_over;
	}

	public void setId_hand_over(int id_hand_over) {
		this.id_hand_over = id_hand_over;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public HandOver(int id_hand_over, String label, int id_contract, int id_property, String property_name, int id_room,
			String room_code, int id_owner, List<TenantInContract> tenant_list, Date created_date, Date updated_date,
			Date date, String content) {
		super();
		this.id_hand_over = id_hand_over;
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
		this.date = date;
		this.content = content;
	}

	public HandOver() {
		super();
	}

}
