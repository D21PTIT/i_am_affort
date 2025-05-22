package com.example.demo.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Post {
	private int id_post;
	private String header;
	private String province;
	private String district;
	private String ward;
	private String detail_address;
	private List<Doc> doc_list = new ArrayList<Doc>();
	private float surface_area;
	private float useable_area;
	private float width;
	private float length;
	private int flours;
	private int bedrooms;
	private int toilets;
	private List<Direction> direction_list = new ArrayList<Direction>();
	private List<Price> price_month = new ArrayList<>();
	private List<Price> price_quater = new ArrayList<>();
	private List<Price> price_half_year = new ArrayList<>();
	private List<Price> price_year = new ArrayList<>();
	private int status;
	private String short_des;
	private String detail_des;
	private List<Img> image_list = new ArrayList<Img>();
	private List<RoomInPost> room_list = new ArrayList<RoomInPost>();
	private int created_by_staff;
	private int created_by_user;
	private int type;
	private String owner;
	private String phone_number;
	private String email;
	private String company_name;
	private Date exp_date;
	private Date created_at;
	private Date updated_at;
	private int delete;
	private int id_property;
	private int public_price;
	private float electric;
	private float water;
	private int water_type;
	private float internet;
	private float clean;
	private float elevator;
	private float other_service;
	private float longitude;
	private float latitude;

	public Post() {
		super();
	}

	public Post(int id_post, String header, String province, String district, String ward, String detail_address,
			List<Doc> doc_list, float surface_area, float useable_area, float width, float length, int flours,
			int bedrooms, int toilets, List<Direction> direction_list, List<Price> price_month,
			List<Price> price_quater, List<Price> price_half_year, List<Price> price_year, int status, String short_des,
			String detail_des, List<Img> image_list, List<RoomInPost> room_list, int created_by_staff,
			int created_by_user, int type, String owner, String phone_number, String email, String company_name,
			Date exp_date, Date created_at, Date updated_at, int delete, int id_property, int public_price,
			float electric, float water, int water_type, float internet, float clean, float elevator,
			float other_service, float longitude, float latitude) {
		super();
		this.id_post = id_post;
		this.header = header;
		this.province = province;
		this.district = district;
		this.ward = ward;
		this.detail_address = detail_address;
		this.doc_list = doc_list;
		this.surface_area = surface_area;
		this.useable_area = useable_area;
		this.width = width;
		this.length = length;
		this.flours = flours;
		this.bedrooms = bedrooms;
		this.toilets = toilets;
		this.direction_list = direction_list;
		this.price_month = price_month;
		this.price_quater = price_quater;
		this.price_half_year = price_half_year;
		this.price_year = price_year;
		this.status = status;
		this.short_des = short_des;
		this.detail_des = detail_des;
		this.image_list = image_list;
		this.room_list = room_list;
		this.created_by_staff = created_by_staff;
		this.created_by_user = created_by_user;
		this.type = type;
		this.owner = owner;
		this.phone_number = phone_number;
		this.email = email;
		this.company_name = company_name;
		this.exp_date = exp_date;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.delete = delete;
		this.id_property = id_property;
		this.public_price = public_price;
		this.electric = electric;
		this.water = water;
		this.water_type = water_type;
		this.internet = internet;
		this.clean = clean;
		this.elevator = elevator;
		this.other_service = other_service;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public int getPublic_price() {
		return public_price;
	}

	public void setPublic_price(int public_price) {
		this.public_price = public_price;
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

	public int getId_post() {
		return id_post;
	}

	public void setId_post(int id_post) {
		this.id_post = id_post;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getDetail_address() {
		return detail_address;
	}

	public void setDetail_address(String detail_address) {
		this.detail_address = detail_address;
	}

	public List<Doc> getDoc_list() {
		return doc_list;
	}

	public void setDoc_list(List<Doc> doc_list) {
		this.doc_list = doc_list;
	}

	public float getSurface_area() {
		return surface_area;
	}

	public void setSurface_area(float surface_area) {
		this.surface_area = surface_area;
	}

	public float getUseable_area() {
		return useable_area;
	}

	public void setUseable_area(float useable_area) {
		this.useable_area = useable_area;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public int getFlours() {
		return flours;
	}

	public void setFlours(int flours) {
		this.flours = flours;
	}

	public int getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}

	public int getToilets() {
		return toilets;
	}

	public void setToilets(int toilets) {
		this.toilets = toilets;
	}

	public List<Direction> getDirection_list() {
		return direction_list;
	}

	public void setDirection_list(List<Direction> direction_list) {
		this.direction_list = direction_list;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getShort_des() {
		return short_des;
	}

	public void setShort_des(String short_des) {
		this.short_des = short_des;
	}

	public String getDetail_des() {
		return detail_des;
	}

	public void setDetail_des(String detail_des) {
		this.detail_des = detail_des;
	}

	public List<Img> getImage_list() {
		return image_list;
	}

	public void setImage_list(List<Img> image_list) {
		this.image_list = image_list;
	}

	public List<RoomInPost> getRoom_list() {
		return room_list;
	}

	public void setRoom_list(List<RoomInPost> room_list) {
		this.room_list = room_list;
	}

	public int getCreated_by_staff() {
		return created_by_staff;
	}

	public void setCreated_by_staff(int created_by_staff) {
		this.created_by_staff = created_by_staff;
	}

	public int getCreated_by_user() {
		return created_by_user;
	}

	public void setCreated_by_user(int created_by_user) {
		this.created_by_user = created_by_user;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
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

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

}
