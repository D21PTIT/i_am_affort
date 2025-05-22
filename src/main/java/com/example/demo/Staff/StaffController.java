package com.example.demo.Staff;

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

import com.example.demo.Model.Staff;

@RestController
@CrossOrigin
public class StaffController {
	
	@GetMapping("/staff_list")
	public ResponseEntity<List<Staff>> getStaffList(Model model) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Staff> staff_list = new ArrayList<Staff>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM `tester`.`staff` WHERE `delete` = 0");

			while (resultSet.next()) {

				int id_staff = resultSet.getInt("id_staff");
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");
				int status = resultSet.getInt("status");
				String name = resultSet.getString("name");
				int role = resultSet.getInt("role");
				String email = resultSet.getString("email");
				String phone_number = resultSet.getString("phone_number");
				Date dob = resultSet.getDate("dob");
				String address = resultSet.getString("address");
				String url_ava_img = resultSet.getString("url_ava_img");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");

				staff_list.add(new Staff(id_staff, username, password, status, name, role, email,
						phone_number, dob, address, url_ava_img, created_at, updated_at));
			}
			return new ResponseEntity<>(staff_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/staff_detail/{id_staff}")
	public ResponseEntity<Staff> getUserDetail(Model model, @PathVariable String id_staff) {
		model.addAttribute("id_staff", id_staff);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		Staff staff = new Staff();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`staff` WHERE id_staff = ?");
			ps.setInt(1, Integer.valueOf(id_staff));
			result = ps.executeQuery();
			while (result.next()) {
				staff.setId_staff(result.getInt("id_staff"));
				staff.setUsername(result.getString("username"));
				staff.setPassword(result.getString("password"));
				staff.setStatus(result.getInt("status"));
				staff.setName(result.getString("name"));
				staff.setRole(result.getInt("role"));
				staff.setEmail(result.getString("email"));
				staff.setPhone_number(result.getString("phone_number"));
				staff.setDob(result.getDate("dob"));
				staff.setAddress(result.getString("address"));
				staff.setUrl_ava_img(result.getString("url_ava_img"));
				staff.setCreated_at(result.getDate("created_at"));
				staff.setUpdated_at(result.getDate("updated_at"));
			} // end of while block

			return new ResponseEntity<>(staff, HttpStatus.OK);

		} // end of try block

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/staff/create")
	public ResponseEntity<Map<String, String>> addUser(@RequestBody Staff staff) {

		Map<String, String> response = new HashMap<>();

		if (staff.getUsername() == null || staff.getUsername().isEmpty()) {
			response.put("message", "Username must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (staff.getPassword() == null || staff.getPassword().isEmpty()) {
			response.put("message", "Password must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (staff.getName() == null || staff.getName().isEmpty()) {
			response.put("message", "Name not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (staff.getEmail() == null || staff.getEmail().isEmpty()) {
			response.put("message", "Email must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`staff` WHERE `username` = ?");
			ps.setString(1, staff.getUsername());
			rs = ps.executeQuery();
			if (rs.next()) {
				// username đã tồn tại trong cơ sở dữ liệu
				response.put("message", "Username already exists");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} else {
				
				String sql = "INSERT INTO `tester`.`staff` (`username`, `password`, `status`, `name`, `email`, `phone_number`, `dob`, `address`, `url_ava_img`";

				// Nếu role không phải là null, thêm vào phần role ở cuối chuỗi
				if ((Integer.valueOf(staff.getRole()) != 0)) {
				    sql += ", `role`"; 
				}

				// Thêm phần VALUES vào câu lệnh SQL
				sql += ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?";

				// Nếu role không phải là null, thêm một dấu ? cho giá trị role
				if ((Integer.valueOf(staff.getRole()) != 0)) {
				    sql += ", ?";
				}

				sql += ")";

				// Chuẩn bị câu lệnh PreparedStatement
				ps = connection.prepareStatement(sql);
				ps.setString(1, staff.getUsername());
				ps.setString(2, staff.getPassword());
				ps.setInt(3, Integer.valueOf(staff.getStatus()));
				ps.setString(4, staff.getName());
				ps.setString(5, staff.getEmail());
				ps.setString(6, staff.getPhone_number());
				ps.setDate(7, staff.getDob());
				ps.setString(8, staff.getAddress());
				ps.setString(9, staff.getUrl_ava_img());

				// Nếu role không phải là null, thiết lập giá trị cho role
				if ((Integer.valueOf(staff.getRole()) != 0)) {
				    ps.setInt(10, Integer.valueOf(staff.getRole()));
				}
				
				System.out.println(sql);
				System.out.println(Integer.valueOf(staff.getRole()));
				System.out.println(Integer.valueOf(staff.getRole()).TYPE);
				
				ps.executeUpdate();
				response.put("message", "Staff added successfully");
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
	
	@PutMapping("/staff/update_info/{id_staff}")
	public ResponseEntity<Map<String, String>> updateUserInfo(@RequestBody Staff staff, @PathVariable String id_staff) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		// Kiểm tra các trường name, email không phải là null hoặc chuỗi rỗng
		if (staff.getName() == null || staff.getName().isEmpty()) {
			response.put("message", "Name not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (staff.getEmail() == null || staff.getEmail().isEmpty()) {
			response.put("message", "Email must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			
			// Start building the SQL query
			String sql = "UPDATE staff SET username = ?, password = ?, status = ?, name = ?, email = ?, phone_number = ?, dob = ?, address = ?, url_ava_img = ?";

			// If role is not null or zero, add the role field
			if (Integer.valueOf(staff.getRole()) != 0) {
			    sql += ", role = ?";
			}

			// Add the WHERE clause
			sql += " WHERE id_staff = ?";

			// Prepare the PreparedStatement
			ps = connection.prepareStatement(sql);

			// Set values for the placeholders
			ps.setString(1, staff.getUsername());
			ps.setString(2, staff.getPassword());
			ps.setInt(3, Integer.valueOf(staff.getStatus()));
			ps.setString(4, staff.getName());
			ps.setString(5, staff.getEmail());
			ps.setString(6, staff.getPhone_number());
			ps.setDate(7, staff.getDob());
			ps.setString(8, staff.getAddress());
			ps.setString(9, staff.getUrl_ava_img());

			// If role is not null or zero, set the value for role
			int index = 10;
			if (Integer.valueOf(staff.getRole()) != 0) {
			    ps.setInt(index++, Integer.valueOf(staff.getRole()));
			}

			// Set the ID for the WHERE clause
			ps.setInt(index, Integer.valueOf(staff.getId_staff()));

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
	
	@PutMapping("/user/delete/{id_staff}")
	public ResponseEntity<Map<String, String>> deleteStaff(@RequestBody Staff staff, @PathVariable String id_staff) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE user SET `delete` = 1 WHERE id_staff = ?");
			ps.setInt(1, Integer.valueOf(staff.getId_staff()));

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
