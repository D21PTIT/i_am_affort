package com.example.demo.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.Model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class UserController {

	
	@PostMapping("/property/sign-in")
	public ResponseEntity<String> signIn(@RequestBody User user) {
		Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet result = null;
	    String token = null;
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester","root","123456");
	        ps = connection.prepareStatement("SELECT * FROM `tester`.`user` WHERE username = ? AND password = ? AND user_type = 0");
	        ps.setString(1, user.getUsername());
	        ps.setString(2, user.getPassword());
	        
	        result = ps.executeQuery();
	        
	        if (result.next()) {
	        	if (result.getInt("status") == 0 || result.getInt("delete") == 1) {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inactive account");
		        }
	        	
	            // Lấy thông tin user từ ResultSet
	            int id_user = result.getInt("id_user");
	            String username = result.getString("username");
	            String password = result.getString("password");
	            String name = result.getString("name");
	            int user_type = result.getInt("user_type");
	            
	            
	            // Tạo token với thông tin quyền hạn
	            Algorithm algorithm = Algorithm.HMAC256("secret_key");
	            token = JWT.create()
	                    .withClaim("id_user", id_user)
	                    .withClaim("username", username)
	                    .withClaim("password", password)
	                    .withClaim("name", name)
	                    .withClaim("user_type", user_type)
	                    .withExpiresAt(new Date(System.currentTimeMillis() + 600000))
	                    .sign(algorithm);
	            
	            // Trả về token trong header và status code 200 OK
	            HttpHeaders headers = new HttpHeaders();
	            headers.add("Authorization", "Bearer " + token);
	            headers.add("Content-Type", "application/json"); // Thêm header Content-Type
	            return ResponseEntity.ok().headers(headers).body(token);
	        } else {
	            // Trả về status code 401 Unauthorized nếu thông tin đăng nhập không hợp lệ
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Trả về status code 500 Internal Server Error nếu có lỗi xảy ra
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } finally {
	        // Đóng kết nối và statement
	        try {
	            if(connection != null) {
	                connection.close();
	            }
	            if (ps != null) {
	                ps.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	@PostMapping("/property/sign-in/user_type1")
	public ResponseEntity<String> signInForUserType1(@RequestBody User user) {
		Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet result = null;
	    String token = null;
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester","root","123456");
	        ps = connection.prepareStatement("SELECT * FROM `tester`.`user` WHERE username = ? AND password = ? AND user_type = 1");
	        ps.setString(1, user.getUsername());
	        ps.setString(2, user.getPassword());
	        
	        result = ps.executeQuery();
	        
	        if (result.next()) {
	        	if (result.getInt("status") == 0 || result.getInt("delete") == 1) {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inactive account");
		        }
	        	
	            // Lấy thông tin user từ ResultSet
	            int id_user = result.getInt("id_user");
	            String username = result.getString("username");
	            String password = result.getString("password");
	            String name = result.getString("name");
	            int user_type = result.getInt("user_type");
	            
	            
	            // Tạo token với thông tin quyền hạn
	            Algorithm algorithm = Algorithm.HMAC256("secret_key");
	            token = JWT.create()
	                    .withClaim("id_user", id_user)
	                    .withClaim("username", username)
	                    .withClaim("password", password)
	                    .withClaim("name", name)
	                    .withClaim("user_type", user_type)
	                    .withExpiresAt(new Date(System.currentTimeMillis() + 600000))
	                    .sign(algorithm);
	            
	            // Trả về token trong header và status code 200 OK
	            HttpHeaders headers = new HttpHeaders();
	            headers.add("Authorization", "Bearer " + token);
	            headers.add("Content-Type", "application/json"); // Thêm header Content-Type
	            return ResponseEntity.ok().headers(headers).body(token);
	        } else {
	            // Trả về status code 401 Unauthorized nếu thông tin đăng nhập không hợp lệ
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Trả về status code 500 Internal Server Error nếu có lỗi xảy ra
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } finally {
	        // Đóng kết nối và statement
	        try {
	            if(connection != null) {
	                connection.close();
	            }
	            if (ps != null) {
	                ps.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	

	// Hàm đăng xuất
	@PostMapping("/property/log-out")
	public ResponseEntity<String> logOut(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // Hủy session
		}
		return ResponseEntity.ok("Logged out successfully.");
	}
	

	@PutMapping("/user/update_auth_info/{id_user}")
	public ResponseEntity<Map<String, String>> updateUserInfo(@RequestBody User user, @PathVariable String id_user) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		if (user.getUsername() == null || user.getUsername().isEmpty()) {
			response.put("message", "Username must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			response.put("message", "Password must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			// Kiểm tra sự trùng lặp của username trước khi cập nhật
			ps = connection.prepareStatement("SELECT username FROM user WHERE username = ? AND id_user <> ?");
			ps.setString(1, user.getUsername());
			ps.setString(2, id_user);
			rs = ps.executeQuery();

			if (rs.next()) {
				response.put("message", "Username already exists");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			// Cập nhật thông tin người dùng
			ps = connection.prepareStatement("UPDATE user SET username = ?, password = ? WHERE id_user = ?");
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, id_user);
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
}
