package com.example.demo.Room;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.example.demo.Model.Room;
@Transactional

@SpringBootTest
@ActiveProfiles("test")
public class AddRoomTest {

    @Autowired
    private RoomController roomController;

    private Connection connection;
    private static final int TEST_PROPERTY_ID = 10022;
    private static final int TEST_OWNER_ID = 33;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);

        // Clear all data with delete = 0 from room
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.room WHERE `delete` = 0")) {
            ps.executeUpdate();
        }

        // Clear all data with id_property = 10022 from property
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
            ps.setInt(20, TEST_OWNER_ID);
            ps.setDate(21, Date.valueOf("2025-05-21"));
            ps.setDate(22, Date.valueOf("2025-05-21"));
            ps.setInt(23, 0);
            ps.setInt(24, 0);
            ps.setInt(25, TEST_OWNER_ID);
            ps.setInt(26, 1);

            int rows = ps.executeUpdate();
            assertEquals(1, rows, "Failed to insert test property");
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
    public void testAddRoom_Success() {
        // Arrange
        Room room = new Room();
        room.setName("Test Room");
        room.setArea(50.0f);
        room.setBathroom(1);
        room.setBedroom(2);
        room.setKitchen(1);
        room.setInterial("Fully furnished");
        room.setBalcony(1);
        room.setStatus(1);
        room.setMax_people(4);
        room.setId_property(TEST_PROPERTY_ID);
        room.setPrice(5000000.0f); // Valid price
        room.setFrequency(1);
        room.setId_owner(TEST_OWNER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = roomController.addRoom(room);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Room added successfully", responseBody.get("message"), "Message should match");

        // Verify inserted data
        try (PreparedStatement ps = connection.prepareStatement("SELECT name, id_property, id_owner, price FROM bds.room WHERE id_property = ? AND `delete` = 0")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Room should exist");
                assertEquals("Test Room", rs.getString("name"), "Inserted name should match");
                assertEquals(TEST_PROPERTY_ID, rs.getInt("id_property"), "Inserted id_property should match");
                assertEquals(TEST_OWNER_ID, rs.getInt("id_owner"), "Inserted id_owner should match");
                assertEquals(5000000.0f, rs.getFloat("price"), 0.01, "Inserted price should match");
            }
        } catch (SQLException e) {
            fail("SQLException during verification: " + e.getMessage());
        }
    }

    @Test
    public void testAddRoom_NameEmpty() {
        // Arrange
        Room room = new Room();
        room.setName(""); // Empty name
        room.setArea(50.0f);
        room.setId_property(TEST_PROPERTY_ID);
        room.setPrice(5000000.0f);
        room.setId_owner(TEST_OWNER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = roomController.addRoom(room);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Room's name must not be null or empty", responseBody.get("message"), "Message should match");
    }

    @Test
    public void testAddRoom_NameNull() {
        // Arrange
        Room room = new Room();
        room.setName(null); // Null name
        room.setArea(50.0f);
        room.setId_property(TEST_PROPERTY_ID);
        room.setPrice(5000000.0f);
        room.setId_owner(TEST_OWNER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = roomController.addRoom(room);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Room's name must not be null or empty", responseBody.get("message"), "Message should match");
    }

    @Test
    public void testAddRoom_InvalidPrice() {
        // Arrange
        Room room = new Room();
        room.setName("Test Room");
        room.setArea(50.0f);
        room.setId_property(TEST_PROPERTY_ID);
        room.setPrice(0.0f); // Invalid price
        room.setId_owner(TEST_OWNER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = roomController.addRoom(room);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Price must be greater than 0", responseBody.get("message"), "Message should match");
    }
}