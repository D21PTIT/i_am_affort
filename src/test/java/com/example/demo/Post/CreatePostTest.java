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
public class CreatePostTest {

    @Autowired
    private PostController postController;

    private Connection connection;
    private static final int TEST_PROPERTY_ID = 60;
    private static final int TEST_USER_ID = 33;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);

        // Clear existing data
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.post WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.executeUpdate();
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
    public void testCreatePost_Success() {
        // Arrange
        Post post = new Post();
        post.setHeader("Test Post");
        post.setProvince("Hanoi");
        post.setDistrict("Cau Giay");
        post.setWard("Dich Vong");
        post.setDetail_address("123 Street");
        post.setSurface_area(100.0f);
        post.setUseable_area(80.0f);
        post.setWidth(10.0f);
        post.setLength(10.0f);
        post.setFlours(2);
        post.setBedrooms(3);
        post.setToilets(2);
        post.setStatus(1);
        post.setShort_des("Short desc");
        post.setDetail_des("Detail desc");
        post.setCreated_by_user(TEST_USER_ID);
        post.setType(1);
        post.setOwner("John Doe");
        post.setPhone_number("1234567890");
        post.setEmail("john@example.com");
        post.setCompany_name("ABC Company");
        post.setExp_date(Date.valueOf("2025-12-31"));
        post.setCreated_at(Date.valueOf("2025-05-21"));
        post.setUpdated_at(Date.valueOf("2025-05-21"));
        post.setId_property(TEST_PROPERTY_ID);
        post.setPublic_price(1000000);
        post.setElectric(3500.0f);
        post.setWater(5000.0f);
        post.setWater_type(1);
        post.setInternet(200000.0f);
        post.setClean(100000.0f);
        post.setElevator(50000.0f);
        post.setOther_service(30000.0f);
        post.setLongitude(105.0f);
        post.setLatitude(20.0f);

        // Act
        ResponseEntity<Map<String, String>> response = postController.createPost(post);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Post created successfully", responseBody.get("message"), "Message should match");

        // Verify data in database
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM bds.post WHERE id_property = ? AND created_by_user = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            ps.setInt(2, TEST_USER_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Post should exist in database");
                assertEquals("Test Post", rs.getString("header"), "Header should match");
                assertEquals("Hanoi", rs.getString("province"), "Province should match");
            }
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testCreatePost_InvalidData() {
        // Arrange
        Post post = new Post();
        post.setHeader(null); // Invalid header
        post.setProvince("Hanoi");
        post.setDistrict("Cau Giay");
        post.setWard("Dich Vong");
        post.setDetail_address("123 Street");
        post.setSurface_area(100.0f);
        post.setUseable_area(80.0f);
        post.setWidth(10.0f);
        post.setLength(10.0f);
        post.setFlours(2);
        post.setBedrooms(3);
        post.setToilets(2);
        post.setStatus(1);
        post.setShort_des("Short desc");
        post.setDetail_des("Detail desc");
        post.setCreated_by_user(TEST_USER_ID);
        post.setType(1);
        post.setOwner("John Doe");
        post.setPhone_number("1234567890");
        post.setEmail("john@example.com");
        post.setCompany_name("ABC Company");
        post.setExp_date(Date.valueOf("2025-12-31"));
        post.setCreated_at(Date.valueOf("2025-05-21"));
        post.setUpdated_at(Date.valueOf("2025-05-21"));
        post.setId_property(TEST_PROPERTY_ID);
        post.setPublic_price(1000000);
        post.setElectric(3500.0f);
        post.setWater(5000.0f);
        post.setWater_type(1);
        post.setInternet(200000.0f);
        post.setClean(100000.0f);
        post.setElevator(50000.0f);
        post.setOther_service(30000.0f);
        post.setLongitude(105.0f);
        post.setLatitude(20.0f);

        // Act
        ResponseEntity<Map<String, String>> response = postController.createPost(post);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR for invalid data");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Error occurred while creating post", responseBody.get("message"), "Message should match");

        // Verify no data in database
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT COUNT(*) FROM bds.post WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have data");
                assertEquals(0, rs.getInt(1), "No post should be created");
            }
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testCreatePost_MissingImageList() {
        // Arrange
        Post post = new Post();
        post.setHeader("Test Post");
        post.setProvince("Hanoi");
        post.setDistrict("Cau Giay");
        post.setWard("Dich Vong");
        post.setDetail_address("123 Street");
        post.setSurface_area(100.0f);
        post.setUseable_area(80.0f);
        post.setWidth(10.0f);
        post.setLength(10.0f);
        post.setFlours(2);
        post.setBedrooms(3);
        post.setToilets(2);
        post.setStatus(1);
        post.setShort_des("Short desc");
        post.setDetail_des("Detail desc");
        post.setCreated_by_user(TEST_USER_ID);
        post.setType(1);
        post.setOwner("John Doe");
        post.setPhone_number("1234567890");
        post.setEmail("john@example.com");
        post.setCompany_name("ABC Company");
        post.setExp_date(Date.valueOf("2025-12-31"));
        post.setCreated_at(Date.valueOf("2025-05-21"));
        post.setUpdated_at(Date.valueOf("2025-05-21"));
        post.setId_property(TEST_PROPERTY_ID);
        post.setPublic_price(1000000);
        post.setElectric(3500.0f);
        post.setWater(5000.0f);
        post.setWater_type(1);
        post.setInternet(200000.0f);
        post.setClean(100000.0f);
        post.setElevator(50000.0f);
        post.setOther_service(30000.0f);
        post.setLongitude(105.0f);
        post.setLatitude(20.0f);
        post.setImage_list(null); // Missing image list

        // Act
        ResponseEntity<Map<String, String>> response = postController.createPost(post);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST for missing image list");
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals("Image list is required and cannot be empty", responseBody.get("message"), "Message should match");

        // Verify no data in database
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT COUNT(*) FROM bds.post WHERE id_property = ?")) {
            ps.setInt(1, TEST_PROPERTY_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have data");
                assertEquals(0, rs.getInt(1), "No post should be created");
            }
        } catch (SQLException e) {
            fail("Unexpected SQLException: " + e.getMessage());
        }
    }
}