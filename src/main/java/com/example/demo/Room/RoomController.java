package com.example.demo.Room;

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

import com.example.demo.Model.Direction;
import com.example.demo.Model.Doc;
import com.example.demo.Model.Room;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class RoomController {
	@GetMapping("/room_list")
	public ResponseEntity<List<Room>> getRoomList(Model model) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Room> room_list = new ArrayList<Room>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM `tester`.`room` WHERE `delete` = 0 order by `id_room` DESC");

			while (resultSet.next()) {

				int id_room = resultSet.getInt("id_room");
				String name = resultSet.getString("name");
				float area = resultSet.getFloat("area");
				int bathroom = resultSet.getInt("bathroom");
				int bedroom = resultSet.getInt("bedroom");
				int kitchen = resultSet.getInt("kitchen");
				String interior = resultSet.getString("interior");
				int balcony = resultSet.getInt("balcony");
				int status = resultSet.getInt("status");
				int max_people = resultSet.getInt("max_people");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");
				int id_property = resultSet.getInt("id_property");
				float price = resultSet.getFloat("price");
				int frequency = resultSet.getInt("frequency");
				int id_owner = resultSet.getInt("id_owner");

				room_list.add(new Room(id_room, name, area, bathroom, bedroom, kitchen, interior, balcony, status, max_people,
						created_at, updated_at, id_property, frequency, id_owner));
			}
			return new ResponseEntity<>(room_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/property_room_list/{id_property}")
	public ResponseEntity<List<Room>> getPropertyRoomList(Model model, @PathVariable String id_property)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Room> room_list = new ArrayList<Room>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement("SELECT * FROM `tester`.`room` WHERE `delete` = 0 AND `id_property` = ? order by `id_room` DESC");
			ps.setInt(1, Integer.valueOf(id_property));

			resultSet = ps.executeQuery();

			while (resultSet.next()) {

				int id_room = resultSet.getInt("id_room");
				String name = resultSet.getString("name");
				float area = resultSet.getFloat("area");
				int bathroom = resultSet.getInt("bathroom");
				int bedroom = resultSet.getInt("bedroom");
				int kitchen = resultSet.getInt("kitchen");
				String interior = resultSet.getString("interior");
				int balcony = resultSet.getInt("balcony");
				int status = resultSet.getInt("status");
				int max_people = resultSet.getInt("max_people");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");
				int id_prop = Integer.parseInt(id_property);
				float price = resultSet.getFloat("price");
				int frequency = resultSet.getInt("frequency");
				int id_owner = resultSet.getInt("id_owner");

				room_list.add(new Room(id_room, name, area, bathroom,bedroom, kitchen, interior, balcony, status, max_people,
						created_at, updated_at, id_prop, price, frequency, id_owner));
			}
			return new ResponseEntity<>(room_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/owner_all_room/{id_owner}")
	public ResponseEntity<List<Room>> getPropertyRoomListByOwner(Model model, @PathVariable String id_owner)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Room> room_list = new ArrayList<Room>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement("SELECT * FROM `tester`.`room` WHERE `delete` = 0 AND `id_owner` = ? order by `id_room` DESC");
			ps.setInt(1, Integer.valueOf(id_owner));

			resultSet = ps.executeQuery();

			while (resultSet.next()) {

				int id_room = resultSet.getInt("id_room");
				String name = resultSet.getString("name");
				float area = resultSet.getFloat("area");
				int bathroom = resultSet.getInt("bathroom");
				int bedroom = resultSet.getInt("bedroom");
				int kitchen = resultSet.getInt("kitchen");
				String interior = resultSet.getString("interior");
				int balcony = resultSet.getInt("balcony");
				int status = resultSet.getInt("status");
				int max_people = resultSet.getInt("max_people");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");
				int id_prop = resultSet.getInt("id_property");
				float price = resultSet.getFloat("price");
				int frequency = resultSet.getInt("frequency");
				int id_owner1 = resultSet.getInt("id_owner");

				room_list.add(new Room(id_room, name, area, bathroom,bedroom, kitchen, interior, balcony, status, max_people,
						created_at, updated_at, id_prop, price, frequency, id_owner1));
			}
			return new ResponseEntity<>(room_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/room_detail/{id_room}")
	public ResponseEntity<Room> getRoomDetail(Model model, @PathVariable String id_room) {
		model.addAttribute("id_room", id_room);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		Room room = new Room();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`room` WHERE id_room = ?");
			ps.setInt(1, Integer.valueOf(id_room));
			result = ps.executeQuery();
			while (result.next()) {
				room.setId_room(result.getInt("id_room"));
				room.setName(result.getString("name"));
				room.setArea(result.getFloat("area"));
				room.setBathroom(result.getInt("bathroom"));
				room.setBedroom(result.getInt("bedroom"));
				room.setKitchen(result.getInt("kitchen"));
				room.setInterial(result.getString("interior"));
				room.setBalcony(result.getInt("balcony"));
				room.setStatus(result.getInt("status"));
				room.setCreated_at(result.getDate("created_at"));
				room.setUpdated_at(result.getDate("updated_at"));
				room.setId_property(result.getInt("id_property"));
				room.setPrice(result.getFloat("price"));
				room.setFrequency(result.getInt("frequency"));

			} // end of while block

			return new ResponseEntity<>(room, HttpStatus.OK);

		} // end of try block

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/room/create")
	public ResponseEntity<Map<String, String>> addRoom(@RequestBody Room room) {

		Map<String, String> response = new HashMap<>();

		if (room.getName() == null || room.getName().isEmpty()) {
			response.put("message", "Room's name must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			String sql = "INSERT INTO `tester`.`room`" + "(`name`, `area`, `bathroom`, `bedroom`, `kitchen`, "
					+ "`interior`, `balcony`, `status`, `max_people`, " + "`id_property`, `price`, `frequency`, `id_owner`) "
					+ "VALUES " + "(?, ?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, ?);";

			// Chuẩn bị câu lệnh PreparedStatement
			ps = connection.prepareStatement(sql);
			ps.setString(1, room.getName());
			ps.setFloat(2, room.getArea());
			ps.setInt(3, Integer.valueOf(room.getBathroom()));
			ps.setInt(4, Integer.valueOf(room.getBedroom()));
			ps.setInt(5, Integer.valueOf(room.getKitchen()));
			ps.setString(6, room.getInterial());
			ps.setInt(7, Integer.valueOf(room.getBalcony()));
			ps.setInt(8, Integer.valueOf(room.getStatus()));
			ps.setInt(9, Integer.valueOf(room.getMax_people()));
			ps.setInt(10, Integer.valueOf(room.getId_property()));
			ps.setFloat(11, room.getPrice());
			ps.setInt(12, Integer.valueOf(room.getFrequency()));
			ps.setInt(13, Integer.valueOf(room.getId_owner()));

			System.out.println(sql);

			ps.executeUpdate();
			response.put("message", "Room added successfully");
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

	@PutMapping("/room/update_info/{id_room}")
	public ResponseEntity<Map<String, String>> updateRoom(@RequestBody Room room, @PathVariable String id_room) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		// Kiểm tra các trường name, email không phải là null hoặc chuỗi rỗng
		if (room.getName() == null || room.getName().isEmpty()) {
			response.put("message", "Name not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			// Start building the SQL query
			String sql = "UPDATE `tester`.`room` " + "SET " + "`name` = ?, `area` = ?, `bathroom` = ?, `bedroom` = ?, "
					+ "`kitchen` = ?, `interior` = ?, `balcony` = ?, "
					+ "`status` = ?, `max_people` = ?, `id_property` = ?, " + "`price` = ?, `frequency` = ? "
					+ "WHERE (`id_room` = ?);";

			// Prepare the PreparedStatement
			ps = connection.prepareStatement(sql);

			ps.setString(1, room.getName());
			ps.setFloat(2, room.getArea());
			ps.setInt(3, Integer.valueOf(room.getBathroom()));
			ps.setInt(4, Integer.valueOf(room.getBedroom()));
			ps.setInt(5, Integer.valueOf(room.getKitchen()));
			ps.setString(6, room.getInterial());
			ps.setInt(7, Integer.valueOf(room.getBalcony()));
			ps.setInt(8, Integer.valueOf(room.getStatus()));
			ps.setInt(9, Integer.valueOf(room.getMax_people()));
			if (Integer.valueOf(room.getId_property()) != 0) {
				ps.setInt(10, Integer.valueOf(room.getId_property()));
			} else {
				ps.setString(10, "");
			}
			ps.setFloat(11, Float.valueOf(room.getPrice()));
			ps.setInt(12, Integer.valueOf(room.getFrequency()));
			ps.setInt(13, Integer.parseInt(id_room));

			ps.executeUpdate();
			response.put("message", "Room updated successfully!");

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
	
	@PutMapping("/room/delete/{id_room}")
	public ResponseEntity<Map<String, String>> deleteRoom(@RequestBody Room room, @PathVariable String id_room) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement(
					"UPDATE room SET `delete` = 1 WHERE id_room = ?");
			ps.setInt(1, Integer.valueOf(room.getId_room()));

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
