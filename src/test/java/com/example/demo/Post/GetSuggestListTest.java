package com.example.demo.Post;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import com.example.demo.Model.Post;
@Transactional

@SpringBootTest
@ActiveProfiles("test")
public class GetSuggestListTest {

    @Autowired
    private PostController postController;

    private Connection connection;
    private static final int TEST_PROPERTY_ID = 60;
    private static final int TEST_USER_ID = 33;
    private static final int TEST_TENANT_ID = 9999;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);

        // Clear existing data
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.post WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.suggest_form WHERE id_tenant = ?")) {
            ps.setInt(1, TEST_TENANT_ID);
            ps.executeUpdate();
        }

        // Insert test data into suggest_form
        String insertSuggestQuery = "INSERT INTO bds.suggest_form (id_tenant, province, district, ward, detail_address) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertSuggestQuery)) {
            ps.setInt(1, TEST_TENANT_ID);
            ps.setString(2, "Hanoi");
            ps.setString(3, "Cau Giay");
            ps.setString(4, "Dich Vong");
            ps.setString(5, "123 Street");
            int rows = ps.executeUpdate();
            assertEquals(1, rows, "Failed to insert test suggest_form");
        }

        // Insert test data into post
        String insertPostQuery = "INSERT INTO bds.post (header, province, district, ward, detail_address, " +
                "surface_area, useable_area, width, length, flours, bedrooms, toilets, status, short_description, " +
                "detail_des, created_by_user, type, owner, phone_number, email, company_name, exp_date, created_at, " +
                "updated_at, `delete`, id_property, public_price, electric, water, water_type, internet, clean, elevator, " +
                "other_service, longitude, latitude) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertPostQuery)) {
            ps.setString(1, "Test Post");
            ps.setString(2, "Hanoi");
            ps.setString(3, "Cau Giay");
            ps.setString(4, "Dich Vong");
            ps.setString(5, "123 Street");
            ps.setFloat(6, 100.0f);
            ps.setFloat(7, 80.0f);
            ps.setFloat(8, 10.0f);
            ps.setFloat(9, 10.0f);
            ps.setInt(10, 2);
            ps.setInt(11, 3);
            ps.setInt(12, 2);
            ps.setInt(13, 1);
            ps.setString(14, "Short desc");
            ps.setString(15, "Detail desc");
            ps.setInt(16, TEST_USER_ID);
            ps.setInt(17, 1);
            ps.setString(18, "John Doe");
            ps.setString(19, "1234567890");
            ps.setString(20, "john@example.com");
            ps.setString(21, "ABC Company");
            ps.setDate(22, Date.valueOf("2025-12-31"));
            ps.setDate(23, Date.valueOf("2025-05-21"));
            ps.setDate(24, Date.valueOf("2025-05-21"));
            ps.setInt(25, 0);
            ps.setInt(26, TEST_PROPERTY_ID);
            ps.setInt(27, 1000000);
            ps.setFloat(28, 3500.0f);
            ps.setFloat(29, 5000.0f);
            ps.setInt(30, 1);
            ps.setFloat(31, 200000.0f);
            ps.setFloat(32, 100000.0f);
            ps.setFloat(33, 50000.0f);
            ps.setFloat(34, 30000.0f);
            ps.setFloat(35, 105.0f);
            ps.setFloat(36, 20.0f);

            int rows = ps.executeUpdate();
            assertEquals(1, rows, "Failed to insert test post");
        }

        connection.commit();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.post WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.suggest_form WHERE id_tenant = ?")) {
            ps.setInt(1, TEST_TENANT_ID);
            ps.executeUpdate();
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testGetSuggestList_Success() {
        // Arrange
        String idTenant = String.valueOf(TEST_TENANT_ID);

        // Act
        ResponseEntity<List<Post>> response = postController.getSuggestList(idTenant);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Post> posts = response.getBody();
        assertNotNull(posts, "Post list should not be null");
        assertEquals(1, posts.size(), "Post list should contain one post");
        Post post = posts.get(0);
        assertEquals("Test Post", post.getHeader(), "Header should match");
        assertEquals("Hanoi", post.getProvince(), "Province should match");
        assertEquals(TEST_PROPERTY_ID, post.getId_property(), "Property ID should match");
        assertEquals(TEST_USER_ID, post.getCreated_by_user(), "Created by user should match");
    }

    @Test
    public void testGetSuggestList_TenantNotFound() {
        // Arrange
        String idTenant = "8888"; // Non-existent tenant ID

        // Act
        ResponseEntity<List<Post>> response = postController.getSuggestList(idTenant);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Post> posts = response.getBody();
        assertNotNull(posts, "Post list should not be null");
        assertTrue(posts.isEmpty(), "Post list should be empty for non-existent tenant");

        // Verify no changes in database
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM bds.post WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Original post should still exist");
                assertEquals(0, rs.getInt("delete"), "Original post should not be deleted");
            }
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testGetSuggestList_InvalidTenantId() {
        // Arrange
        String idTenant = "invalid"; // Non-numeric tenant ID

        // Act
        ResponseEntity<List<Post>> response = postController.getSuggestList(idTenant);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR for invalid tenant ID");
        List<Post> posts = response.getBody();
        assertNotNull(posts, "Post list should not be null");
        assertTrue(posts.isEmpty(), "Post list should be empty for invalid tenant ID");

        // Verify no changes in database
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM bds.post WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Original post should still exist");
                assertEquals(0, rs.getInt("delete"), "Original post should not be deleted");
            }
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        }
    }
}