package com.example.demo.SavePost;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.SavePost;
import com.example.demo.Model.TenantInContract;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class SavePostController {
	@GetMapping("/save_post_list/{id_tenant}")
	public ResponseEntity<List<SavePost>> getSavePostList(Model model, @PathVariable String id_tenant)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<SavePost> save_post_list = new ArrayList<SavePost>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement(
					"SELECT p.*, sp.* "
					+ "FROM post p "
					+ "JOIN save_post sp ON p.id_post = sp.id_post "
					+ "WHERE sp.id_tenant = ? order by `id_save_post` DESC");
			ps.setInt(1, Integer.valueOf(id_tenant));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_save_post = resultSet.getInt("id_save_post");
				int id_tenant1 = resultSet.getInt("id_tenant");
				int id_post = resultSet.getInt("id_post");
				Date created_date = resultSet.getDate("created_date");
				String header = resultSet.getString("header");
				String province = resultSet.getString("province");
                String district = resultSet.getString("district");
                float other_service = resultSet.getFloat("other_service");
				

                save_post_list.add(new SavePost(id_save_post, id_tenant1, id_post, created_date, header, province,
            			district, other_service));
			}

			return new ResponseEntity<>(save_post_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/save_post/create")
	public ResponseEntity<Map<String, String>> addSavePost(@RequestBody SavePost savePost) {

		Map<String, String> response = new HashMap<>();

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();

			ps = connection.prepareStatement(
					"INSERT INTO `tester`.`save_post` "
					+ "(`id_tenant`, `id_post`, `header`, `province`, `district`, `other_service`) "
					+ "VALUES (?, ?, ?, ?, ?, ?);");
			ps.setInt(1, savePost.getId_tenant());
			ps.setInt(2, savePost.getId_post());
			ps.setString(3, savePost.getHeader());
			ps.setString(4, savePost.getProvince());
			ps.setString(5, savePost.getDistrict());
			ps.setFloat(6, savePost.getOther_service());

			ps.executeUpdate();
			response.put("message", "Save post successfully");
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (

		Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.put("message", "Error occurred");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			// đóng các đối tượng ResultSet, PreparedStatement và Connection
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@DeleteMapping("/save_post_delete/{id_save_post}")

	public void delete(@PathVariable String id_save_post) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
			ps = connection.prepareStatement("DELETE FROM save_post WHERE id_save_post = ?");
			ps.setInt(1, Integer.parseInt(id_save_post));
			ps.executeUpdate();
			ps.close();
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}



