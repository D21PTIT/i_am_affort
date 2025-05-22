package com.example.demo.Auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Model.User;
import com.example.demo.User.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for signIn method in UserController.
 * Assumes database has:
 * - A valid user with username="b21dccn406", password="manhhung09", user_type=0, status=1, delete=0.
 * - An inactive user with username="kientran11", password="123456", user_type=0, status=0, delete=0.
 * Tests successful login, invalid credentials, null/empty inputs, and token expiration.
 */
@SpringBootTest
@ActiveProfiles("test")
public class SignInTest {

    @Autowired
    private UserController userController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: AUTH_SIGNIN01
    // Description: Test successful login with valid credentials
    // Input Conditions:
    //   - username="b21dccn406", password="manhhung09"
    //   - Database: User exists with username="b21dccn406", password="manhhung09", user_type=0, status=1, delete=0
    // Expected Output:
    //   - HTTP Status: 200 OK
    //   - Header: Authorization contains "Bearer <token>"
    //   - Body: Valid JWT token with claims (id_user, username, password, name, user_type)
    @Test
    public void testSignIn_Success() {
        // Arrange
        User user = new User();
        user.setUsername("b21dccn406");
        user.setPassword("manhhung09");

        // Act
        ResponseEntity<String> response = userController.signIn(user);

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should contain token");
        assertNotNull(response.getHeaders().get(HttpHeaders.AUTHORIZATION), "Authorization header should be present");
        assertTrue(response.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).startsWith("Bearer "), "Authorization header should contain Bearer token");

