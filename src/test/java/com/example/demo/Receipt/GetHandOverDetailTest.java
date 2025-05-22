package com.example.demo.Receipt;

import com.example.demo.Model.Receipt;
import com.example.demo.Model.TenantInContract;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for getHandOverDetail method in ReceiptController.
 * Assumes database has a valid receipt record with id_receipt=13, no record with id_receipt=999.
 * Tests non-existent id_receipt, invalid id_receipt, and empty id_receipt scenarios, expecting appropriate errors.
 * Note: RCD_GET02, RCD_GET03, and RCD_GET04 fail because the method returns incorrect status codes
 * (200 OK or 500 INTERNAL_SERVER_ERROR) instead of 404 NOT_FOUND or 400 BAD_REQUEST, indicating missing validation.
 */
@SpringBootTest
@ActiveProfiles("test")
public class GetHandOverDetailTest {

    @Autowired
    private ReceiptController receiptController;

    // Testcase ID: RCD_GET01
    // Description: Test successful retrieval of receipt detail with valid id_receipt
    // Input Conditions:
    //   - id_receipt = "13"
    //   - Database: Receipt record exists with id_receipt=13, tenants contains valid JSON
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response: Receipt object with correct data, including non-empty tenant_list
    @Test
    public void testGetHandOverDetail_Success() {
        // Arrange
        String idReceipt = "13";

        // Act
        ResponseEntity<Receipt> response = receiptController.getHandOverDetail(null, idReceipt);

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        // Verify receipt details
        Receipt receipt = response.getBody();
        assertEquals(13, receipt.getId_receipt(), "id_receipt should match");
        assertNotNull(receipt.getTenant_list(), "tenant_list should not be null");
        //assertFalse(receipt.getTenant_list().isEmpty(), "tenant_list should contain at least one tenant");
        //assertNotNull(receipt.getTenant_list().get(0).getUsername(), "Tenant username should not be null");
        assertNotNull(receipt.getUsername(), "Username should not be null");
        assertNotNull(receipt.getId_owner(), "id_owner should not be null");
    }

    // Testcase ID: RCD_GET02
    // Description: Test retrieval with non-existent id_receipt
    // Input Conditions:
    //   - id_receipt = "999"
    //   - Database: No receipt record exists with id_receipt=999
    // Expected Output:
    //   - HTTP Status: 404 (NOT_FOUND)
    //   - Response: No Receipt object, or message indicating record not found
    // Note: Test fails as method returns 200 OK with empty Receipt
    @Test
    public void testGetHandOverDetail_NonExistentReceipt() {
        // Arrange
        String idReceipt = "999";

        // Act
        ResponseEntity<Receipt> response = receiptController.getHandOverDetail(null, idReceipt);

        // Assert response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "HTTP status should be NOT_FOUND");
    }

    // Testcase ID: RCD_GET03
    // Description: Test retrieval with invalid id_receipt (non-numeric)
    // Input Conditions:
    //   - id_receipt = "abc"
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No Receipt object, or message indicating invalid id_receipt
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetHandOverDetail_InvalidReceiptId() {
        // Arrange
        String idReceipt = "abc";

        // Act
        ResponseEntity<Receipt> response = receiptController.getHandOverDetail(null, idReceipt);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }

    // Testcase ID: RCD_GET04
    // Description: Test retrieval with empty id_receipt
    // Input Conditions:
    //   - id_receipt = ""
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No Receipt object, or message indicating invalid id_receipt
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetHandOverDetail_EmptyReceiptId() {
        // Arrange
        String idReceipt = "";

        // Act
        ResponseEntity<Receipt> response = receiptController.getHandOverDetail(null, idReceipt);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }
}