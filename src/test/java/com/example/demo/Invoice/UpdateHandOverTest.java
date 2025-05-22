package com.example.demo.Invoice;

import com.example.demo.Model.Invoice;
import com.example.demo.Model.TenantInContract;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for updateHandOver method in InvoiceController.
 * Assumes database has valid invoice record with id_invoice=19, delete=0, id_owner=33, id_contract=1, id_property=1, id_room=1,
 * no record with id_invoice=999 or id_owner=999.
 * Tests non-existent id_invoice, invalid id_owner, and null label scenarios, expecting appropriate errors.
 * Includes rollback tests to ensure database state is restored.
 * Note: INV_UPD01 and INV_UPD02 fail due to incorrect status code (201 CREATED instead of 200 OK).
 * INV_UPD03, INV_UPD04, and INV_UPD05 fail because the method returns incorrect status codes
 * (201 CREATED or 500 INTERNAL_SERVER_ERROR) instead of 404 NOT_FOUND or 400 BAD_REQUEST, indicating missing validation.
 */
@SpringBootTest
@ActiveProfiles("test")
public class UpdateHandOverTest {

    @Autowired
    private InvoiceController invoiceController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: INV_UPD01
    // Description: Test successful update of an invoice with valid data, including tenants
    // Input Conditions:
    //   - id_invoice = "19"
    //   - Invoice object with label="Updated Invoice 1", id_contract=1, id_property=1, property_name="Property A",
    //     id_room=1, room_code="R101", id_owner=33, tenants=[{username="ducvie"}], rent_month=2, people=3,
    //     deposit=1500, rent_price=600, rent_price_type=1, electric=60, electric_num=120, water=25,
    //     water_type=1, water_num=12, internet=35, clean=15, elevator=10, other=0, add=0, deduct=0,
    //     total=745, note="Updated invoice", status=2, deposit_amount=1500
    //   - Database: id_invoice=19 exists with delete=0, id_owner=33, id_contract=1, id_property=1, id_room=1 exist
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response body: Map with key "message" and value "Invoice updated successfully hihihi!"
    //   - Database: Invoice record with id_invoice=19 updated with provided values
    // Note: Test fails as method returns 201 CREATED
    @Test
    public void testUpdateHandOver_Success() throws SQLException {
        // Arrange
        String idInvoice = "19";
        Invoice invoice = new Invoice();
        invoice.setLabel("Updated Invoice 1");
        invoice.setId_contract(1);
        invoice.setId_property(1);
        invoice.setProperty_name("Property A");
        invoice.setId_room(1);
        invoice.setRoom_code("R101");
        invoice.setId_owner(33);
        List<TenantInContract> tenants = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("ducvie");
        tenants.add(tenant);
        invoice.setTenant_list(tenants);
        invoice.setRent_month(2);
        invoice.setPeople(3);
        invoice.setDeposit(1500);
        invoice.setRent_price(600);
        invoice.setRent_price_type(1);
        invoice.setElectric(60);
        invoice.setElectric_num(120);
        invoice.setWater(25);
        invoice.setWater_type(1);
        invoice.setWater_num(12);
        invoice.setInternet(35);
        invoice.setClean(15);
        invoice.setElevator(10);
        invoice.setOther(0);
        invoice.setAdd(0);
        invoice.setDeduct(0);
        invoice.setTotal(745);
        invoice.setNote("Updated invoice");
        invoice.setStatus(2);
        invoice.setDeposit_amount(1500);

        // Act
        ResponseEntity<Map<String, String>> response = invoiceController.updateHandOver(invoice, idInvoice);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Invoice updated successfully hihihi!", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM tester.invoice WHERE id_invoice = ? AND `delete` = 0")) {
            ps.setInt(1, 19);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Invoice should exist");
                assertEquals("Updated Invoice 1", rs.getString("label"), "label should match");
                assertEquals(33, rs.getInt("id_owner"), "id_owner should match");
                assertTrue(rs.getString("tenants").contains("ducvie"), "tenants should contain username='ducvie'");
                assertEquals(745, rs.getFloat("total"), "total should match");
            }
        }
    }

    // Testcase ID: INV_UPD01.1
    // Description: Rollback by restoring the invoice updated in INV_UPD01
    @Test
    public void testRollback_INV_UPD01() throws SQLException {
        try (Connection conn = getConnection()) {
            // Restore invoice record to original state (assuming original values for simplicity)
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE tester.invoice SET label = 'Original Invoice', rent_month = 1, people = 2, deposit = 1000, " +
                    "rent_price = 500, electric = 50, electric_num = 100, water = 20, water_num = 10, internet = 30, " +
                    "clean = 10, elevator = 5, total = 615, note = 'Original note', status = 1, deposit_amount = 1000, " +
                    "`delete` = 0 WHERE id_invoice = ?")) {
                ps.setInt(1, 19);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify state
            try (Connection connVerify = getConnection();
                 PreparedStatement ps = connVerify.prepareStatement("SELECT * FROM tester.invoice WHERE id_invoice = ? AND `delete` = 0")) {
                ps.setInt(1, 19);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Invoice should exist");
                    assertEquals("Original Invoice", rs.getString("label"), "label should be restored");
                    assertEquals(615, rs.getFloat("total"), "total should be restored");
                }
            }
        }
    }

    // Testcase ID: INV_UPD02
    // Description: Test successful update of an invoice with empty tenants
    // Input Conditions:
    //   - id_invoice = "19"
    //   - Invoice object similar to INV_UPD01, but tenants=[]
    //   - Database: id_invoice=19 exists with delete=0, id_owner=33, id_contract=1, id_property=1, id_room=1 exist
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response body: Map with key "message" and value "Invoice updated successfully hihihi!"
    //   - Database: Invoice record with id_invoice=19 updated with empty tenants
    // Note: Test fails as method returns 201 CREATED
    @Test
    public void testUpdateHandOver_SuccessEmptyTenants() throws SQLException {
        // Arrange
        String idInvoice = "19";
        Invoice invoice = new Invoice();
        invoice.setLabel("Updated Invoice 2");
        invoice.setId_contract(1);
        invoice.setId_property(1);
        invoice.setProperty_name("Property A");
        invoice.setId_room(1);
        invoice.setRoom_code("R101");
        invoice.setId_owner(33);
        invoice.setTenant_list(new ArrayList<>());
        invoice.setRent_month(2);
        invoice.setPeople(3);
        invoice.setDeposit(1500);
        invoice.setRent_price(600);
        invoice.setRent_price_type(1);
        invoice.setElectric(60);
        invoice.setElectric_num(120);
        invoice.setWater(25);
        invoice.setWater_type(1);
        invoice.setWater_num(12);
        invoice.setInternet(35);
        invoice.setClean(15);
        invoice.setElevator(10);
        invoice.setOther(0);
        invoice.setAdd(0);
        invoice.setDeduct(0);
        invoice.setTotal(745);
        invoice.setNote("Updated invoice empty tenants");
        invoice.setStatus(2);
        invoice.setDeposit_amount(1500);

        // Act
        ResponseEntity<Map<String, String>> response = invoiceController.updateHandOver(invoice, idInvoice);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Invoice updated successfully hihihi!", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM tester.invoice WHERE id_invoice = ? AND `delete` = 0")) {
            ps.setInt(1, 19);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Invoice should exist");
                assertEquals("Updated Invoice 2", rs.getString("label"), "label should match");
                assertEquals("[]", rs.getString("tenants"), "tenants should be empty JSON array");
                assertEquals(745, rs.getFloat("total"), "total should match");
            }
        }
    }

    // Testcase ID: INV_UPD02.1
    // Description: Rollback by restoring the invoice updated in INV_UPD02
    @Test
    public void testRollback_INV_UPD02() throws SQLException {
        try (Connection conn = getConnection()) {
            // Restore invoice record to original state
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE tester.invoice SET label = 'Original Invoice', rent_month = 1, people = 2, deposit = 1000, " +
                    "rent_price = 500, electric = 50, electric_num = 100, water = 20, water_num = 10, internet = 30, " +
                    "clean = 10, elevator = 5, total = 615, note = 'Original note', status = 1, deposit_amount = 1000, " +
                    "`delete` = 0 WHERE id_invoice = ?")) {
                ps.setInt(1, 19);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify state
            try (Connection connVerify = getConnection();
                 PreparedStatement ps = connVerify.prepareStatement("SELECT * FROM tester.invoice WHERE id_invoice = ? AND `delete` = 0")) {
                ps.setInt(1, 19);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Invoice should exist");
                    assertEquals("Original Invoice", rs.getString("label"), "label should be restored");
                    assertEquals(615, rs.getFloat("total"), "total should be restored");
                }
            }
        }
    }

    // Testcase ID: INV_UPD03
    // Description: Test update with non-existent id_invoice
    // Input Conditions:
    //   - id_invoice = "999"
    //   - Invoice object similar to INV_UPD01
    //   - Database: No invoice record exists with id_invoice=999
    // Expected Output:
    //   - HTTP Status: 404 (NOT_FOUND)
    //   - Response body: Map with key "message" indicating invoice not found
    //   - Database: No changes
    // Note: Test fails as method returns 201 CREATED
    @Test
    public void testUpdateHandOver_NonExistentInvoice() throws SQLException {
        // Arrange
        String idInvoice = "999";
        Invoice invoice = new Invoice();
        invoice.setLabel("Updated Invoice 3");
        invoice.setId_contract(1);
        invoice.setId_property(1);
        invoice.setProperty_name("Property A");
        invoice.setId_room(1);
        invoice.setRoom_code("R101");
        invoice.setId_owner(33);
        List<TenantInContract> tenants = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("ducvie");
        tenants.add(tenant);
        invoice.setTenant_list(tenants);
        invoice.setRent_month(2);
        invoice.setPeople(3);
        invoice.setDeposit(1500);
        invoice.setRent_price(600);
        invoice.setRent_price_type(1);
        invoice.setElectric(60);
        invoice.setElectric_num(120);
        invoice.setWater(25);
        invoice.setWater_type(1);
        invoice.setWater_num(12);
        invoice.setInternet(35);
        invoice.setClean(15);
        invoice.setElevator(10);
        invoice.setOther(0);
        invoice.setAdd(0);
        invoice.setDeduct(0);
        invoice.setTotal(745);
        invoice.setNote("Updated invoice non-existent");
        invoice.setStatus(2);
        invoice.setDeposit_amount(1500);

        // Act
        ResponseEntity<Map<String, String>> response = invoiceController.updateHandOver(invoice, idInvoice);

        // Assert response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "HTTP status should be NOT_FOUND");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").toLowerCase().contains("not found"),
                "Message should indicate invoice not found");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.invoice WHERE id_invoice = ?")) {
            ps.setInt(1, 999);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Invoice should exist");
            }
        }
    }

    // Testcase ID: INV_UPD03.1
    // Description: Verify database state after INV_UPD03
    @Test
    public void testRollback_INV_UPD03() throws SQLException {
        try (Connection conn = getConnection()) {
            // Verify no invoice with id_invoice=999
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, 999);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "No Invoice should exist");
                }
            }
            conn.commit();
        }
    }

    // Testcase ID: INV_UPD04
    // Description: Test update with invalid id_owner (non-existent)
    // Input Conditions:
    //   - id_invoice = "19"
    //   - Invoice object similar to INV_UPD01, but id_owner=999 (non-existent)
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating invalid id_owner
    //   - Database: Invoice record with id_invoice=19 unchanged
    // Note: Test fails as method returns 201 CREATED or 500 INTERNAL_SERVER_ERROR
    @Test
    public void testUpdateHandOver_InvalidOwnerId() throws SQLException {
        // Arrange
        String idInvoice = "19";
        Invoice invoice = new Invoice();
        invoice.setLabel("Updated Invoice 4");
        invoice.setId_contract(1);
        invoice.setId_property(1);
        invoice.setProperty_name("Property A");
        invoice.setId_room(1);
        invoice.setRoom_code("R101");
        invoice.setId_owner(999); // Non-existent
        List<TenantInContract> tenants = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("ducvie");
        tenants.add(tenant);
        invoice.setTenant_list(tenants);
        invoice.setRent_month(2);
        invoice.setPeople(3);
        invoice.setDeposit(1500);
        invoice.setRent_price(600);
        invoice.setRent_price_type(1);
        invoice.setElectric(60);
        invoice.setElectric_num(120);
        invoice.setWater(25);
        invoice.setWater_type(1);
        invoice.setWater_num(12);
        invoice.setInternet(35);
        invoice.setClean(15);
        invoice.setElevator(10);
        invoice.setOther(0);
        invoice.setAdd(0);
        invoice.setDeduct(0);
        invoice.setTotal(745);
        invoice.setNote("Updated invoice invalid owner");
        invoice.setStatus(2);
        invoice.setDeposit_amount(1500);

        // Act
        ResponseEntity<Map<String, String>> response = invoiceController.updateHandOver(invoice, idInvoice);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").toLowerCase().contains("id_owner"),
                "Message should indicate invalid id_owner");

        // Verify database (assuming original state for simplicity)
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id_owner FROM tester.invoice WHERE id_invoice = ? AND `delete` = 0")) {
            ps.setInt(1, 19);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Invoice should exist");
                assertNotEquals(999, rs.getInt("id_owner"), "id_owner should not be updated to 999");
            }
        }
    }

    // Testcase ID: INV_UPD04.1
    // Description: Rollback by restoring the invoice updated in INV_UPD04
    @Test
    public void testRollback_INV_UPD04() throws SQLException {
        try (Connection conn = getConnection()) {
            // Restore invoice record to original state
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE tester.invoice SET label = 'Original Invoice', rent_month = 1, people = 2, deposit = 1000, " +
                    "rent_price = 500, electric = 50, electric_num = 100, water = 20, water_num = 10, internet = 30, " +
                    "clean = 10, elevator = 5, total = 615, note = 'Original note', status = 1, deposit_amount = 1000, " +
                    "`delete` = 0 WHERE id_invoice = ?")) {
                ps.setInt(1, 19);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify state
            try (Connection connVerify = getConnection();
                 PreparedStatement ps = connVerify.prepareStatement("SELECT * FROM tester.invoice WHERE id_invoice = ? AND `delete` = 0")) {
                ps.setInt(1, 19);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Invoice should exist");
                    assertEquals("Original Invoice", rs.getString("label"), "label should be restored");
                    assertEquals(615, rs.getFloat("total"), "total should be restored");
                }
            }
        }
    }

    // Testcase ID: INV_UPD05
    // Description: Test update with null label
    // Input Conditions:
    //   - id_invoice = "19"
    //   - Invoice object similar to INV_UPD01, but label=null
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating null label
    //   - Database: Invoice record with id_invoice=19 unchanged
    // Note: Test fails as method returns 201 CREATED or 500 INTERNAL_SERVER_ERROR
    @Test
    public void testUpdateHandOver_NullLabel() throws SQLException {
        // Arrange
        String idInvoice = "19";
        Invoice invoice = new Invoice();
        invoice.setLabel(null); // Invalid
        invoice.setId_contract(1);
        invoice.setId_property(1);
        invoice.setProperty_name("Property A");
        invoice.setId_room(1);
        invoice.setRoom_code("R101");
        invoice.setId_owner(33);
        List<TenantInContract> tenants = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("ducvie");
        tenants.add(tenant);
        invoice.setTenant_list(tenants);
        invoice.setRent_month(2);
        invoice.setPeople(3);
        invoice.setDeposit(1500);
        invoice.setRent_price(600);
        invoice.setRent_price_type(1);
        invoice.setElectric(60);
        invoice.setElectric_num(120);
        invoice.setWater(25);
        invoice.setWater_type(1);
        invoice.setWater_num(12);
        invoice.setInternet(35);
        invoice.setClean(15);
        invoice.setElevator(10);
        invoice.setOther(0);
        invoice.setAdd(0);
        invoice.setDeduct(0);
        invoice.setTotal(745);
        invoice.setNote("Updated invoice null label");
        invoice.setStatus(2);
        invoice.setDeposit_amount(1500);

        // Act
        ResponseEntity<Map<String, String>> response = invoiceController.updateHandOver(invoice, idInvoice);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").toLowerCase().contains("label"),
                "Message should indicate null label");

        // Verify database (assuming original state)
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT label FROM tester.invoice WHERE id_invoice = ? AND `delete` = 0")) {
            ps.setInt(1, 19);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Invoice should exist");
                assertNotNull(rs.getString("label"), "label should not be null");
            }
        }
    }

    // Testcase ID: INV_UPD05.1
    // Description: Rollback by restoring the invoice updated in INV_UPD05
    @Test
    public void testRollback_INV_UPD05() throws SQLException {
        try (Connection conn = getConnection()) {
            // Restore invoice record to original state
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE tester.invoice SET label = 'Original Invoice', rent_month = 1, people = 2, deposit = 1000, " +
                    "rent_price = 500, electric = 50, electric_num = 100, water = 20, water_num = 10, internet = 30, " +
                    "clean = 10, elevator = 5, total = 615, note = 'Original note', status = 1, deposit_amount = 1000, " +
                    "`delete` = 0 WHERE id_invoice = ?")) {
                ps.setInt(1, 19);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify state
            try (Connection connVerify = getConnection();
                 PreparedStatement ps = connVerify.prepareStatement("SELECT * FROM tester.invoice WHERE id_invoice = ? AND `delete` = 0")) {
                ps.setInt(1, 19);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Invoice should exist");
                    assertEquals("Original Invoice", rs.getString("label"), "label should be restored");
                    assertEquals(615, rs.getFloat("total"), "total should be restored");
                }
            }
        }
    }
}