        // Verify token
        String token = response.getBody();
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256("secret_key")).build().verify(token);
        assertEquals("b21dccn406", decodedJWT.getClaim("username").asString(), "Token should contain correct username");
        assertEquals("manhhung09", decodedJWT.getClaim("password").asString(), "Token should contain correct password");
        assertEquals(0, decodedJWT.getClaim("user_type").asInt(), "Token should contain user_type 0");
        assertNotNull(decodedJWT.getClaim("id_user").asInt(), "Token should contain id_user");
        assertNotNull(decodedJWT.getClaim("name").asString(), "Token should contain name");
        assertTrue(decodedJWT.getExpiresAt().after(new Date()), "Token should not be expired");
    }

    // Testcase ID: AUTH_SIGNIN02
    // Description: Test login with non-existent username
    // Input Conditions:
    //   - username="nonexistent", password="testpass"
    //   - Database: No user with username="nonexistent"
    // Expected Output:
    //   - HTTP Status: 401 UNAUTHORIZED
    //   - No body or Authorization header
    @Test
    public void testSignIn_NonExistentUsername() {
        // Arrange
        User user = new User();
        user.setUsername("nonexistent");
        user.setPassword("testpass");

        // Act
        ResponseEntity<String> response = userController.signIn(user);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "HTTP status should be UNAUTHORIZED");
        assertNull(response.getBody(), "Response body should be null");
        assertNull(response.getHeaders().get(HttpHeaders.AUTHORIZATION), "No Authorization header should be present");
    }

    // Testcase ID: AUTH_SIGNIN03
    // Description: Test login with incorrect password
    // Input Conditions:
    //   - username="b21dccn406", password="wrongpass"
    //   - Database: User exists with username="b21dccn406", password="manhhung09"
    // Expected Output:
    //   - HTTP Status: 401 UNAUTHORIZED
    //   - No body or Authorization header
    @Test
    public void testSignIn_IncorrectPassword() {
        // Arrange
        User user = new User();
        user.setUsername("b21dccn406");
        user.setPassword("wrongpass");

        // Act
        ResponseEntity<String> response = userController.signIn(user);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "HTTP status should be UNAUTHORIZED");
        assertNull(response.getBody(), "Response body should be null");
        assertNull(response.getHeaders().get(HttpHeaders.AUTHORIZATION), "No Authorization header should be present");
    }

    // Testcase ID: AUTH_SIGNIN04
    // Description: Test login with inactive account using existing kientran11 (status=0)
    // Input Conditions:
    //   - username="kientran11", password="123456"
    //   - Database: User exists with username="kientran11", password="123456", user_type=0, status=0, delete=0
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST
    //   - Body: "Inactive account"
    @Test
    public void testSignIn_InactiveKientran11() {
        // Arrange
        User user = new User();
        user.setUsername("kientran11");
        user.setPassword("123456");

        // Act
        ResponseEntity<String> response = userController.signIn(user);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertEquals("Inactive account", response.getBody(), "Response should indicate inactive account");
        assertNull(response.getHeaders().get(HttpHeaders.AUTHORIZATION), "No Authorization header should be present");
    }

    // Testcase ID: AUTH_SIGNIN05
    // Description: Test login with null username
    // Input Conditions:
    //   - username=null, password="testpass"
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST or 500 INTERNAL_SERVER_ERROR
    // Note: Controller does not validate null username, may throw SQLException
    @Test
    public void testSignIn_NullUsername() {
        // Arrange
        User user = new User();
        user.setUsername(null);
        user.setPassword("testpass");

        // Act
        ResponseEntity<String> response = userController.signIn(user);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR due to SQLException");
        assertNotNull(response.getBody(), "Response body should contain error message");
        assertTrue(response.getBody().contains("Error"), "Response should indicate an error");
    }

    // Testcase ID: AUTH_SIGNIN06
    // Description: Test login with null password
    // Input Conditions:
    //   - username="b21dccn406", password=null
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST or 500 INTERNAL_SERVER_ERROR
    @Test
    public void testSignIn_NullPassword() {
        // Arrange
        User user = new User();
        user.setUsername("b21dccn406");
        user.setPassword(null);

        // Act
        ResponseEntity<String> response = userController.signIn(user);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "HTTP status should be INTERNAL_SERVER_ERROR due to SQLException");
        assertNotNull(response.getBody(), "Response body should contain error message");
        assertTrue(response.getBody().contains("Error"), "Response should indicate an error");
    }

    // Testcase ID: AUTH_SIGNIN07
    // Description: Test login with empty username
    // Input Conditions:
    //   - username="", password="testpass"
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST or 500 INTERNAL_SERVER_ERROR
    @Test
    public void testSignIn_EmptyUsername() {
        // Arrange
        User user = new User();
        user.setUsername("");
        user.setPassword("testpass");

        // Act
        ResponseEntity<String> response = userController.signIn(user);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "HTTP status should be UNAUTHORIZED due to no matching user");
        assertNull(response.getBody(), "Response body should be null");
        assertNull(response.getHeaders().get(HttpHeaders.AUTHORIZATION), "No Authorization header should be present");
    }

    // Testcase ID: AUTH_SIGNIN08
    // Description: Test login with empty password
    // Input Conditions:
    //   - username="b21dccn406", password=""
    // Expected Output:
    //   - HTTP Status: 401 UNAUTHORIZED
    @Test
    public void testSignIn_EmptyPassword() {
        // Arrange
        User user = new User();
        user.setUsername("b21dccn406");
        user.setPassword("");

        // Act
        ResponseEntity<String> response = userController.signIn(user);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "HTTP status should be UNAUTHORIZED due to incorrect password");
        assertNull(response.getBody(), "Response body should be null");
        assertNull(response.getHeaders().get(HttpHeaders.AUTHORIZATION), "No Authorization header should be present");
    }

    // Testcase ID: AUTH_SIGNIN09
    // Description: Test token expiration time
    // Input Conditions:
    //   - username="b21dccn406", password="manhhung09"
    //   - Database: User exists with username="b21dccn406", password="manhhung09"
    // Expected Output:
    //   - HTTP Status: 200 OK
    //   - Token contains exp claim ~10 minutes from current time
    @Test
    public void testSignIn_TokenExpiration() {
        // Arrange
        User user = new User();
        user.setUsername("b21dccn406");
        user.setPassword("manhhung09");

        // Act
        ResponseEntity<String> response = userController.signIn(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should contain token");

        // Verify token expiration
        String token = response.getBody();
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256("secret_key")).build().verify(token);
        long expiresAt = decodedJWT.getExpiresAt().getTime();
        long currentTime = System.currentTimeMillis();
        long expectedExpiration = currentTime + 600000; // 10 minutes
        assertTrue(expiresAt > currentTime, "Token should not be expired");
        assertTrue(expiresAt <= expectedExpiration + 1000, "Token expiration should be within 10 minutes");
    }
}