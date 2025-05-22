package com.example.demo.SuggestForm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;

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

import com.example.demo.Model.SuggestForm;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class SuggestFormController {
	@GetMapping("/suggest_form/count/{id_tenant}")
	public ResponseEntity<Map<String, Integer>> getSuggestFormCount(@PathVariable String id_tenant) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		Map<String, Integer> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection
					.prepareStatement("SELECT `id_suggest_form`, COUNT(*) as `suggest_form_count` "
							+ "FROM `tester`.`suggest_form` "
							+ "WHERE `id_tenant` = ?  "
							+ "GROUP BY `id_suggest_form` LIMIT 1;");
			ps.setInt(1, Integer.valueOf(id_tenant));

			resultSet = ps.executeQuery();

			if (resultSet.next()) {
				int suggest_form_count = resultSet.getInt("suggest_form_count");
				int id_suggest_form = resultSet.getInt("id_suggest_form");
				response.put("suggest_form_count", suggest_form_count);
				response.put("id_suggest_form", id_suggest_form);
			} else {
				response.put("suggest_form_count", 0);
				response.put("id_suggest_form", null);
			}

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("suggest_form_count", -1);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (ps != null)
					ps.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@PostMapping("/suggest_form/create")
	public ResponseEntity<Map<String, String>> addSuggestForm(@RequestBody SuggestForm suggestForm) {
		Map<String, String> response = new HashMap<>();

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();

			String query = "INSERT INTO `tester`.`suggest_form` "
					+ "(`id_tenant`, `property_type`, `province`, " + "`district`, `ward`, `detail_address`, "
					+ "`longitude`, `latitude`, `distance`) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, suggestForm.getId_tenant());
			ps.setInt(2, suggestForm.getProperty_type());
			ps.setString(3, suggestForm.getProvince());
			ps.setString(4, suggestForm.getDistrict());
			ps.setString(5, suggestForm.getWard());
			ps.setString(6, suggestForm.getDetail_address());
			ps.setFloat(7, suggestForm.getLongitude());
			ps.setFloat(8, suggestForm.getLatitude());
			ps.setFloat(9, suggestForm.getDistance());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int newHandOverId = rs.getInt(1);
				response.put("id_hand_over", String.valueOf(newHandOverId));
			}

			response.put("message", "Suggest form added successfully");
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Error occurred");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@PutMapping("/suggest_form/update/{id_suggest_form}")
	public ResponseEntity<Map<String, String>> updateSuggestForm(@RequestBody SuggestForm suggestForm,
			@PathVariable String id_suggest_form) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();

			String query = "UPDATE `tester`.`suggest_form` " + "SET "
					+ "`id_tenant` = ?, `property_type` = ?, `province` = ?, "
					+ "`district` = ?, `ward` = ?, `detail_address` = ?, "
					+ "`longitude` = ?, `latitude` = ?, `distance` = ? " + "WHERE (`id_suggest_form` = ?);";

			ps = connection.prepareStatement(query);

			ps.setInt(1, suggestForm.getId_tenant());
			ps.setInt(2, suggestForm.getProperty_type());
			ps.setString(3, suggestForm.getProvince());
			ps.setString(4, suggestForm.getDistrict());
			ps.setString(5, suggestForm.getWard());
			ps.setString(6, suggestForm.getDetail_address());
			ps.setFloat(7, suggestForm.getLongitude());
			ps.setFloat(8, suggestForm.getLatitude());
			ps.setFloat(9, suggestForm.getDistance());
			ps.setInt(10, Integer.valueOf(id_suggest_form));

			ps.executeUpdate();

			response.put("message", "Suggest form updated successfully!");
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "Error occurred");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@GetMapping("/suggest_form/detail/{id_suggest_form}")
	public ResponseEntity<SuggestForm> getSugegstFormDetail(Model model, @PathVariable String id_suggest_form) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		SuggestForm suggestForm = new SuggestForm();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM tester.suggest_form WHERE id_suggest_form = ?");
			ps.setInt(1, Integer.valueOf(id_suggest_form));
			result = ps.executeQuery();

			while (result.next()) {
				suggestForm.setId_suggest_form(result.getInt("id_suggest_form"));
				suggestForm.setId_tenant(result.getInt("id_tenant"));
				suggestForm.setProperty_type(result.getInt("property_type"));
				suggestForm.setProvince(result.getString("province"));
				suggestForm.setDistrict(result.getString("district"));
				suggestForm.setWard(result.getString("ward"));
				suggestForm.setDetail_address(result.getString("detail_address"));
				suggestForm.setLongitude(result.getFloat("longitude"));
				suggestForm.setLatitude(result.getFloat("latitude"));
				suggestForm.setDistance(result.getFloat("distance"));
				suggestForm.setCreated_date(result.getDate("created_date"));
				suggestForm.setUpdated_date(result.getDate("updated_date"));
			}

			return new ResponseEntity<>(suggestForm, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (result != null)
					result.close();
				if (ps != null)
					ps.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
