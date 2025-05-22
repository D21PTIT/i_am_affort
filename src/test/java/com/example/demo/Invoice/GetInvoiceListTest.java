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
 * Test class for getInvoiceList method in InvoiceController.
 * Assumes database has valid invoice records with id_owner=33 and delete=0, no records with id_owner=999.
 * Tests non-existent id_owner, invalid id_owner, and empty id_owner scenarios, expecting appropriate errors.
 * Note: INV_GET02, INV_GET03, and INV_GET04 fail because the method returns incorrect status codes
 * (200 OK or 500 INTERNAL_SERVER_ERROR) instead of 404 NOT_FOUND or 400 BAD_REQUEST, indicating missing validation.
 */
@SpringBootTest
@ActiveProfiles("test")
public class GetInvoiceListTest {

    @Autowired
    private InvoiceController invoiceController;

    // Testcase ID: INV_GET01
    // Description: Test successful retrieval of invoice list with valid id_owner, ensuring latest record is first
    // Input Conditions:
    //   - id_owner = "33"
    //   - Database: At least one invoice record exists with id_owner=33 and delete=0
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response: Non-empty List<Invoice> with latest record first (highest id_invoice)
    @Test
    public void testGetInvoiceList_Success() {
        // Arrange
        String idOwner = "33";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<List<Invoice>> response;
        try {
            response = invoiceController.getInvoiceList(model, idOwner);
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
        assertEquals(33, latestInvoice.getId_owner(), "id_owner should match");
        assertEquals(0, latestInvoice.getDelete(), "delete should be 0");
        assertNotNull(latestInvoice.getId_invoice(), "id_invoice should not be null");
        assertNotNull(latestInvoice.getTotal(), "total should not be null");

        // Verify latest record (highest id_invoice)
        List<Invoice> invoiceList = response.getBody();
        for (int i = 0; i < invoiceList.size() - 1; i++) {
            assertTrue(invoiceList.get(i).getId_invoice() > invoiceList.get(i + 1).getId_invoice(),
                    "Invoices should be sorted by id_invoice DESC");
        }
    }

    // Testcase ID: INV_GET02
    // Description: Test retrieval of invoice list with non-existent id_owner
    // Input Conditions:
    //   - id_owner = "999"
    //   - Database: No invoice records exist with id_owner=999 and delete=0
    // Expected Output:
    //   - HTTP Status: 404 (NOT_FOUND)
    //   - Response: Empty List<Invoice> or message indicating no records found
    // Note: Test fails as method returns 200 OK with empty list
    @Test
    public void testGetInvoiceList_NonExistentOwner() {
        // Arrange
        String idOwner = "999";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<List<Invoice>> response;
        try {
            response = invoiceController.getInvoiceList(model, idOwner);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "HTTP status should be NOT_FOUND");
    }

    // Testcase ID: INV_GET03
    // Description: Test retrieval with invalid id_owner (non-numeric)
    // Input Conditions:
    //   - id_owner = "abc"
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No List<Invoice>, or message indicating invalid id_owner
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetInvoiceList_InvalidOwnerId() {
        // Arrange
        String idOwner = "abc";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<List<Invoice>> response;
        try {
            response = invoiceController.getInvoiceList(model, idOwner);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }

    // Testcase ID: INV_GET04
    // Description: Test retrieval with empty id_owner
    // Input Conditions:
    //   - id_owner = ""
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response: No List<Invoice>, or message indicating invalid id_owner
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testGetInvoiceList_EmptyOwnerId() {
        // Arrange
        String idOwner = "";
        Model model = new ExtendedModelMap();

        // Act
        ResponseEntity<List<Invoice>> response;
        try {
            response = invoiceController.getInvoiceList(model, idOwner);
        } catch (IOException e) {
            fail("IOException should not be thrown: " + e.getMessage());
            return;
        }

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
    }
}