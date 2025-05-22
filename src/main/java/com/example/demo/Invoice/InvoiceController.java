package com.example.demo.Invoice;

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

import com.example.demo.Model.Invoice;
import com.example.demo.Model.TenantInContract;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class InvoiceController {
	@GetMapping("/invoice/{id_owner}")
	public ResponseEntity<List<Invoice>> getInvoiceList(Model model, @PathVariable String id_owner) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Invoice> invoice_list = new ArrayList<Invoice>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement(
					"SELECT * FROM `tester`.`invoice` WHERE `delete` = 0 AND `id_owner` = ? order by `id_invoice` DESC");
			ps.setInt(1, Integer.valueOf(id_owner));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_invoice = resultSet.getInt("id_invoice");
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

				Date created_date = resultSet.getDate("created_date");
				Date updated_date = resultSet.getDate("updated_date");
				int rent_month = resultSet.getInt("rent_month");
				int people = resultSet.getInt("people");
				float deposit = resultSet.getFloat("deposit");
				float rent_price = resultSet.getFloat("rent_price");
				int rent_price_type = resultSet.getInt("rent_price_type");
				float electric = resultSet.getFloat("electric");
				int electric_num = resultSet.getInt("electric_num");
				float water = resultSet.getFloat("water");
				int water_type = resultSet.getInt("water_type");
				int water_num = resultSet.getInt("water_num");
				float internet = resultSet.getFloat("internet");
				float clean = resultSet.getFloat("clean");
				float elevator = resultSet.getFloat("elevator");
				float other = resultSet.getFloat("other");
				float add = resultSet.getFloat("add");
				float deduct = resultSet.getFloat("deduct");
				float total = resultSet.getFloat("total");
				String note = resultSet.getString("note");
				int delete = resultSet.getInt("delete");
				int status = resultSet.getInt("status");
				int deposit_amount = resultSet.getInt("deposit_amount");

				invoice_list.add(new Invoice(id_invoice, label, id_contract, id_property, property_name, id_room,
						room_code, id_owner1, tenant_list, created_date, updated_date, rent_month, people, deposit,
						rent_price, rent_price_type, electric, electric_num, water, water_type, water_num, internet,
						clean, elevator, other, add, deduct, total, note, delete, status, deposit_amount));
			}

			return new ResponseEntity<>(invoice_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/invoice_for_tenant/{username}")
	public ResponseEntity<List<Invoice>> getContractListForTenant(Model model, @PathVariable String username)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Invoice> invoice_list = new ArrayList<Invoice>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();

			ps = connection.prepareStatement("SELECT * FROM tester.invoice "
					+ "WHERE JSON_CONTAINS(tenants, JSON_OBJECT('username', ?))AND `delete` = 0 order by `id_invoice` DESC");
			ps.setString(1, username);

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_invoice = resultSet.getInt("id_invoice");
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

				Date created_date = resultSet.getDate("created_date");
				Date updated_date = resultSet.getDate("updated_date");
				int rent_month = resultSet.getInt("rent_month");
				int people = resultSet.getInt("people");
				float deposit = resultSet.getFloat("deposit");
				float rent_price = resultSet.getFloat("rent_price");
				int rent_price_type = resultSet.getInt("rent_price_type");
				float electric = resultSet.getFloat("electric");
				int electric_num = resultSet.getInt("electric_num");
				float water = resultSet.getFloat("water");
				int water_type = resultSet.getInt("water_type");
				int water_num = resultSet.getInt("water_num");
				float internet = resultSet.getFloat("internet");
				float clean = resultSet.getFloat("clean");
				float elevator = resultSet.getFloat("elevator");
				float other = resultSet.getFloat("other");
				float add = resultSet.getFloat("add");
				float deduct = resultSet.getFloat("deduct");
				float total = resultSet.getFloat("total");
				String note = resultSet.getString("note");
				int delete = resultSet.getInt("delete");
				int status = resultSet.getInt("status");
				int deposit_amount = resultSet.getInt("deposit_amount");

				invoice_list.add(new Invoice(id_invoice, label, id_contract, id_property, property_name, id_room,
						room_code, id_owner1, tenant_list, created_date, updated_date, rent_month, people, deposit,
						rent_price, rent_price_type, electric, electric_num, water, water_type, water_num, internet,
						clean, elevator, other, add, deduct, total, note, delete, status, deposit_amount));
			}

			return new ResponseEntity<>(invoice_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/invoice/create")
	public ResponseEntity<Map<String, String>> addContract(@RequestBody Invoice invoice) {
		Map<String, String> response = new HashMap<>();

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();
			String tenantJson = objectMapper.writeValueAsString(invoice.getTenant_list());

			String query = "INSERT INTO `tester`.`invoice` " + "(`label`, `id_contract`, `id_property`, "
					+ "`property_name`, `id_room`, `room_code`, " + "`id_owner`, `tenants`, `rent_month`, "
					+ "`people`, `deposit`, `rent_price`, `rent_price_type`, " + "`electric`, `electric_num`, `water`, "
					+ "`water_type`, `water_num`, `internet`, `clean`, " + "`elevator`, `other`, `add`, `deduct`, "
					+ "`total`, `note`, `status`, `deposit_amount`) " + "VALUES (?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, invoice.getLabel());
			ps.setInt(2, invoice.getId_contract());
			ps.setInt(3, invoice.getId_property());
			ps.setString(4, invoice.getProperty_name());
			ps.setInt(5, invoice.getId_room());
			ps.setString(6, invoice.getRoom_code());
			ps.setInt(7, invoice.getId_owner());
			ps.setString(8, tenantJson);
			ps.setInt(9, invoice.getRent_month());
			ps.setInt(10, invoice.getPeople());
			ps.setFloat(11, invoice.getDeposit());
			ps.setFloat(12, invoice.getRent_price());
			ps.setInt(13, invoice.getRent_price_type());
			ps.setFloat(14, invoice.getElectric());
			ps.setInt(15, invoice.getElectric_num());
			ps.setFloat(16, invoice.getWater());
			ps.setInt(17, invoice.getWater_type());
			ps.setInt(18, invoice.getWater_num());
			ps.setFloat(19, invoice.getInternet());
			ps.setFloat(20, invoice.getClean());
			ps.setFloat(21, invoice.getElevator());
			ps.setFloat(22, invoice.getOther());
			ps.setFloat(23, invoice.getAdd());
			ps.setFloat(24, invoice.getDeduct());
			ps.setFloat(25, invoice.getTotal());
			ps.setString(26, invoice.getNote());
			ps.setInt(27, invoice.getStatus());
			ps.setInt(28, invoice.getDeposit_amount());

			ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
	        if (rs.next()) {
	            int newInvoiceId = rs.getInt(1);
	            response.put("id_invoice", String.valueOf(newInvoiceId));
	        }
			
			response.put("message", "Invoice added successfully");
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

	@PutMapping("/invoice/update/{id_invoice}")
	public ResponseEntity<Map<String, String>> updateHandOver(@RequestBody Invoice invoice,
			@PathVariable String id_invoice) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();
			String tenantJson = objectMapper.writeValueAsString(invoice.getTenant_list());

			String query = "UPDATE `tester`.`invoice` " + "SET "
					+ "`label` = ?, `id_contract` = ?, `id_property` = ?, "
					+ "`property_name` = ?, `id_room` = ?, `room_code` = ?, "
					+ "`id_owner` = ?, `tenants` = ?, `rent_month` = ?, "
					+ "`people` = ?, `deposit` = ?, `rent_price` = ?, `rent_price_type` = ?, "
					+ "`electric` = ?, `electric_num` = ?, `water` = ?, "
					+ "`water_type` = ?, `water_num` = ?, `internet` = ?, `clean` = ?, "
					+ "`elevator` = ?, `other` = ?, `add` = ?, " + "`deduct` = ?, `total` = ?, `note` = ?, "
					+ "`status` = ?, `deposit_amount` = ? " + "WHERE (`id_invoice` = ?);";

			ps = connection.prepareStatement(query);

			ps.setString(1, invoice.getLabel());
			ps.setInt(2, invoice.getId_contract());
			ps.setInt(3, invoice.getId_property());
			ps.setString(4, invoice.getProperty_name());
			ps.setInt(5, invoice.getId_room());
			ps.setString(6, invoice.getRoom_code());
			ps.setInt(7, invoice.getId_owner());
			ps.setString(8, tenantJson);
			ps.setInt(9, invoice.getRent_month());
			ps.setInt(10, invoice.getPeople());
			ps.setFloat(11, invoice.getDeposit());
			ps.setFloat(12, invoice.getRent_price());
			ps.setInt(13, invoice.getRent_price_type());
			ps.setFloat(14, invoice.getElectric());
			ps.setInt(15, invoice.getElectric_num());
			ps.setFloat(16, invoice.getWater());
			ps.setInt(17, invoice.getWater_type());
			ps.setInt(18, invoice.getWater_num());
			ps.setFloat(19, invoice.getInternet());
			ps.setFloat(20, invoice.getClean());
			ps.setFloat(21, invoice.getElevator());
			ps.setFloat(22, invoice.getOther());
			ps.setFloat(23, invoice.getAdd());
			ps.setFloat(24, invoice.getDeduct());
			ps.setFloat(25, invoice.getTotal());
			ps.setString(26, invoice.getNote());
			ps.setInt(27, invoice.getStatus());
			ps.setInt(28, invoice.getDeposit_amount());
			ps.setInt(29, Integer.valueOf(id_invoice));

			ps.executeUpdate();

			response.put("message", "Invoice updated successfully hihihi!");
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
	
	@PutMapping("/invoice/update_status/{id_invoice}")
	public ResponseEntity<Map<String, String>> updateStatusInvoice(@RequestBody Invoice invoice,
			@PathVariable String id_invoice) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();
			String tenantJson = objectMapper.writeValueAsString(invoice.getTenant_list());

			String query = "UPDATE `tester`.`invoice` SET "
					+ "`status` = '1' WHERE (`id_invoice` = '1');";

			ps = connection.prepareStatement(query);

			ps.setInt(1, Integer.valueOf(id_invoice));

			ps.executeUpdate();

			response.put("message", "Invoice updated successfully hihihi!");
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

	@GetMapping("/invoice/detail/{id_invoice}")
	public ResponseEntity<Invoice> getHandOverDetail(Model model, @PathVariable String id_invoice) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		Invoice invoice = new Invoice();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`invoice` WHERE id_invoice = ?");
			ps.setInt(1, Integer.valueOf(id_invoice));
			result = ps.executeQuery();

			while (result.next()) {

				invoice.setId_invoice(result.getInt("id_invoice"));
				invoice.setLabel(result.getString("label"));
				invoice.setId_contract(result.getInt("id_contract"));
				invoice.setId_property(result.getInt("id_property"));
				invoice.setProperty_name(result.getString("property_name"));
				invoice.setId_room(result.getInt("id_room"));
				invoice.setRoom_code(result.getString("room_code"));
				invoice.setId_owner(result.getInt("id_owner"));

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
				invoice.setTenant_list(tenant_list);
				invoice.setCreated_date(result.getDate("created_date"));
				invoice.setUpdated_date(result.getDate("updated_date"));
				invoice.setRent_month(result.getInt("rent_month"));
				invoice.setPeople(result.getInt("people"));
				invoice.setDeposit(result.getFloat("deposit"));
				invoice.setRent_price(result.getFloat("rent_price"));
				invoice.setRent_price_type(result.getInt("rent_price_type"));
				invoice.setElectric(result.getFloat("electric"));
				invoice.setElectric_num(result.getInt("electric_num"));
				invoice.setWater(result.getFloat("water"));
				invoice.setWater_type(result.getInt("water_type"));
				invoice.setWater_num(result.getInt("water_num"));
				invoice.setInternet(result.getFloat("internet"));
				invoice.setClean(result.getFloat("clean"));
				invoice.setElevator(result.getFloat("elevator"));
				invoice.setOther(result.getFloat("other"));
				invoice.setAdd(result.getFloat("add"));
				invoice.setDeduct(result.getFloat("deduct"));
				invoice.setTotal(result.getFloat("total"));
				invoice.setNote(result.getString("note"));
				invoice.setStatus(result.getInt("status"));
				invoice.setDeposit_amount(result.getInt("deposit_amount"));

			}

			return new ResponseEntity<>(invoice, HttpStatus.OK);

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

	@PutMapping("/invoice/delete/{id_invoice}")
	public ResponseEntity<Map<String, String>> deleteContract(@RequestBody Invoice invoice,
			@PathVariable String id_invoice) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement("UPDATE invoice SET `delete` = 1 WHERE id_invoice = ?");
			ps.setInt(1, Integer.valueOf(invoice.getId_invoice()));

			ps.executeUpdate();
			response.put("message", "Invoice deleted successfully!");

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
