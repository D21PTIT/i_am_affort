package com.example.demo.Notification;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Notification;
import com.example.demo.Model.TenantInContract;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class NotificationController {
	@GetMapping("/notification/{id_owner}")
	public ResponseEntity<List<Notification>> getNotificationList(Model model, @PathVariable String id_owner)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Notification> noti_list = new ArrayList<Notification>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement(
					"SELECT * FROM `tester`.`notification` WHERE `delete` = 0 AND `id_owner` = ? order by `id_notification` DESC");
			ps.setInt(1, Integer.valueOf(id_owner));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {
				int id_notification = resultSet.getInt("id_notification");
				String label = resultSet.getString("label");
				String content = resultSet.getString("content");
				String path = resultSet.getString("path");
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

				int status = resultSet.getInt("status");
				Date created_date = resultSet.getDate("created_date");

				noti_list.add(new Notification(id_notification, label, content, path, id_owner1, tenant_list, status,
						created_date));
			}

			return new ResponseEntity<>(noti_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/notification_tenant/{username}")
	public ResponseEntity<List<Notification>> getNotificationListForTenant(Model model, @PathVariable String username)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<Notification> noti_list = new ArrayList<Notification>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement("SELECT * FROM tester.notification "
					+ "WHERE JSON_CONTAINS(tenants, JSON_OBJECT('username', ?)) " + "order by `id_notification` DESC;");
			ps.setString(1,(username));

			resultSet = ps.executeQuery();
			ObjectMapper objectMapper = new ObjectMapper();

			while (resultSet.next()) {
				int id_notification = resultSet.getInt("id_notification");
				String label = resultSet.getString("label");
				String content = resultSet.getString("content");
				String path = resultSet.getString("path");
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

				int status = resultSet.getInt("status");
				Date created_date = resultSet.getDate("created_date");

				noti_list.add(new Notification(id_notification, label, content, path, id_owner1, tenant_list, status,
						created_date));
			}

			return new ResponseEntity<>(noti_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/notification/create")
    public ResponseEntity<Map<String, String>> addNotification(@RequestBody Notification notification) {
        Map<String, String> response = new HashMap<>();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");

            ObjectMapper objectMapper = new ObjectMapper();
            String tenantJson = objectMapper.writeValueAsString(notification.getTenant_list());

            String query = "INSERT INTO `tester`.`notification` " +
                           "(`label`, `content`, `path`, `id_owner`, `tenants`, `status`, `created_date`) " + 
                           "VALUES (?, ?, ?, ?, ?, 0, CURRENT_TIMESTAMP);";

            ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, notification.getLabel());
            ps.setString(2, notification.getContent());
            ps.setString(3, notification.getPath());
            ps.setInt(4, notification.getId_owner());
            ps.setString(5, tenantJson);

            ps.executeUpdate();

            // Lấy ID thông báo vừa tạo
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int newNotificationId = rs.getInt(1);
                
                // Gửi thông báo real-time
                Map<String, Object> notificationPayload = new HashMap<>();
                notificationPayload.put("id_notification", newNotificationId);
                notificationPayload.put("id_owner", notification.getId_owner());
                
                messagingTemplate.convertAndSend(
                    "/topic/notifications/" + notification.getId_owner(), 
                    notificationPayload
                );
             // Gửi thông báo cho từng tenant
                for (TenantInContract tenant : notification.getTenant_list()) {
                    messagingTemplate.convertAndSend(
                        "/topic/notifications/tenant/" + tenant.getUsername(), 
                        notificationPayload
                    );
                }
                
            }

            response.put("message", "Noti added successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error occurred");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            // Đóng kết nối như cũ
        }
    }

	@PutMapping("/notification/update_status/{id_notification}")
	public ResponseEntity<Map<String, String>> updateStatusContact(@RequestBody Notification notification,
			@PathVariable String id_notification) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> response = new HashMap<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");

			ps = connection.prepareStatement("UPDATE notification SET status = 1 WHERE id_notification = ?");
			
			ps.setInt(1, Integer.valueOf(Integer.valueOf(id_notification)));

			ps.executeUpdate();
			response.put("message", "Update noti status successfully!");

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
	
	@GetMapping("/notification/unread_count/{id_owner}")
	public ResponseEntity<Map<String, Integer>> getUnreadNotificationCount(@PathVariable String id_owner) {
	    Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet resultSet = null;
	    Map<String, Integer> response = new HashMap<>();

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
	        
	        ps = connection.prepareStatement(
	            "SELECT COUNT(*) as unread_count FROM `tester`.`notification` " +
	            "WHERE `delete` = 0 AND `id_owner` = ? AND `status` = 0"
	        );
	        ps.setInt(1, Integer.valueOf(id_owner));

	        resultSet = ps.executeQuery();
	        
	        if (resultSet.next()) {
	            int unreadCount = resultSet.getInt("unread_count");
	            response.put("unreadCount", unreadCount);
	        } else {
	            response.put("unreadCount", 0);
	        }

	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("unreadCount", -1);
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    } finally {
	        try {
	            if (resultSet != null) resultSet.close();
	            if (ps != null) ps.close();
	            if (connection != null) connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	@GetMapping("/notification_tenant/unread_count/{username}")
	public ResponseEntity<Map<String, Integer>> getUnreadNotificationCountTenant(@PathVariable String username) {
	    Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet resultSet = null;
	    Map<String, Integer> response = new HashMap<>();

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
	        
	        ps = connection.prepareStatement(
	            "SELECT COUNT(*) as unread_count "
	            + "FROM `tester`.`notification` "
	            + "WHERE `delete` = 0 "
	            + "AND JSON_CONTAINS(tenants, JSON_OBJECT('username', ?)) "
	            + "AND `status` = 0;"
	        );
	        ps.setString(1, username);

	        resultSet = ps.executeQuery();
	        
	        if (resultSet.next()) {
	            int unreadCount = resultSet.getInt("unread_count");
	            response.put("unreadCount", unreadCount);
	        } else {
	            response.put("unreadCount", 0);
	        }

	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("unreadCount", -1);
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    } finally {
	        try {
	            if (resultSet != null) resultSet.close();
	            if (ps != null) ps.close();
	            if (connection != null) connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
}
