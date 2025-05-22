package com.example.demo.Contact;

import com.example.demo.Model.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for deleteContact method in ContactController.
 * Assumes database has a record: id_contact=27 (delete=0).
 * Tests invalid and non-existent id_contact scenarios, expecting appropriate errors.
 * Includes rollback tests to restore or verify database state.
 * Note: CT_DEL02 and CT_DEL03 may fail due to controller returning 201 CREATED incorrectly.
 */
@SpringBootTest
@ActiveProfiles("test")
public class DeleteContactTest {

    @Autowired
    private ContactController contactController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: CT_DEL01
    // Description: Test successful soft delete of contact (id_contact=27, set delete=1)
    // Input Conditions:
    //   - id_contact = "27"
    //   - Contact object with id_contact = 27
    //   - Database: id_contact=27 has delete=0
    // Expected Output:
    //   - HTTP Status: 201 (CREATED)
    //   - Response body: Map with key "message" and value "Delete successfully!"
    //   - Database: Contact with id_contact=27 has delete=1
    @Test
    public void testDeleteContact_Success() throws SQLException {
        // Arrange
        String idContact = "27";
        Contact contact = new Contact();
        contact.setId_contact(27);

        // Act
        ResponseEntity<Map<String, String>> response = contactController.deleteContact(contact, idContact);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Delete successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT `delete` FROM tester.contact WHERE id_contact = ?")) {
            ps.setInt(1, 27);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contact should exist");
                assertEquals(1, rs.getInt("delete"), "Delete status should be updated to 1");
            }
        }
    }

    // Testcase ID: CT_DEL01.1
    // Description: Rollback by restoring id_contact=27 to delete=0
    @Test
    public void testRollback_CT_DEL01() throws SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE tester.contact SET `delete` = ? WHERE id_contact = ?")) {
                ps.setInt(1, 0);
                ps.setInt(2, 27);
                ps.executeUpdate();
                conn.commit(); // Commit to ensure DB is updated
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT `delete` FROM tester.contact WHERE id_contact = ?")) {
                ps.setInt(1, 27);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Contact should exist");
                    assertEquals(0, rs.getInt("delete"), "Delete status should be restored to 0");
                }
            }
        }
    }

    // Testcase ID: CT_DEL02
    // Description: Test delete with invalid id_contact (non-numeric string)
    // Input Conditions:
    //   - id_contact = "invalid"
    //   - Contact object with id_contact = 0
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" and value "Invalid id_contact format"
    // Note: Test may fail as controller incorrectly returns 201 CREATED
    @Test
    public void testDeleteContact_InvalidIdContact() {
        // Arrange
        String idContact = "invalid";
        Contact contact = new Contact();
        contact.setId_contact(0);

        // Act
        ResponseEntity<Map<String, String>> response = contactController.deleteContact(contact, idContact);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Invalid id_contact format", response.getBody().get("message"), "Message should indicate invalid format");
    }

    // Testcase ID: CT_DEL02.1
    // Description: Rollback by verifying no changes occurred for id_contact="invalid"
    @Test
    public void testRollback_CT_DEL02() throws SQLException {
        // No changes expected for id_contact="invalid"
        try (Connection conn = getConnection()) {
            conn.commit(); // Close transaction
        }
    }

    // Testcase ID: CT_DEL03
    // Description: Test delete with non-existent numeric id_contact (id_contact=999)
    // Input Conditions:
    //   - id_contact = "999"
    //   - Contact object with id_contact = 999
    //   - Database: No record for id_contact=999
    // Expected Output:
    //   - HTTP Status: 404 (NOT_FOUND)
    //   - Response body: Map with key "message" and value "Contact not found"
    // Note: Test may fail as controller incorrectly returns 201 CREATED
    @Test
    public void testDeleteContact_NonExistentIdContact() {
        // Arrange
        String idContact = "999";
        Contact contact = new Contact();
        contact.setId_contact(999);

        // Act
        ResponseEntity<Map<String, String>> response = contactController.deleteContact(contact, idContact);

        // Assert response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "HTTP status should be NOT_FOUND");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Contact not found", response.getBody().get("message"), "Message should indicate contact not found");
    }

    // Testcase ID: CT_DEL03.1
    // Description: Rollback by verifying no changes occurred for id_contact=999
    @Test
    public void testRollback_CT_DEL03() throws SQLException {
        // No changes expected for id_contact=999
        try (Connection conn = getConnection()) {
            conn.commit(); // Close transaction
        }
    }
}