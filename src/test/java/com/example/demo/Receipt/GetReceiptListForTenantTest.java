package com.example.demo.Receipt;

import com.example.demo.Model.Receipt;
import com.example.demo.Model.TenantInContract;
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
 * Test class for getContractListForTenant method in ReceiptController.
 * Assumes database has receipt records with tenants containing username="ducvie",
 * no records with tenants containing username="kientran1" or "invalid".
 * Tests non-existent and empty username scenarios, expecting appropriate errors.
 * Note: RCT_GET03 and RCT_GET04 fail because the method returns incorrect status codes
 * (200 OK or 500 INTERNAL_SERVER_ERROR) instead of 400 BAD_REQUEST, indicating incorrect error handling.
 */
@SpringBootTest
@ActiveProfiles("test")
public class GetReceiptListForTenantTest {

    @Autowired
    private ReceiptController receiptController;

    // Testcase ID: RCT_GET01
    // Description: Test successful retrieval of receipt list with valid username
    // Input Conditions:
    //   - username = "ducvie"
    //   - Database: At least one receipt record exists with tenants containing username="ducvie"
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response: Non-empty List<Receipt> with correct receipt data, including parsed tenant_list
    @Test
    public void testGetReceiptListForTenant_Success() {
        // Arrange
        String username = "ducvie";

        // Act
        ResponseEntity<List<Receipt>> response;
        try {
            response = receiptController.getContractListForTenant(null, username);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody().isEmpty(), "Receipt list should not be empty");

        // Verify first receipt
        Receipt receipt = response.getBody().get(0);
        assertNotNull(receipt.getTenant_list(), "tenant_list should not be null");
        assertFalse(receipt.getTenant_list().isEmpty(), "tenant_list should contain at least one tenant");
        boolean found = false;
        for (TenantInContract tenant : receipt.getTenant_list()) {
            if ("ducvie".equals(tenant.getUsername())) {
                found = true;
                break;
            }
        }
        assertTrue(found, "tenant_list should contain username=ducvie");
    }

    // Testcase ID: RCT_GET02
    // Description: Test retrieval of receipt list with existing username but no receipt records
    // Input Conditions:
    //   - username = "kientran1"
    //   - Database: username="kientran1" exists in system, but no receipt records with tenants containing kientran1
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response: Empty List<Receipt>
    @Test
    public void testGetReceiptListForTenant_ExistingTenantNoReceipts() {
        // Arrange
        String username = "kientran1";

        // Act
        ResponseEntity<List<Receipt>> response;
        try {
            response = receiptController.getContractListForTenant(null, username);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Receipt list should be empty");
    }

    // Testcase ID: RCT_GET03
    // Description: Test retrieval with non-existent username
    // Input Conditions:
    //   - username = "invalid"
    //   - Database: username="invalid" does not exist in system
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    // Note: Test fails as method returns 200 OK with empty list
    @Test
    public void testGetReceiptListForTenant_NonExistentTenant() {
        // Arrange
        String username = "invalid";

        // Act
        ResponseEntity<List<Receipt>> response;
        try {
            response = receiptController.getContractListForTenant(null, username);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }

    // Testcase ID: RCT_GET04
    // Description: Test retrieval with empty username
    // Input Conditions:
    //   - username = ""
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    // Note: Test fails as method may return 200 OK or 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetReceiptListForTenant_EmptyUsername() {
        // Arrange
        String username = "";

        // Act
        ResponseEntity<List<Receipt>> response;
        try {
            response = receiptController.getContractListForTenant(null, username);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }
}