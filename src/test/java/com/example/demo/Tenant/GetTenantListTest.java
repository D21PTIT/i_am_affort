package com.example.demo.Tenant;

import com.example.demo.Model.Tenant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for getTenantList method in TenantController.
 * Assumes database has valid tenant records with id_owner=33 and delete=0, no records with id_owner=999.
 * Tests non-existent id_owner, invalid id_owner, and empty id_owner scenarios, expecting appropriate errors.
 * Note: TEN_GET02, TEN_GET03, and TEN_GET04 fail because the method returns incorrect status codes
 * (200 OK or 500 INTERNAL_SERVER_ERROR) instead of 404 NOT_FOUND or 400 BAD_REQUEST, indicating missing validation.
 */
@SpringBootTest
@ActiveProfiles("test")
public class GetTenantListTest {

    @Autowired
    private TenantController tenantController;

    // Testcase ID: TEN_GET01
    // Description: Test successful retrieval of tenant list with valid id_owner
    // Input Conditions:
    //   - id_owner = "33"
    //   - Database: At least one tenant record exists with id_owner=33 and delete=0
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response: Non-empty List<Tenant> with correct tenant data
    @Test
    public void testGetTenantList_Success() {
        // Arrange
        String idOwner = "33";

        // Act
        ResponseEntity<List<Tenant>> response;
        try {
            response = tenantController.getTenantList(null, idOwner);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody().isEmpty(), "Tenant list should not be empty");

        // Verify tenant details
        Tenant tenant = response.getBody().get(0);
        assertEquals(33, tenant.getId_owner(), "id_owner should match");
        assertNotNull(tenant.getUsername(), "Username should not be null");
        assertNotNull(tenant.getName(), "Name should not be null");
    }

    // Testcase ID: TEN_GET02
    // Description: Test retrieval of tenant list with non-existent id_owner
    // Input Conditions:
    //   - id_owner = "999"
    //   - Database: No tenant records exist with id_owner=999 and delete=0
    // Expected Output:
    //   - HTTP Status: 404 (NOT_FOUND)
    //   - Response: Empty List<Tenant> or message indicating no records found
    // Note: Test fails as method returns 200 OK with empty list
    @Test
    public void testGetTenantList_NonExistentOwner() {
        // Arrange
        String idOwner = "999";

        // Act
        ResponseEntity<List<Tenant>> response;
        try {
            response = tenantController.getTenantList(null, idOwner);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "HTTP status should be NOT_FOUND");
    }

    // Testcase ID: TEN_GET03
    // Description: Test retrieval with invalid id_owner (non-numeric)
    // Input Conditions:
    //   - id_owner = "abc"
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No List<Tenant>, or message indicating invalid id_owner
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetTenantList_InvalidOwnerId() {
        // Arrange
        String idOwner = "abc";

        // Act
        ResponseEntity<List<Tenant>> response;
        try {
            response = tenantController.getTenantList(null, idOwner);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }

    // Testcase ID: TEN_GET04
    // Description: Test retrieval with empty id_owner
    // Input Conditions:
    //   - id_owner = ""
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No List<Tenant>, or message indicating invalid id_owner
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetTenantList_EmptyOwnerId() {
        // Arrange
        String idOwner = "";

        // Act
        ResponseEntity<List<Tenant>> response;
        try {
            response = tenantController.getTenantList(null, idOwner);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }
}