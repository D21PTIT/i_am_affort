package com.example.demo.Auth;

import com.example.demo.Model.User;
import com.example.demo.User.UserController;
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
 * Test class for updateUserInfo method in UserController.
 * Assumes database has:
 * - A valid user with id_user=33, username="b21dccn406", password="manhhung09", user_type=0, status=1, delete=0.
 * - Another user with id_user=2, username="kientran11", password="123456" (inserted for duplicate test).
 * Tests successful update, invalid username/password inputs, and duplicate username.
 * Includes rollback tests to restore original data after each test.
 */
@SpringBootTest
@ActiveProfiles("test")
public class UpdateUserInfoTest {

    @Autowired
    private UserController userController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: AUTH_UPDATE_01
    // Description: Test successful update of username and password for an existing user
    // Input Conditions:
    //   - id_user="33"
    //   - User object with username="newb21dccn406", password="newmanhhung09"
    // Expected Output:
    //   - HTTP Status: 201 CREATED
    //   - Response body: Map with "message": "Updated successfully!"
    //   - Database: User updated to username="newb21dccn406", password="newmanhhung09"
    @Test
    public void testUpdateUserInfo_Success() throws SQLException {
        // Arrange
        User user = new User();
        user.setUsername("newb21dccn406");
        user.setPassword("newmanhhung09");
        String idUser = "33";

        // Act
        ResponseEntity<Map<String, String>> response = userController.updateUserInfo(user, idUser);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Updated successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT username, password FROM tester.user WHERE id_user = ?")) {
            ps.setInt(1, 33);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "User should exist");
                assertEquals("newb21dccn406", rs.getString("username"), "Username should be updated");
                assertEquals("newmanhhung09", rs.getString("password"), "Password should be updated");
            }
        }
    }

    // Testcase ID: AUTH_UPDATE_01.1
    // Description: Rollback data for AUTH_UPDATE_01
    @Test
    public void testRollback_AUTH_UPDATE_01() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE tester.user SET username = ?, password = ? WHERE id_user = ?")) {
            ps.setString(1, "b21dccn406");
            ps.setString(2, "manhhung09");
            ps.setInt(3, 33);
            ps.executeUpdate();
            conn.commit();

            // Verify rollback
            try (PreparedStatement verifyPs = conn.prepareStatement("SELECT username, password FROM tester.user WHERE id_user = ?")) {
                verifyPs.setInt(1, 33);
                try (ResultSet rs = verifyPs.executeQuery()) {
                    assertTrue(rs.next(), "User should exist");
                    assertEquals("b21dccn406", rs.getString("username"), "Username should be restored");
                    assertEquals("manhhung09", rs.getString("password"), "Password should be restored");
                }
            }
        }
    }

    // Testcase ID: AUTH_UPDATE_02
    // Description: Test failure when username is null
    // Input Conditions:
    //   - id_user="33"
    //   - User object with username=null, password="newmanhhung09"
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST
    //   - Response body: Map with "message": "Username must not be null or empty"
    @Test
    public void testUpdateUserInfo_NullUsername() {
        // Arrange
        User user = new User();
        user.setUsername(null);
        user.setPassword("newmanhhung09");
        String idUser = "33";

        // Act
        ResponseEntity<Map<String, String>> response = userController.updateUserInfo(user, idUser);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Username must not be null or empty", response.getBody().get("message"), "Message should indicate invalid username");
    }

    // Testcase ID: AUTH_UPDATE_02.1
    // Description: Rollback data for AUTH_UPDATE_02 (no change expected)
    @Test
    public void testRollback_AUTH_UPDATE_02() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT username, password FROM tester.user WHERE id_user = ?")) {
            ps.setInt(1, 33);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "User should exist");
                assertEquals("b21dccn406", rs.getString("username"), "Username should remain unchanged");
                assertEquals("manhhung09", rs.getString("password"), "Password should remain unchanged");
            }
            conn.commit();
        }
    }

    // Testcase ID: AUTH_UPDATE_03
    // Description: Test failure when password is null
    // Input Conditions:
    //   - id_user="33"
    //   - User object with username="newb21dccn406", password=null
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST
    //   - Response body: Map with "message": "Password must not be null or empty"
    @Test
    public void testUpdateUserInfo_NullPassword() {
        // Arrange
        User user = new User();
        user.setUsername("newb21dccn406");
        user.setPassword(null);
        String idUser = "33";

        // Act
        ResponseEntity<Map<String, String>> response = userController.updateUserInfo(user, idUser);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Password must not be null or empty", response.getBody().get("message"), "Message should indicate invalid password");
    }

    // Testcase ID: AUTH_UPDATE_03.1
    // Description: Rollback data for AUTH_UPDATE_03 (no change expected)
    @Test
    public void testRollback_AUTH_UPDATE_03() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT username, password FROM tester.user WHERE id_user = ?")) {
            ps.setInt(1, 33);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "User should exist");
                assertEquals("b21dccn406", rs.getString("username"), "Username should remain unchanged");
                assertEquals("manhhung09", rs.getString("password"), "Password should remain unchanged");
            }
            conn.commit();
        }
    }

    // Testcase ID: AUTH_UPDATE_04
    // Description: Test failure when username is empty
    // Input Conditions:
    //   - id_user="33"
    //   - User object with username="", password="newmanhhung09"
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST
    //   - Response body: Map with "message": "Username must not be null or empty"
    @Test
    public void testUpdateUserInfo_EmptyUsername() {
        // Arrange
        User user = new User();
        user.setUsername("");
        user.setPassword("newmanhhung09");
        String idUser = "33";

        // Act
        ResponseEntity<Map<String, String>> response = userController.updateUserInfo(user, idUser);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Username must not be null or empty", response.getBody().get("message"), "Message should indicate invalid username");
    }

    // Testcase ID: AUTH_UPDATE_04.1
    // Description: Rollback data for AUTH_UPDATE_04 (no change expected)
    @Test
    public void testRollback_AUTH_UPDATE_04() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT username, password FROM tester.user WHERE id_user = ?")) {
            ps.setInt(1, 33);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "User should exist");
                assertEquals("b21dccn406", rs.getString("username"), "Username should remain unchanged");
                assertEquals("manhhung09", rs.getString("password"), "Password should remain unchanged");
            }
            conn.commit();
        }
    }

    // Testcase ID: AUTH_UPDATE_05
    // Description: Test failure when password is empty
    // Input Conditions:
    //   - id_user="33"
    //   - User object with username="newb21dccn406", password=""
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST
    //   - Response body: Map with "message": "Password must not be null or empty"
    @Test
    public void testUpdateUserInfo_EmptyPassword() {
        // Arrange
        User user = new User();
        user.setUsername("newb21dccn406");
        user.setPassword("");
        String idUser = "33";

        // Act
        ResponseEntity<Map<String, String>> response = userController.updateUserInfo(user, idUser);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Password must not be null or empty", response.getBody().get("message"), "Message should indicate invalid password");
    }

    // Testcase ID: AUTH_UPDATE_05.1
    // Description: Rollback data for AUTH_UPDATE_05 (no change expected)
    @Test
    public void testRollback_AUTH_UPDATE_05() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT username, password FROM tester.user WHERE id_user = ?")) {
            ps.setInt(1, 33);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "User should exist");
                assertEquals("b21dccn406", rs.getString("username"), "Username should remain unchanged");
                assertEquals("manhhung09", rs.getString("password"), "Password should remain unchanged");
            }
            conn.commit();
        }
    }

    // Testcase ID: AUTH_UPDATE_06
    // Description: Test failure when username duplicates another user's username
    // Input Conditions:
    //   - id_user="33"
    //   - User object with username="kientran11", password="newmanhhung09"
    //   - Database: User exists with id_user=33, username="b21dccn406", and insert another user with id_user=2, username="kientran11"
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST
    //   - Response body: Map with "message": "Username already exists"
    //   - Database: No changes to id_user=33
    @Test
    public void testUpdateUserInfo_DuplicateUsername() throws SQLException {
        // Arrange: Insert kientran11
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tester.user (id_user, username, password, user_type, status, `delete`, name, email) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE username = ?, password = ?, email = ?")) {
            ps.setInt(1, 2);
            ps.setString(2, "kientran11");
            ps.setString(3, "123456");
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setString(7, "Kien Tran");
            ps.setString(8, "kientran11@example.com");
            ps.setString(9, "kientran11");
            ps.setString(10, "123456");
            ps.setString(11, "kientran11@example.com");
            ps.executeUpdate();
            conn.commit();
        }

        User user = new User();
        user.setUsername("kientran11");
        user.setPassword("newmanhhung09");
        String idUser = "33";

        // Act
        ResponseEntity<Map<String, String>> response = userController.updateUserInfo(user, idUser);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Username already exists", response.getBody().get("message"), "Message should indicate duplicate username");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT username, password FROM tester.user WHERE id_user = ?")) {
            ps.setInt(1, 33);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "User should exist");
                assertEquals("b21dccn406", rs.getString("username"), "Username should remain unchanged");
                assertEquals("manhhung09", rs.getString("password"), "Password should remain unchanged");
            }
        }
    }

    // Testcase ID: AUTH_UPDATE_06.1
    // Description: Rollback data for AUTH_UPDATE_06 (remove kientran11 if inserted)
    @Test
    public void testRollback_AUTH_UPDATE_06() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.user WHERE id_user = ?")) {
            ps.setInt(1, 2);
            ps.executeUpdate();
            conn.commit();

            // Verify rollback
            try (PreparedStatement verifyPs = conn.prepareStatement("SELECT COUNT(*) FROM tester.user WHERE id_user = ?")) {
                verifyPs.setInt(1, 2);
                try (ResultSet rs = verifyPs.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "User id_user=2 should be removed");
                }
            }
        }
    }
}