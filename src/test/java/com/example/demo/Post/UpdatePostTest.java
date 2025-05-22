package com.example.demo.Post;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.example.demo.Model.Post;
@Transactional

@SpringBootTest
@ActiveProfiles("test")
public class UpdatePostTest {

    @Autowired
    private PostController postController;

    private Connection connection;
    private static final int TEST_PROPERTY_ID = 60;
    private static final int TEST_USER_ID = 33;
    private int testPostId;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);

        // Clear existing data
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.post WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.executeUpdate();
        }

        // Insert test data
        String insertQuery = "INSERT INTO bds.post (header, province, district, ward, detail_address, " +
                "surface_area, useable_area, width, length, flours, bedrooms, toilets, status, short_description, " +
                "detail_des, created_by_user, type, owner, phone_number, email, company_name, exp_date, created_at, " +
                "updated_at, `delete`, id_property, public_price, electric, water, water_type, internet, clean, elevator, " +
                "other_service, longitude, latitude) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Original Post");
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

            // Get generated id_post
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    testPostId = rs.getInt(1);
                    assertTrue(testPostId > 0, "Generated id_post should be positive");
                } else {
                    fail("No generated key returned for post");
                }
            }
        }

        connection.commit();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.post WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.executeUpdate();
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testUpdatePost_Success() {
        // Arrange
        Post post = new Post();
        post.setHeader("Updated Post");
        post.setProvince("Ho Chi Minh");
        post.setDistrict("District 1");
        post.setWard("Ben Nghe");
        post.setDetail_address("456 Street");
        post.setSurface_area(120.0f);
        post.setUseable_area(100.0f);
        post.setWidth(12.0f);
        post.setLength(10.0f);
        post.setFlours(3);
        post.setBedrooms(4);
        post.setToilets(3);
        post.setStatus(1);
        post.setShort_des("Updated short desc");
        post.setDetail_des("Updated detail desc");
        post.setCreated_by_user(TEST_USER_ID);
        post.setType(1);
        post.setOwner("Jane Doe");
        post.setPhone_number("0987654321");
        post.setEmail("jane@example.com");
        post.setCompany_name("XYZ Company");
        post.setExp_date(Date.valueOf("2026-12-31"));
        post.setCreated_at(Date.valueOf("2025-05-21"));
        post.setUpdated_at(Date.valueOf("2025-05-22"));
        post.setId_property(TEST_PROPERTY_ID);
        post.setPublic_price(2000000);
        post.setElectric(4000.0f);
        post.setWater(6000.0f);
        post.setWater_type(1);
        post.setInternet(250000.0f);
        post.setClean(120000.0f);
        post.setElevator(60000.0f);
        post.setOther_service(40000.0f);
        post.setLongitude(106.0f);
        post.setLatitude(10.0f);

        String idPost = String.valueOf(testPostId);

        // Act
        ResponseEntity<Map<String, String>> response = postController.updatePost(post, idPost);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Post updated successfully!", responseBody.get("message"), "Message should match");

        // Verify data in database
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM bds.post WHERE id_post = ?")) {
            ps.setInt(1, testPostId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Post should exist in database");
                assertEquals("Updated Post", rs.getString("header"), "Header should match");
                assertEquals("Ho Chi Minh", rs.getString("province"), "Province should match");
            }
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testUpdatePost_PostNotFound() {
        // Arrange
        Post post = new Post();
        post.setHeader("Updated Post");
        post.setProvince("Ho Chi Minh");
        post.setDistrict("District 1");
        post.setWard("Ben Nghe");
        post.setDetail_address("456 Street");
        post.setSurface_area(120.0f);
        post.setUseable_area(100.0f);
        post.setWidth(12.0f);
        post.setLength(10.0f);
        post.setFlours(3);
        post.setBedrooms(4);
        post.setToilets(3);
        post.setStatus(1);
        post.setShort_des("Updated short desc");
        post.setDetail_des("Updated detail desc");
        post.setCreated_by_user(TEST_USER_ID);
        post.setType(1);
        post.setOwner("Jane Doe");
        post.setPhone_number("0987654321");
        post.setEmail("jane@example.com");
        post.setCompany_name("XYZ Company");
        post.setExp_date(Date.valueOf("2026-12-31"));
        post.setCreated_at(Date.valueOf("2025-05-21"));
        post.setUpdated_at(Date.valueOf("2025-05-22"));
        post.setId_property(TEST_PROPERTY_ID);
        post.setPublic_price(2000000);
        post.setElectric(4000.0f);
        post.setWater(6000.0f);
        post.setWater_type(1);
        post.setInternet(250000.0f);
        post.setClean(120000.0f);
        post.setElevator(60000.0f);
        post.setOther_service(40000.0f);
        post.setLongitude(106.0f);
        post.setLatitude(10.0f);

        String idPost = "999"; // Non-existent post ID

        // Act
        ResponseEntity<Map<String, String>> response = postController.updatePost(post, idPost);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR for non-existent post");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Error occurred", responseBody.get("message"), "Message should match");

        // Verify data not updated in database
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM bds.post WHERE id_post = ?")) {
            ps.setInt(1, testPostId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Original post should still exist");
                assertEquals("Original Post", rs.getString("header"), "Header should not be updated");
            }
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testUpdatePost_InvalidData() {
        // Arrange
        Post post = new Post();
        post.setHeader(null); // Invalid header
        post.setProvince("Ho Chi Minh");
        post.setDistrict("District 1");
        post.setWard("Ben Nghe");
        post.setDetail_address("456 Street");
        post.setSurface_area(120.0f);
        post.setUseable_area(100.0f);
        post.setWidth(12.0f);
        post.setLength(10.0f);
        post.setFlours(3);
        post.setBedrooms(4);
        post.setToilets(3);
        post.setStatus(1);
        post.setShort_des("Updated short desc");
        post.setDetail_des("Updated detail desc");
        post.setCreated_by_user(TEST_USER_ID);
        post.setType(1);
        post.setOwner("Jane Doe");
        post.setPhone_number("0987654321");
        post.setEmail("jane@example.com");
        post.setCompany_name("XYZ Company");
        post.setExp_date(Date.valueOf("2026-12-31"));
        post.setCreated_at(Date.valueOf("2025-05-21"));
        post.setUpdated_at(Date.valueOf("2025-05-22"));
        post.setId_property(TEST_PROPERTY_ID);
        post.setPublic_price(2000000);
        post.setElectric(4000.0f);
        post.setWater(6000.0f);
        post.setWater_type(1);
        post.setInternet(250000.0f);
        post.setClean(120000.0f);
        post.setElevator(60000.0f);
        post.setOther_service(40000.0f);
        post.setLongitude(106.0f);
        post.setLatitude(10.0f);

        String idPost = String.valueOf(testPostId);

        // Act
        ResponseEntity<Map<String, String>> response = postController.updatePost(post, idPost);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR for invalid data");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Error occurred", responseBody.get("message"), "Message should match");

        // Verify data not updated in database
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM bds.post WHERE id_post = ?")) {
            ps.setInt(1, testPostId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Original post should still exist");
                assertEquals("Original Post", rs.getString("header"), "Header should not be updated");
            }
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testUpdatePost_InvalidPostId() {
        // Arrange
        Post post = new Post();
        post.setHeader("Updated Post");
        post.setProvince("Ho Chi Minh");
        post.setDistrict("District 1");
        post.setWard("Ben Nghe");
        post.setDetail_address("456 Street");
        post.setSurface_area(120.0f);
        post.setUseable_area(100.0f);
        post.setWidth(12.0f);
        post.setLength(10.0f);
        post.setFlours(3);
        post.setBedrooms(4);
        post.setToilets(3);
        post.setStatus(1);
        post.setShort_des("Updated short desc");
        post.setDetail_des("Updated detail desc");
        post.setCreated_by_user(TEST_USER_ID);
        post.setType(1);
        post.setOwner("Jane Doe");
        post.setPhone_number("0987654321");
        post.setEmail("jane@example.com");
        post.setCompany_name("XYZ Company");
        post.setExp_date(Date.valueOf("2026-12-31"));
        post.setCreated_at(Date.valueOf("2025-05-21"));
        post.setUpdated_at(Date.valueOf("2025-05-22"));
        post.setId_property(TEST_PROPERTY_ID);
        post.setPublic_price(2000000);
        post.setElectric(4000.0f);
        post.setWater(6000.0f);
        post.setWater_type(1);
        post.setInternet(250000.0f);
        post.setClean(120000.0f);
        post.setElevator(60000.0f);
        post.setOther_service(40000.0f);
        post.setLongitude(106.0f);
        post.setLatitude(10.0f);

        String idPost = "invalid"; // Non-numeric post ID

        // Act
        ResponseEntity<Map<String, String>> response = postController.updatePost(post, idPost);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR for invalid post ID");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Error occurred", responseBody.get("message"), "Message should match");

        // Verify data not updated in database
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM bds.post WHERE id_post = ?")) {
            ps.setInt(1, testPostId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Original post should still exist");
                assertEquals("Original Post", rs.getString("header"), "Header should not be updated");
            }
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        }
    }
}