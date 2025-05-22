package com.example.demo.HandOver;

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

import com.example.demo.Model.HandOver;
import com.example.demo.Model.TenantInContract;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class HandOverController {
	@GetMapping("/hand_over/{id_owner}")
	public ResponseEntity<List<HandOver>> getContractList(Model model, @PathVariable String id_owner)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<HandOver> hand_over_list = new ArrayList<HandOver>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement(
					"SELECT * FROM `tester`.`hand_over` WHERE `delete` = 0 AND `id_owner` = ? order by `id_hand_over` DESC");
			ps.setInt(1, Integer.valueOf(id_owner));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_hand_over = resultSet.getInt("id_hand_over");
				String label = resultSet.getString("label");
				int id_contract = resultSet.getInt("id_contract");
				int id_property = resultSet.getInt("id_property");
				String property_name = resultSet.getString("property_name");
				int id_room = resultSet.getInt("id_room");
				String room_code = resultSet.getString("room_code");
				int id_owner1 = resultSet.getInt("id_owner");

				List<TenantInContract> tenant_list = new ArrayList<>();
				String tenant_js = resultSet.getString("tenants");
				if (tenant_js != null && !tenant_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (tenant_js.matches("\\d+")) {
							TenantInContract singleTenant = new TenantInContract();
							// Xử lý tùy theo logic của bạn
							tenant_list.add(singleTenant);
						} else {
							// Thử parse như một mảng JSON
							try {
								tenant_list = objectMapper.readValue(tenant_js,
										new TypeReference<List<TenantInContract>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									TenantInContract singleTenant = objectMapper.readValue(tenant_js,
											TenantInContract.class);
									tenant_list.add(singleTenant);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Date date = resultSet.getDate("date");
				Date created_date = resultSet.getDate("created_date");
				Date updated_date = resultSet.getDate("updated_date");
				String content = resultSet.getString("content");

				hand_over_list.add(new HandOver(id_hand_over, label, id_contract, id_property, property_name, id_room,
						room_code, id_owner1, tenant_list, created_date, updated_date, date, content));
			}

			return new ResponseEntity<>(hand_over_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/hand_over_for_tenant/{username}")
	public ResponseEntity<List<HandOver>> getContractListForTenant(Model model, @PathVariable String username)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<HandOver> hand_over_list = new ArrayList<HandOver>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement("SELECT * FROM tester.hand_over "
					+ "WHERE JSON_CONTAINS(tenants, JSON_OBJECT('username', ?)) AND `delete` = 0 order by `id_hand_over` DESC");
			ps.setString(1, username);

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_hand_over = resultSet.getInt("id_hand_over");
				String label = resultSet.getString("label");
				int id_contract = resultSet.getInt("id_contract");
				int id_property = resultSet.getInt("id_property");
				String property_name = resultSet.getString("property_name");
				int id_room = resultSet.getInt("id_room");
				String room_code = resultSet.getString("room_code");
				int id_owner1 = resultSet.getInt("id_owner");

				List<TenantInContract> tenant_list = new ArrayList<>();
				String tenant_js = resultSet.getString("tenants");
				if (tenant_js != null && !tenant_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (tenant_js.matches("\\d+")) {
							TenantInContract singleTenant = new TenantInContract();
							// Xử lý tùy theo logic của bạn
							tenant_list.add(singleTenant);
						} else {
							// Thử parse như một mảng JSON
							try {
								tenant_list = objectMapper.readValue(tenant_js,
										new TypeReference<List<TenantInContract>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									TenantInContract singleTenant = objectMapper.readValue(tenant_js,
											TenantInContract.class);
									tenant_list.add(singleTenant);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Date date = resultSet.getDate("date");
				Date created_date = resultSet.getDate("created_date");
				Date updated_date = resultSet.getDate("updated_date");
				String content = resultSet.getString("content");

				hand_over_list.add(new HandOver(id_hand_over, label, id_contract, id_property, property_name, id_room,
						room_code, id_owner1, tenant_list, created_date, updated_date, date, content));
			}

			return new ResponseEntity<>(hand_over_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/hand_over/create")
	public ResponseEntity<Map<String, String>> addContract(@RequestBody HandOver handOver) {
		Map<String, String> response = new HashMap<>();

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();
			String tenantJson = objectMapper.writeValueAsString(handOver.getTenant_list());

			String query = "INSERT INTO `tester`.`hand_over` " + "(`label`, `id_contract`, `id_property`, "
					+ "`property_name`, `id_room`, `room_code`, " + "`id_owner`, `tenants`, `date`, `content`) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, handOver.getLabel());
			ps.setInt(2, handOver.getId_contract());
			ps.setInt(3, handOver.getId_property());
			ps.setString(4, handOver.getProperty_name());
			ps.setInt(5, handOver.getId_room());
			ps.setString(6, handOver.getRoom_code());
			ps.setInt(7, handOver.getId_owner());
			ps.setString(8, tenantJson);
			ps.setDate(9, handOver.getDate());
			ps.setString(10, handOver.getContent());

			ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
	        if (rs.next()) {
	            int newHandOverId = rs.getInt(1);
	            response.put("id_hand_over", String.valueOf(newHandOverId));
	        }
			
			response.put("message", "Hand over added successfully");
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

	@PutMapping("/hand_over/update/{id_hand_over}")
	public ResponseEntity<Map<String, String>> updateHandOver(@RequestBody HandOver handOver,
			@PathVariable String id_hand_over) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();
			String tenantJson = objectMapper.writeValueAsString(handOver.getTenant_list());

			String query = "UPDATE `tester`.`hand_over` "
					+ "SET `label` = ?, `id_contract` = ?, `id_property` = ?, "
					+ "`property_name` = ?, `id_room` = ?, `room_code` = ?, "
					+ "`id_owner` = ?, `tenants` = ?, `date` = ?, `content` = ? " + " WHERE (`id_hand_over` = ?);";

			ps = connection.prepareStatement(query);

			ps.setString(1, handOver.getLabel());
			ps.setInt(2, handOver.getId_contract());
			ps.setInt(3, handOver.getId_property());
			ps.setString(4, handOver.getProperty_name());
			ps.setInt(5, handOver.getId_room());
			ps.setString(6, handOver.getRoom_code());
			ps.setInt(7, handOver.getId_owner());
			ps.setString(8, tenantJson);
			ps.setDate(9, handOver.getDate());
			ps.setString(10, handOver.getContent());
			ps.setInt(11, Integer.valueOf(id_hand_over));

			ps.executeUpdate();

			response.put("message", "Hand over updated successfully hihihi!");
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

	@GetMapping("/hand_over/detail/{id_hand_over}")
	public ResponseEntity<HandOver> getHandOverDetail(Model model, @PathVariable String id_hand_over) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		HandOver handOver = new HandOver();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`hand_over` WHERE id_hand_over = ?");
			ps.setInt(1, Integer.valueOf(id_hand_over));
			result = ps.executeQuery();

			while (result.next()) {

				handOver.setId_hand_over(result.getInt("id_hand_over"));
				handOver.setLabel(result.getString("label"));
				handOver.setId_contract(result.getInt("id_contract"));
				handOver.setId_property(result.getInt("id_property"));
				handOver.setProperty_name(result.getString("property_name"));
				handOver.setId_room(result.getInt("id_room"));
				handOver.setRoom_code(result.getString("room_code"));
				handOver.setId_owner(result.getInt("id_owner"));

				// Handle tenants JSON
				List<TenantInContract> tenant_list = new ArrayList<>();
				String tenant_js = result.getString("tenants");
				if (tenant_js != null && !tenant_js.trim().isEmpty()) {
					try {
						if (tenant_js.matches("\\d+")) {
							TenantInContract singleTenant = new TenantInContract();
							tenant_list.add(singleTenant);
						} else {
							try {
								tenant_list = objectMapper.readValue(tenant_js,
										new TypeReference<List<TenantInContract>>() {
										});
							} catch (JsonProcessingException arrayError) {
								try {
									TenantInContract singleTenant = objectMapper.readValue(tenant_js,
											TenantInContract.class);
									tenant_list.add(singleTenant);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				handOver.setTenant_list(tenant_list);
				handOver.setCreated_date(result.getDate("created_date"));
				handOver.setUpdated_date(result.getDate("updated_date"));
				handOver.setDate(result.getDate("date"));
				handOver.setContent(result.getString("content"));
			}

			return new ResponseEntity<>(handOver, HttpStatus.OK);

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

	@PutMapping("/hand_over/delete/{id_hand_over}")
	public ResponseEntity<Map<String, String>> deleteContract(@RequestBody HandOver handOver,
			@PathVariable String id_hand_over) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement("UPDATE hand_over SET `delete` = 1 WHERE id_hand_over = ?");
			ps.setInt(1, Integer.valueOf(handOver.getId_hand_over()));

			ps.executeUpdate();
			response.put("message", "Hand over deleted successfully!");

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
}
