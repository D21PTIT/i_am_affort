

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

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class addContactTest {

    @Autowired
    private ContactController contactController;

    private Connection connection;
    private static final int TEST_OWNER_ID = 9999; // Unique id for owner
    private static final int TEST_TENANT_ID = 9998; // Unique id for tenant
    private static final List<Integer> insertedContactIds = new ArrayList<>(); // Store inserted contact IDs from CT_ADD01

    @BeforeEach
    public void setUp() throws SQLException {
        // Establish connection to the test database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        connection.setAutoCommit(false); // Start transaction

        // Clear existing data for TEST_OWNER_ID and TEST_TENANT_ID to ensure clean state
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tester.contact WHERE id_owner = ? OR id_tenant = ?")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.setInt(2, TEST_TENANT_ID);
            ps.executeUpdate();
        }
        connection.commit(); // Commit cleanup
        insertedContactIds.clear(); // Clear stored IDs for new test run
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Do not delete records automatically to allow inspection
        // Records from CT_ADD01 persist until cleaned by CT_ADD03 or next test's setUp
        if (connection != null && !connection.isClosed()) {
            connection.commit(); // Ensure changes are committed for inspection
            connection.close();
        }
    }

    // Testcase ID: CT_ADD01
    // Description: Test successful addition of a valid contact
    // Input Conditions:
    //   - Contact object with valid data:
    //     - user_name = "John Doe"
    //     - user_email = "john@example.com"
    //     - content = "Test contact message"
    //     - status = 1
    //     - id_owner = 9999
    //     - id_tenant = 9998
    //     - id_post = 3
    //     - header = "Contact Request"
    //     - phone_number = "1234567890"
    // Expected Output:
    //   - HTTP Status: 201 (CREATED)
    //   - Response body: Map with key "message" and value "Contact added successfully"
    //   - Database: Contains the inserted contact with matching data, persists for inspection until cleaned by CT_ADD03 or next test
    @Test
    public void testAddContact_Success() {
        // Arrange
        Contact contact = new Contact();
        contact.setUsername("John Doe");
        contact.setUser_email("john@example.com");
        contact.setContent("Test contact message");
        contact.setStatus(1);
        contact.setId_owner(TEST_OWNER_ID);
        contact.setId_tenant(TEST_TENANT_ID);
        contact.setId_post(3);
        contact.setHeader("Contact Request");
        contact.setPhone_number("1234567890");

        // Act
        ResponseEntity<Map<String, String>> response;
        try {
            response = contactController.addContact(contact);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Contact added successfully", responseBody.get("message"), "Message should indicate success");

        // Verify database and store inserted id_contact
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM tester.contact WHERE id_owner = ? AND id_tenant = ? AND `delete` = 0")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.setInt(2, TEST_TENANT_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contact should be inserted into database");
                insertedContactIds.add(rs.getInt("id_contact")); // Store id for CT_ADD03 to delete
                assertEquals("John Doe", rs.getString("user_name"), "Username should match");
                assertEquals("john@example.com", rs.getString("user_email"), "Email should match");
                assertEquals("Test contact message", rs.getString("content"), "Content should match");
                assertEquals(1, rs.getInt("status"), "Status should match");
                assertEquals(TEST_OWNER_ID, rs.getInt("id_owner"), "Owner ID should match");
                assertEquals(TEST_TENANT_ID, rs.getInt("id_tenant"), "Tenant ID should match");
                assertEquals(3, rs.getInt("id_post"), "Post ID should match");
                assertEquals("Contact Request", rs.getString("header"), "Header should match");
                assertEquals("1234567890", rs.getString("phone_number"), "Phone number should match");
                assertFalse(rs.next(), "No additional contacts should be inserted");
            }
        } catch (SQLException e) {
            fail("Database verification failed: " + e.getMessage());
        }
    }

    // Testcase ID: CT_ADD02
    // Description: Test addition of a contact with invalid data (null user_name, violating NOT NULL constraint)
    // Input Conditions:
    //   - Contact object with invalid data:
    //     - user_name = null (violates NOT NULL constraint)
    //     - user_email = "john@example.com"
    //     - content = "Test contact message"
    //     - status = 1
    //     - id_owner = 9999
    //     - id_tenant = 9998
    //     - id_post = 3
    //     - header = "Contact Request"
    //     - phone_number = "1234567890"
    // Expected Output:
    //   - HTTP Status: 500 (INTERNAL_SERVER_ERROR)
    //   - Response body: Map with key "message" and value "Error occurred"
    //   - Database: No new contact inserted
    @Test
    public void testAddContact_InvalidData() {
        // Arrange
        Contact contact = new Contact();
        contact.setUsername(null); // Invalid: null user_name
        contact.setUser_email("john@example.com");
        contact.setContent("Test contact message");
        contact.setStatus(1);
        contact.setId_owner(TEST_OWNER_ID);
        contact.setId_tenant(TEST_TENANT_ID);
        contact.setId_post(3);
        contact.setHeader("Contact Request");
        contact.setPhone_number("1234567890");

        // Act
        ResponseEntity<Map<String, String>> response;
        try {
            response = contactController.addContact(contact);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Error occurred", responseBody.get("message"), "Message should indicate error");

        // Verify database
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM tester.contact WHERE id_owner = ? AND id_tenant = ?")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.setInt(2, TEST_TENANT_ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    assertEquals(0, rs.getInt(1), "No contact should be inserted due to invalid data");
                }
            }
        } catch (SQLException e) {
            fail("Database verification failed: " + e.getMessage());
        }
    }

    // Testcase ID: CT_ADD03
    // Description: Test deletion of the contact inserted by CT_ADD01
    // Input Conditions:
    //   - Assumes CT_ADD01 has run and inserted a contact with id_contact stored in insertedContactIds
    //   - No new contact is inserted
    // Expected Output:
    //   - Database: Contact from CT_ADD01 (user_name = "John Doe") is deleted
    //   - If CT_ADD01 has not run, no deletion occurs, and database remains empty
    // Note: Run this test after CT_ADD01 to ensure the contact from CT_ADD01 exists
    @Test
    public void testDeleteContactFromCT_ADD01() {
        // Act: Delete contact from CT_ADD01
        if (!insertedContactIds.isEmpty()) {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tester.contact WHERE id_contact = ?")) {
                ps.setInt(1, insertedContactIds.get(0)); // Delete contact from CT_ADD01
                int rows = ps.executeUpdate();
                assertTrue(rows <= 1, "Should delete at most one contact from CT_ADD01");
            } catch (SQLException e) {
                fail("Failed to delete contact from CT_ADD01: " + e.getMessage());
            }
        }

        // Assert: Verify database
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM tester.contact WHERE id_owner = ? AND id_tenant = ? AND `delete` = 0")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.setInt(2, TEST_TENANT_ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    assertEquals(0, rs.getInt(1), "No contacts should remain after deleting CT_ADD01 contact");
                }
            }
        } catch (SQLException e) {
            fail("Database verification failed: " + e.getMessage());
        }

        // Additional verification: Ensure CT_ADD01 contact is gone
        if (!insertedContactIds.isEmpty()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM tester.contact WHERE id_contact = ?")) {
                ps.setInt(1, insertedContactIds.get(0));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        assertEquals(0, rs.getInt(1), "Contact from CT_ADD01 should be deleted");
                    }
                }
            } catch (SQLException e) {
                fail("Database verification failed: " + e.getMessage());
            }
        }
    }

    // Testcase ID: CT_ADD04
    // Description: Test addition of a contact with invalid id_tenant (non-existent in system)
    // Input Conditions:
    //   - Contact object with invalid id_tenant:
    //     - user_name = "Jane Doe"
    //     - user_email = "jane@example.com"
    //     - content = "Test contact message"
    //     - status = 1
    //     - id_owner = 9999
    //     - id_tenant = -1 (assumed non-existent, violating foreign key constraint)
    //     - id_post = 3
    //     - header = "Contact Request"
    //     - phone_number = "1234567890"
    // Expected Output:
    //   - HTTP Status: 500 (INTERNAL_SERVER_ERROR)
    //   - Response body: Map with key "message" and value "Error occurred"
    //   - Database: No new contact inserted
    @Test
    public void testAddContact_InvalidTenantId() {
        // Arrange
        Contact contact = new Contact();
        contact.setUsername("Jane Doe");
        contact.setUser_email("jane@example.com");
        contact.setContent("Test contact message");
        contact.setStatus(1);
        contact.setId_owner(TEST_OWNER_ID);
        contact.setId_tenant(-1); // Invalid: non-existent id_tenant
        contact.setId_post(3);
        contact.setHeader("Contact Request");
        contact.setPhone_number("1234567890");

        // Act
        ResponseEntity<Map<String, String>> response;
        try {
            response = contactController.addContact(contact);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Error occurred", responseBody.get("message"), "Message should indicate error");

        // Verify database
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM tester.contact WHERE id_owner = ? AND id_tenant = ?")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.setInt(2, -1);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    assertEquals(0, rs.getInt(1), "No contact should be inserted due to invalid id_tenant");
                }
            }
        } catch (SQLException e) {
            fail("Database verification failed: " + e.getMessage());
        }
    }

    // Testcase ID: CT_ADD05
    // Description: Test addition of a contact with invalid id_owner (non-existent in system)
    // Input Conditions:
    //   - Contact object with invalid id_owner:
    //     - user_name = "Jane Doe"
    //     - user_email = "jane@example.com"
    //     - content = "Test contact message"
    //     - status = 1
    //     - id_owner = -1 (assumed non-existent, violating foreign key constraint)
    //     - id_tenant = 9998
    //     - id_post = 3
    //     - header = "Contact Request"
    //     - phone_number = "1234567890"
    // Expected Output:
    //   - HTTP Status: 500 (INTERNAL_SERVER_ERROR)
    //   - Response body: Map with key "message" and value "Error occurred"
    //   - Database: No new contact inserted
    @Test
    public void testAddContact_InvalidOwnerId() {
        // Arrange
        Contact contact = new Contact();
        contact.setUsername("Jane Doe");
        contact.setUser_email("jane@example.com");
        contact.setContent("Test contact message");
        contact.setStatus(1);
        contact.setId_owner(-1); // Invalid: non-existent id_owner
        contact.setId_tenant(TEST_TENANT_ID);
        contact.setId_post(3);
        contact.setHeader("Contact Request");
        contact.setPhone_number("1234567890");

        // Act
        ResponseEntity<Map<String, String>> response;
        try {
            response = contactController.addContact(contact);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Error occurred", responseBody.get("message"), "Message should indicate error");

        // Verify database
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM tester.contact WHERE id_owner = ? AND id_tenant = ?")) {
            ps.setInt(1, -1);
            ps.setInt(2, TEST_TENANT_ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    assertEquals(0, rs.getInt(1), "No contact should be inserted due to invalid id_owner");
                }
            }
        } catch (SQLException e) {
            fail("Database verification failed: " + e.getMessage());
        }
    }
}
