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
 * Test class for getContractList method in ReceiptController.
 * Assumes database has records with id_owner=33, no records with id_owner=999 or id_owner=43.
 * Tests invalid id_owner and non-existent id_owner scenarios, expecting appropriate errors.
 * Note: RC_GET02 and RC_GET03 fail because the method returns incorrect status codes
 * (200 OK or 500 INTERNAL_SERVER_ERROR) instead of 400 BAD_REQUEST, indicating incorrect error handling.
 */
@SpringBootTest
@ActiveProfiles("test")
public class GetReceiptListTest {

    @Autowired
    private ReceiptController receiptController;

    // Testcase ID: RC_GET01
    // Description: Test successful retrieval of receipt list with valid id_owner
    // Input Conditions:
    //   - id_owner = "33"
    //   - Database: At least one receipt record exists with id_owner=33, tenants contains valid JSON
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response: Non-empty List<Receipt> with correct receipt data, including parsed tenant_list
    @Test
    public void testGetReceiptList_Success() {
        // Arrange
        String idOwner = "33";

        // Act
        ResponseEntity<List<Receipt>> response;
        try {
            response = receiptController.getContractList(null, idOwner);
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
        assertEquals(33, receipt.getId_owner(), "id_owner should match");
        assertNotNull(receipt.getTenant_list(), "tenant_list should not be null");
        assertFalse(receipt.getTenant_list().isEmpty(), "tenant_list should contain at least one tenant");
        assertNotNull(receipt.getTenant_list().get(0).getUsername(), "Tenant username should not be null");
    }

    // Testcase ID: RC_GET02
    // Description: Test retrieval of receipt list with id_owner not existing in system
    // Input Conditions:
    //   - id_owner = "999"
    //   - Database: id_owner=999 does not exist in owner table and has no receipt records
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    // Note: Test fails as method returns 200 OK with empty list
    @Test
    public void testGetReceiptList_NonExistentOwner() {
        // Arrange
        String idOwner = "999";

        // Act
        ResponseEntity<List<Receipt>> response;
        try {
            response = receiptController.getContractList(null, idOwner);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }

    // Testcase ID: RC_GET03
    // Description: Test retrieval with invalid id_owner (non-numeric)
    // Input Conditions:
    //   - id_owner = "abc"
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetReceiptList_InvalidOwnerId() {
        // Arrange
        String idOwner = "abc";

        // Act
        ResponseEntity<List<Receipt>> response;
        try {
            response = receiptController.getContractList(null, idOwner);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }

    // Testcase ID: RC_GET04
    // Description: Test retrieval with id_owner existing in system but no receipt records
    // Input Conditions:
    //   - id_owner = "43"
    //   - Database: id_owner=43 exists in owner table, but no receipt records with id_owner=43
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response: Empty List<Receipt>
    @Test
    public void testGetReceiptList_ExistingOwnerNoReceipts() {
        // Arrange
        String idOwner = "43";

        // Act
        ResponseEntity<List<Receipt>> response;
        try {
            response = receiptController.getContractList(null, idOwner);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().isEmpty(), "Receipt list should be empty");
    }
}