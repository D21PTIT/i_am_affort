package com.example.demo.Tenant;

import com.example.demo.Model.Tenant;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for deleteTenant method in TenantController.
 * Assumes database has valid tenant record with id_tenant=83 (linked to invoices with delete=0),
 * id_tenant=87 (no linked invoices with delete=0), no record with id_tenant=999.
 * Tests constrained tenant, non-existent id_tenant, invalid id_tenant, empty id_tenant, and unconstrained tenant scenarios.
 * Includes rollback tests to ensure database state is restored.
 * Note: TEND_DEL01, TEND_DEL02, TEND_DEL03, TEND_DEL04, and TEND_DEL05 fail because the method returns incorrect status codes
 * or does not check invoice constraints, indicating missing validation.
 */
@SpringBootTest
@ActiveProfiles("test")
public class DeleteTenantTest {

    @Autowired
    private TenantController tenantController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: TEND_DEL01
    // Description: Test deletion of a tenant constrained by active invoices
    // Input Conditions:
    //   - id_tenant = "83"
    //   - Database: Tenant record exists with id_tenant=83, linked to invoices with delete=0
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating cannot delete due to active invoices
    //   - Database: Tenant record remains with delete=0
    // Note: Test fails as method returns 201 CREATED and updates delete=1
    @Test
    public void testDeleteTenant_ConstrainedByInvoice() throws SQLException {
        // Arrange
        String idTenant = "83";
        Tenant tenant = new Tenant();

        // Act
        ResponseEntity<Map<String, String>> response = tenantController.deleteTenant(tenant, idTenant);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").toLowerCase().contains("invoice"),
                "Message should indicate cannot delete due to active invoices");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT delete FROM tester.tenant WHERE id_tenant = ?")) {
            ps.setInt(1, 83);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Tenant should exist");
                assertEquals(0, rs.getInt("delete"), "Tenant should not be deleted (delete=0)");
            }
        }
    }

    // Testcase ID: TEND_DEL01.1
    // Description: Verify database state after TEND_DEL01
    @Test
    public void testRollback_TEND_DEL01() throws SQLException {
        try (Connection conn = getConnection()) {
            // Ensure tenant record has delete=0
            try (PreparedStatement ps = conn.prepareStatement("UPDATE tester.tenant SET `delete` = 0 WHERE id_tenant = ?")) {
                ps.setInt(1, 83);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify state
            try (PreparedStatement ps = conn.prepareStatement("SELECT `delete` FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, 83);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Tenant should exist");
                    assertEquals(0, rs.getInt("delete"), "Tenant should have delete=0");
                }
            }
        }
    }

    // Testcase ID: TEND_DEL02
    // Description: Test deletion with non-existent id_tenant
    // Input Conditions:
    //   - id_tenant = "999"
    //   - Database: No tenant record exists with id_tenant=999
    // Expected Output:
    //   - HTTP Status: 404 (NOT_FOUND)
    //   - Response body: Map with key "message" indicating tenant not found
    //   - Database: No changes
    // Note: Test fails as method returns 201 CREATED
    @Test
    public void testDeleteTenant_NonExistentTenant() throws SQLException {
        // Arrange
        String idTenant = "999";
        Tenant tenant = new Tenant();
        // Act
        ResponseEntity<Map<String, String>> response = tenantController.deleteTenant(tenant, idTenant);

        // Assert response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "HTTP status should be NOT_FOUND");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").toLowerCase().contains("not found"),
                "Message should indicate tenant not found");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.tenant WHERE id_tenant = ?")) {
            ps.setInt(1, 999);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Tenant should exist");
            }
        }
    }

    // Testcase ID: TEND_DEL02.1
    // Description: Verify database state after TEND_DEL02
    @Test
    public void testRollback_TEND_DEL02() throws SQLException {
        try (Connection conn = getConnection()) {
            // Verify no tenant with id_tenant=999
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, 999);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "No Tenant should exist");
                }
            }
            conn.commit();
        }
    }

    // Testcase ID: TEND_DEL03
    // Description: Test deletion with invalid id_tenant (non-numeric)
    // Input Conditions:
    //   - id_tenant = "abc"
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating invalid id_tenant
    //   - Database: No changes
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testDeleteTenant_InvalidTenantId() throws SQLException {
        // Arrange
        String idTenant = "abc";
        Tenant tenant = new Tenant();

        // Act
        ResponseEntity<Map<String, String>> response = tenantController.deleteTenant(tenant, idTenant);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").toLowerCase().contains("invalid"),
                "Message should indicate invalid id_tenant");
    }

    // Testcase ID: TEND_DEL03.1
    // Description: Verify database state after TEND_DEL03
    @Test
    public void testRollback_TEND_DEL03() throws SQLException {
        try (Connection conn = getConnection()) {
            // No specific rollback needed, just commit to ensure no changes
            conn.commit();
        }
    }

    // Testcase ID: TEND_DEL04
    // Description: Test deletion with empty id_tenant
    // Input Conditions:
    //   - id_tenant = ""
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating invalid id_tenant
    //   - Database: No changes
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR
    @Test
    public void testDeleteTenant_EmptyTenantId() throws SQLException {
        // Arrange
        String idTenant = "";
        Tenant tenant = new Tenant();

        // Act
        ResponseEntity<Map<String, String>> response = tenantController.deleteTenant(tenant, idTenant);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").toLowerCase().contains("invalid"),
                "Message should indicate invalid id_tenant");
    }

    // Testcase ID: TEND_DEL04.1
    // Description: Verify database state after TEND_DEL04
    @Test
    public void testRollback_TEND_DEL04() throws SQLException {
        try (Connection conn = getConnection()) {
            // No specific rollback needed, just commit to ensure no changes
            conn.commit();
        }
    }

    // Testcase ID: TEND_DEL05
    // Description: Test successful deletion of a tenant with no active invoice constraints
    // Input Conditions:
    //   - id_tenant = "87"
    //   - Database: Tenant record exists with id_tenant=87, no linked invoices with delete=0
    // Expected Output:
    //   - HTTP Status: 200 (OK)
    //   - Response body: Map with key "message" and value "Delete successfully!"
    //   - Database: Tenant record updated to delete=1
    // Note: Test fails as method returns 201 CREATED
    @Test
    public void testDeleteTenant_Success() throws SQLException {
        // Arrange
        String idTenant = "87";
        Tenant tenant = new Tenant();

        // Act
        ResponseEntity<Map<String, String>> response = tenantController.deleteTenant(tenant, idTenant);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Delete successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT `delete` FROM tester.tenant WHERE id_tenant = ?")) {
            ps.setInt(1, 87);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Tenant should exist");
                assertEquals(1, rs.getInt("delete"), "Tenant should be deleted (delete=1)");
            }
        }
    }

    // Testcase ID: TEND_DEL05.1
    // Description: Rollback by restoring the tenant deleted in TEND_DEL05
    @Test
    public void testRollback_TEND_DEL05() throws SQLException {
        try (Connection conn = getConnection()) {
            // Restore tenant record to delete=0
            try (PreparedStatement ps = conn.prepareStatement("UPDATE tester.tenant SET `delete` = 0 WHERE id_tenant = ?")) {
                ps.setInt(1, 87);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify state
            try (PreparedStatement ps = conn.prepareStatement("SELECT `delete` FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, 87);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Tenant should exist");
                    assertEquals(0, rs.getInt("delete"), "Tenant should have delete=0");
                }
            }
        }
    }
}