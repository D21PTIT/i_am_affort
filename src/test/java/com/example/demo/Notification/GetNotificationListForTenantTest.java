package com.example.demo.Notification;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import com.example.demo.Model.Notification;
@Transactional

@SpringBootTest
@ActiveProfiles("test")
public class GetNotificationListForTenantTest {

    @Autowired
    private NotificationController notificationController;

    private Connection connection;
    private static final int TEST_NOTIFICATION_ID = 9999; // Unique id_notification
    private static final String TEST_USERNAME = "test_user"; // Unique username

    @BeforeEach
    public void setUp() throws SQLException {
        // Establish connection to the test database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false); // Start transaction

        // Clear existing data for TEST_NOTIFICATION_ID
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.notification WHERE id_notification = ?")) {
            ps.setInt(1, TEST_NOTIFICATION_ID);
            ps.executeUpdate();
        }
        connection.commit();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.notification WHERE id_notification = ?")) {
            ps.setInt(1, TEST_NOTIFICATION_ID);
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
    public void testGetNotificationListForTenant_Success() throws SQLException, IOException {
        // Arrange
        insertTestNotification(TEST_NOTIFICATION_ID, TEST_USERNAME);
        Model model = new ConcurrentModel();

        // Act
        ResponseEntity<List<Notification>> response = notificationController.getNotificationListForTenant(model, TEST_USERNAME);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(1, response.getBody().size(), "Should return one notification");

        Notification notification = response.getBody().get(0);
        assertEquals(TEST_NOTIFICATION_ID, notification.getId_notification(), "Notification ID should match");
        assertEquals("Test Notification", notification.getLabel(), "Label should match");
        assertEquals("This is a test notification", notification.getContent(), "Content should match");
        assertEquals("/test/path", notification.getPath(), "Path should match");
        assertEquals(0, notification.getStatus(), "Status should be 0");
        assertEquals(Date.valueOf("2025-05-20"), notification.getCreated_date(), "Created date should match");
    }

    @Test
    public void testGetNotificationListForTenant_NoDataFound() throws IOException {
        // Arrange
        Model model = new ConcurrentModel();

        // Act
        ResponseEntity<List<Notification>> response = notificationController.getNotificationListForTenant(model, TEST_USERNAME);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Should return empty list when no notifications exist");
    }

    @Test
    public void testGetNotificationListForTenant_InvalidUsername() throws IOException {
        // Arrange
        Model model = new ConcurrentModel();
        String invalidUsername = ""; // Empty username

        // Act
        ResponseEntity<List<Notification>> response = notificationController.getNotificationListForTenant(model, invalidUsername);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Should return empty list for invalid username");
    }

    // Helper method to insert a test notification
    private void insertTestNotification(int notificationId, String username) throws SQLException {
        String tenantsJson = "[{\"username\": \"" + username + "\"}]";
        String insertQuery = "INSERT INTO bds.notification (id_notification, label, content, path, id_owner, tenants, status, created_date, `delete`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setInt(1, notificationId);
            ps.setString(2, "Test Notification");
            ps.setString(3, "This is a test notification");
            ps.setString(4, "/test/path");
            ps.setInt(5, 1); // Arbitrary id_owner
            ps.setString(6, tenantsJson);
            ps.setInt(7, 0);
            ps.setDate(8, Date.valueOf("2025-05-20"));
            ps.executeUpdate();
        }
        connection.commit();
    }
}