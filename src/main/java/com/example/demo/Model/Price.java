package com.example.demo.Model;

public class Price {
	private int check;
	private Float value;
	public int getCheck() {
		return check;
	}
	public void setCheck(int check) {
		this.check = check;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	public Price(int check, Float value) {
		super();
		this.check = check;
		this.value = value;
	}
	public Price() {
		super();
	}

	

}
