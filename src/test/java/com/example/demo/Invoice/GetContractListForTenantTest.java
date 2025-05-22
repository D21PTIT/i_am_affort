package com.example.demo.Invoice;

import com.example.demo.Model.Invoice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for getContractListForTenant method in InvoiceController.
 * Assumes database has valid invoice records with username="ducvie" in tenants JSON and delete=0,
 * no records with username="nonexistent".
 * Tests non-existent username, empty username, invalid username, and long username scenarios, expecting appropriate errors.
 * Note: INVFT_GET02, INVFT_GET03, INVFT_GET04, and INVFT_GET05 fail because the method returns incorrect status codes
 * (200 OK or 500 INTERNAL_SERVER_ERROR) instead of 404 NOT_FOUND or 400 BAD_REQUEST, indicating missing validation.
 */
@SpringBootTest
@ActiveProfiles("test")
public class GetContractListForTenantTest {

    @Autowired
    private InvoiceController invoiceController;

    // Testcase ID: INVFT_GET01
    // Description: Test successful retrieval of invoice list with valid username, ensuring latest record is first
    // Input Conditions:
    //   - username = "ducvie"
    //   - Database: At least one invoice record exists with username="ducvie" in tenants JSON and delete=0
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response: Non-empty List<Invoice> with latest record first (highest id_invoice)
    @Test
    public void testGetContractListForTenant_Success() {
        // Arrange
        String username = "ducvie";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<List<Invoice>> response;
        try {
            response = invoiceController.getContractListForTenant(model, username);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertFalse(response.getBody().isEmpty(), "Invoice list should not be empty");

        // Verify invoice details
        Invoice latestInvoice = response.getBody().get(0);
        assertEquals(0, latestInvoice.getDelete(), "delete should be 0");
        assertNotNull(latestInvoice.getId_invoice(), "id_invoice should not be null");
        assertNotNull(latestInvoice.getTotal(), "total should not be null");
        assertFalse(latestInvoice.getTenant_list().isEmpty(), "tenants should not be empty");
        assertTrue(latestInvoice.getTenant_list().stream().anyMatch(tenant -> "ducvie".equals(tenant.getUsername())),
                "tenants should contain username='ducvie'");

        // Verify latest record (highest id_invoice)
        List<Invoice> invoiceList = response.getBody();
        for (int i = 0; i < invoiceList.size() - 1; i++) {
            assertTrue(invoiceList.get(i).getId_invoice() > invoiceList.get(i + 1).getId_invoice(),
                    "Invoices should be sorted by id_invoice DESC");
        }
    }

    // Testcase ID: INVFT_GET02
    // Description: Test retrieval of invoice list with non-existent username
    // Input Conditions:
    //   - username = "nonexistent"
    //   - Database: No invoice records exist with username="nonexistent" in tenants JSON and delete=0
    // Expected Output:
    //   - HTTP Status: 404 (NOT_FOUND)
    //   - Response: Empty List<Invoice> or message indicating no records found
    // Note: Test fails as method returns 200 OK with empty list
    @Test
    public void testGetContractListForTenant_NonExistentUsername() {
        // Arrange
        String username = "nonexistent";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<List<Invoice>> response;
        try {
            response = invoiceController.getContractListForTenant(model, username);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "HTTP status should be NOT_FOUND");
    }

    // Testcase ID: INVFT_GET03
    // Description: Test retrieval with empty username
    // Input Conditions:
    //   - username = ""
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No List<Invoice>, or message indicating invalid username
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetContractListForTenant_EmptyUsername() {
        // Arrange
        String username = "";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<List<Invoice>> response;
        try {
            response = invoiceController.getContractListForTenant(model, username);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }

    // Testcase ID: INVFT_GET04
    // Description: Test retrieval with invalid username (special characters)
    // Input Conditions:
    //   - username = "@#$%"
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No List<Invoice>, or message indicating invalid username
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetContractListForTenant_InvalidUsername() {
        // Arrange
        String username = "@#$%";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<List<Invoice>> response;
        try {
            response = invoiceController.getContractListForTenant(model, username);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }

    // Testcase ID: INVFT_GET05
    // Description: Test retrieval with excessively long username
    // Input Conditions:
    //   - username = 256-character string
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No List<Invoice>, or message indicating invalid username
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetContractListForTenant_LongUsername() {
        // Arrange
        String username = "a".repeat(256);
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<List<Invoice>> response;
        try {
            response = invoiceController.getContractListForTenant(model, username);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }
}