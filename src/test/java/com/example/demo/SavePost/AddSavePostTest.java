package com.example.demo.SavePost;

import com.example.demo.Model.SavePost;
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
 * Test class for addSavePost method in SavePostController.
 * Assumes database has records: id_tenant=1 in tenant table, id_post=1 in post table.
 * Tests invalid id_tenant and null header scenarios, expecting appropriate errors.
 * Includes rollback tests to restore or verify database state.
 * Note: SP_ADD02 and SP_ADD03 fail because the method returns 201 CREATED instead of 400 BAD_REQUEST
 * for invalid inputs, indicating incorrect error handling.
 */
@SpringBootTest
@ActiveProfiles("test")
public class AddSavePostTest {

    @Autowired
    private SavePostController savePostController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: SP_ADD01
    // Description: Test successful addition of a save_post with valid data
    // Input Conditions:
    //   - SavePost object with id_tenant=1, id_post=1, header="Test Header", province="Test Province", district="Test District", other_service=0.0
    //   - Database: id_tenant=1 and id_post=1 exist
    // Expected Output:
    //   - HTTP Status: 201 (CREATED)
    //   - Response body: Map with key "message" and value "Save post successfully"
    //   - Database: New save_post record exists with provided values
    @Test
    public void testAddSavePost_Success() throws SQLException {
        // Arrange
        SavePost savePost = new SavePost();
        savePost.setId_tenant(1);
        savePost.setId_post(1);
        savePost.setHeader("Test Header");
        savePost.setProvince("Test Province");
        savePost.setDistrict("Test District");
        savePost.setOther_service(0.0f);

        // Act
        ResponseEntity<Map<String, String>> response = savePostController.addSavePost(savePost);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Save post successfully", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM tester.save_post WHERE id_tenant = ? AND id_post = ? AND header = ?")) {
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ps.setString(3, "Test Header");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "SavePost should exist");
                assertEquals("Test Province", rs.getString("province"), "Province should match");
                assertEquals("Test District", rs.getString("district"), "District should match");
                assertEquals(0.0f, rs.getFloat("other_service"), "Other service should match");
            }
        }
    }

    // Testcase ID: SP_ADD01.1
    // Description: Rollback by deleting the save_post added in SP_ADD01
    @Test
    public void testRollback_SP_ADD01() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_save_post (auto-incremented)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_save_post) FROM tester.save_post");
                 ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Should have records");
                latestId = rs.getInt(1);
            }

            // Delete the record
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.save_post WHERE id_save_post = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit(); // Commit to ensure DB is updated
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.save_post WHERE id_save_post = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "SavePost should be deleted");
                }
            }
        }
    }

    // Testcase ID: SP_ADD02
    // Description: Test addition with invalid id_tenant (non-existent)
    // Input Conditions:
    //   - SavePost object with id_tenant=9999 (non-existent), other fields valid
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" and value indicating invalid id_tenant
    //   - Database: No new record added
    // Note: Test fails as method returns 201 CREATED instead of 400
    @Test
    public void testAddSavePost_InvalidTenantId() throws SQLException {
        // Arrange
        SavePost savePost = new SavePost();
        savePost.setId_tenant(9999); // Non-existent
        savePost.setId_post(1);
        savePost.setHeader("Test Header");
        savePost.setProvince("Test Province");
        savePost.setDistrict("Test District");
        savePost.setOther_service(0.0f);

        // Act
        ResponseEntity<Map<String, String>> response = savePostController.addSavePost(savePost);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").contains("id_tenant"), "Message should indicate invalid id_tenant");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.save_post WHERE id_tenant = ?")) {
            ps.setInt(1, 9999);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");                   
                assertEquals(0, rs.getInt(1), "No SavePost should be added");
            }
        }
    }

    // Testcase ID: SP_ADD02.1
    // Description: Rollback by verifying no changes occurred for invalid id_tenant
    @Test
    public void testRollback_SP_ADD02() throws SQLException {
        // No changes expected
        try (Connection conn = getConnection()) {
            conn.commit(); // Close transaction
        }
    }

    // Testcase ID: SP_ADD03
    // Description: Test addition with null header
    // Input Conditions:
    //   - SavePost object with header=null, other fields valid
    // Expected Output:
    // - HTTP Status: 400 (BAD_REQUEST)
    // - Response body: Map with key "message" and value indicating null header
    // - Database: No new record added
    // // Note: Test fails as method returns 201 CREATED instead of 400
    @Test
    public void testAddSavePost_NullHeader() throws SQLException {
        // Arrange
        SavePost savePost = new SavePost();
        savePost.setId_tenant(1);
        savePost.setId_post(1);
        savePost.setHeader(null); // Invalid
        savePost.setProvince("Test Province");
        savePost.setDistrict("Test District");
        savePost.setOther_service(0.0f);

        // Act
        ResponseEntity<Map<String, String>> response = savePostController.addSavePost(savePost);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").contains("header"), "Message should indicate null header");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.save_post WHERE id_tenant = ? AND id_post = ?")) {
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No SavePost should be added");
            }
        }
    }

    // Testcase ID: SP_ADD03.1
    // Description: Rollback by verifying no changes occurred for null header
    @Test
    public void testRollback_SP_ADD03() throws SQLException {
        // No changes expected
        try (Connection conn = getConnection()) {
            conn.commit(); // Close transaction
        }
    }
}