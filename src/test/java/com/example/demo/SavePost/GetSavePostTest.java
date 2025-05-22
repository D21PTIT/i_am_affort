package com.example.demo.SavePost;

import com.example.demo.Model.SavePost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for getSavePostList method in SavePostController.
 * Inserts test data for successful case and cleans up afterward.
 * Tests successful retrieval and error cases for id_tenant.
 */
@SpringBootTest
@ActiveProfiles("test")
public class GetSavePostTest {

    @Autowired
    private SavePostController savePostController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: SP_GET01
    // Description: Test successful retrieval of save_post list for existing id_tenant
    // Input Conditions:
    //   - id_tenant = "1"
    //   - Database: Insert temporary post and save_post records
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response body: List of SavePost objects with correct data
    @Test
    public void testGetSavePostList_Success() throws SQLException {
        // Arrange: Insert test data
        try (Connection conn = getConnection()) {
            // Insert post with required fields
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO tester.post (id_post, header, province, district, ward, detail_address, created_at, status, `delete`, other_service, type, owner, phone_number) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, 100);
                ps.setString(2, "Test Post");
                ps.setString(3, "Hanoi");
                ps.setString(4, "Ba Dinh");
                ps.setString(5, "Cau Giay");
                ps.setString(6, "123 Main St");
                ps.setDate(7, new Date(System.currentTimeMillis()));
                ps.setInt(8, 1); // status
                ps.setInt(9, 0); // delete
                ps.setFloat(10, 100.0f); // other_service
                ps.setInt(11, 1); // type
                ps.setString(12, "John Doe"); // owner
                ps.setString(13, "0123456789"); // phone_number
                ps.executeUpdate();
            }
            // Insert save_post
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO tester.save_post (id_save_post, id_tenant, id_post, created_date) " +
                    "VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, 1);
                ps.setInt(2, 1);
                ps.setInt(3, 100);
                ps.setDate(4, new Date(System.currentTimeMillis()));
                ps.executeUpdate();
            }
            conn.commit();
        }

        String idTenant = "1";

        // Act
        ResponseEntity<List<SavePost>> response;
        try {
            response = savePostController.getSavePostList(null, idTenant);
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody().isEmpty(), "Save post list should not be empty");
        assertEquals(1, response.getBody().get(0).getId_save_post(), "id_save_post should be 1");
        assertEquals(1, response.getBody().get(0).getId_tenant(), "id_tenant should be 1");
        assertEquals(100, response.getBody().get(0).getId_post(), "id_post should be 100");
        assertEquals("Test Post", response.getBody().get(0).getHeader(), "header should match");
        assertEquals("Hanoi", response.getBody().get(0).getProvince(), "province should match");
        assertEquals("Ba Dinh", response.getBody().get(0).getDistrict(), "district should match");
        assertEquals(100.0f, response.getBody().get(0).getOther_service(), "other_service should match");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT sp.*, p.header, p.province, p.district, p.other_service " +
                     "FROM tester.save_post sp " +
                     "JOIN tester.post p ON sp.id_post = p.id_post " +
                     "WHERE sp.id_tenant = ?")) {
            ps.setInt(1, 1);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Save post should exist");
                assertEquals(1, rs.getInt("id_save_post"), "id_save_post should be 1");
                assertEquals(1, rs.getInt("id_tenant"), "id_tenant should be 1");
                assertEquals(100, rs.getInt("id_post"), "id_post should be 100");
                //assertEquals("Test Post", rs.getString("header"), "header should match");
                assertEquals("Hanoi", rs.getString("province"), "province should match");
                assertEquals("Ba Dinh", rs.getString("district"), "district should match");
                assertEquals(100.0f, rs.getFloat("other_service"), "other_service should match");
            }
        }
    }

    // Testcase ID: SP_GET01.1
    // Description: Cleanup by deleting test data inserted for SP_GET01
    @Test
    public void testCleanup_SP_GET01() throws SQLException {
        try (Connection conn = getConnection()) {
            // Delete save_post
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM tester.save_post WHERE id_save_post = ?")) {
                ps.setInt(1, 1);
                ps.executeUpdate();
            }
            // Delete post
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM tester.post WHERE id_post = ?")) {
                ps.setInt(1, 100);
                ps.executeUpdate();
            }
            conn.commit();

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM tester.save_post WHERE id_save_post = ?")) {
                ps.setInt(1, 1);
                try (ResultSet rs = ps.executeQuery()) {
                    assertFalse(rs.next(), "Save post should be deleted");
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM tester.post WHERE id_post = ?")) {
                ps.setInt(1, 100);
                try (ResultSet rs = ps.executeQuery()) {
                    assertFalse(rs.next(), "Post should be deleted");
                }
            }
        }
    }

    // Testcase ID: SP_GET02
    // Description: Test retrieval with invalid id_tenant (non-numeric)
    // Input Conditions:
    //   - id_tenant = "invalid"
    // Expected Output:
    //   - HTTP Status: 500 (INTERNAL_SERVER_ERROR)
    //   - Response body: null
    @Test
    public void testGetSavePostList_InvalidIdTenant() {
        // Arrange
        String idTenant = "invalid";

        // Act
        ResponseEntity<List<SavePost>> response;
        try {
            response = savePostController.getSavePostList(null, idTenant);
        } catch (IOException e) {
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Assert response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR");
        assertNull(response.getBody(), "Response body should be null");
    }

    // Testcase ID: SP_GET03
    // Description: Test retrieval with non-existent id_tenant
    // Input Conditions:
    //   - id_tenant = "999"
    //   - Database: No save_post records for id_tenant=999
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response body: Empty list
    @Test
    public void testGetSavePostList_NonExistentIdTenant() {
        // Arrange
        String idTenant = "999";

        // Act
        ResponseEntity<List<SavePost>> response;
        try {
            response = savePostController.getSavePostList(null, idTenant);
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Save post list should be empty");
    }
}