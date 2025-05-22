package com.example.demo.Property;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
public class GetPropertyListTest {

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
            ps.setString(2, "Test Property");
            ps.setString(3, "Hanoi");
            ps.setString(4, "Cau Giay");
            ps.setString(5, "Dich Vong");
            ps.setString(6, "123 Street");
            ps.setString(7, "[]"); // Empty JSON array for legal_doc
            ps.setFloat(8, 100.0f);
            ps.setFloat(9, 80.0f);
            ps.setFloat(10, 10.0f);
            ps.setFloat(11, 10.0f);
            ps.setInt(12, 2);
            ps.setInt(13, 3);
            ps.setInt(14, 2);
            ps.setString(15, "[]"); // Empty JSON array for direction
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

        // Verify inserted data
        try (PreparedStatement ps = connection.prepareStatement("SELECT id_user FROM bds.property WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Property should exist");
                assertEquals(TEST_USER_ID, rs.getInt("id_user"), "Inserted id_user should match");
            }
        }

        connection.commit();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.room WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.property WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.executeUpdate();
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testGetPropertyList_Success() {
        try {
            // Act
            ResponseEntity<List<Property>> response = propertyController.getPropertyList(null);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
            List<Property> properties = response.getBody();
            assertNotNull(properties, "Property list should not be null");
            assertEquals(1, properties.size(), "Property list should contain one property");
            Property property = properties.get(0);
            assertEquals("Test Property", property.getName(), "Name should match");
            assertEquals("Hanoi", property.getProvince(), "Province should match");
            assertEquals(TEST_PROPERTY_ID, property.getId_property(), "Property ID should match");
            assertEquals(TEST_USER_ID, property.getId_user(), "User ID should match");
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test
    public void testGetPropertyList_NoDataFound() {
        try {
            // Arrange: Clear all data with delete = 0
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.room WHERE `delete` = 0")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.property WHERE `delete` = 0")) {
                ps.executeUpdate();
            }
            connection.commit();

            // Act
            ResponseEntity<List<Property>> response = propertyController.getPropertyList(null);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
            List<Property> properties = response.getBody();
            assertNotNull(properties, "Property list should not be null");
            assertTrue(properties.isEmpty(), "Property list should be empty when no data exists");
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }
}