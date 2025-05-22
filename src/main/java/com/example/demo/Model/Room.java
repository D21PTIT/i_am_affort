package com.example.demo.Model;

import java.sql.Date;

public class Room {
	private int id_room;
	private String name;
	private float area;
	private int bathroom;
	private int bedroom;
	private int kitchen;
	private String interial;
	private int balcony;
	private int status;
	private int max_people;
	private Date created_at;
	private Date updated_at;
	private int delete;
	private int id_property;
	private float price;
	private int frequency;
	private int id_owner;

	public Room() {
		super();
	}

	public int getId_owner() {
		return id_owner;
	}

	public void setId_owner(int id_owner) {
		this.id_owner = id_owner;
	}

	public int getBedroom() {
		return bedroom;
	}

	public void setBedroom(int bedroom) {
		this.bedroom = bedroom;
	}

	public Room(int id_room, String name, float area, int bathroom, int bedroom, int kitchen, String interial, int balcony,
			int status, int max_people, Date created_at, Date updated_at, float price, int frequency, int id_owner) {
		super();
		this.id_room = id_room;
		this.name = name;
		this.area = area;
		this.bathroom = bathroom;
		this.bedroom = bedroom;
		this.kitchen = kitchen;
		this.interial = interial;
		this.balcony = balcony;
		this.status = status;
		this.max_people = max_people;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.price = price;
		this.frequency = frequency;
		this.id_owner = id_owner;
	}

	public Room(int id_room, String name, float area, int bathroom, int bedroom, int kitchen, String interial, int balcony,
			int status, int max_people, Date created_at, Date updated_at, int id_property, float price, int frequency, int id_owner) {
		super();
		this.id_room = id_room;
		this.name = name;
		this.area = area;
		this.bathroom = bathroom;
		this.bedroom = bedroom;
		this.kitchen = kitchen;
		this.interial = interial;
		this.balcony = balcony;
		this.status = status;
		this.max_people = max_people;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.id_property = id_property;
		this.price = price;
		this.frequency = frequency;
		this.id_owner = id_owner;
	}

	public Room(int id_room, String name, float area, int bathroom, int bedroom, int kitchen, String interial, int balcony,
			int status, int max_people, Date created_at, Date updated_at, int delete, int id_property, float price,
			int frequency, int id_owner) {
		super();
		this.id_room = id_room;
		this.name = name;
		this.area = area;
		this.bathroom = bathroom;
		this.bedroom = bedroom;
		this.kitchen = kitchen;
		this.interial = interial;
		this.balcony = balcony;
		this.status = status;
		this.max_people = max_people;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.delete = delete;
		this.id_property = id_property;
		this.price = price;
		this.frequency = frequency;
		this.id_owner = id_owner;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getId_room() {
		return id_room;
	}

	public void setId_room(int id_room) {
		this.id_room = id_room;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public int getBathroom() {
		return bathroom;
	}

	public void setBathroom(int bathroom) {
		this.bathroom = bathroom;
	}

	public int getKitchen() {
		return kitchen;
	}

	public void setKitchen(int kitchen) {
		this.kitchen = kitchen;
	}

	public String getInterial() {
		return interial;
	}

	public void setInterial(String interial) {
		this.interial = interial;
	}

	public int getBalcony() {
		return balcony;
	}

	public void setBalcony(int balcony) {
		this.balcony = balcony;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMax_people() {
		return max_people;
	}

	public void setMax_people(int max_people) {
		this.max_people = max_people;
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

	public int getDelete() {
		return delete;
	}

	public void setDelete(int delete) {
		this.delete = delete;
	}

	public int getId_property() {
		return id_property;
	}

	public void setId_property(int id_property) {
		this.id_property = id_property;
	}
}
