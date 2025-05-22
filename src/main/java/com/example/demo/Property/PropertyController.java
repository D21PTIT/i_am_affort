package com.example.demo.Property;

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
import com.example.demo.Model.Property;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;

@RestController
@CrossOrigin
public class PropertyController {
	@GetMapping("/property_list")
	public ResponseEntity<List<Property>> getPropertyList(Model model) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Property> property_list = new ArrayList<Property>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM `tester`.`property` WHERE `delete` = 0 order by `id_property` DESC");

			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_property = resultSet.getInt("id_property");
				String name = resultSet.getString("name");

				String province = resultSet.getString("province");
				String district = resultSet.getString("district");
				String ward = resultSet.getString("ward");
				String detail_address = resultSet.getString("detail_address");

				List<Doc> doc_list = new ArrayList<>();
				String legal_doc = resultSet.getString("legal_doc");
				if (legal_doc != null && !legal_doc.trim().isEmpty()) {
					try {
						

						// Nếu legal_doc là một số nguyên
						if (legal_doc.matches("\\d+")) {
							Doc singleDoc = new Doc();
							// Xử lý tùy theo logic của bạn
							doc_list.add(singleDoc);
						} else {
							// Thử parse như một mảng JSON
							try {
								doc_list = objectMapper.readValue(legal_doc, new TypeReference<List<Doc>>() {
								});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Doc singleDoc = objectMapper.readValue(legal_doc, Doc.class);
									doc_list.add(singleDoc);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				float surface_area = resultSet.getFloat("surface_area");
				float useable_area = resultSet.getFloat("useable_area");
				float width = resultSet.getFloat("width");
				float length = resultSet.getFloat("length");
				int flour = resultSet.getInt("flours");
				int bedrooms = resultSet.getInt("bedrooms");
				int toilets = resultSet.getInt("toilet");

				List<Direction> direction_list = new ArrayList<>();
				String directionJs = resultSet.getString("direction");
				if (directionJs != null && !directionJs.trim().isEmpty()) {
					try {
						

						// Nếu directionJs là một số nguyên
						if (directionJs.matches("\\d+")) {
							Direction singleDirection = new Direction();
							// Xử lý tùy theo logic của bạn
							direction_list.add(singleDirection);
						} else {
							// Thử parse như một mảng JSON
							try {
								direction_list = objectMapper.readValue(directionJs,
										new TypeReference<List<Direction>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Direction singleDirection = objectMapper.readValue(directionJs, Direction.class);
									direction_list.add(singleDirection);
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
				int status = resultSet.getInt("status");
				String note = resultSet.getString("note");

				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");

				int id_user = resultSet.getInt("id_user");

				int created_by_staff = resultSet.getInt("created_by_staff");
				int created_by_user = resultSet.getInt("created_by_user");

				int type = resultSet.getInt("type");

				property_list.add(new Property(id_property, name, province, district, ward, detail_address, doc_list,
						surface_area, useable_area, width, length, flour, bedrooms, toilets, direction_list, price,
						price_type, status, note, id_user, created_at, updated_at, created_by_staff, created_by_user,
						type));
			}
			return new ResponseEntity<>(property_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/property_list/{id_user}")
	public ResponseEntity<List<Property>> getPropertyUserList(@PathVariable String id_user) {
		List<Property> property_list = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester",
				"root", "123456");
				PreparedStatement ps = connection.prepareStatement(
						"SELECT * FROM `tester`.`property` WHERE `delete` = 0 AND `id_user` = ? order by `id_property` DESC");) {
			ps.setInt(1, Integer.valueOf(id_user));

			ObjectMapper objectMapper = new ObjectMapper();
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					int id_property = resultSet.getInt("id_property");
					String name = resultSet.getString("name");
					String province = resultSet.getString("province");
					String district = resultSet.getString("district");
					String ward = resultSet.getString("ward");
					String detail_address = resultSet.getString("detail_address");

					// Xử lý legal_doc an toàn
					List<Doc> doc_list = new ArrayList<>();
					String legal_doc = resultSet.getString("legal_doc");
					if (legal_doc != null && !legal_doc.trim().isEmpty()) {
						try {
							

							// Nếu legal_doc là một số nguyên
							if (legal_doc.matches("\\d+")) {
								Doc singleDoc = new Doc();
								// Xử lý tùy theo logic của bạn
								doc_list.add(singleDoc);
							} else {
								// Thử parse như một mảng JSON
								try {
									doc_list = objectMapper.readValue(legal_doc, new TypeReference<List<Doc>>() {
									});
								} catch (JsonProcessingException arrayError) {
									// Nếu không phải mảng, thử parse như một object đơn
									try {
										Doc singleDoc = objectMapper.readValue(legal_doc, Doc.class);
										doc_list.add(singleDoc);
									} catch (JsonProcessingException objectError) {
										objectError.printStackTrace();
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					float surface_area = resultSet.getFloat("surface_area");
					float useable_area = resultSet.getFloat("useable_area");
					float width = resultSet.getFloat("width");
					float length = resultSet.getFloat("length");
					int flour = resultSet.getInt("flours");
					int bedrooms = resultSet.getInt("bedrooms");
					int toilets = resultSet.getInt("toilet");

					// Xử lý direction an toàn
					List<Direction> direction_list = new ArrayList<>();
					String directionJs = resultSet.getString("direction");
					if (directionJs != null && !directionJs.trim().isEmpty()) {
						try {
							

							// Nếu directionJs là một số nguyên
							if (directionJs.matches("\\d+")) {
								Direction singleDirection = new Direction();
								// Xử lý tùy theo logic của bạn
								direction_list.add(singleDirection);
							} else {
								// Thử parse như một mảng JSON
								try {
									direction_list = objectMapper.readValue(directionJs,
											new TypeReference<List<Direction>>() {
											});
								} catch (JsonProcessingException arrayError) {
									// Nếu không phải mảng, thử parse như một object đơn
									try {
										Direction singleDirection = objectMapper.readValue(directionJs,
												Direction.class);
										direction_list.add(singleDirection);
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
					int status = resultSet.getInt("status");
					String note = resultSet.getString("note");
					Date created_at = resultSet.getDate("created_at");
					Date updated_at = resultSet.getDate("updated_at");
					int created_by_staff = resultSet.getInt("created_by_staff");
					int created_by_user = resultSet.getInt("created_by_user");
					int type = resultSet.getInt("type");

					property_list.add(new Property(id_property, name, province, district, ward, detail_address,
							doc_list, surface_area, useable_area, width, length, flour, bedrooms, toilets,
							direction_list, price, price_type, status, note, created_at, updated_at, created_by_staff,
							created_by_user, type));
				}
			}
			return ResponseEntity.ok(property_list);

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>()); // Trả về list rỗng
																									// thay vì null
		} catch (NumberFormatException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>()); // Trả về list rỗng thay vì
																							// null
		}
	}
	
	@GetMapping("/property_list_type_1/{id_user}")
	public ResponseEntity<List<Property>> getPropertyType1UserList(@PathVariable String id_user) {
		
		List<Property> property_list = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester",
				"root", "123456");
				PreparedStatement ps = connection.prepareStatement(
						"SELECT * FROM `tester`.`property` WHERE `delete` = 0 AND `type` = 1 AND `id_user` = ? order by `id_property` DESC");) {
			ps.setInt(1, Integer.valueOf(id_user));

			ObjectMapper objectMapper = new ObjectMapper();
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					int id_property = resultSet.getInt("id_property");
					String name = resultSet.getString("name");
					String province = resultSet.getString("province");
					String district = resultSet.getString("district");
					String ward = resultSet.getString("ward");
					String detail_address = resultSet.getString("detail_address");

					// Xử lý legal_doc an toàn
					List<Doc> doc_list = new ArrayList<>();
					String legal_doc = resultSet.getString("legal_doc");
					if (legal_doc != null && !legal_doc.trim().isEmpty()) {
						try {
							

							// Nếu legal_doc là một số nguyên
							if (legal_doc.matches("\\d+")) {
								Doc singleDoc = new Doc();
								// Xử lý tùy theo logic của bạn
								doc_list.add(singleDoc);
							} else {
								// Thử parse như một mảng JSON
								try {
									doc_list = objectMapper.readValue(legal_doc, new TypeReference<List<Doc>>() {
									});
								} catch (JsonProcessingException arrayError) {
									// Nếu không phải mảng, thử parse như một object đơn
									try {
										Doc singleDoc = objectMapper.readValue(legal_doc, Doc.class);
										doc_list.add(singleDoc);
									} catch (JsonProcessingException objectError) {
										objectError.printStackTrace();
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					float surface_area = resultSet.getFloat("surface_area");
					float useable_area = resultSet.getFloat("useable_area");
					float width = resultSet.getFloat("width");
					float length = resultSet.getFloat("length");
					int flour = resultSet.getInt("flours");
					int bedrooms = resultSet.getInt("bedrooms");
					int toilets = resultSet.getInt("toilet");

					// Xử lý direction an toàn
					List<Direction> direction_list = new ArrayList<>();
					String directionJs = resultSet.getString("direction");
					if (directionJs != null && !directionJs.trim().isEmpty()) {
						try {
							

							// Nếu directionJs là một số nguyên
							if (directionJs.matches("\\d+")) {
								Direction singleDirection = new Direction();
								// Xử lý tùy theo logic của bạn
								direction_list.add(singleDirection);
							} else {
								// Thử parse như một mảng JSON
								try {
									direction_list = objectMapper.readValue(directionJs,
											new TypeReference<List<Direction>>() {
											});
								} catch (JsonProcessingException arrayError) {
									// Nếu không phải mảng, thử parse như một object đơn
									try {
										Direction singleDirection = objectMapper.readValue(directionJs,
												Direction.class);
										direction_list.add(singleDirection);
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
					int status = resultSet.getInt("status");
					String note = resultSet.getString("note");
					Date created_at = resultSet.getDate("created_at");
					Date updated_at = resultSet.getDate("updated_at");
					int created_by_staff = resultSet.getInt("created_by_staff");
					int created_by_user = resultSet.getInt("created_by_user");
					int type = resultSet.getInt("type");

					property_list.add(new Property(id_property, name, province, district, ward, detail_address,
							doc_list, surface_area, useable_area, width, length, flour, bedrooms, toilets,
							direction_list, price, price_type, status, note, created_at, updated_at, created_by_staff,
							created_by_user, type));
				}
			}
			return ResponseEntity.ok(property_list);

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>()); // Trả về list rỗng
																									// thay vì null
		} catch (NumberFormatException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>()); // Trả về list rỗng thay vì
																							// null
		}
	}

	@PostMapping("/property/create")
	public ResponseEntity<Map<String, String>> addProperty(@RequestBody Property property) {

		Map<String, String> response = new HashMap<>();

		if (property.getName() == null || property.getName().isEmpty()) {
			response.put("message", "Name must not be null or empty");
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
			String docJson = objectMapper.writeValueAsString(property.getDoc_list());
			String direcJson = objectMapper.writeValueAsString(property.getDirection_list());

			// Xác định xem có thêm id_user vào SQL hay không
			
			String query = "INSERT INTO `tester`.`property`" 
			+ "(`name`, `province`, `district`, `ward`, `detail_address`, " 
			+ "`legal_doc`, `surface_area`, `useable_area`, `width`, `length`,"
			+ "`flours`, `bedrooms`, `toilet`, `direction`, `price`, `price_type`, `type`, "
			+ "`status`, `id_user`, `created_by_staff`, `created_by_user`, `note`) "
			+ "VALUES " 
			+ "(?, ?, ?, ?, ?, "
			+ "?, ?, ?, ?, ?, "
			+ "?, ?, ?, ?, ?, ?, ?, "
			+ "?, ?, ?, ?, ?) ";
					
			ps = connection.prepareStatement(query);
			
			ps.setString(1, property.getName());
			ps.setString(2, property.getProvince());
			ps.setString(3, property.getDistrict());
			ps.setString(4, property.getWard());
			ps.setString(5, property.getDetail_address());
			ps.setString(6, docJson);
			ps.setFloat(7, Float.valueOf(property.getSurface_area()));
			ps.setFloat(8, Float.valueOf(property.getUseable_area()));
			ps.setFloat(9, Float.valueOf(property.getWidth()));
			ps.setFloat(10, Float.valueOf(property.getLength()));
			ps.setInt(11, Integer.valueOf(property.getFlours()));
			ps.setInt(12, Integer.valueOf(property.getBedroom()));
			ps.setInt(13, Integer.valueOf(property.getToilet()));
			ps.setString(14, direcJson);
			ps.setFloat(15, Float.valueOf(property.getPrice()));
			ps.setInt(16, Integer.valueOf(property.getPrice_type()));
			ps.setInt(17, Integer.valueOf(property.getType()));
			ps.setInt(18, Integer.valueOf(property.getStatus()));
			ps.setInt(19, Integer.valueOf(property.getId_user()));
			ps.setInt(20, Integer.valueOf(property.getCreated_by_staff()));
			ps.setInt(21, Integer.valueOf(property.getCreated_by_user()));
			ps.setString(22, property.getNote());	

			ps.executeUpdate();
			response.put("message", "Property added successfully");
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

	@PutMapping("/property/update/{id_property}")
	public ResponseEntity<Map<String, String>> updateProperty(@RequestBody Property property,
			@PathVariable String id_property) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		if (property.getName() == null || property.getName().isEmpty()) {
			response.put("message", "Name must not be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();
			String docJson = objectMapper.writeValueAsString(property.getDoc_list());
			String direcJson = objectMapper.writeValueAsString(property.getDirection_list());

			// Xác định xem có thêm id_user vào SQL hay không
			boolean includeUserId = Integer.valueOf(property.getId_user()) != 0;

			String query = "UPDATE `tester`.`property` " + "SET "
					+ "`name` = ?, `province` = ?, `district` = ?, " + "`ward` = ?, `detail_address` = ?, "
					+ "`legal_doc` = ?, " + "`surface_area` = ?, `useable_area` = ?, `width` = ?, "
					+ "`length` = ?, `flours` = ?, `bedrooms` = ?, `toilet` = ?, "
					+ "`direction` = ?, `price` = ?, `price_type` = ?, `status` = ?, `note` = ?, "
					+ "`id_user` = ?, `type` = ? " + "WHERE `id_property` = ?;";

			ps = connection.prepareStatement(query);
			ps.setString(1, property.getName());
			ps.setString(2, property.getProvince());
			ps.setString(3, property.getDistrict());
			ps.setString(4, property.getWard());
			ps.setString(5, property.getDetail_address());
			ps.setString(6, docJson);
			ps.setFloat(7, Float.valueOf(property.getSurface_area()));
			ps.setFloat(8, Float.valueOf(property.getUseable_area()));
			ps.setFloat(9, Float.valueOf(property.getWidth()));
			ps.setFloat(10, Float.valueOf(property.getLength()));
			ps.setInt(11, Integer.valueOf(property.getFlours()));
			ps.setInt(12, Integer.valueOf(property.getBedroom()));
			ps.setInt(13, Integer.valueOf(property.getToilet()));
			ps.setString(14, direcJson);
			ps.setFloat(15, Float.valueOf(property.getPrice()));
			ps.setInt(16, Integer.valueOf(property.getPrice_type()));
			ps.setInt(17, Integer.valueOf(property.getStatus()));
			ps.setString(18, property.getNote());
			ps.setInt(19, Integer.valueOf(property.getId_user()));
			ps.setInt(20, Integer.valueOf(property.getType()));
			ps.setInt(21, Integer.valueOf(property.getId_property()));
		

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

	@GetMapping("/property_detail/{id_property}")
	public ResponseEntity<Property> getPropertyDetail(Model model, @PathVariable String id_property) {
		model.addAttribute("id_property", id_property);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet result = null;

		Property property = new Property();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`property` WHERE id_property = ?");
			ps.setInt(1, Integer.valueOf(id_property));
			result = ps.executeQuery();
			while (result.next()) {
				property.setId_property(result.getInt("id_property"));
				property.setName(result.getString("name"));
				property.setProvince(result.getString("province"));
				property.setDistrict(result.getString("district"));
				property.setWard(result.getString("ward"));
				property.setDetail_address(result.getString("detail_address"));

				List<Doc> doc_list = new ArrayList<>();
				String legal_doc = result.getString("legal_doc");
				if (legal_doc != null && !legal_doc.trim().isEmpty()) {
					try {
						

						// Nếu legal_doc là một số nguyên
						if (legal_doc.matches("\\d+")) {
							Doc singleDoc = new Doc();
							// Xử lý tùy theo logic của bạn
							doc_list.add(singleDoc);
						} else {
							// Thử parse như một mảng JSON
							try {
								doc_list = objectMapper.readValue(legal_doc, new TypeReference<List<Doc>>() {
								});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Doc singleDoc = objectMapper.readValue(legal_doc, Doc.class);
									doc_list.add(singleDoc);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				property.setDoc_list(doc_list);
				property.setSurface_area(result.getFloat("surface_area"));
				property.setUseable_area(result.getFloat("useable_area"));
				property.setWidth(result.getFloat("width"));
				property.setLength(result.getFloat("length"));
				property.setFlours(result.getInt("flours"));
				property.setBedroom(result.getInt("bedrooms"));
				property.setToilet(result.getInt("toilet"));

				List<Direction> direction_list = new ArrayList<>();
				String directionJs = result.getString("direction");
				if (directionJs != null && !directionJs.trim().isEmpty()) {
					try {
						

						// Nếu directionJs là một số nguyên
						if (directionJs.matches("\\d+")) {
							Direction singleDirection = new Direction();
							// Xử lý tùy theo logic của bạn
							direction_list.add(singleDirection);
						} else {
							// Thử parse như một mảng JSON
							try {
								direction_list = objectMapper.readValue(directionJs,
										new TypeReference<List<Direction>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Direction singleDirection = objectMapper.readValue(directionJs, Direction.class);
									direction_list.add(singleDirection);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				property.setDirection_list(direction_list);
				property.setPrice(result.getFloat("price"));
				property.setPrice_type(result.getInt("price_type"));
				property.setType(result.getInt("type"));
				property.setStatus(result.getInt("status"));
				property.setNote(result.getString("note"));
				property.setCreated_at(result.getDate("created_at"));
				property.setUpdated_at(result.getDate("updated_at"));
				property.setDelete(result.getInt("delete"));
				property.setId_user(result.getInt("id_user"));
				property.setCreated_by_staff(result.getInt("created_by_staff"));
				property.setCreated_by_user(result.getInt("created_by_user"));

			} // end of while block

			return new ResponseEntity<>(property, HttpStatus.OK);

		} // end of try block

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/property/delete/{id_property}")
	public ResponseEntity<Map<String, String>> deleteProperty(@RequestBody Property property,
			@PathVariable String id_property) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement("UPDATE property SET `delete` = 1 WHERE id_property = ?");
			ps.setInt(1, Integer.valueOf(property.getId_property()));

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
