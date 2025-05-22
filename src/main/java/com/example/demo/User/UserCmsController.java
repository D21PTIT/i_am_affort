package com.example.demo.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
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

import com.example.demo.Model.User;

@RestController
@CrossOrigin
public class UserCmsController {

	@GetMapping("/user_list/type_0")
	public ResponseEntity<List<User>> getUserListType0(Model model) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<User> user_list = new ArrayList<User>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM `tester`.`user` WHERE user_type = 0 AND `delete` = 0;");

			while (resultSet.next()) {

				int id_user = resultSet.getInt("id_user");
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");
				int status = resultSet.getInt("status");
				String name = resultSet.getString("name");
				int gender = resultSet.getInt("gender");
				Date dob = resultSet.getDate("dob");
				String email = resultSet.getString("email");
				int user_type = resultSet.getInt("user_type");
				String phone_number = resultSet.getString("phone_number");
				String url_ava_img = resultSet.getString("url_ava_img");
				String company_name = resultSet.getString("company_name");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");

				user_list.add(new User(id_user, username, password, status, name, gender, dob, email, user_type, phone_number,
						url_ava_img, company_name,  created_at, updated_at));
			}
			return new ResponseEntity<>(user_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/user_list/type_1")
	public ResponseEntity<List<User>> getUserListType1(Model model) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<User> user_list = new ArrayList<User>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM `tester`.`user` WHERE user_type = 1 AND `delete` = 0;");

			while (resultSet.next()) {

				int id_user = resultSet.getInt("id_user");
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");
				int status = resultSet.getInt("status");
				String name = resultSet.getString("name");
				int gender = resultSet.getInt("gender");
				Date dob = resultSet.getDate("dob");
				String email = resultSet.getString("email");
				int user_type = resultSet.getInt("user_type");
				String phone_number = resultSet.getString("phone_number");
				String url_ava_img = resultSet.getString("url_ava_img");
				String company_name = resultSet.getString("company_name");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");

				user_list.add(new User(id_user, username, password, status, name, gender, dob, email, user_type, phone_number,
						url_ava_img, company_name,  created_at, updated_at));
			}
			return new ResponseEntity<>(user_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/user_detail/{id_user}")
	public ResponseEntity<User> getUserDetail(Model model, @PathVariable String id_user) {
		model.addAttribute("id_user", id_user);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		User user = new User();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`user` WHERE id_user = ?");
			ps.setInt(1, Integer.valueOf(id_user));
			result = ps.executeQuery();
			while (result.next()) {
				user.setId_user(result.getInt("id_user"));
				user.setUsername(result.getString("username"));
				user.setPassword(result.getString("password"));
				user.setStatus(result.getInt("status"));
				user.setName(result.getString("name"));
				user.setGender(result.getInt("gender"));
				user.setDob(result.getDate("dob"));
				user.setEmail(result.getString("email"));
				user.setUser_type(result.getInt("user_type"));
				user.setPhone_number(result.getString("phone_number"));
				user.setUrl_ava_img(result.getString("url_ava_img"));
				user.setCompany_name(result.getString("company_name"));
				user.setCreated_at(result.getDate("created_at"));
				user.setUpdated_at(result.getDate("updated_at"));
			} // end of while block

			return new ResponseEntity<>(user, HttpStatus.OK);

		} // end of try block

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/user/create")
	public ResponseEntity<Map<String, String>> addUser(@RequestBody User user) {

		Map<String, String> response = new HashMap<>();

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`user` WHERE `username` = ?");
			ps.setString(1, user.getUsername());
			rs = ps.executeQuery();
			if (rs.next()) {
				// username đã tồn tại trong cơ sở dữ liệu
				response.put("message", "Username already exists");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} else {
				// username chưa tồn tại, tiến hành thêm user vào cơ sở dữ liệu
				ps = connection.prepareStatement(
						"INSERT INTO `tester`.`user` (`username`, `password`, `status`, `name`, `gender`, `dob`, `email`, `user_type`, `phone_number`, `url_ava_img`, `company_name`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());
				ps.setInt(3, Integer.valueOf(user.getStatus()));
				ps.setString(4, user.getName());
				ps.setInt(5, Integer.valueOf(user.getGender()));
				ps.setDate(6, user.getDob());
				ps.setString(7, user.getEmail());
				ps.setInt(8, Integer.valueOf(user.getUser_type()));
				ps.setString(9, user.getPhone_number());
				ps.setString(10, user.getUrl_ava_img());
				ps.setString(11, user.getCompany_name());
				ps.executeUpdate();
				response.put("message", "User added successfully");
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

	@PutMapping("/user/update_info/{id_user}")
	public ResponseEntity<Map<String, String>> updateUserInfo(@RequestBody User user, @PathVariable String id_user) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		// Kiểm tra các trường name, email không phải là null hoặc chuỗi rỗng
		if (user.getName() == null || user.getName().isEmpty()) {
			response.put("message", "Name not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			response.put("message", "Email must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE user SET `username` = ?, `password` = ?, status = ?, name = ?, `gender` = ?, `dob` = ?, email = ?, user_type = ?, phone_number = ?, url_ava_img = ?, company_name = ? WHERE id_user = ?");
						
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setInt(3, Integer.valueOf(user.getStatus()));
			ps.setString(4, user.getName());
			ps.setInt(5, Integer.valueOf(user.getGender()));
			ps.setDate(6, user.getDob());
			ps.setString(7, user.getEmail());
			ps.setInt(8, Integer.valueOf(user.getUser_type()));
			ps.setString(9, user.getPhone_number());
			ps.setString(10, user.getUrl_ava_img());
			ps.setString(11, user.getCompany_name());
			ps.setInt(12, Integer.parseInt(id_user));

			ps.executeUpdate();
			response.put("message", "Updated successfully!");

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
	
	@PutMapping("/user/delete/{id_user}")
	public ResponseEntity<Map<String, String>> deleteUser(@RequestBody User user, @PathVariable String id_user) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE user SET `delete` = 1 WHERE id_user = ?");
			ps.setInt(1, Integer.valueOf(user.getId_user()));

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
