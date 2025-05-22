package com.example.demo.Notification;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.Model.Notification;
import com.example.demo.Model.TenantInContract;

@SpringBootTest
@ActiveProfiles("test")
public class UpdateStatusContactTest {

    @Autowired
    private NotificationController notificationController;

    private Connection connection;
    private static final int TEST_NOTIFICATION_ID = 9999; // Unique id_notification
    private static final int TEST_OWNER_ID = 9999; // Unique id_owner

    @BeforeEach
    public void setUp() throws SQLException {
        // Establish connection to the test database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false); // Start transaction

        // Clear existing data for TEST_NOTIFICATION_ID
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.notification WHERE id_notification = ? OR id_owner = ?")) {
            ps.setInt(1, TEST_NOTIFICATION_ID);
            ps.setInt(2, TEST_OWNER_ID);
            ps.executeUpdate();
        }
        connection.commit();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.notification WHERE id_notification = ? OR id_owner = ?")) {
            ps.setInt(1, TEST_NOTIFICATION_ID);
            ps.setInt(2, TEST_OWNER_ID);
            ps.executeUpdate();
        }
        connection.commit();

        // Rollback to release any locks
        try {
            connection.rollback();
        } catch (SQLException e) {
            // Ignore rollback errors
        }

        // Close connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testUpdateStatusContact_Success() throws SQLException {
        // Arrange
        insertTestNotification(TEST_NOTIFICATION_ID, TEST_OWNER_ID);
        Notification notification = createValidNotification();
        String idNotification = String.valueOf(TEST_NOTIFICATION_ID);

        // Act
        ResponseEntity<Map<String, String>> response = notificationController.updateStatusContact(notification, idNotification);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Update noti status successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify data in database
        try (PreparedStatement ps = connection.prepareStatement("SELECT status FROM bds.notification WHERE id_notification = ?")) {
            ps.setInt(1, TEST_NOTIFICATION_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Notification should exist in database");
                assertEquals(1, rs.getInt("status"), "Status should be updated to 1");
            }
        }
    }

    @Test
    public void testUpdateStatusContact_NoDataFound() throws SQLException {
        // Arrange
        Notification notification = createValidNotification();
        String idNotification = String.valueOf(TEST_NOTIFICATION_ID);

        // Act
        ResponseEntity<Map<String, String>> response = notificationController.updateStatusContact(notification, idNotification);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Update noti status successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify data in database
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM bds.notification WHERE id_notification = ?")) {
            ps.setInt(1, TEST_NOTIFICATION_ID);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                assertEquals(0, rs.getInt(1), "No notification should exist in database");
            }
        }
    }

    @Test
    public void testUpdateStatusContact_InvalidNotificationId() {
        // Arrange
        Notification notification = createValidNotification();
        String idNotification = "invalid"; // Non-numeric id_notification

        // Act
        ResponseEntity<Map<String, String>> response = notificationController.updateStatusContact(notification, idNotification);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "HTTP status should be INTERNAL_SERVER_ERROR for invalid notification ID");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Error occurred", response.getBody().get("message"), "Message should indicate error");
    }

    // Helper method to insert a test notification
    private void insertTestNotification(int notificationId, int ownerId) throws SQLException {
        String tenantsJson = "[{\"username\": \"test_user\"}]";
        String insertQuery = "INSERT INTO bds.notification (id_notification, label, content, path, id_owner, tenants, status, created_date, `delete`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setInt(1, notificationId);
            ps.setString(2, "Test Notification");
            ps.setString(3, "This is a test notification");
            ps.setString(4, "/test/path");
            ps.setInt(5, ownerId);
            ps.setString(6, tenantsJson);
            ps.setInt(7, 0);
            ps.setDate(8, Date.valueOf("2025-05-20"));
            ps.executeUpdate();
        }
        connection.commit();
    }

    // Helper method to create a valid Notification object
    private Notification createValidNotification() {
        Notification notification = new Notification();
        notification.setLabel("Test Notification");
        notification.setContent("This is a test notification");
        notification.setPath("/test/path");
        notification.setId_owner(TEST_OWNER_ID);
        List<TenantInContract> tenantList = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("test_user");
        tenantList.add(tenant);
        notification.setTenant_list(tenantList);
        notification.setStatus(0);
        notification.setCreated_date(new Date(System.currentTimeMillis()));
        return notification;
    }
}