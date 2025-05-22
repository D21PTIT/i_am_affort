package com.example.demo.Property;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class UpdatePropertyTest {

    @Autowired
    private PropertyController propertyController;

    private Connection connection;
    private static final int TEST_USER_ID = 33;
    private static final int TEST_PROPERTY_ID = 10020;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);

        // Clear all data with delete = 0 from room
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.room WHERE `delete` = 0")) {
            ps.executeUpdate();
        }

        // Clear all data with delete = 0 from property
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.property WHERE `delete` = 0")) {
            ps.executeUpdate();
        }

        // Insert test property
        String insertQuery = "INSERT INTO bds.property (id_property, name, province, district, ward, detail_address, " +
                "legal_doc, surface_area, useable_area, width, length, flours, bedrooms, toilet, direction, price, price_type, " +
                "status, note, id_user, created_at, updated_at, `delete`, created_by_staff, created_by_user, type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.setString(2, "Original Property");
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
            ps.setString(19, "Original note");
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

        // Verify inserted data
        try (PreparedStatement ps = connection.prepareStatement("SELECT id_user, name FROM bds.property WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Property should exist");
                assertEquals(TEST_USER_ID, rs.getInt("id_user"), "Inserted id_user should match");
                assertEquals("Original Property", rs.getString("name"), "Inserted name should match");
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
    public void testUpdateProperty_Success() {
        // Arrange
        Property property = new Property();
        property.setId_property(TEST_PROPERTY_ID);
        property.setName("Updated Property");
        property.setProvince("Hanoi Updated");
        property.setDistrict("Cau Giay Updated");
        property.setWard("Dich Vong Updated");
        property.setDetail_address("456 Street");
        property.setDoc_list(new ArrayList<>());
        property.setSurface_area(120.0f);
        property.setUseable_area(90.0f);
        property.setWidth(12.0f);
        property.setLength(12.0f);
        property.setFlours(3);
        property.setBedroom(4);
        property.setToilet(3);
        property.setDirection_list(new ArrayList<>());
        property.setPrice(1500000.0f);
        property.setPrice_type(2);
        property.setStatus(2);
        property.setNote("Updated note");
        property.setId_user(TEST_USER_ID);
        property.setCreated_at(Date.valueOf("2025-05-21"));
        property.setUpdated_at(Date.valueOf("2025-05-21"));
        property.setDelete(0);
        property.setCreated_by_staff(0);
        property.setCreated_by_user(TEST_USER_ID);
        property.setType(2);

        // Act
        ResponseEntity<Map<String, String>> response = propertyController.updateProperty(property, String.valueOf(TEST_PROPERTY_ID));

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("News updated successfully!", responseBody.get("message"), "Message should match");

        // Verify updated data
        try (PreparedStatement ps = connection.prepareStatement("SELECT id_user, name, province FROM bds.property WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Property should exist");
                assertEquals(TEST_USER_ID, rs.getInt("id_user"), "Updated id_user should match");
                assertEquals("Updated Property", rs.getString("name"), "Updated name should match");
                assertEquals("Hanoi Updated", rs.getString("province"), "Updated province should match");
            }
        } catch (SQLException e) {
            fail("SQLException during verification: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateProperty_NameEmpty() {
        // Arrange
        Property property = new Property();
        property.setId_property(TEST_PROPERTY_ID);
        property.setName(""); // Empty name
        property.setProvince("Hanoi");
        property.setId_user(TEST_USER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = propertyController.updateProperty(property, String.valueOf(TEST_PROPERTY_ID));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Name must not be null or empty", responseBody.get("message"), "Message should match");
    }

    @Test
    public void testUpdateProperty_NameNull() {
        // Arrange
        Property property = new Property();
        property.setId_property(TEST_PROPERTY_ID);
        property.setName(null); // Null name
        property.setProvince("Hanoi");
        property.setId_user(TEST_USER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = propertyController.updateProperty(property, String.valueOf(TEST_PROPERTY_ID));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Name must not be null or empty", responseBody.get("message"), "Message should match");
    }
}