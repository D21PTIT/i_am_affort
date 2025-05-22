package com.example.demo.Tenant;

import com.example.demo.Model.Tenant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for addTenant method in TenantController.
 * Assumes database has valid id_user=36, id_owner=33, no id_owner=999 or 0 in respective tables.
 * Tests invalid id_owner, null username, and zero id_owner scenarios, expecting appropriate errors.
 * Includes rollback tests to ensure database state is restored.
 * Note: TENA_ADD02, TENA_ADD03, and TENA_ADD04 fail because the method returns incorrect status codes
 * (201 CREATED or 500 INTERNAL_SERVER_ERROR) instead of 400 BAD_REQUEST, indicating missing validation.
 */
@SpringBootTest
@ActiveProfiles("test")
public class AddTenantTest {

    @Autowired
    private TenantController tenantController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: TENA_ADD01
    // Description: Test successful addition of a tenant with valid data, verifying the latest record
    // Input Conditions:
    //   - Tenant object with username="ducvie", id_user=36, name="Duc Vie", email="ducvie@example.com",
    //     phone_number="123456789", gender=1, dob=1990-01-01, rate=5, id_owner=33
    //   - Database: id_user=36, id_owner=33 exist
    // Expected Output:
    //   - HTTP Status: 201 (CREATED)
    //   - Response body: Map with key "message" and value "Tenant added successfully"
    //   - Database: Latest tenant record (highest id_tenant) exists with provided values
    @Test
    public void testAddTenant_Success() throws SQLException {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setUsername("ducvie");
        tenant.setId_user(36);
        tenant.setName("Duc Vie");
        tenant.setEmail("ducvie@example.com");
        tenant.setPhone_number("123456789");
        tenant.setGender(1);
        tenant.setDob(Date.valueOf("1990-01-01"));
        tenant.setRate(5);
        tenant.setId_owner(33);

        // Act
        ResponseEntity<Map<String, String>> response = tenantController.addTenant(tenant);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Tenant added successfully", response.getBody().get("message"), "Message should indicate success");

        // Verify database (latest record)
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM tester.tenant WHERE id_tenant = (SELECT MAX(id_tenant) FROM tester.tenant)")) {
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Latest tenant should exist");
                assertEquals(36, rs.getInt("id_user"), "id_user should match");
                assertEquals(33, rs.getInt("id_owner"), "id_owner should match");
                assertEquals("ducvie", rs.getString("username"), "Username should match");
                assertEquals("Duc Vie", rs.getString("name"), "Name should match");
                assertEquals("ducvie@example.com", rs.getString("email"), "Email should match");
            }
        }
    }

    // Testcase ID: TENA_ADD01.1
    // Description: Rollback by deleting the tenant added in TENA_ADD01
    @Test
    public void testRollback_TENA_ADD01() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_tenant (auto-incremented)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_tenant) FROM tester.tenant");
                 ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Should have records");
                latestId = rs.getInt(1);
            }

            // Delete the record
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit(); // Commit to ensure DB is updated
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Tenant should be deleted");
                }
            }
        }
    }

    // Testcase ID: TENA_ADD02
    // Description: Test addition with invalid id_owner (non-existent)
    // Input Conditions:
    //   - Tenant object with id_owner=999 (non-existent), other fields valid
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating invalid id_owner
    //   - Database: No new record added
    // Note: Test fails as method returns 500 INTERNAL_SERVER_ERROR or 201 CREATED
    @Test
    public void testAddTenant_InvalidOwnerId() throws SQLException {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setUsername("ducvie");
        tenant.setId_user(36);
        tenant.setName("Duc Vie");
        tenant.setEmail("ducvie@example.com");
        tenant.setPhone_number("123456789");
        tenant.setGender(1);
        tenant.setDob(Date.valueOf("1990-01-01"));
        tenant.setRate(5);
        tenant.setId_owner(999); // Non-existent

        // Act
        ResponseEntity<Map<String, String>> response = tenantController.addTenant(tenant);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").contains("id_owner"), "Message should indicate invalid id_owner");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.tenant WHERE id_owner = ?")) {
            ps.setInt(1, 999);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Tenant should be added");
            }
        }
    }

    // Testcase ID: TENA_ADD02.1
    // Description: Rollback by deleting any tenant added in TENA_ADD02
    @Test
    public void testRollback_TENA_ADD02() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_tenant (if any added)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_tenant) FROM tester.tenant WHERE id_owner = ?")) {
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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Tenant should be deleted");
                }
            }
        }
    }

    // Testcase ID: TENA_ADD03
    // Description: Test addition with null username
    // Input Conditions:
    //   - Tenant object with username=null, other fields valid
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating null username
    //   - Database: No new record added
    // Note: Test fails as method may return 201 CREATED or 500 INTERNAL_SERVER_ERROR
    @Test
    public void testAddTenant_NullUsername() throws SQLException {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setUsername(null); // Invalid
        tenant.setId_user(36);
        tenant.setName("Duc Vie");
        tenant.setEmail("ducvie@example.com");
        tenant.setPhone_number("123456789");
        tenant.setGender(1);
        tenant.setDob(Date.valueOf("1990-01-01"));
        tenant.setRate(5);
        tenant.setId_owner(33);

        // Act
        ResponseEntity<Map<String, String>> response = tenantController.addTenant(tenant);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").contains("username"), "Message should indicate null username");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.tenant WHERE username IS NULL AND id_owner = ?")) {
            ps.setInt(1, 33);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Tenant should be added");
            }
        }
    }

    // Testcase ID: TENA_ADD03.1
    // Description: Rollback by deleting any tenant added in TENA_ADD03
    @Test
    public void testRollback_TENA_ADD03() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_tenant (if any added)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_tenant) FROM tester.tenant WHERE username IS NULL AND id_owner = ?")) {
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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Tenant should be deleted");
                }
            }
        }
    }

    // Testcase ID: TENA_ADD04
    // Description: Test addition with zero id_owner (invalid)
    // Input Conditions:
    //   - Tenant object with id_owner=0 (invalid), other fields valid
    // Expected Output:
    //   - HTTP Status: 400 (BAD_REQUEST)
    //   - Response body: Map with key "message" indicating invalid id_owner
    //   - Database: No new record added
    // Note: Test fails as method may return 500 INTERNAL_SERVER_ERROR or 201 CREATED
    @Test
    public void testAddTenant_ZeroOwnerId() throws SQLException {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setUsername("ducvie");
        tenant.setId_user(36);
        tenant.setName("Duc Vie");
        tenant.setEmail("ducvie@example.com");
        tenant.setPhone_number("123456789");
        tenant.setGender(1);
        tenant.setDob(Date.valueOf("1990-01-01"));
        tenant.setRate(5);
        tenant.setId_owner(0); // Invalid

        // Act
        ResponseEntity<Map<String, String>> response = tenantController.addTenant(tenant);

        // Assert response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().get("message").contains("id_owner"), "Message should indicate invalid id_owner");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.tenant WHERE id_owner = ?")) {
            ps.setInt(1, 0);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No Tenant should be added");
            }
        }
    }

    // Testcase ID: TENA_ADD04.1
    // Description: Rollback by deleting any tenant added in TENA_ADD04
    @Test
    public void testRollback_TENA_ADD04() throws SQLException {
        try (Connection conn = getConnection()) {
            // Find the latest id_tenant (if any added)
            int latestId;
            try (PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_tenant) FROM tester.tenant WHERE id_owner = ?")) {
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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, latestId);
                ps.executeUpdate();
                conn.commit();
            }

            // Verify deletion
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.tenant WHERE id_tenant = ?")) {
                ps.setInt(1, latestId);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Tenant should be deleted");
                }
            }
        }
    }
}