package com.example.demo.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class News {
	private int id_news;
	private String label;
	private String content;
	private int status;
	private int created_by;
	private List<Img> img_list = new ArrayList<Img>();
	private Date created_at;
	private Date updated_at;
	private int delete;
	
	public News() {
		super();
	}
	
	public News(int id_news, String label, String content, int status, int created_by, List<Img> img_list,
			Date created_at, Date updated_at) {
		super();
		this.id_news = id_news;
		this.label = label;
		this.content = content;
		this.status = status;
		this.created_by = created_by;
		this.img_list = img_list;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}
	
	public News(int id_news, String label, String content, int status, int created_by, List<Img> img_list) {
		super();
		this.id_news = id_news;
		this.label = label;
		this.content = content;
		this.status = status;
		this.created_by = created_by;
		this.img_list = img_list;
	}
	
	public News(int id_news, String label, String content, int status, int created_by, List<Img> img_list, Date created_at,
			Date updated_at, int delete) {
		super();
		this.id_news = id_news;
		this.label = label;
		this.content = content;
		this.status = status;
		this.created_by = created_by;
		this.img_list = img_list;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.delete = delete;
	}


	public List<Img> getImg_list() {
		return img_list;
	}

	public void setImg_list(List<Img> img_list) {
		this.img_list = img_list;
	}

	public int getDelete() {
		return delete;
	}

	public void setDelete(int delete) {
		this.delete = delete;
	}

	public int getId_news() {
		return id_news;
	}
	public void setId_news(int id_news) {
		this.id_news = id_news;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	public List<Img> getImgList() {
		return img_list;
	}
	public void setImgList(List<Img> img_list) {
		this.img_list = img_list;
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
	
	
}
