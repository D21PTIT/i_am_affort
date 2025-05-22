package com.example.demo.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Property {
	private int id_property;
	private String name;
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
	private int bedroom;
	private int toilet;
	private List<Direction> direction_list = new ArrayList<Direction>();
	private float price;
	private int price_type;
	private int status;
	private String note;
	
	private int id_user;

	private Date created_at;
	private Date updated_at;
	private int delete;
	private int created_by_staff;
	private int created_by_user;
	private int type;
	
	

	public Property() {
		super();
	}

	public Property(int id_property, String name, String province, String district, String ward, String detail_address,
			List<Doc> doc_list, float surface_area, float useable_area, float width, float length, int flours,
			int bedroom, int toilet, List<Direction> direction_list, float price, int price_type, int status, String note, int id_user,
			Date created_at, Date updated_at, int delete, int created_by_staff, int created_by_user, int type) {
		super();
		this.id_property = id_property;
		this.name = name;
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
		this.bedroom = bedroom;
		this.toilet = toilet;
		this.direction_list = direction_list;
		this.price = price;
		this.price_type = price_type;
		this.status = status;
		this.note = note;
		this.id_user = id_user;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.delete = delete;
		this.created_by_staff = created_by_staff;
		this.created_by_user = created_by_user;
		this.type = type;
	}

	public Property(int id_property, String name, String province, String district, String ward, String detail_address,
			List<Doc> doc_list, float surface_area, float useable_area, float width, float length, int flours,
			int bedroom, int toilet, List<Direction> direction_list, float price, int price_type, int status, String note, int id_user,
			Date created_at, Date updated_at, int created_by_staff, int created_by_user, int type) {
		super();
		this.id_property = id_property;
		this.name = name;
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
		this.bedroom = bedroom;
		this.toilet = toilet;
		this.direction_list = direction_list;
		this.price = price;
		this.price_type = price_type;
		this.status = status;
		this.note = note;
		this.id_user = id_user;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.created_by_staff = created_by_staff;
		this.created_by_user = created_by_user;
		this.type = type;
	}


	public Property(int id_property, String name, String province, String district, String ward, String detail_address,
			List<Doc> doc_list, float surface_area, float useable_area, float width, float length, int flours,
			int bedroom, int toilet, List<Direction> direction_list, float price, int price_type, int status, String note,
			Date created_at, Date updated_at, int created_by_staff, int created_by_user, int type) {
		// TODO Auto-generated constructor stub
		
		super();
		this.id_property = id_property;
		this.name = name;
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
		this.bedroom = bedroom;
		this.toilet = toilet;
		this.direction_list = direction_list;
		this.price = price;
		this.price_type = price_type;
		this.status = status;
		this.note = note;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.created_by_staff = created_by_staff;
		this.created_by_user = created_by_user;
		this.type = type;
	}

	public int getPrice_type() {
		return price_type;
	}

	public void setPrice_type(int price_type) {
		this.price_type = price_type;
	}

	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
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


	public int getId_property() {
		return id_property;
	}
	public void setId_property(int id_property) {
		this.id_property = id_property;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getBedroom() {
		return bedroom;
	}
	public void setBedroom(int bedroom) {
		this.bedroom = bedroom;
	}
	public int getToilet() {
		return toilet;
	}
	public void setToilet(int toilet) {
		this.toilet = toilet;
	}
	public List<Direction> getDirection_list() {
		return direction_list;
	}
	public void setDirection_list(List<Direction> direction_list) {
		this.direction_list = direction_list;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
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
	
	
}
