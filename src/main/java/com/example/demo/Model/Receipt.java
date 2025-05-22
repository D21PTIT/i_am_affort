package com.example.demo.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Receipt {
	private int id_receipt;
	private int id_invoice;
	private String label;
	private int id_owner;
	private List<TenantInContract> tenant_list = new ArrayList<TenantInContract>();
	private String image;
	private String note;
	private Date created_date;
	private Date updated_date;
	private String username;
	private String name;
	private String phone_number;
	private String email;

	public int getId_receipt() {
		return id_receipt;
	}

	public void setId_receipt(int id_receipt) {
		this.id_receipt = id_receipt;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Receipt() {
		super();
	}

	public Receipt(int id_receipt, int id_invoice, String label, int id_owner, List<TenantInContract> tenant_list,
			String image, String note, Date created_date, Date updated_date, String username, String name,
			String phone_number, String email) {
		super();
		this.id_receipt = id_receipt;
		this.id_invoice = id_invoice;
		this.label = label;
		this.id_owner = id_owner;
		this.tenant_list = tenant_list;
		this.image = image;
		this.note = note;
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.username = username;
		this.name = name;
		this.phone_number = phone_number;
		this.email = email;
	}

}
