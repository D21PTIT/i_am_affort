package com.example.demo.Receipt;

import com.example.demo.Model.Receipt;
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
 * Test class for addContract method in ReceiptController.
 * Assumes database has valid id_invoice=15, id_owner=33, username="ducvie" in respective tables.
 * Tests invalid id_owner, null username, zero id_owner, and invalid username scenarios, expecting appropriate errors.
 * Includes rollback tests to ensure database state is restored.
 * Note: RC_ADD02, RC_ADD03, RC_ADD04, and RC_ADD05 fail because the method returns incorrect status codes
 * (201 CREATED or 500 INTERNAL_SERVER_ERROR) instead of 400 BAD_REQUEST, indicating missing validation.
 */
@SpringBootTest
@ActiveProfiles("test")
public class AddReceiptTest {

    @Autowired
    private ReceiptController receiptController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: RC_ADD01
    // Description: Test successful addition of a receipt with valid data
    // Input Conditions:
    //   - Receipt object with id_invoice=15, label="Test Receipt", id_owner=33, tenant_list=[TenantInContract(username="ducvie")],
    //     image="test.jpg", note="Test note", username="ducvie", name="Duc Vie", phone_number="123456789", email="ducvie@example.com"
    //   - Database: id_invoice=15, id_owner=33, username="ducvie" exist
    // Expected Output:
    //   - HTTP Status: 201 (CREATED)
    //   - Response body: Map with key "message" and value "Receipt added successfully"
    //   - Database: New receipt record exists with provided values
    @Test
    public void testAddReceipt_Success() throws SQLException {
        // Arrange
        Receipt receipt = new Receipt();
        receipt.setId_invoice(15);
        receipt.setLabel("Test Receipt");
        receipt.setId_owner(33);
        List<TenantInContract> tenantList = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("ducvie");
        tenantList.add(tenant);
        receipt.setTenant_list(tenantList);
        receipt.setImage("test.jpg");
        receipt.setNote("Test note");
        receipt.setUsername("ducvie");
        receipt.setName("Duc Vie");
        receipt.setPhone_number("123456789");
        receipt.setEmail("ducvie@example.com");

        // Act
        ResponseEntity<Map<String, String>> response = receiptController.addContract(receipt);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Receipt added successfully", response.getBody().get("message"), "Message should indicate success");

        // Get the latest id_receipt to verify the newly added record
        int latestId;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_receipt) FROM tester.receipt")) {
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Should have records");
                latestId = rs.getInt(1);
            }
        }

        // Verify database for the new record
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM tester.receipt WHERE id_receipt = ?")) {
            ps.setInt(1, latestId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Newly added receipt should exist");
                assertEquals(15, rs.getInt("id_invoice"), "id_invoice should match");
                assertEquals("Test Receipt", rs.getString("label"), "Label should match");
                assertEquals(33, rs.getInt("id_owner"), "id_owner should match");
                assertTrue(rs.getString("tenants").contains("ducvie"), "tenants should contain ducvie");
                assertEquals("test.jpg", rs.getString("image"), "Image should match");
                assertEquals("Test note", rs.getString("note"), "Note should match");
                assertEquals("ducvie", rs.getString("username"), "Username should match");
                assertEquals("Duc Vie", rs.getString("name"), "Name should match");
                assertEquals("123456789", rs.getString("phone_number"), "Phone number should match");
                assertEquals("ducvie@example.com", rs.getString("email"), "Email should match");
            }
        }
    }

    // Testcase ID: RC_ADD01.1
    // Description: Rollback by deleting the receipt added in RC_ADD01
    @Test
    public void testRollback_RC_ADD01() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_receipt (auto-incremented)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_receipt) FROM tester.receipt");
                 ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Should have records");
                latestId = rs.getInt(1);
            }

            // Delete the record
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.receipt WHERE id_receipt = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit(); // Commit to ensure DB is updated
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.receipt WHERE id_receipt = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Receipt should be deleted");
                }
            }
        }
    }

    // Testcase ID: RC_ADD02
    // Description: Test addition with invalid id_owner (non-existent)
    // Input Conditions:
    //   - Receipt object with id_owner=999 (non-existent), other fields valid
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating invalid id_owner
    //   - Database: No new record added
    // Note: Test fails as method returns 201 CREATED
    @Test
    public void testAddReceipt_InvalidOwnerId() throws SQLException {
        // Arrange
        Receipt receipt = new Receipt();
        receipt.setId_invoice(15);
        receipt.setLabel("Test Receipt");
        receipt.setId_owner(999); // Non-existent
        List<TenantInContract> tenantList = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("ducvie");
        tenantList.add(tenant);
        receipt.setTenant_list(tenantList);
        receipt.setImage("test.jpg");
        receipt.setNote("Test note");
        receipt.setUsername("ducvie");
        receipt.setName("Duc Vie");
        receipt.setPhone_number("123456789");
        receipt.setEmail("ducvie@example.com");

        // Act
        ResponseEntity<Map<String, String>> response = receiptController.addContract(receipt);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").contains("id_owner"), "Message should indicate invalid id_owner");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.receipt WHERE id_owner = ?")) {
            ps.setInt(1, 999);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Receipt should be added");
            }
        }
    }

    // Testcase ID: RC_ADD02.1
    // Description: Rollback by deleting any receipt added in RC_ADD02
    @Test
    public void testRollback_RC_ADD02() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_receipt (if any added)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_receipt) FROM tester.receipt WHERE id_owner = ?")) {
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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.receipt WHERE id_receipt = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.receipt WHERE id_receipt = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Receipt should be deleted");
                }
            }
        }
    }

    // Testcase ID: RC_ADD03
    // Description: Test addition with null username
    // Input Conditions:
    //   - Receipt object with username=null, other fields valid
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating null username
    //   - Database: No new record added
    // Note: Test fails as method returns 201 CREATED
    @Test
    public void testAddReceipt_NullUsername() throws SQLException {
        // Arrange
        Receipt receipt = new Receipt();
        receipt.setId_invoice(15);
        receipt.setLabel("Test Receipt");
        receipt.setId_owner(33);
        List<TenantInContract> tenantList = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("ducvie");
        tenantList.add(tenant);
        receipt.setTenant_list(tenantList);
        receipt.setImage("test.jpg");
        receipt.setNote("Test note");
        receipt.setUsername(null); // Invalid
        receipt.setName("Duc Vie");
        receipt.setPhone_number("123456789");
        receipt.setEmail("ducvie@example.com");

        // Act
        ResponseEntity<Map<String, String>> response = receiptController.addContract(receipt);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").contains("username"), "Message should indicate null username");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.receipt WHERE username IS NULL AND id_owner = ?")) {
            ps.setInt(1, 33);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Receipt should be added");
            }
        }
    }

    // Testcase ID: RC_ADD03.1
    // Description: Rollback by deleting any receipt added in RC_ADD03
    @Test
    public void testRollback_RC_ADD03() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_receipt (if any added)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_receipt) FROM tester.receipt WHERE username IS NULL AND id_owner = ?")) {
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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.receipt WHERE id_receipt = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.receipt WHERE id_receipt = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Receipt should be deleted");
                }
            }
        }
    }

    // Testcase ID: RC_ADD04
    // Description: Test addition with invalid zero id_owner
    // Input Conditions:
    //   - Receipt object with id_owner=0 (invalid), other fields valid
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating invalid id_owner
    //   - Database: No new record added
    // Note: Test fails as method returns 201 CREATED
    @Test
    public void testAddReceipt_InvalidZeroOwnerId() throws SQLException {
        // Arrange
        Receipt receipt = new Receipt();
        receipt.setId_invoice(15);
        receipt.setLabel("Test Receipt");
        receipt.setId_owner(0); // Invalid
        List<TenantInContract> tenantList = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("ducvie");
        tenantList.add(tenant);
        receipt.setTenant_list(tenantList);
        receipt.setImage("test.jpg");
        receipt.setNote("Test note");
        receipt.setUsername("ducvie");
        receipt.setName("Duc Vie");
        receipt.setPhone_number("123456789");
        receipt.setEmail("ducvie@example.com");

        // Act
        ResponseEntity<Map<String, String>> response = receiptController.addContract(receipt);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").contains("id_owner"), "Message should indicate invalid id_owner");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.receipt WHERE id_owner = ?")) {
            ps.setInt(1, 0);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Receipt should be added");
            }
        }
    }

    // Testcase ID: RC_ADD04.1
    // Description: Rollback by deleting any receipt added in RC_ADD04
    @Test
    public void testRollback_RC_ADD04() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_receipt (if any added)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_receipt) FROM tester.receipt WHERE id_owner = ?")) {
                ps.setInt(1, 0);
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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.receipt WHERE id_receipt = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.receipt WHERE id_receipt = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Receipt should be deleted");
                }
            }
        }
    }

    // Testcase ID: RC_ADD05
    // Description: Test addition with invalid username (non-existent)
    // Input Conditions:
    //   - Receipt object with username="invalid_user" (non-existent), other fields valid
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating invalid username
    //   - Database: No new record added
    // Note: Test fails as method returns 201 CREATED
    @Test
    public void testAddReceipt_InvalidUsername() throws SQLException {
        // Arrange
        Receipt receipt = new Receipt();
        receipt.setId_invoice(15);
        receipt.setLabel("Test Receipt");
        receipt.setId_owner(33);
        List<TenantInContract> tenantList = new ArrayList<>();
        TenantInContract tenant = new TenantInContract();
        tenant.setUsername("ducvie");
        tenantList.add(tenant);
        receipt.setTenant_list(tenantList);
        receipt.setImage("test.jpg");
        receipt.setNote("Test note");
        receipt.setUsername("invalid_user"); // Invalid
        receipt.setName("Duc Vie");
        receipt.setPhone_number("123456789");
        receipt.setEmail("ducvie@example.com");

        // Act
        ResponseEntity<Map<String, String>> response = receiptController.addContract(receipt);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").contains("username"), "Message should indicate invalid username");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.receipt WHERE username = ?")) {
            ps.setString(1, "invalid_user");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Receipt should be added");
            }
        }
    }

    // Testcase ID: RC_ADD05.1
    // Description: Rollback by deleting any receipt added in RC_ADD05
    @Test
    public void testRollback_RC_ADD05() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_receipt (if any added)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_receipt) FROM tester.receipt WHERE username = ?")) {
                ps.setString(1, "invalid_user");
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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.receipt WHERE id_receipt = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.receipt WHERE id_receipt = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Receipt should be deleted");
                }
            }
        }
    }
}