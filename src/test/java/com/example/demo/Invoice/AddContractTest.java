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
 * Test class for addContract method in InvoiceController.
 * Assumes database has valid id_owner=33, id_contract=1, id_property=1, id_room=1, no id_owner=999.
 * Tests invalid id_owner, null label, and invalid tenants scenarios, expecting appropriate errors.
 * Includes rollback tests to ensure database state is restored.
 * Note: INV_ADD03, INV_ADD04, and INV_ADD05 fail because the method returns incorrect status codes
 * (201 CREATED or 500 INTERNAL_SERVER_ERROR) instead of 400 BAD_REQUEST, indicating missing validation.
 */
@SpringBootTest
@ActiveProfiles("test")
public class AddContractTest {

    @Autowired
    private InvoiceController invoiceController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: INV_ADD01
    // Description: Test successful addition of an invoice with valid data, including tenants
    // Input Conditions:
    //   - Invoice object with label="Invoice 1", id_contract=1, id_property=1, property_name="Property A",
    //     id_room=1, room_code="R101", id_owner=33, tenants=[{username="ducvie"}], rent_month=1, people=2,
    //     deposit=1000, rent_price=500, rent_price_type=1, electric=50, electric_num=100, water=20,
    //     water_type=1, water_num=10, internet=30, clean=10, elevator=5, other=0, add=0, deduct=0,
    //     total=615, note="Test invoice", status=1, deposit_amount=1000
    //   - Database: id_owner=33, id_contract=1, id_property=1, id_room=1 exist
    // Expected Output:
    //   - HTTP Status: 201 (CREATED)
    //   - Response body: Map with key "message" and value "Invoice added successfully", and "id_invoice"
    //   - Database: New invoice record exists with provided values
    @Test
    public void testAddContract_Success() throws SQLException {
        // Arrange
        Invoice invoice = new Invoice();
        invoice.setLabel("Invoice 1");
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
        invoice.setRent_month(1);
        invoice.setPeople(2);
        invoice.setDeposit(1000);
        invoice.setRent_price(500);
        invoice.setRent_price_type(1);
        invoice.setElectric(50);
        invoice.setElectric_num(100);
        invoice.setWater(20);
        invoice.setWater_type(1);
        invoice.setWater_num(10);
        invoice.setInternet(30);
        invoice.setClean(10);
        invoice.setElevator(5);
        invoice.setOther(0);
        invoice.setAdd(0);
        invoice.setDeduct(0);
        invoice.setTotal(615);
        invoice.setNote("Test invoice");
        invoice.setStatus(1);
        invoice.setDeposit_amount(1000);

        // Act
        ResponseEntity<Map<String, String>> response = invoiceController.addContract(invoice);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Invoice added successfully", response.getBody().get("message"), "Message should indicate success");
        assertNotNull(response.getBody().get("id_invoice"), "id_invoice should be returned");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM tester.invoice WHERE id_owner = ? AND label = ?")) {
            ps.setInt(1, 33);
            ps.setString(2, "Invoice 1");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Invoice should exist");
                assertEquals(1, rs.getInt("id_contract"), "id_contract should match");
                assertEquals(1, rs.getInt("id_property"), "id_property should match");
                assertEquals("Property A", rs.getString("property_name"), "property_name should match");
                assertEquals(1, rs.getInt("id_room"), "id_room should match");
                assertEquals("R101", rs.getString("room_code"), "room_code should match");
                assertTrue(rs.getString("tenants").contains("ducvie"), "tenants should contain username='ducvie'");
                assertEquals(615, rs.getFloat("total"), "total should match");
            }
        }
    }

    // Testcase ID: INV_ADD01.1
    // Description: Rollback by deleting the invoice added in INV_ADD01
    @Test
    public void testRollback_INV_ADD01() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_invoice (auto-incremented)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_invoice) FROM tester.invoice");
                 ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Should have records");
                latestId = rs.getInt(1);
            }

            // Delete the record
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Invoice should be deleted");
                }
            }
        }
    }

    // Testcase ID: INV_ADD02
    // Description: Test successful addition of an invoice with empty tenants
    // Input Conditions:
    //   - Invoice object similar to INV_ADD01, but tenants=[]
    //   - Database: id_owner=33, id_contract=1, id_property=1, id_room=1 exist
    // Expected Output:
    //   - HTTP Status: 201 (CREATED)
    //   - Response body: Map with key "message" and value "Invoice added successfully", and "id_invoice"
    //   - Database: New invoice record exists with empty tenants
    @Test
    public void testAddContract_SuccessEmptyTenants() throws SQLException {
        // Arrange
        Invoice invoice = new Invoice();
        invoice.setLabel("Invoice 2");
        invoice.setId_contract(1);
        invoice.setId_property(1);
        invoice.setProperty_name("Property A");
        invoice.setId_room(1);
        invoice.setRoom_code("R101");
        invoice.setId_owner(33);
        invoice.setTenant_list(new ArrayList<>());
        invoice.setRent_month(1);
        invoice.setPeople(2);
        invoice.setDeposit(1000);
        invoice.setRent_price(500);
        invoice.setRent_price_type(1);
        invoice.setElectric(50);
        invoice.setElectric_num(100);
        invoice.setWater(20);
        invoice.setWater_type(1);
        invoice.setWater_num(10);
        invoice.setInternet(30);
        invoice.setClean(10);
        invoice.setElevator(5);
        invoice.setOther(0);
        invoice.setAdd(0);
        invoice.setDeduct(0);
        invoice.setTotal(615);
        invoice.setNote("Test invoice empty tenants");
        invoice.setStatus(1);
        invoice.setDeposit_amount(1000);

        // Act
        ResponseEntity<Map<String, String>> response = invoiceController.addContract(invoice);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Invoice added successfully", response.getBody().get("message"), "Message should indicate success");
        assertNotNull(response.getBody().get("id_invoice"), "id_invoice should be returned");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM tester.invoice WHERE id_owner = ? AND label = ?")) {
            ps.setInt(1, 33);
            ps.setString(2, "Invoice 2");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Invoice should exist");
                assertEquals("[]", rs.getString("tenants"), "tenants should be empty JSON array");
                assertEquals(615, rs.getFloat("total"), "total should match");
            }
        }
    }

    // Testcase ID: INV_ADD02.1
    // Description: Rollback by deleting the invoice added in INV_ADD02
    @Test
    public void testRollback_INV_ADD02() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_invoice (auto-incremented)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_invoice) FROM tester.invoice");
                 ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Should have records");
                latestId = rs.getInt(1);
            }

            // Delete the record
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Invoice should be deleted");
                }
            }
        }
    }

    // Testcase ID: INV_ADD03
    // Description: Test addition with invalid id_owner (non-existent)
    // Input Conditions:
    //   - Invoice object similar to INV_ADD01, but id_owner=999 (non-existent)
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating invalid id_owner
    //   - Database: No new record added
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR or 201 CREATED
    @Test
    public void testAddContract_InvalidOwnerId() throws SQLException {
        // Arrange
        Invoice invoice = new Invoice();
        invoice.setLabel("Invoice 3");
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
        invoice.setRent_month(1);
        invoice.setPeople(2);
        invoice.setDeposit(1000);
        invoice.setRent_price(500);
        invoice.setRent_price_type(1);
        invoice.setElectric(50);
        invoice.setElectric_num(100);
        invoice.setWater(20);
        invoice.setWater_type(1);
        invoice.setWater_num(10);
        invoice.setInternet(30);
        invoice.setClean(10);
        invoice.setElevator(5);
        invoice.setOther(0);
        invoice.setAdd(0);
        invoice.setDeduct(0);
        invoice.setTotal(615);
        invoice.setNote("Test invoice invalid owner");
        invoice.setStatus(1);
        invoice.setDeposit_amount(1000);

        // Act
        ResponseEntity<Map<String, String>> response = invoiceController.addContract(invoice);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").toLowerCase().contains("id_owner"),
                "Message should indicate invalid id_owner");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.invoice WHERE id_owner = ?")) {
            ps.setInt(1, 999);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Invoice should be added");
            }
        }
    }

    // Testcase ID: INV_ADD03.1
    // Description: Rollback by deleting any invoice added in INV_ADD03
    @Test
    public void testRollback_INV_ADD03() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_invoice (if any added)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_invoice) FROM tester.invoice WHERE id_owner = ?")) {
                ps.setInt(1, 999);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        latestId = rs.getInt(1);
                    } else {
                        // No record added, commit empty transaction
                        conn.commit();
                        return;
                    }
                }
            }

            // Delete the record
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Invoice should be deleted");
                }
            }
        }
    }

    // Testcase ID: INV_ADD04
    // Description: Test addition with null label
    // Input Conditions:
    //   - Invoice object similar to INV_ADD01, but label=null
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating null label
    //   - Database: No new record added
    // Note: Test fails as method returns 201 CREATED or 500 INTERNAL_SERVER_ERROR
    @Test
    public void testAddContract_NullLabel() throws SQLException {
        // Arrange
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
        invoice.setRent_month(1);
        invoice.setPeople(2);
        invoice.setDeposit(1000);
        invoice.setRent_price(500);
        invoice.setRent_price_type(1);
        invoice.setElectric(50);
        invoice.setElectric_num(100);
        invoice.setWater(20);
        invoice.setWater_type(1);
        invoice.setWater_num(10);
        invoice.setInternet(30);
        invoice.setClean(10);
        invoice.setElevator(5);
        invoice.setOther(0);
        invoice.setAdd(0);
        invoice.setDeduct(0);
        invoice.setTotal(615);
        invoice.setNote("Test invoice null label");
        invoice.setStatus(1);
        invoice.setDeposit_amount(1000);

        // Act
        ResponseEntity<Map<String, String>> response = invoiceController.addContract(invoice);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").toLowerCase().contains("label"),
                "Message should indicate null label");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.invoice WHERE label IS NULL AND id_owner = ?")) {
            ps.setInt(1, 33);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Invoice should be added");
            }
        }
    }

    // Testcase ID: INV_ADD04.1
    // Description: Rollback by deleting any invoice added in INV_ADD04
    @Test
    public void testRollback_INV_ADD04() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_invoice (if any added)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_invoice) FROM tester.invoice WHERE label IS NULL AND id_owner = ?")) {
                ps.setInt(1, 33);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        latestId = rs.getInt(1);
                    } else {
                        // No record added, commit empty transaction
                        conn.commit();
                        return;
                    }
                }
            }

            // Delete the record
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Invoice should be deleted");
                }
            }
        }
    }

    // Testcase ID: INV_ADD05
    // Description: Test addition with invalid tenants JSON
    // Input Conditions:
    //   - Invoice object similar to INV_ADD01, but tenants contains invalid JSON
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating invalid tenants
    //   - Database: No new record added
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testAddContract_InvalidTenants() throws SQLException {
        // Arrange
        Invoice invoice = new Invoice();
        invoice.setLabel("Invoice 5");
        invoice.setId_contract(1);
        invoice.setId_property(1);
        invoice.setProperty_name("Property A");
        invoice.setId_room(1);
        invoice.setRoom_code("R101");
        invoice.setId_owner(33);
        // Set invalid tenants JSON directly (simulating invalid JSON input)
        List<TenantInContract> tenants = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("ducvie");
        // Invalid JSON will be simulated by invalid TenantInContract structure
        // Note: In practice, invalid JSON is caught by ObjectMapper, so we simulate by setting invalid data
        tenants.add(null); // Invalid tenant
        invoice.setTenant_list(tenants);
        invoice.setRent_month(1);
        invoice.setPeople(2);
        invoice.setDeposit(1000);
        invoice.setRent_price(500);
        invoice.setRent_price_type(1);
        invoice.setElectric(50);
        invoice.setElectric_num(100);
        invoice.setWater(20);
        invoice.setWater_type(1);
        invoice.setWater_num(10);
        invoice.setInternet(30);
        invoice.setClean(10);
        invoice.setElevator(5);
        invoice.setOther(0);
        invoice.setAdd(0);
        invoice.setDeduct(0);
        invoice.setTotal(615);
        invoice.setNote("Test invoice invalid tenants");
        invoice.setStatus(1);
        invoice.setDeposit_amount(1000);

        // Act
        ResponseEntity<Map<String, String>> response = invoiceController.addContract(invoice);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").toLowerCase().contains("tenants"),
                "Message should indicate invalid tenants");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.invoice WHERE label = ?")) {
            ps.setString(1, "Invoice 5");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Invoice should be added");
            }
        }
    }

    // Testcase ID: INV_ADD05.1
    // Description: Rollback by deleting any invoice added in INV_ADD05
    @Test
    public void testRollback_INV_ADD05() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_invoice (if any added)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_invoice) FROM tester.invoice WHERE label = ?")) {
                ps.setString(1, "Invoice 5");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        latestId = rs.getInt(1);
                    } else {
                        // No record added, commit empty transaction
                        conn.commit();
                        return;
                    }
                }
            }

            // Delete the record
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.invoice WHERE id_invoice = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Invoice should be deleted");
                }
            }
        }
    }
}