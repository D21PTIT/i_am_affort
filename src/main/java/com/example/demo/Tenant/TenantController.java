package com.example.demo.Tenant;

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

import com.example.demo.Model.Tenant;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@CrossOrigin
public class TenantController {
	@GetMapping("/tenant_list/{id_owner}")
	public ResponseEntity<List<Tenant>> getTenantList(Model model, @PathVariable String id_owner)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Tenant> tenant_list = new ArrayList<Tenant>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement("SELECT * FROM `tester`.`tenant` WHERE `delete` = 0 AND `id_owner` = ? order by `id_tenant` DESC");
			ps.setInt(1, Integer.valueOf(id_owner));

			resultSet = ps.executeQuery();

			while (resultSet.next()) {

				int id_tenant = resultSet.getInt("id_tenant");
				String username = resultSet.getString("username");
				int id_user = resultSet.getInt("id_user");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String phone_number = resultSet.getString("phone_number");
				String id_card = resultSet.getString("id_card");
				int gender = resultSet.getInt("gender");
				Date dob = resultSet.getDate("dob");
				String note = resultSet.getString("note");
				int rate = resultSet.getInt("rate");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");
				int id_owner1 = resultSet.getInt("id_owner");
				

				tenant_list.add(new Tenant(id_tenant, username, id_user, name, email, phone_number, id_card,
						gender, dob, note, rate, created_at, updated_at, id_owner1));
			}
			return new ResponseEntity<>(tenant_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/tenant_detail/{id_tenant}")
	public ResponseEntity<Tenant> getTenantDetail(Model model, @PathVariable String id_tenant) {
		model.addAttribute("id_tenant", id_tenant);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;

		Tenant tenant = new Tenant();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`tenant` WHERE id_tenant = ?");
			ps.setInt(1, Integer.valueOf(id_tenant));
			resultSet = ps.executeQuery();
			while (resultSet.next()) {
				tenant.setId_tenant(resultSet.getInt("id_tenant"));
				tenant.setUsername(resultSet.getString("username"));
				tenant.setId_user(resultSet.getInt("id_user"));
				tenant.setName(resultSet.getString("name"));
				tenant.setEmail(resultSet.getString("email"));
				tenant.setPhone_number(resultSet.getString("phone_number"));
				tenant.setId_card(resultSet.getString("id_card"));
				tenant.setGender(resultSet.getInt("gender"));
				tenant.setDob(resultSet.getDate("dob"));
				tenant.setNote(resultSet.getString("note"));
				tenant.setRate(resultSet.getInt("rate"));
				tenant.setId_owner(resultSet.getInt("id_owner"));
				
			} // end of while block

			return new ResponseEntity<>(tenant, HttpStatus.OK);

		} // end of try block

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/tenant/create")
	public ResponseEntity<Map<String, String>> addTenant(@RequestBody Tenant tenant) {

		Map<String, String> response = new HashMap<>();

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			String sql = "INSERT INTO `tester`.`tenant` \r\n"
					+ "(`username`, `id_user`, `name`, "
					+ "`email`, `phone_number`, `id_card`, `gender`, "
					+ "`dob`, `note`, `rate`, `id_owner`) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			// Chuẩn bị câu lệnh PreparedStatement
			ps = connection.prepareStatement(sql);
			ps.setString(1, tenant.getUsername());
			ps.setInt(2, tenant.getId_user());
			ps.setString(3, tenant.getName());
			ps.setString(4, tenant.getEmail());
			ps.setString(5, tenant.getPhone_number());
			ps.setString(6, tenant.getId_card());
			ps.setInt(7, tenant.getGender());
			ps.setDate(8, tenant.getDob());
			ps.setString(9, tenant.getNote());
			ps.setInt(10, tenant.getRate());
			ps.setInt(11, tenant.getId_owner());

			ps.executeUpdate();
			response.put("message", "Tenant added successfully");
			return new ResponseEntity<>(response, HttpStatus.CREATED);

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

	@PutMapping("/tenant/update_info/{id_tenant}")
	public ResponseEntity<Map<String, String>> updateTenant(@RequestBody Tenant tenant, @PathVariable String id_tenant) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();		

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			// Start building the SQL query
			String sql = "UPDATE `tester`.`tenant` "
					+ "SET "
					+ "`username` = ?, `id_user` = ?, `name` = ?, "
					+ "`email` = ?, `phone_number` = ?, `id_card` = ?, `gender` = ?, "
					+ "`dob` = ?, `note` = ?, `rate` = ?, `id_owner` = ? "
					+ "WHERE (`id_tenant` = ?);";

			// Prepare the PreparedStatement
			ps = connection.prepareStatement(sql);

			ps = connection.prepareStatement(sql);
			ps.setString(1, tenant.getUsername());
			ps.setInt(2, tenant.getId_user());
			ps.setString(3, tenant.getName());
			ps.setString(4, tenant.getEmail());
			ps.setString(5, tenant.getPhone_number());
			ps.setString(6,tenant.getId_card());
			ps.setInt(7, tenant.getGender());
			ps.setDate(8, tenant.getDob());
			ps.setString(9, tenant.getNote());
			ps.setInt(10, tenant.getRate());
			ps.setInt(11, tenant.getId_owner());
			ps.setInt(12, Integer.parseInt(id_tenant));

			ps.executeUpdate();
			response.put("message", "Tenant updated successfully!");

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
	
	@PutMapping("/tenant/delete/{id_tenant}")
	public ResponseEntity<Map<String, String>> deleteTenant(@RequestBody Tenant tenant, @PathVariable String id_tenant) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE tenant SET `delete` = 1 WHERE id_tenant = ?");
			ps.setInt(1, Integer.valueOf(id_tenant));

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
