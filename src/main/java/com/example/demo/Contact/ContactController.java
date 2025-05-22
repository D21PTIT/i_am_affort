package com.example.demo.Contact;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Contact;
import com.fasterxml.jackson.databind.ObjectMapper; 


@RestController
@CrossOrigin
public class ContactController {
	
	@GetMapping("/contact_list_tenant/{id_tenant}")
	public ResponseEntity<List<Contact>> getContactList(Model model, @PathVariable String id_tenant)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Contact> contact_list = new ArrayList<Contact>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement(
					"SELECT * FROM tester.contact "
					+ "WHERE id_tenant = ? AND `delete` = 0 order by `id_contact` DESC");
			ps.setInt(1, Integer.valueOf(id_tenant));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_contact = resultSet.getInt("id_contact");
				String user_name = resultSet.getString("user_name");
				String user_email = resultSet.getString("user_email");
				String content = resultSet.getString("content");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");
				int delete = resultSet.getInt("delete");
				int status = resultSet.getInt("status");
				int id_owner = resultSet.getInt("id_owner");
				int id_tenant1 = resultSet.getInt("id_tenant");
				int id_post = resultSet.getInt("id_post");
				String header = resultSet.getString("header");
				String phone_number = resultSet.getString("phone_number");

                contact_list.add(new Contact(id_contact, user_name, user_email, content, created_at, updated_at,
            			delete, status, id_owner, id_tenant1, id_post, header, phone_number));
			}

			return new ResponseEntity<>(contact_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/contact_list_owner/{id_owner}")
	public ResponseEntity<List<Contact>> getContactListOwner(Model model, @PathVariable String id_owner)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Contact> contact_list = new ArrayList<Contact>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement(
					"SELECT * FROM tester.contact "
					+ "WHERE id_owner = ? AND `delete` = 0 order by `id_contact` DESC");
			ps.setInt(1, Integer.valueOf(id_owner));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_contact = resultSet.getInt("id_contact");
				String user_name = resultSet.getString("user_name");
				String user_email = resultSet.getString("user_email");
				String content = resultSet.getString("content");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");
				int delete = resultSet.getInt("delete");
				int status = resultSet.getInt("status");
				int id_owner1 = resultSet.getInt("id_owner");
				int id_tenant1 = resultSet.getInt("id_tenant");
				int id_post = resultSet.getInt("id_post");
				String header = resultSet.getString("header");
				String phone_number = resultSet.getString("phone_number");

                contact_list.add(new Contact(id_contact, user_name, user_email, content, created_at, updated_at,
            			delete, status, id_owner1, id_tenant1, id_post, header, phone_number));
			}

			return new ResponseEntity<>(contact_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/contact/create")
	public ResponseEntity<Map<String, String>> addContact(@RequestBody Contact contact) {

		Map<String, String> response = new HashMap<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"INSERT INTO `tester`.`contact` "
					+ "(`user_name`, `user_email`, "
					+ "`content`, `status`, `id_owner`, "
					+ "`id_tenant`, `id_post`, `header`, "
					+ "`phone_number`) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?);");
			ps.setString(1, contact.getUsername());
			ps.setString(2, contact.getUser_email());
			ps.setString(3, contact.getContent());
			ps.setInt(4, contact.getStatus());
			ps.setInt(5, contact.getId_owner());
			ps.setInt(6, contact.getId_tenant());
			ps.setInt(7, contact.getId_post());
			ps.setString(8, contact.getHeader());
			ps.setString(9, contact.getPhone_number());

			ps.executeUpdate();
			response.put("message", "Contact added successfully");
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
	
	
	@PutMapping("/contact/update_status/{id_contact}")
	public ResponseEntity<Map<String, String>> updateStatusContact(@RequestBody Contact contact, @PathVariable String id_contact) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE contact SET status = ? WHERE id_contact = ?");
			ps.setInt(1, Integer.valueOf(contact.getStatus()));
			ps.setInt(2, Integer.valueOf(Integer.valueOf(id_contact)));

			ps.executeUpdate();
			response.put("message", "Update successfully!");

			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Error occurred");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} finally {
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
	
	
	@PutMapping("/contact/delete/{id_contact}")
	public ResponseEntity<Map<String, String>> deleteContact(@RequestBody Contact contact, @PathVariable String id_contact) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE contact SET `delete` = 1 WHERE id_contact = ?");
			ps.setInt(1, Integer.valueOf(contact.getId_contact()));

			ps.executeUpdate();
			response.put("message", "Delete successfully!");

			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Error occurred");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} finally {
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
}
