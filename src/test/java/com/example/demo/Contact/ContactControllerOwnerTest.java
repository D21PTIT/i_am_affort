package com.example.demo.Contact;

import com.example.demo.Model.Contact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class  ContactControllerOwnerTest {

    @Autowired
    private ContactController contactController;

    private Connection connection;
    private static final int TEST_OWNER_ID = 9999; // Unique id for owner

    @BeforeEach
    public void setUp() throws SQLException {
        // Establish connection to the test database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        connection.setAutoCommit(false); // Start transaction

        // Clear existing data for TEST_OWNER_ID
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tester.contact WHERE id_owner = ?")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.executeUpdate();
        }

        // Insert test data for success case (delete = 0)
        String insertQuery = "INSERT INTO tester.contact (user_name, user_email, content, created_at, updated_at, `delete`, status, id_owner, id_tenant, id_post, header, phone_number) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "John Doe");
            ps.setString(2, "john@example.com");
            ps.setString(3, "Test content");
            ps.setDate(4, Date.valueOf("2025-05-18"));
            ps.setDate(5, Date.valueOf("2025-05-18"));
            ps.setInt(6, 0); // delete = 0
            ps.setInt(7, 1);
            ps.setInt(8, TEST_OWNER_ID);
            ps.setInt(9, 1);
            ps.setInt(10, 3);
            ps.setString(11, "Test Header");
            ps.setString(12, "1234567890");
            int rows = ps.executeUpdate();
            assertEquals(1, rows, "Failed to insert test contact (delete = 0)");

            // Get generated id_contact
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    assertTrue(rs.getInt(1) > 0, "Generated id_contact should be positive");
                } else {
                    fail("No generated key returned for contact (delete = 0)");
                }
            }
        }

        // Insert test data for deleted case (delete = 1)
        try (PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Jane Doe");
            ps.setString(2, "jane@example.com");
            ps.setString(3, "Deleted content");
            ps.setDate(4, Date.valueOf("2025-05-18"));
            ps.setDate(5, Date.valueOf("2025-05-18"));
            ps.setInt(6, 1); // delete = 1
            ps.setInt(7, 1);
            ps.setInt(8, TEST_OWNER_ID);
            ps.setInt(9, 1);
            ps.setInt(10, 3);
            ps.setString(11, "Deleted Header");
            ps.setString(12, "0987654321");
            int rows = ps.executeUpdate();
            assertEquals(1, rows, "Failed to insert test contact (delete = 1)");

            // Get generated id_contact
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    assertTrue(rs.getInt(1) > 0, "Generated id_contact should be positive");
                } else {
                    fail("No generated key returned for contact (delete = 1)");
                }
            }
        }

        // Commit transaction to make data visible to ContactController
        connection.commit();

        // Verify data exists in the database
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM tester.contact WHERE id_owner = ? AND `delete` = 0")) {
            ps.setInt(1, TEST_OWNER_ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    assertEquals(1, rs.getInt(1), "Expected one non-deleted contact in database");
                }
            }
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tester.contact WHERE id_owner = ?")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.executeUpdate();
        }

        // Close connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Testcase ID: CT_GETOWN01
    @Test
    public void testGetContactListOwner_Success() {
        // Arrange
        String idOwner = String.valueOf(TEST_OWNER_ID);
        Model model = null; // Model is not used in the controller logic

        // Act
        ResponseEntity<List<Contact>> response;
        try {
            response = contactController.getContactListOwner(model, idOwner);
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
            return;
        }

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Contact> contacts = response.getBody();
        assertNotNull(contacts, "Contact list should not be null");
        assertEquals(1, contacts.size(), "Contact list should contain one contact (delete = 0)");
        Contact contact = contacts.get(0);
        assertEquals("John Doe", contact.getUsername(), "Username should be John Doe");
        assertEquals("john@example.com", contact.getUser_email(), "Email should be john@example.com");
        assertEquals("Test content", contact.getContent(), "Content should be Test content");
        assertEquals(Date.valueOf("2025-05-18"), contact.getCreated_at(), "Created date should match");
        assertEquals(Date.valueOf("2025-05-18"), contact.getUpdated_at(), "Updated date should match");
        assertEquals(0, contact.getDelete(), "Delete flag should be 0");
        assertEquals(1, contact.getStatus(), "Status should be 1");
        assertEquals(TEST_OWNER_ID, contact.getId_owner(), "Owner ID should be " + TEST_OWNER_ID);
        assertEquals(1, contact.getId_tenant(), "Tenant ID should be 1");
        assertEquals(3, contact.getId_post(), "Post ID should be 3");
        assertEquals("Test Header", contact.getHeader(), "Header should be Test Header");
        assertEquals("1234567890", contact.getPhone_number(), "Phone number should be 1234567890");
    }

    // Testcase ID: CT_GETOWN02
    @Test
    public void testGetContactListOwner_DeletedContact() {
        // Arrange
        String idOwner = String.valueOf(TEST_OWNER_ID);
        Model model = null;

        // Act
        ResponseEntity<List<Contact>> response;
        try {
            response = contactController.getContactListOwner(model, idOwner);
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
            return;
        }

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Contact> contacts = response.getBody();
        assertNotNull(contacts, "Contact list should not be null");
        assertEquals(1, contacts.size(), "Contact list should contain only non-deleted contacts");
        Contact contact = contacts.get(0);
        assertEquals("John Doe", contact.getUsername(), "Username should be John Doe (delete = 0)");
        assertNotEquals("Jane Doe", contact.getUsername(), "Deleted contact (delete = 1) should not be included");
    }

    // Testcase ID: CT_GETOWN03

    @Test
    public void testGetContactListOwner_NoDataFound() {
        // Arrange
        String idOwner = "999"; // Non-existent owner ID
        Model model = null;

        // Act
        ResponseEntity<List<Contact>> response;
        try {
            response = contactController.getContactListOwner(model, idOwner);
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
            return;
        }

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Contact> contacts = response.getBody();
        assertNotNull(contacts, "Contact list should not be null");
        assertTrue(contacts.isEmpty(), "Contact list should be empty for non-existent owner ID");
    }

    // Testcase ID: CT_GETOWN04

    @Test
    public void testGetContactListOwner_InvalidOwnerId() {
        // Arrange
        String idOwner = "invalid"; // Non-numeric owner ID
        Model model = null;

        // Act
        ResponseEntity<List<Contact>> response;
        try {
            response = contactController.getContactListOwner(model, idOwner);
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
            return;
        }

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), 
                     "HTTP status should be INTERNAL_SERVER_ERROR for invalid owner ID due to NumberFormatException");
        assertNull(response.getBody(), "Response body should be null for invalid owner ID");
    }
}