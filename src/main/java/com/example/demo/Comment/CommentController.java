package com.example.demo.Comment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.example.demo.Model.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class CommentController {
	@GetMapping("/comment_list/{id_post}")
	public ResponseEntity<List<Comment>> getCommentList(Model model, @PathVariable String id_post) throws IOException {
		model.addAttribute("id_post", id_post);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		List<Comment> comment_list = new ArrayList<Comment>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			
			ps = connection.prepareStatement("SELECT * FROM `tester`.`comment` WHERE `delete` = 0 AND `id_post` = ?;");
			ps.setInt(1, Integer.valueOf(id_post));
			resultSet = ps.executeQuery();

			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_cmt = resultSet.getInt("id_comment");
				int id_user = resultSet.getInt("id_user");
				String user_name = resultSet.getString("user_name");
				String content = resultSet.getString("content");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");

				comment_list.add(new Comment(id_cmt, id_user, user_name, content, created_at, updated_at));
			}
			return new ResponseEntity<>(comment_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/comment/create")
	public ResponseEntity<Map<String, String>> addComment(@RequestBody Comment comment) {

		Map<String, String> response = new HashMap<>();

		if (comment.getContent() == null || comment.getContent().isEmpty()) {
			response.put("message", "Content must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}


		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"INSERT INTO `tester`.`comment` (`id_user`, `user_name`, `id_post`, `content`) VALUES (?, ?, ?, ?);");
			ps.setInt(1, Integer.valueOf(comment.getId_user()));
			ps.setString(2, comment.getUser_name());
			ps.setInt(3, Integer.valueOf(comment.getId_post()));
			ps.setString(4, comment.getContent());

			ps.executeUpdate();
			response.put("message", "Comment added successfully");
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
	
	@PutMapping("/user/delete/{id_comment}")
	public ResponseEntity<Map<String, String>> deleteComment(@RequestBody Comment comment, @PathVariable String id_comment) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE comment SET `delete` = 1 WHERE id_comment = ?");
			ps.setInt(1, Integer.valueOf(comment.getId_comment()));

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
