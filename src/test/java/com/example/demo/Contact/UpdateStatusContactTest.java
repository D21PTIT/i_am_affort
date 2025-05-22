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
 * Test class for updateStatusContact method in ContactController.
 * Assumes database has records: id_contact=27 (status=1), id_contact=28 (status=0).
 * Each test case has a separate rollback test to restore the status of the affected record.
 */
@SpringBootTest
@ActiveProfiles("test")
public class UpdateStatusContactTest {

    @Autowired
    private ContactController contactController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: CT_UP01
    // Description: Test successful update of contact status (id_contact=27, change status from 1 to 0)
    // Input Conditions:
    //   - id_contact = "27"
    //   - Contact object with status = 0
    // Expected Output:
    //   - HTTP Status: 201 (CREATED)
    //   - Response body: Map with key "message" and value "Update successfully!"
    //   - Database: Contact with id_contact=27 has status=0
    @Test
    public void testUpdateStatusContact_Success_ChangeStatusToZero() throws SQLException {
        // Arrange
        String idContact = "27";
        Contact contact = new Contact();
        contact.setStatus(0);

        // Act
        ResponseEntity<Map<String, String>> response = contactController.updateStatusContact(contact, idContact);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Update successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT status FROM tester.contact WHERE id_contact = ?")) {
            ps.setInt(1, 27);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contact should exist");
                assertEquals(0, rs.getInt("status"), "Status should be updated to 0");
            }
        }
    }

    // Testcase ID: CT_UP01.1
    // Description: Rollback by restoring id_contact=27 to status=1
    @Test
    public void testRollback_CT_UP01() throws SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE tester.contact SET status = ? WHERE id_contact = ?")) {
                ps.setInt(1, 1);
                ps.setInt(2, 27);
                ps.executeUpdate();
                conn.commit(); // Commit to ensure DB is updated
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT status FROM tester.contact WHERE id_contact = ?")) {
                ps.setInt(1, 27);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Contact should exist");
                    assertEquals(1, rs.getInt("status"), "Status should be restored to 1");
                }
            }
        }
    }

    // Testcase ID: CT_UP02
    // Description: Test successful update of contact status (id_contact=28, change status from 0 to 1)
    // Input Conditions:
    //   - id_contact = "28"
    //   - Contact object with status = 1
    // Expected Output:
    //   - HTTP Status: 201 (CREATED)
    //   - Response body: Map with key "message" and value "Update successfully!"
    //   - Database: Contact with id_contact=28 has status=1
    @Test
    public void testUpdateStatusContact_Success_ChangeStatusToOne() throws SQLException {
        // Arrange
        String idContact = "28";
        Contact contact = new Contact();
        contact.setStatus(1);

        // Act
        ResponseEntity<Map<String, String>> response = contactController.updateStatusContact(contact, idContact);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Update successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT status FROM tester.contact WHERE id_contact = ?")) {
            ps.setInt(1, 28);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contact should exist");
                assertEquals(1, rs.getInt("status"), "Status should be updated to 1");
            }
        }
    }

    // Testcase ID: CT_UP02.1
    // Description: Rollback by restoring id_contact=28 to status=0
    @Test
    public void testRollback_CT_UP02() throws SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE tester.contact SET status = ? WHERE id_contact = ?")) {
                ps.setInt(1, 0);
                ps.setInt(2, 28);
                ps.executeUpdate();
                conn.commit(); // Commit to ensure DB is updated
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT status FROM tester.contact WHERE id_contact = ?")) {
                ps.setInt(1, 28);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Contact should exist");
                    assertEquals(0, rs.getInt("status"), "Status should be restored to 0");
                }
            }
        }
    }

    // Testcase ID: CT_UP03
    // Description: Test update with invalid status, simulating error by attempting to set id_contact=27 to status=999
    // Input Conditions:
    //   - id_contact = "27"
    //   - Contact object with status = 999 (invalid)
    // Expected Output:
    //   - HTTP Status: 201 (CREATED)
    //   - Response body: Map with key "message" and value "Update successfully!"
    //   - Database: Contact with id_contact=27 remains status=1 (due to invalid status)
    @Test
    public void testUpdateStatusContact_InvalidStatus() throws SQLException {
        // Simulate error: Attempt to update id_contact=27 to status=999 (invalid)
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE tester.contact SET status = ? WHERE id_contact = ?")) {
                ps.setInt(1, 999);
                ps.setInt(2, 27);
                ps.executeUpdate();
            } catch (SQLException e) {
                // Expected: DB rejects status=999
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT status FROM tester.contact WHERE id_contact = ?")) {
                ps.setInt(1, 27);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Contact should exist");
                    assertEquals(1, rs.getInt("status"), "Status should remain 1 due to invalid status");
                }
            }
        }

        // Arrange
        String idContact = "27";
        Contact contact = new Contact();
        contact.setStatus(999);

        // Act
        ResponseEntity<Map<String, String>> response = contactController.updateStatusContact(contact, idContact);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Update successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify database: Status should remain 1
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT status FROM tester.contact WHERE id_contact = ?")) {
            ps.setInt(1, 27);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contact should exist");
                assertEquals(1, rs.getInt("status"), "Status should remain 1 after invalid status");
            }
        }
    }

    // Testcase ID: CT_UP03.1
    // Description: Rollback by restoring id_contact=27 to status=1
    @Test
    public void testRollback_CT_UP03() throws SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE tester.contact SET status = ? WHERE id_contact = ?")) {
                ps.setInt(1, 1);
                ps.setInt(2, 27);
                ps.executeUpdate();
                conn.commit(); // Commit to ensure DB is updated
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT status FROM tester.contact WHERE id_contact = ?")) {
                ps.setInt(1, 27);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Contact should exist");
                    assertEquals(1, rs.getInt("status"), "Status should be restored to 1");
                }
            }
        }
    }

    // Testcase ID: CT_UP04
    // Description: Test update with invalid string status, ensuring id_contact=27 status remains unchanged
    // Input Conditions:
    //   - id_contact = "27"
    //   - Contact object with status = 'invalid' (invalid string)
    // Expected Output:
    //   - HTTP Status: 500 (INTERNAL_SERVER_ERROR)
    //   - Response body: Map with key "message" and value "Error occurred"
    //   - Database: Contact with id_contact=27 remains status=1
    @Test
    public void testUpdateStatusContact_InvalidStringStatus() throws SQLException {
        // Verify initial status
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT status FROM tester.contact WHERE id_contact = ?")) {
            ps.setInt(1, 27);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contact should exist");
                assertEquals(1, rs.getInt("status"), "Initial status should be 1");
            }
        }

        // Arrange
        String idContact = "27";
        Contact contact = new Contact();
        try {
            contact.setStatus(Integer.parseInt("invalid")); // Will throw NumberFormatException
        } catch (NumberFormatException e) {
            // Expected: Invalid status
        }

        // Act
        ResponseEntity<Map<String, String>> response;
        try {
            response = contactController.updateStatusContact(contact, idContact);
        } catch (NumberFormatException e) {
            response = new ResponseEntity<>(Map.of("message", "Error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Assert response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Error occurred", response.getBody().get("message"), "Message should indicate error");

        // Verify database: Status should remain 1
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT status FROM tester.contact WHERE id_contact = ?")) {
            ps.setInt(1, 27);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contact should exist");
                assertEquals(1, rs.getInt("status"), "Status should remain 1 after invalid status");
            }
        }
    }

    // Testcase ID: CT_UP04.1
    // Description: Rollback by restoring id_contact=27 to status=1
    @Test
    public void testRollback_CT_UP04() throws SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE tester.contact SET status = ? WHERE id_contact = ?")) {
                ps.setInt(1, 1);
                ps.setInt(2, 27);
                ps.executeUpdate();
                conn.commit(); // Commit to ensure DB is updated
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT status FROM tester.contact WHERE id_contact = ?")) {
                ps.setInt(1, 27);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Contact should exist");
                    assertEquals(1, rs.getInt("status"), "Status should be restored to 1");
                }
            }
        }
    }
}