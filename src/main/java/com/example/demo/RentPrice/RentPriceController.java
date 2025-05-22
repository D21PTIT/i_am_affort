package com.example.demo.RentPrice;

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

import com.example.demo.Model.Price;
import com.example.demo.Model.RentPrice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class RentPriceController {
	@GetMapping("/rent_price_list/{id_owner}")
	public ResponseEntity<List<RentPrice>> getRentPriceList(Model model, @PathVariable String id_owner)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<RentPrice> rent_price_list = new ArrayList<RentPrice>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement(
					"SELECT * FROM `tester`.`rent_price` WHERE `delete` = 0 AND `id_owner` = ? order by `id_rent_price` DESC");
			ps.setInt(1, Integer.valueOf(id_owner));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_rent_price = resultSet.getInt("id_rent_price");
				int id_prop = resultSet.getInt("id_prop");
				int prop_type = resultSet.getInt("prop_type");
				String prop_name = resultSet.getString("prop_name");
				int id_room = resultSet.getInt("id_room");
				String room_code = resultSet.getString("room_code");

				List<Price> price_month_list = new ArrayList<>();
				String price_month_js = resultSet.getString("price_month");
				if (price_month_js != null && !price_month_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_month_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_month_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_month_list = objectMapper.readValue(price_month_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_month_js, Price.class);
									price_month_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_quater_list = new ArrayList<>();
				String price_quater_js = resultSet.getString("price_quater");
				if (price_quater_js != null && !price_quater_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_quater_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_quater_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_quater_list = objectMapper.readValue(price_quater_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_quater_js, Price.class);
									price_quater_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_half_year_list = new ArrayList<>();
				String price_half_year_js = resultSet.getString("price_half_year");
				if (price_half_year_js != null && !price_half_year_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_half_year_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_half_year_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_half_year_list = objectMapper.readValue(price_half_year_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_half_year_js, Price.class);
									price_half_year_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_year_list = new ArrayList<>();
				String price_year_js = resultSet.getString("price_year");
				if (price_year_js != null && !price_year_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_year_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_year_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_year_list = objectMapper.readValue(price_year_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_year_js, Price.class);
									price_year_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Date validity_date = resultSet.getDate("validity_date");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");
				int id_owner1 = resultSet.getInt("id_owner");
				float electric = resultSet.getFloat("electric");
				float water = resultSet.getFloat("water");
				int water_type = resultSet.getInt("water_type");
				float internet = resultSet.getFloat("internet");
				float clean = resultSet.getFloat("clean");
				float elevator = resultSet.getFloat("elevator");
				float other_service = resultSet.getFloat("other_service");

				rent_price_list.add(new RentPrice(id_rent_price, id_prop, prop_type, prop_name, id_room, room_code,
						price_month_list, price_quater_list, price_half_year_list, price_year_list, validity_date,
						created_at, updated_at, id_owner1, electric, water, water_type, internet, clean, elevator,
						other_service));
			}

			return new ResponseEntity<>(rent_price_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("rent_price/create")
	public ResponseEntity<Map<String, String>> addRentPrice(@RequestBody RentPrice rentPrice) {

		Map<String, String> response = new HashMap<>();

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ObjectMapper objectMapper = new ObjectMapper();
			String priceMonthJson = objectMapper.writeValueAsString(rentPrice.getPrice_month());
			String priceQuaterJson = objectMapper.writeValueAsString(rentPrice.getPrice_quater());
			String priceHalfYearJson = objectMapper.writeValueAsString(rentPrice.getPrice_half_year());
			String priceYearJson = objectMapper.writeValueAsString(rentPrice.getPrice_year());

			String sql = "INSERT INTO `tester`.`rent_price`"
					+ "(`id_prop`, `prop_type`, `prop_name`, `id_room`, `room_code`, `price_month`, "
					+ "`price_quater`, `price_half_year`, `price_year`, "
					+ "`validity_date`, `id_owner`, `electric`, `water`, `water_type`, `internet`, `clean`, `elevator`, `other_service`) "
					+ "VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// Chuẩn bị câu lệnh PreparedStatement
			ps = connection.prepareStatement(sql);

			ps.setInt(1, rentPrice.getProp_id());
			ps.setInt(2, rentPrice.getProp_type());
			ps.setString(3, rentPrice.getProp_name());
			ps.setInt(4, rentPrice.getRoom_id());
			ps.setString(5, rentPrice.getRoom_code());
			ps.setString(6, priceMonthJson);
			ps.setString(7, priceQuaterJson);
			ps.setString(8, priceHalfYearJson);
			ps.setString(9, priceYearJson);
			ps.setDate(10, rentPrice.getValidity_date());
			ps.setInt(11, rentPrice.getId_owner());
			ps.setFloat(12, rentPrice.getElectric());
			ps.setFloat(13, rentPrice.getWater());
			ps.setInt(14, rentPrice.getWater_type());
			ps.setFloat(15, rentPrice.getInternet());
			ps.setFloat(16, rentPrice.getClean());
			ps.setFloat(17, rentPrice.getElevator());
			ps.setFloat(18, rentPrice.getOther_service());

			ps.executeUpdate();
			response.put("message", "Rent price added successfully");
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

	@GetMapping("/rent_price_detail/{id_rent_price}")
	public ResponseEntity<RentPrice> getRentPriceDetail(Model model, @PathVariable String id_rent_price) {
		model.addAttribute("id_rent_price", id_rent_price);
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;

		RentPrice rentPrice = new RentPrice();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ps = connection.prepareStatement("SELECT * FROM `tester`.`rent_price` WHERE id_rent_price = ?");
			ps.setInt(1, Integer.valueOf(id_rent_price));
			resultSet = ps.executeQuery();
			while (resultSet.next()) {
				rentPrice.setProp_id(resultSet.getInt("id_prop"));
				rentPrice.setProp_name(resultSet.getString("prop_name"));
				rentPrice.setProp_type(resultSet.getInt("prop_type"));
				rentPrice.setRoom_id(resultSet.getInt("id_room"));
				rentPrice.setRoom_code(resultSet.getString("room_code"));

				List<Price> price_month_list = new ArrayList<>();
				String price_month_js = resultSet.getString("price_month");
				if (price_month_js != null && !price_month_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_month_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_month_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_month_list = objectMapper.readValue(price_month_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_month_js, Price.class);
									price_month_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_quater_list = new ArrayList<>();
				String price_quater_js = resultSet.getString("price_quater");
				if (price_quater_js != null && !price_quater_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_quater_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_quater_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_quater_list = objectMapper.readValue(price_quater_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_quater_js, Price.class);
									price_quater_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_half_year_list = new ArrayList<>();
				String price_half_year_js = resultSet.getString("price_half_year");
				if (price_half_year_js != null && !price_half_year_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_half_year_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_half_year_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_half_year_list = objectMapper.readValue(price_half_year_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_half_year_js, Price.class);
									price_half_year_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_year_list = new ArrayList<>();
				String price_year_js = resultSet.getString("price_year");
				if (price_year_js != null && !price_year_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_year_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_year_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_year_list = objectMapper.readValue(price_year_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_year_js, Price.class);
									price_year_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				rentPrice.setPrice_month(price_month_list);
				rentPrice.setPrice_quater(price_quater_list);
				rentPrice.setPrice_half_year(price_half_year_list);
				rentPrice.setPrice_year(price_year_list);
				rentPrice.setValidity_date(resultSet.getDate("validity_date"));
				rentPrice.setId_owner(resultSet.getInt("id_owner"));
				rentPrice.setElectric(resultSet.getFloat("electric"));
				rentPrice.setWater(resultSet.getFloat("water"));
				rentPrice.setWater_type(resultSet.getInt("water_type"));
				rentPrice.setInternet(resultSet.getFloat("internet"));
				rentPrice.setClean(resultSet.getFloat("clean"));
				rentPrice.setElevator(resultSet.getFloat("elevator"));
				rentPrice.setOther_service(resultSet.getFloat("other_service"));

			} // end of while block

			return new ResponseEntity<>(rentPrice, HttpStatus.OK);

		} // end of try block

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/rent_price/update/{id_rent_price}")
	public ResponseEntity<Map<String, String>> updateTenant(@RequestBody RentPrice rentPrice,
			@PathVariable String id_rent_price) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			ObjectMapper objectMapper = new ObjectMapper();

			String price_month_js = objectMapper.writeValueAsString(rentPrice.getPrice_month());
			String price_quater_js = objectMapper.writeValueAsString(rentPrice.getPrice_quater());
			String price_half_year_js = objectMapper.writeValueAsString(rentPrice.getPrice_half_year());
			String price_year_js = objectMapper.writeValueAsString(rentPrice.getPrice_year());

			// Start building the SQL query
			String sql = "UPDATE `tester`.`rent_price` "
					+ "SET `id_prop` = ?, `prop_name` = ?, `prop_type` = ?, " + "`id_room` = ?, `room_code` = ?, "
					+ "`price_month` = ?, " + "`price_quater` = ?, " + "`price_half_year` = ?, " + "`price_year` = ?, "
					+ "`validity_date` = ?, `id_owner` = ?, " 
					+ "`electric` = ?, `water` = ?, `water_type` = ?, `internet` = ?, `clean` = ?, `elevator` = ?, `other_service` = ? "
					+ "WHERE (`id_rent_price` = ?);";

			// Prepare the PreparedStatement
			ps = connection.prepareStatement(sql);

			ps.setInt(1, rentPrice.getProp_id());
			ps.setString(2, rentPrice.getProp_name());
			ps.setInt(3, rentPrice.getProp_type());
			ps.setInt(4, rentPrice.getRoom_id());
			ps.setString(5, rentPrice.getRoom_code());
			ps.setString(6, price_month_js);
			ps.setString(7, price_quater_js);
			ps.setString(8, price_half_year_js);
			ps.setString(9, price_year_js);
			ps.setDate(10, rentPrice.getValidity_date());
			ps.setInt(11, rentPrice.getId_owner());
			ps.setFloat(12, rentPrice.getElectric());
			ps.setFloat(13, rentPrice.getWater());
			ps.setInt(14, rentPrice.getWater_type());
			ps.setFloat(15, rentPrice.getInternet());
			ps.setFloat(16, rentPrice.getClean());
			ps.setFloat(17, rentPrice.getElevator());
			ps.setFloat(18, rentPrice.getOther_service());
			ps.setInt(19, Integer.valueOf(id_rent_price));

			ps.executeUpdate();
			response.put("message", "Rent price updated successfully!");

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

	@PutMapping("/rent_price/delete/{id_rent_price}")
	public ResponseEntity<Map<String, String>> deleteTenant(@RequestBody RentPrice rentPrice,
			@PathVariable String id_rent_price) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement("UPDATE rent_price SET `delete` = 1 WHERE id_rent_price = ?");
			ps.setInt(1, Integer.valueOf(id_rent_price));

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

	@GetMapping("/rent_price/prop_type_0/{id_prop}")
	public ResponseEntity<List<RentPrice>> getRentPricePropType1(Model model, @PathVariable String id_prop)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<RentPrice> rent_price_list = new ArrayList<RentPrice>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement("SELECT * " + "FROM tester.rent_price "
					+ "WHERE `validity_date` <= CURRENT_DATE() " + "AND `id_prop` = ? " + "AND `delete` = 0 "
					+ "ORDER BY `id_rent_price` DESC " + "LIMIT 1;");
			ps.setInt(1, Integer.valueOf(id_prop));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_rent_price = resultSet.getInt("id_rent_price");
				int id_prop1 = resultSet.getInt("id_prop");
				int prop_type = resultSet.getInt("prop_type");
				String prop_name = resultSet.getString("prop_name");
				int id_room = resultSet.getInt("id_room");
				String room_code = resultSet.getString("room_code");

				List<Price> price_month_list = new ArrayList<>();
				String price_month_js = resultSet.getString("price_month");
				if (price_month_js != null && !price_month_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_month_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_month_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_month_list = objectMapper.readValue(price_month_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_month_js, Price.class);
									price_month_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_quater_list = new ArrayList<>();
				String price_quater_js = resultSet.getString("price_quater");
				if (price_quater_js != null && !price_quater_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_quater_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_quater_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_quater_list = objectMapper.readValue(price_quater_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_quater_js, Price.class);
									price_quater_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_half_year_list = new ArrayList<>();
				String price_half_year_js = resultSet.getString("price_half_year");
				if (price_half_year_js != null && !price_half_year_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_half_year_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_half_year_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_half_year_list = objectMapper.readValue(price_half_year_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_half_year_js, Price.class);
									price_half_year_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_year_list = new ArrayList<>();
				String price_year_js = resultSet.getString("price_year");
				if (price_year_js != null && !price_year_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_year_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_year_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_year_list = objectMapper.readValue(price_year_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_year_js, Price.class);
									price_year_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Date validity_date = resultSet.getDate("validity_date");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");
				int id_owner1 = resultSet.getInt("id_owner");
				float electric = resultSet.getFloat("electric");
				float water = resultSet.getFloat("water");
				int water_type = resultSet.getInt("water_type");
				float internet = resultSet.getFloat("internet");
				float clean = resultSet.getFloat("clean");
				float elevator = resultSet.getFloat("elevator");
				float other_service = resultSet.getFloat("other_service");

				rent_price_list.add(new RentPrice(id_rent_price, id_prop1, prop_type, prop_name, id_room, room_code,
						price_month_list, price_quater_list, price_half_year_list, price_year_list, validity_date,
						created_at, updated_at, id_owner1, electric, water, water_type, internet, clean, elevator,
						other_service));
			}

			return new ResponseEntity<>(rent_price_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/rent_price/prop_type_1/{id_prop}/room/{id_room}")
	public ResponseEntity<List<RentPrice>> getRentPriceList(Model model, @PathVariable String id_prop,
			@PathVariable String id_room) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<RentPrice> rent_price_list = new ArrayList<RentPrice>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement("SELECT * " + "FROM tester.rent_price "
					+ "WHERE `validity_date` <= CURRENT_DATE() " + "AND `id_prop` = ? " + "AND `id_room` = ? "
					+ "AND `delete` = 0 " + "ORDER BY `id_rent_price` DESC " + "LIMIT 1;");
			ps.setInt(1, Integer.valueOf(id_prop));
			ps.setInt(2, Integer.valueOf(id_room));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {

				int id_rent_price = resultSet.getInt("id_rent_price");
				int id_prop1 = resultSet.getInt("id_prop");
				int prop_type = resultSet.getInt("prop_type");
				String prop_name = resultSet.getString("prop_name");
				int id_room1 = resultSet.getInt("id_room");
				String room_code = resultSet.getString("room_code");

				List<Price> price_month_list = new ArrayList<>();
				String price_month_js = resultSet.getString("price_month");
				if (price_month_js != null && !price_month_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_month_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_month_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_month_list = objectMapper.readValue(price_month_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_month_js, Price.class);
									price_month_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_quater_list = new ArrayList<>();
				String price_quater_js = resultSet.getString("price_quater");
				if (price_quater_js != null && !price_quater_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_quater_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_quater_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_quater_list = objectMapper.readValue(price_quater_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_quater_js, Price.class);
									price_quater_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_half_year_list = new ArrayList<>();
				String price_half_year_js = resultSet.getString("price_half_year");
				if (price_half_year_js != null && !price_half_year_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_half_year_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_half_year_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_half_year_list = objectMapper.readValue(price_half_year_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_half_year_js, Price.class);
									price_half_year_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Price> price_year_list = new ArrayList<>();
				String price_year_js = resultSet.getString("price_year");
				if (price_year_js != null && !price_year_js.trim().isEmpty()) {
					try {

						// Nếu legal_doc là một số nguyên
						if (price_year_js.matches("\\d+")) {
							Price singlePrice = new Price();
							// Xử lý tùy theo logic của bạn
							price_year_list.add(singlePrice);
						} else {
							// Thử parse như một mảng JSON
							try {
								price_year_list = objectMapper.readValue(price_year_js,
										new TypeReference<List<Price>>() {
										});
							} catch (JsonProcessingException arrayError) {
								// Nếu không phải mảng, thử parse như một object đơn
								try {
									Price singlePrice = objectMapper.readValue(price_year_js, Price.class);
									price_year_list.add(singlePrice);
								} catch (JsonProcessingException objectError) {
									objectError.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Date validity_date = resultSet.getDate("validity_date");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");
				int id_owner1 = resultSet.getInt("id_owner");
				float electric = resultSet.getFloat("electric");
				float water = resultSet.getFloat("water");
				int water_type = resultSet.getInt("water_type");
				float internet = resultSet.getFloat("internet");
				float clean = resultSet.getFloat("clean");
				float elevator = resultSet.getFloat("elevator");
				float other_service = resultSet.getFloat("other_service");

				rent_price_list.add(new RentPrice(id_rent_price, id_prop1, prop_type, prop_name, id_room1, room_code,
						price_month_list, price_quater_list, price_half_year_list, price_year_list, validity_date,
						created_at, updated_at, id_owner1, electric, water, water_type, internet, clean, elevator,
						other_service));
			}

			return new ResponseEntity<>(rent_price_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
