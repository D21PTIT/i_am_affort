package com.example.demo.Model;

import java.util.ArrayList;
import java.util.List;

public class RoomInPost {
	private int id_room;
	private String name;
	private float area;
	private int bathroom;
	private int bedroom;
	private int kitchen;
	private String interial;
	private int balcony;
	private int max_people;
	private List<Price> price_month = new ArrayList<>();
	private List<Price> price_quater = new ArrayList<>();
	private List<Price> price_half_year = new ArrayList<>();
	private List<Price> price_year = new ArrayList<>();
	private float electric;
	private float water;
	private int water_type;
	private float internet;
	private float clean;
	private float elevator;
	private float other_service;

	public RoomInPost(int id_room, String name, float area, int bathroom, int bedroom, int kitchen, String interial,
			int balcony, int max_people, List<Price> price_month, List<Price> price_quater, List<Price> price_half_year,
			List<Price> price_year, float electric, float water, int water_type, float internet, float clean,
			float elevator, float other_service) {
		super();
		this.id_room = id_room;
		this.name = name;
		this.area = area;
		this.bathroom = bathroom;
		this.bedroom = bedroom;
		this.kitchen = kitchen;
		this.interial = interial;
		this.balcony = balcony;
		this.max_people = max_people;
		this.price_month = price_month;
		this.price_quater = price_quater;
		this.price_half_year = price_half_year;
		this.price_year = price_year;
		this.electric = electric;
		this.water = water;
		this.water_type = water_type;
		this.internet = internet;
		this.clean = clean;
		this.elevator = elevator;
		this.other_service = other_service;
	}

	public RoomInPost() {
		super();
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

	public int getBedroom() {
		return bedroom;
	}

	public void setBedroom(int bedroom) {
		this.bedroom = bedroom;
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

	public int getMax_people() {
		return max_people;
	}

	public void setMax_people(int max_people) {
		this.max_people = max_people;
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

}
