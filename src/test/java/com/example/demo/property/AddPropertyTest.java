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
public class AddPropertyTest {

    @Autowired
    private PropertyController propertyController;

    private Connection connection;
    private static final int TEST_USER_ID = 33;
    private static final int TEST_PROPERTY_ID = 10020;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);
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
    public void testAddProperty_Success() {
        // Arrange
        Property property = new Property();
        property.setId_property(TEST_PROPERTY_ID);
        property.setName("Test Property");
        property.setProvince("Hanoi");
        property.setDistrict("Cau Giay");
        property.setWard("Dich Vong");
        property.setDetail_address("123 Street");
        property.setDoc_list(new ArrayList<>());
        property.setSurface_area(100.0f);
        property.setUseable_area(80.0f);
        property.setWidth(10.0f);
        property.setLength(10.0f);
        property.setFlours(2);
        property.setBedroom(3);
        property.setToilet(2);
        property.setDirection_list(new ArrayList<>());
        property.setPrice(1000000.0f);
        property.setPrice_type(1);
        property.setStatus(1);
        property.setNote("Test note");
        property.setId_user(TEST_USER_ID);
        property.setCreated_at(Date.valueOf("2025-05-21"));
        property.setUpdated_at(Date.valueOf("2025-05-21"));
        property.setDelete(0);
        property.setCreated_by_staff(0);
        property.setCreated_by_user(TEST_USER_ID);
        property.setType(1);

        // Act
        ResponseEntity<Map<String, String>> response = propertyController.addProperty(property);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Property added successfully", responseBody.get("message"), "Message should match");

        // Verify inserted data
        try (PreparedStatement ps = connection.prepareStatement("SELECT id_user, name FROM bds.property WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Property should exist");
                assertEquals(TEST_USER_ID, rs.getInt("id_user"), "Inserted id_user should match");
                assertEquals("Test Property", rs.getString("name"), "Inserted name should match");
            }
        } catch (SQLException e) {
            fail("SQLException during verification: " + e.getMessage());
        }
    }

    @Test
    public void testAddProperty_NameEmpty() {
        // Arrange
        Property property = new Property();
        property.setId_property(TEST_PROPERTY_ID);
        property.setName(""); // Empty name
        property.setProvince("Hanoi");
        property.setId_user(TEST_USER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = propertyController.addProperty(property);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Name must not be null or empty", responseBody.get("message"), "Message should match");
    }

    @Test
    public void testAddProperty_NameNull() {
        // Arrange
        Property property = new Property();
        property.setId_property(TEST_PROPERTY_ID);
        property.setName(null); // Null name
        property.setProvince("Hanoi");
        property.setId_user(TEST_USER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = propertyController.addProperty(property);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Name must not be null or empty", responseBody.get("message"), "Message should match");
    }
}