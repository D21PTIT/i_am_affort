package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Direction {
    private int east;
    private int west;
    private int south;
    private int north;
    private int south_east;
    private int north_east;
    private int south_west;
    private int north_west;
    private int contact;
    private String other;

    // Default constructor cần thiết cho Jackson
    public Direction() {
    }

    public Direction(int east, int west, int south, int north, int south_east, int north_east, int south_west,
            int north_west, int contact, String other) {
        this.east = east;
        this.west = west;
        this.south = south;
        this.north = north;
        this.south_east = south_east;
        this.north_east = north_east;
        this.south_west = south_west;
        this.north_west = north_west;
        this.contact = contact;
        this.other = other;
    }
	
	public int getContact() {
		return contact;
	}

	public void setContact(int contact) {
		this.contact = contact;
	}

	public int getEast() {
		return east;
	}
	public void setEast(int east) {
		this.east = east;
	}
	public int getWest() {
		return west;
	}
	public void setWest(int west) {
		this.west = west;
	}
	public int getSouth() {
		return south;
	}
	public void setSouth(int south) {
		this.south = south;
	}
	public int getNorth() {
		return north;
	}
	public void setNorth(int north) {
		this.north = north;
	}
	public int getSouth_east() {
		return south_east;
	}
	public void setSouth_east(int south_east) {
		this.south_east = south_east;
	}
	public int getNorth_east() {
		return north_east;
	}
	public void setNorth_east(int north_east) {
		this.north_east = north_east;
	}
	public int getSouth_west() {
		return south_west;
	}
	public void setSouth_west(int south_west) {
		this.south_west = south_west;
	}
	public int getNorth_west() {
		return north_west;
	}
	public void setNorth_west(int north_west) {
		this.north_west = north_west;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
	
}
