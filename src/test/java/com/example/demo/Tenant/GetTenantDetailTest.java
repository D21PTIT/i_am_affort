package com.example.demo.Tenant;

import com.example.demo.Model.Tenant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for getTenantDetail method in TenantController.
 * Assumes database has a valid tenant record with id_tenant=83, no record with id_tenant=999.
 * Tests non-existent id_tenant, invalid id_tenant, and empty id_tenant scenarios, expecting appropriate errors.
 * Note: TENDET_GET02, TENDET_GET03, and TENDET_GET04 fail because the method returns incorrect status codes
 * (200 OK or 500 INTERNAL_SERVER_ERROR) instead of 400 BAD_REQUEST, indicating missing validation.
 */
@SpringBootTest
@ActiveProfiles("test")
public class GetTenantDetailTest {

    @Autowired
    private TenantController tenantController;

    // Testcase ID: TENDET_GET01
    // Description: Test successful retrieval of tenant detail with valid id_tenant
    // Input Conditions:
    //   - id_tenant = "83"
    //   - Database: Tenant record exists with id_tenant=83
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response: Tenant object with correct data
    @Test
    public void testGetTenantDetail_Success() {
        // Arrange
        String idTenant = "83";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<Tenant> response = tenantController.getTenantDetail(model, idTenant);

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        // Verify tenant details
        Tenant tenant = response.getBody();
        assertEquals(83, tenant.getId_tenant(), "id_tenant should match");
        assertNotNull(tenant.getUsername(), "Username should not be null");
        assertNotNull(tenant.getId_owner(), "id_owner should not be null");
        assertNotNull(tenant.getName(), "Name should not be null");
    }

    // Testcase ID: TENDET_GET02
    // Description: Test retrieval with non-existent id_tenant
    // Input Conditions:
    //   - id_tenant = "999"
    //   - Database: No tenant record exists with id_tenant=999
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No Tenant object, or message indicating invalid id_tenant
    // Note: Test fails as method returns 200 OK with empty Tenant
    @Test
    public void testGetTenantDetail_NonExistentTenant() {
        // Arrange
        String idTenant = "999";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<Tenant> response = tenantController.getTenantDetail(model, idTenant);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }

    // Testcase ID: TENDET_GET03
    // Description: Test retrieval with invalid id_tenant (non-numeric)
    // Input Conditions:
    //   - id_tenant = "abc"
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No Tenant object, or message indicating invalid id_tenant
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetTenantDetail_InvalidTenantId() {
        // Arrange
        String idTenant = "abc";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<Tenant> response = tenantController.getTenantDetail(model, idTenant);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }

    // Testcase ID: TENDET_GET04
    // Description: Test retrieval with empty id_tenant
    // Input Conditions:
    //   - id_tenant = ""
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No Tenant object, or message indicating invalid id_tenant
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetTenantDetail_EmptyTenantId() {
        // Arrange
        String idTenant = "";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<Tenant> response = tenantController.getTenantDetail(model, idTenant);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }
}