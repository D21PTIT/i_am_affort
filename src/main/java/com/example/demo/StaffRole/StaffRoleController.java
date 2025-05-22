package com.example.demo.StaffRole;

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

import com.example.demo.Model.StaffRole;

@RestController
@CrossOrigin
public class StaffRoleController {
	@GetMapping("/staff_role_list")
	public ResponseEntity<List<StaffRole>> getStaffRoleList(Model model) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<StaffRole> staff_role_list = new ArrayList<StaffRole>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM `tester`.`staff_role` WHERE `delete` = 0");

			while (resultSet.next()) {

				int id_staff_role = resultSet.getInt("id_staff_role");
				String role = resultSet.getString("role");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");

				staff_role_list.add(new StaffRole(id_staff_role, role, created_at, updated_at));
			}
			return new ResponseEntity<>(staff_role_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/staff_role/create")
	public ResponseEntity<Map<String, String>> addUser(@RequestBody StaffRole staff_role) {

		Map<String, String> response = new HashMap<>();

		if (staff_role.getRole() == null || staff_role.getRole().isEmpty()) {
			response.put("message", "Role must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`staff_role` WHERE `role` = ?");
			ps.setString(1, staff_role.getRole());
			rs = ps.executeQuery();
			if (rs.next()) {
				// username đã tồn tại trong cơ sở dữ liệu
				response.put("message", "Role already exists");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} else {
				// username chưa tồn tại, tiến hành thêm user vào cơ sở dữ liệu
				ps = connection.prepareStatement(
						"INSERT INTO `tester`.`staff_role` (`role`) VALUES (?);");
				ps.setString(1, staff_role.getRole());
				
				ps.executeUpdate();
				response.put("message", "Staff role added successfully");
				return new ResponseEntity<>(response, HttpStatus.CREATED);
			}
		} catch (Exception e) {
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
	
	@PutMapping("/staff_role/update/{id_staff_role}")
	public ResponseEntity<Map<String, String>> updateUserInfo(@RequestBody StaffRole staff_role, @PathVariable String id_staff_role) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		// Kiểm tra các trường name, email không phải là null hoặc chuỗi rỗng
		if (staff_role.getRole() == null || staff_role.getRole().isEmpty()) {
			response.put("message", "Role must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}


		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE staff_role SET role = ? WHERE id_role = ?");
			
			ps.setString(1, staff_role.getRole());
			ps.setInt(2, Integer.valueOf(staff_role.getId_staff_role()));

			ps.executeUpdate();
			response.put("message", "Staff updated successfully!");

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
	
	@PutMapping("/user/delete/{id_staff_role}")
	public ResponseEntity<Map<String, String>> deleteStaffRole(@RequestBody StaffRole staff_role, @PathVariable String id_staff_role) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE staff_role SET `delete` = 1 WHERE id_staff_role = ?");
			ps.setInt(1, Integer.valueOf(staff_role.getId_staff_role()));

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
