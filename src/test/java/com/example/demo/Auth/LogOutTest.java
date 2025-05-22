package com.example.demo.Auth;

import com.example.demo.Model.User;
import com.example.demo.User.UserController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for logOut method in UserController.
 * Assumes a valid user with username="b21dccn406", password="manhhung09", user_type=0, status=1, delete=0.
 * Tests successful logout with an active session, logout with no session, and error handling.
 */
@SpringBootTest
@ActiveProfiles("test")
public class LogOutTest {

    @Autowired
    private UserController userController;

    // Helper method to create a real HttpServletRequest from Spring context
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No request context available");
        }
        return attributes.getRequest();
    }

    // Helper method to login and create a session
    private void login() {
        User user = new User();
        user.setUsername("b21dccn406");
        user.setPassword("manhhung09");
        ResponseEntity<String> response = userController.signIn(user);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Login should succeed");
        assertNotNull(response.getBody(), "Token should be returned");
    }

    // Testcase ID: AUTH_LOGOUT01
    // Description: Test successful logout when an active session exists
    // Input Conditions:
    //   - Login with b21dccn406/manhhung09 to create a valid session
    //   - HttpServletRequest from current Spring context
    // Expected Output:
    //   - HTTP Status: 200 OK
    //   - Response body: "Logged out successfully."
    //   - Session is invalidated
    @Test
    public void testLogOut_SuccessWithSession() {
        // Arrange: Login to create session
        login();
        HttpServletRequest request = getCurrentRequest();

        // Act
        ResponseEntity<String> response = userController.logOut(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertEquals("Logged out successfully.", response.getBody(), "Response body should indicate success");

        // Verify session is invalidated
        assertNull(request.getSession(false), "Session should be invalidated");
    }

    // Testcase ID: AUTH_LOGOUT02
    // Description: Test logout when no session exists
    // Input Conditions:
    //   - HttpServletRequest with no active session
    // Expected Output:
    //   - HTTP Status: 200 OK
    //   - Response body: "Logged out successfully."
    //   - No session invalidation attempt
    @Test
    public void testLogOut_NoSession() {
        // Arrange: Ensure no session exists
        HttpServletRequest request = getCurrentRequest();
        if (request.getSession(false) != null) {
            request.getSession().invalidate(); // Clear any existing session
        }

        // Act
        ResponseEntity<String> response = userController.logOut(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertEquals("Logged out successfully.", response.getBody(), "Response body should indicate success");
        assertNull(request.getSession(false), "No session should exist");
    }

    // Testcase ID: AUTH_LOGOUT03
    // Description: Test logout when no request context is available
    // Input Conditions:
    //   - No Spring request context (simulating invalid request scenario)
    // Expected Output:
    //   - Throws IllegalStateException due to missing request context
    @Test
    public void testLogOut_NoRequestContext() {
        // Arrange: Clear request context to simulate error
        RequestContextHolder.resetRequestAttributes();

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            HttpServletRequest request = getCurrentRequest();
            userController.logOut(request);
        }, "Should throw IllegalStateException due to missing request context");

        assertEquals("No request context available", exception.getMessage(), "Exception message should indicate missing context");
    }
}