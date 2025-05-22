package com.example.demo.Contract;

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

import com.example.demo.Model.Contract;
import com.example.demo.Model.TenantInContract;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class ContractController {
	@GetMapping("/contract/{id_owner}")
	public ResponseEntity<List<Contract>> getContractList(Model model, @PathVariable String id_owner)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Contract> contract_list = new ArrayList<Contract>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement(
					"SELECT * FROM `tester`.`contract` WHERE `delete` = 0 AND `prop_owner_id` = ? order by `id_contract` DESC");
			ps.setInt(1, Integer.valueOf(id_owner));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_contract = resultSet.getInt("id_contract");
				int prop_owner_id = resultSet.getInt("prop_owner_id");
				String prop_owner_name = resultSet.getString("prop_owner_name");
				int owner_gender = resultSet.getInt("owner_gender");
				String owner_email = resultSet.getString("owner_email");
				String owner_phone = resultSet.getString("owner_phone");
				String owner_dob = resultSet.getString("owner_dob");
				int prop_id = resultSet.getInt("prop_id");
				String prop_name = resultSet.getString("prop_name");
				int room_id = resultSet.getInt("prop_id");
				String room_code = resultSet.getString("room_code");
				int max_pp = resultSet.getInt("max_pp");

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

				float price = resultSet.getFloat("price");
				int price_type = resultSet.getInt("price_type");
				String rule = resultSet.getString("rule");
				int status = resultSet.getInt("status");

				Date start_date = resultSet.getDate("start_date");
				Date end_date = resultSet.getDate("end_date");
				Date created_date = resultSet.getDate("created_date");
				Date updated_date = resultSet.getDate("updated_date");
				float electric = resultSet.getFloat("electric");
				float water = resultSet.getFloat("water");
				int water_type = resultSet.getInt("water_type");
				float internet = resultSet.getFloat("internet");
				float clean = resultSet.getFloat("clean");
				float elevator = resultSet.getFloat("elevator");
				float other_service = resultSet.getFloat("other_service");
				float deposit = resultSet.getFloat("deposit");

				contract_list.add(new Contract(id_contract, prop_owner_id, prop_owner_name, owner_gender, owner_email,
						owner_phone, owner_dob, prop_id, prop_name, room_id, room_code, max_pp, tenant_list, price,
						price_type, rule, status, start_date, end_date, created_date, updated_date, electric, water,
						water_type, internet, clean, elevator, other_service, deposit));
			}

			return new ResponseEntity<>(contract_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/contract_tenant/{username}")
	public ResponseEntity<List<Contract>> getContractListForTenant(Model model, @PathVariable String username)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Contract> contract_list = new ArrayList<Contract>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement("SELECT * FROM tester.contract\r\n"
					+ "WHERE JSON_CONTAINS(tenants, JSON_OBJECT('username', ?)) order by `id_contract` DESC;");
			ps.setString(1, username);

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_contract = resultSet.getInt("id_contract");
				int prop_owner_id = resultSet.getInt("prop_owner_id");
				String prop_owner_name = resultSet.getString("prop_owner_name");
				int owner_gender = resultSet.getInt("owner_gender");
				String owner_email = resultSet.getString("owner_email");
				String owner_phone = resultSet.getString("owner_phone");
				String owner_dob = resultSet.getString("owner_dob");
				int prop_id = resultSet.getInt("prop_id");
				String prop_name = resultSet.getString("prop_name");
				int room_id = resultSet.getInt("prop_id");
				String room_code = resultSet.getString("room_code");
				int max_pp = resultSet.getInt("max_pp");

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

				float price = resultSet.getFloat("price");
				int price_type = resultSet.getInt("price_type");
				String rule = resultSet.getString("rule");
				int status = resultSet.getInt("status");

				Date start_date = resultSet.getDate("start_date");
				Date end_date = resultSet.getDate("end_date");
				Date created_date = resultSet.getDate("created_date");
				Date updated_date = resultSet.getDate("updated_date");
				float electric = resultSet.getFloat("electric");
				float water = resultSet.getFloat("water");
				int water_type = resultSet.getInt("water_type");
				float internet = resultSet.getFloat("internet");
				float clean = resultSet.getFloat("clean");
				float elevator = resultSet.getFloat("elevator");
				float other_service = resultSet.getFloat("other_service");
				float deposit = resultSet.getFloat("deposit");

				contract_list.add(new Contract(id_contract, prop_owner_id, prop_owner_name, owner_gender, owner_email,
						owner_phone, owner_dob, prop_id, prop_name, room_id, room_code, max_pp, tenant_list, price,
						price_type, rule, status, start_date, end_date, created_date, updated_date, electric, water,
						water_type, internet, clean, elevator, other_service, deposit));
			}

			return new ResponseEntity<>(contract_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/contract/create")
	public ResponseEntity<Map<String, String>> addContract(@RequestBody Contract contract) {
	    Map<String, String> response = new HashMap<>();
	    Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");

	        ObjectMapper objectMapper = new ObjectMapper();
	        String tenantJson = objectMapper.writeValueAsString(contract.getTenant_list());

	        String query = "INSERT INTO `tester`.`contract` "
	                + "(`prop_owner_id`, `prop_owner_name`, `owner_gender`, `owner_email`, "
	                + "`owner_phone`, `owner_dob`, `prop_id`, `prop_name`, `room_id`, "
	                + "`room_code`, `max_pp`, `tenants`, `price`, `price_type`, `rule`, `status`, "
	                + "`start_date`, `end_date`, `electric`, `water`, `water_type`, `internet`, `clean`, `elevator`, `other_service`, `deposit`) "
	                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	        // Sử dụng RETURN_GENERATED_KEYS để lấy ID được tạo tự động
	        ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

	        ps.setInt(1, contract.getProp_owner_id());
	        ps.setString(2, contract.getProp_owner_name());
	        ps.setInt(3, contract.getOwner_gender());
	        ps.setString(4, contract.getOwner_email());
	        ps.setString(5, contract.getOwner_phone());
	        ps.setString(6, contract.getOwner_dob());
	        ps.setInt(7, contract.getProp_id());
	        ps.setString(8, contract.getProp_name());
	        ps.setInt(9, contract.getRoom_id());
	        ps.setString(10, contract.getRoom_code());
	        ps.setInt(11, contract.getMax_pp());
	        ps.setString(12, tenantJson);
	        ps.setFloat(13, contract.getPrice());
	        ps.setInt(14, contract.getPrice_type());
	        ps.setString(15, contract.getRule());
	        ps.setInt(16, contract.getStatus());
	        ps.setDate(17, contract.getStart_date());
	        ps.setDate(18, contract.getEnd_date());
	        ps.setFloat(19, contract.getElectric());
	        ps.setFloat(20, contract.getWater());
	        ps.setInt(21, contract.getWater_type());
	        ps.setFloat(22, contract.getInternet());
	        ps.setFloat(23, contract.getClean());
	        ps.setFloat(24, contract.getElevator());
	        ps.setFloat(25, contract.getOther_service());
	        ps.setFloat(26, contract.getDeposit());

	        // Thực hiện insert
	        ps.executeUpdate();

	        // Lấy ID được tạo tự động
	        rs = ps.getGeneratedKeys();
	        if (rs.next()) {
	            int newContractId = rs.getInt(1);
	            response.put("id_contract", String.valueOf(newContractId));
	        }

	        response.put("message", "Contract added successfully");
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

	@PutMapping("/contract/update/{id_contract}")
	public ResponseEntity<Map<String, String>> updateContract(@RequestBody Contract contract,
			@PathVariable String id_contract) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		if (contract.getProp_name() == null || contract.getProp_name().isEmpty()) {
			response.put("message", "Property name must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();
			String tenantJson = objectMapper.writeValueAsString(contract.getTenant_list());

			String query = "UPDATE `tester`.`contract` SET "
					+ "`prop_owner_id` = ?, `prop_owner_name` = ?, `owner_gender` = ?, `owner_email` = ?, "
					+ "`owner_phone` = ?, `owner_dob` = ?, `prop_id` = ?, `prop_name` = ?, `room_id` = ?, "
					+ "`room_code` = ?, `max_pp` = ?, `tenants` = ?, `price` = ?, `price_type` = ?, `rule` = ?, `status` = ?, "
					+ "`start_date` = ?, `end_date` = ?, "
					+ "`electric` = ?, `water` = ?, `water_type` = ?, `internet` = ?, `clean` = ?, `elevator` = ?, `other_service` = ?, `deposit` = ? "
					+ "WHERE `id_contract` = ?";

			ps = connection.prepareStatement(query);

			ps.setInt(1, contract.getProp_owner_id());
			ps.setString(2, contract.getProp_owner_name());
			ps.setInt(3, contract.getOwner_gender());
			ps.setString(4, contract.getOwner_email());
			ps.setString(5, contract.getOwner_phone());
			ps.setString(6, contract.getOwner_dob());
			ps.setInt(7, contract.getProp_id());
			ps.setString(8, contract.getProp_name());
			ps.setInt(9, contract.getRoom_id());
			ps.setString(10, contract.getRoom_code());
			ps.setInt(11, contract.getMax_pp());
			ps.setString(12, tenantJson);
			ps.setFloat(13, contract.getPrice());
			ps.setInt(14, contract.getPrice_type());
			ps.setString(15, contract.getRule());
			ps.setInt(16, contract.getStatus());
			ps.setDate(17, contract.getStart_date());
			ps.setDate(18, contract.getEnd_date());
			ps.setFloat(19, contract.getElectric());
			ps.setFloat(20, contract.getWater());
			ps.setInt(21, contract.getWater_type());
			ps.setFloat(22, contract.getInternet());
			ps.setFloat(23, contract.getClean());
			ps.setFloat(24, contract.getElevator());
			ps.setFloat(25, contract.getOther_service());
			ps.setFloat(26, contract.getDeposit());
			ps.setInt(27, Integer.parseInt(id_contract));

			ps.executeUpdate();
			System.out.println("Update Query: " + ps.toString());
			response.put("message", "Contract updated successfully hihihi!");
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

	@GetMapping("/contract_detail/{id_contract}")
	public ResponseEntity<Contract> getContractDetail(Model model, @PathVariable String id_contract) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		Contract contract = new Contract();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`contract` WHERE id_contract = ?");
			ps.setInt(1, Integer.valueOf(id_contract));
			result = ps.executeQuery();

			while (result.next()) {
				contract.setId_contract(result.getInt("id_contract"));
				contract.setProp_owner_id(result.getInt("prop_owner_id"));
				contract.setProp_owner_name(result.getString("prop_owner_name"));
				contract.setOwner_gender(result.getInt("owner_gender"));
				contract.setOwner_email(result.getString("owner_email"));
				contract.setOwner_phone(result.getString("owner_phone"));
				contract.setOwner_dob(result.getString("owner_dob"));
				contract.setProp_id(result.getInt("prop_id"));
				contract.setProp_name(result.getString("prop_name"));
				contract.setRoom_id(result.getInt("prop_id"));
				contract.setRoom_code(result.getString("room_code"));
				contract.setMax_pp(result.getInt("max_pp"));
				contract.setElectric(result.getFloat("electric"));
				contract.setWater(result.getFloat("water"));
				contract.setWater_type(result.getInt("water_type"));
				contract.setInternet(result.getFloat("internet"));
				contract.setClean(result.getFloat("clean"));
				contract.setElevator(result.getFloat("elevator"));
				contract.setOther_service(result.getFloat("other_service"));

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
				contract.setTenant_list(tenant_list);

				contract.setPrice(result.getFloat("price"));
				contract.setPrice_type(result.getInt("price_type"));
				contract.setRule(result.getString("rule"));
				contract.setStatus(result.getInt("status"));
				contract.setStart_date(result.getDate("start_date"));
				contract.setEnd_date(result.getDate("end_date"));
				contract.setCreated_date(result.getDate("created_date"));
				contract.setUpdated_date(result.getDate("updated_date"));
				contract.setDeposit(result.getFloat("deposit"));
			}

			return new ResponseEntity<>(contract, HttpStatus.OK);

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

	@PutMapping("/contract/delete/{id_contract}")
	public ResponseEntity<Map<String, String>> deleteContract(@RequestBody Contract contract,
			@PathVariable String id_contract) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement("UPDATE contract SET `delete` = 1 WHERE id_contract = ?");
			ps.setInt(1, Integer.valueOf(contract.getId_contract()));

			ps.executeUpdate();
			response.put("message", "Contract deleted successfully!");

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
