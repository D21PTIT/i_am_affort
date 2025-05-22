
package com.example.demo.Room;

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
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.example.demo.Model.Room;
@Transactional

@SpringBootTest
@ActiveProfiles("test")
public class GetRoomListTest {

    @Autowired
    private RoomController roomController;

    private Connection connection;
    private static final int TEST_PROPERTY_ID = 10022;
    private static final int TEST_OWNER_ID = 33;
    private static final int TEST_ROOM_ID = 1;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);

        // Clear all data from room
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.room")) {
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

        // Insert test room
        String insertRoomQuery = "INSERT INTO bds.room (id_room, name, id_property, `delete`, status, price, id_owner, area, bathroom, bedroom, kitchen, interior, balcony, max_people, created_at, updated_at, frequency) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertRoomQuery)) {
            ps.setInt(1, TEST_ROOM_ID);
            ps.setString(2, "Test Room");
            ps.setInt(3, TEST_PROPERTY_ID);
            ps.setInt(4, 0);
            ps.setInt(5, 1);
            ps.setFloat(6, 5000000.0f);
            ps.setInt(7, TEST_OWNER_ID);
            ps.setFloat(8, 50.0f);
            ps.setInt(9, 1);
            ps.setInt(10, 2);
            ps.setInt(11, 1);
            ps.setString(12, "Fully furnished");
            ps.setInt(13, 1);
            ps.setInt(14, 4);
            ps.setDate(15, Date.valueOf("2025-05-21"));
            ps.setDate(16, Date.valueOf("2025-05-21"));
            ps.setInt(17, 1);
            int rows = ps.executeUpdate();
            assertEquals(1, rows, "Failed to insert test room");
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
    public void testGetRoomList_Success() throws IOException {
        // Arrange
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<List<Room>> response = roomController.getRoomList(model);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Room> rooms = response.getBody();
        assertNotNull(rooms, "Room list should not be null");
        assertEquals(1, rooms.size(), "Room list should contain one room");
        Room room = rooms.get(0);
        assertEquals("Test Room", room.getName(), "Room name should match");
        assertEquals(TEST_PROPERTY_ID, room.getId_property(), "Property ID should match");
        assertEquals(TEST_OWNER_ID, room.getId_owner(), "Owner ID should match");
        assertEquals(5000000.0f, room.getPrice(), 0.01, "Price should match");
    }

    @Test
    public void testGetRoomList_EmptyList() throws IOException {
        // Arrange
        Model model = new ExtendedModelMap();
        // Clear rooms
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.room")) {
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            fail("SQLException during setup: " + e.getMessage());
        }

        // Act
        ResponseEntity<List<Room>> response = roomController.getRoomList(model);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Room> rooms = response.getBody();
        assertNotNull(rooms, "Room list should not be null");
        assertTrue(rooms.isEmpty(), "Room list should be empty");
    }
}