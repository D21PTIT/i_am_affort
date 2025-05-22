package com.example.demo.Property;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Model.Property;
@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class DeletePropertyTest {

    @Autowired
    private PropertyController propertyController;

    private Connection connection;
    private static final int TEST_USER_ID = 33;
    private static final int TEST_PROPERTY_ID = 10020;
    private static final int TEST_ROOM_ID = 1;
    private static final int TEST_INVOICE_ID = 1;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);

        // Clear all data with delete = 0 from invoice
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.invoice WHERE status = 1")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            // Ignore if table doesn't exist, will verify schema later
        }

        // Clear all data with delete = 0 from room
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.room WHERE `delete` = 0")) {
            ps.executeUpdate();
        }

        // Clear all data with id_property = 10020 from property to avoid duplicate key
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.property WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.executeUpdate();
        }

        // Insert test property
        String insertPropertyQuery = "INSERT INTO bds.property (id_property, name, province, district, ward, detail_address, " +
                "legal_doc, surface_area, useable_area, width, length, flours, bedrooms, toilet, direction, price, price_type, " +
                "status, note, id_user, created_at, updated_at, `delete`, created_by_staff, created_by_user, type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertPropertyQuery)) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.setString(2, "Test Property");
            ps.setString(3, "Hanoi");
            ps.setString(4, "Cau Giay");
            ps.setString(5, "Dich Vong");
            ps.setString(6, "123 Street");
            ps.setString(7, "[]");
            ps.setFloat(8, 100.0f);
            ps.setFloat(9, 80.0f);
            ps.setFloat(10, 10.0f);
            ps.setFloat(11, 10.0f);
            ps.setInt(12, 2);
            ps.setInt(13, 3);
            ps.setInt(14, 2);
            ps.setString(15, "[]");
            ps.setFloat(16, 1000000.0f);
            ps.setInt(17, 1);
            ps.setInt(18, 1);
            ps.setString(19, "Test note");
            ps.setInt(20, TEST_USER_ID);
            ps.setDate(21, Date.valueOf("2025-05-21"));
            ps.setDate(22, Date.valueOf("2025-05-21"));
            ps.setInt(23, 0);
            ps.setInt(24, 0);
            ps.setInt(25, TEST_USER_ID);
            ps.setInt(26, 1);

            int rows = ps.executeUpdate();
            assertEquals(1, rows, "Failed to insert test property");
        }

        // Verify inserted property
        try (PreparedStatement ps = connection.prepareStatement("SELECT id_user, name FROM bds.property WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Property should exist");
                assertEquals(TEST_USER_ID, rs.getInt("id_user"), "Inserted id_user should match");
                assertEquals("Test Property", rs.getString("name"), "Inserted name should match");
            }
        }

        connection.commit();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Rollback all changes
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            connection.close();
        }
    }

    @Test
    public void testDeleteProperty_Success() {
        // Arrange
        Property property = new Property();
        property.setId_property(TEST_PROPERTY_ID);
        property.setId_user(TEST_USER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = propertyController.deleteProperty(property, String.valueOf(TEST_PROPERTY_ID));

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Delete successfully!", responseBody.get("message"), "Message should match");

        // Verify property is deleted
        try (PreparedStatement ps = connection.prepareStatement("SELECT id_property FROM bds.property WHERE id_property = ? AND `delete` = 0")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertFalse(rs.next(), "Property should not exist with delete = 0");
            }
        } catch (SQLException e) {
            fail("SQLException during verification: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteProperty_NotFound() {
        // Arrange
        Property property = new Property();
        property.setId_property(TEST_PROPERTY_ID + 1);
        property.setId_user(TEST_USER_ID);
        String nonExistentId = String.valueOf(TEST_PROPERTY_ID + 1); // Non-existent ID

        // Act
        ResponseEntity<Map<String, String>> response = propertyController.deleteProperty(property, nonExistentId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Delete successfully!", responseBody.get("message"), "Message should match");
    }

    @Test
    public void testDeleteProperty_InvalidId() {
        // Arrange
        Property property = new Property();
        property.setId_user(TEST_USER_ID);
        String invalidId = "invalid"; // Non-numeric ID

        // Act
        ResponseEntity<Map<String, String>> response = propertyController.deleteProperty(property, invalidId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Delete successfully!", responseBody.get("message"), "Message should match");
    }

    @Test
    public void testDeleteProperty_HasRoomWithInvoice() throws SQLException {
        // Arrange: Insert room and invoice within a single try-catch block
        try {
            // Insert room
            String insertRoomQuery = "INSERT INTO bds.room (id_room, name, id_property, `delete`, status) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertRoomQuery)) {
                ps.setInt(1, TEST_ROOM_ID);
                ps.setString(2, "Test Room");
                ps.setInt(3, TEST_PROPERTY_ID);
                ps.setInt(4, 0);
                ps.setInt(5, 1); // Room is occupied
                int rows = ps.executeUpdate();
                assertEquals(1, rows, "Failed to insert test room");
            }

            // Insert invoice
            String insertInvoiceQuery = "INSERT INTO bds.invoice (id_invoice, id_room, status) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertInvoiceQuery)) {
                ps.setInt(1, TEST_INVOICE_ID);
                ps.setInt(2, TEST_ROOM_ID);
                ps.setInt(3, 1); // Active invoice
                int rows = ps.executeUpdate();
                assertEquals(1, rows, "Failed to insert test invoice");
            }

            connection.commit();
        } catch (SQLException e) {
            fail("SQLException during room or invoice insertion: " + e.getMessage());
        }

        // Arrange: Create Property object for deletion
        Property property = new Property();
        property.setId_property(TEST_PROPERTY_ID);
        property.setId_user(TEST_USER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = propertyController.deleteProperty(property, String.valueOf(TEST_PROPERTY_ID));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Không thể xóa bất động sản, tồn tại phòng trong đó đang có người thuê", responseBody.get("message"), "Message should match");

        // Verify property is not deleted
        try (PreparedStatement ps = connection.prepareStatement("SELECT id_property FROM bds.property WHERE id_property = ? AND `delete` = 0")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Property should still exist with delete = 0");
            }
        } catch (SQLException e) {
            fail("SQLException during verification: " + e.getMessage());
        }
    }
}