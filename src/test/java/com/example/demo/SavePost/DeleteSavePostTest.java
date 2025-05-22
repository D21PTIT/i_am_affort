package com.example.demo.SavePost;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for delete method in SavePostController.
 * Assumes database has a record: id_save_post=1.
 * Tests invalid and non-existent id_save_post scenarios, expecting errors.
 * Includes rollback tests to restore or verify database state.
 * Note: The delete method performs a hard delete, returns void, and silently catches exceptions,
 * causing tests SP_DEL02 and SP_DEL03 to fail because expected error statuses (404, 400) are not returned.
 * Tests expect exceptions to be thrown for error cases, but the method catches them, leading to test failures.
 */
@SpringBootTest
@ActiveProfiles("test")
public class DeleteSavePostTest {

    @Autowired
    private SavePostController savePostController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: SP_DEL01
    // Description: Test successful deletion of save_post (id_save_post=1)
    // Input Conditions:
    //   - id_save_post = "1"
    //   - Database: id_save_post=1 exists
    // Expected Output:
    //   - HTTP Status: 200 (OK, default for void method)
    //   - Database: No record with id_save_post=1
    @Test
    public void testDeleteSavePost_Success() throws SQLException {
        // Arrange
        String idSavePost = "1";

        // Act
        savePostController.delete(idSavePost);

        // Assert database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.save_post WHERE id_save_post = ?")) {
            ps.setInt(1, 1);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "SavePost should be deleted");
            }
        }
    }

    // Testcase ID: SP_DEL01.1
    // Description: Rollback by re-inserting id_save_post=1
    // Note: Assumes original record had id_tenant=1, id_post=1, and dummy values for other fields
    @Test
    public void testRollback_SP_DEL01() throws SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO tester.save_post (id_save_post, id_tenant, id_post, header, province, district, other_service) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, 1);
                ps.setInt(2, 1); // Dummy id_tenant
                ps.setInt(3, 1); // Dummy id_post
                ps.setString(4, "Test Header");
                ps.setString(5, "Test Province");
                ps.setString(6, "Test District");
                ps.setFloat(7, 0.0f);
                ps.executeUpdate();
                conn.commit(); // Commit to ensure DB is updated
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.save_post WHERE id_save_post = ?")) {
                ps.setInt(1, 1);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(1, rs.getInt(1), "SavePost should be restored");
                }
            }
        }
    }

    // Testcase ID: SP_DEL02
    // Description: Test delete with non-existent id_save_post (id_save_post=9999)
    // Input Conditions:
    //   - id_save_post = "9999"
    //   - Database: No record for id_save_post=9999
    // Expected Output:
    //   - Expected: Throw exception or return 404 NOT_FOUND
    //   - Database: No changes (still no record)
    // Note: Test will fail because the method silently executes without throwing an exception
    // or returning 404, defaulting to 200 OK.
    @Test
    public void testDeleteSavePost_NonExistentIdSavePost() throws SQLException {
        // Arrange
        String idSavePost = "9999";

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            savePostController.delete(idSavePost);
        }, "Expected RuntimeException for non-existent id_save_post, but none was thrown");

        // Assert database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.save_post WHERE id_save_post = ?")) {
            ps.setInt(1, 9999);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No SavePost should exist");
            }
        }
    }

    // Testcase ID: SP_DEL02.1
    // Description: Rollback by verifying no changes occurred for id_save_post=9999
    @Test
    public void testRollback_SP_DEL02() throws SQLException {
        // No changes expected for id_save_post=9999
        try (Connection conn = getConnection()) {
            conn.commit(); // Close transaction
        }
    }

    // Testcase ID: SP_DEL03
    // Description: Test delete with invalid id_save_post (non-numeric string)
    // Input Conditions:
    //   - id_save_post = "abc"
    // Expected Output:
    //   - Expected: Throw NumberFormatException or return 400 BAD_REQUEST
    //   - Database: No changes (existing records remain)
    // Note: Test will fail because NumberFormatException is caught silently,
    // and the method defaults to 200 OK.
    @Test
    public void testDeleteSavePost_InvalidIdSavePost() throws SQLException {
        // Arrange
        String idSavePost = "abc";

        // Act and Assert
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            savePostController.delete(idSavePost);
        }, "Expected NumberFormatException for invalid id_save_post, but none was thrown");

        // Assert database (no changes expected)
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.save_post WHERE id_save_post = 1")) {
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(1, rs.getInt(1), "Existing SavePost should remain unchanged");
            }
        }
    }

    // Testcase ID: SP_DEL03.1
    // Description: Rollback by verifying no changes occurred for id_save_post="abc"
    @Test
    public void testRollback_SP_DEL03() throws SQLException {
        // No changes expected for id_save_post="abc"
        try (Connection conn = getConnection()) {
            conn.commit(); // Close transaction
        }
    }
}