package com.example.demo.Receipt;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Receipt;
import com.example.demo.Model.TenantInContract;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class ReceiptController {
	@GetMapping("/receipt/{id_owner}")
	public ResponseEntity<List<Receipt>> getContractList(Model model, @PathVariable String id_owner)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Receipt> receipt_list = new ArrayList<Receipt>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement(
					"SELECT * FROM `tester`.`receipt` WHERE `id_owner` = ? order by `id_receipt` DESC");
			ps.setInt(1, Integer.valueOf(id_owner));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_receipt = resultSet.getInt("id_receipt");
				int id_invoice = resultSet.getInt("id_invoice");
				String label = resultSet.getString("label");
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

				String image = resultSet.getString("image");
				String note = resultSet.getString("note");
				Date created_date = resultSet.getDate("created_date");
				Date updated_date = resultSet.getDate("updated_date");
				String username1 = resultSet.getString("username");
				String name = resultSet.getString("name");
				String phone_number = resultSet.getString("phone_number");
				String email = resultSet.getString("email");
				

				receipt_list.add(new Receipt(id_receipt, id_invoice, label, id_owner1, tenant_list,
						image, note, created_date, updated_date, username1, name, phone_number, email));
			}

			return new ResponseEntity<>(receipt_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/receipt_tenant/{username}")
	public ResponseEntity<List<Receipt>> getContractListForTenant(Model model, @PathVariable String username)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Receipt> receipt_list = new ArrayList<Receipt>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement(
					"SELECT * FROM tester.receipt "
					+ "WHERE JSON_CONTAINS(tenants, JSON_OBJECT('username', ?)) order by `id_receipt` DESC;");
			ps.setString(1, username);

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_receipt = resultSet.getInt("id_receipt");
				int id_invoice = resultSet.getInt("id_invoice");
				String label = resultSet.getString("label");
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

				String image = resultSet.getString("image");
				String note = resultSet.getString("note");
				Date created_date = resultSet.getDate("created_date");
				Date updated_date = resultSet.getDate("updated_date");
				String username1 = resultSet.getString("username");
				String name = resultSet.getString("name");
				String phone_number = resultSet.getString("phone_number");
				String email = resultSet.getString("email");
				

				receipt_list.add(new Receipt(id_receipt, id_invoice, label, id_owner1, tenant_list,
						image, note, created_date, updated_date, username1, name, phone_number, email));
			}

			return new ResponseEntity<>(receipt_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/receipt/create")
	public ResponseEntity<Map<String, String>> addContract(@RequestBody Receipt receipt) {
		Map<String, String> response = new HashMap<>();

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();
			String tenantJson = objectMapper.writeValueAsString(receipt.getTenant_list());

			String query = "INSERT INTO `tester`.`receipt` "
					+ "(`id_invoice`, `label`, `id_owner`, `tenants`, `image`, `note`, "
					+ "`username`, `name`, `phone_number`, `email` )"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = connection.prepareStatement(query);

			ps.setInt(1, receipt.getId_invoice());
			ps.setString(2, receipt.getLabel());
			ps.setInt(3, receipt.getId_owner());
			ps.setString(4, tenantJson);
			ps.setString(5, receipt.getImage());
			ps.setString(6, receipt.getNote());
			ps.setString(7, receipt.getUsername());
			ps.setString(8, receipt.getName());
			ps.setString(9, receipt.getPhone_number());
			ps.setString(10, receipt.getEmail());

			ps.executeUpdate();
			response.put("message", "Receipt added successfully");
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
	
	@GetMapping("/receipt_detail/{id_receipt}")
	public ResponseEntity<Receipt> getHandOverDetail(Model model, @PathVariable String id_receipt) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		Receipt receipt = new Receipt();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`receipt` WHERE id_receipt = ?");
			ps.setInt(1, Integer.valueOf(id_receipt));
			result = ps.executeQuery();

			while (result.next()) {

				receipt.setId_receipt(result.getInt("id_receipt"));
				receipt.setId_invoice(result.getInt("id_invoice"));
				receipt.setLabel(result.getString("label"));
				receipt.setId_owner(result.getInt("id_owner"));

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
				receipt.setImage(result.getString("image"));
				receipt.setNote(result.getString("note"));
				receipt.setCreated_date(result.getDate("created_date"));
				receipt.setUpdated_date(result.getDate("updated_date"));
				receipt.setUsername(result.getString("username"));
				receipt.setName(result.getString("name"));
				receipt.setPhone_number(result.getString("phone_number"));
				receipt.setEmail(result.getString("email"));
			}

			return new ResponseEntity<>(receipt, HttpStatus.OK);

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
}
