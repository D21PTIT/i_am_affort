package com.example.demo.News;

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

import com.example.demo.Model.Img;
import com.example.demo.Model.News;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class NewsController {
	@GetMapping("/news_list")
	public ResponseEntity<List<News>> getNewsList(Model model) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<News> news_list = new ArrayList<News>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM `tester`.`news` WHERE `delete` = 0");

			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_news = resultSet.getInt("id_news");
				String label = resultSet.getString("label");
				String content = resultSet.getString("content");
				int status = resultSet.getInt("status");
				int created_by = resultSet.getInt("created_by");

				String imgJson = resultSet.getString("img");
				List<Img> img_list = objectMapper.readValue(imgJson, new TypeReference<List<Img>>() {
				});

				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");

				news_list.add(new News(id_news, label, content, status, created_by, img_list, created_at, updated_at));
			}
			return new ResponseEntity<>(news_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/news/create")
	public ResponseEntity<Map<String, String>> addNews(@RequestBody News news) {

		Map<String, String> response = new HashMap<>();

		if (news.getLabel() == null || news.getLabel().isEmpty()) {
			response.put("message", "Label must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (news.getContent() == null || news.getContent().isEmpty()) {
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

			ObjectMapper objectMapper = new ObjectMapper();
			String imgJson = objectMapper.writeValueAsString(news.getImgList());

			ps = connection.prepareStatement(
					"INSERT INTO `tester`.`news` (`label`, `content`, `img`) VALUES (?, ?, ?);");
			ps.setString(1, news.getLabel());
			ps.setString(2, news.getContent());
			ps.setString(3, imgJson);

			ps.executeUpdate();
			response.put("message", "News added successfully");
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
	
	@PutMapping("/news/update/{id_news}")
	public ResponseEntity<Map<String, String>> updateNews(@RequestBody News news, @PathVariable String id_news) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		if (news.getLabel() == null || news.getLabel().isEmpty()) {
			response.put("message", "Label must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (news.getContent() == null || news.getContent().isEmpty()) {
			response.put("message", "Content must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}


		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			
			ObjectMapper objectMapper = new ObjectMapper();
			String imgJson = objectMapper.writeValueAsString(news.getImgList());

			ps = connection.prepareStatement(
					"UPDATE news SET label = ?, content = ?, status = ?, img = ? WHERE id_news = ?;");
			
			ps.setString(1, news.getLabel());
			ps.setString(2, news.getContent());
			ps.setInt(3, Integer.valueOf(news.getStatus()));
			ps.setString(4, imgJson);
			ps.setInt(5, Integer.valueOf(news.getId_news()));

			ps.executeUpdate();
			response.put("message", "News updated successfully!");

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
	
	@PutMapping("/news/delete/{id_news}")
	public ResponseEntity<Map<String, String>> deleteNews(@RequestBody News news, @PathVariable String id_news) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE news SET `delete` = 1 WHERE id_news = ?");
			ps.setInt(1, Integer.valueOf(news.getId_news()));

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
