package com.example.demo.Invoice;

import com.example.demo.Contract.ContractController;
import com.example.demo.Model.Contract;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for getContractDetail method in ContractController.
 * Assumes database may have:
 * - A contract with id_contract=1, prop_owner_id=33, delete=0 or delete=1.
 * Tests successful retrieval, non-existent contract, invalid id_contract, invalid tenants JSON, and deleted contract.
 * Includes rollback tests to restore original data after each test.
 */
@SpringBootTest
@ActiveProfiles("test")
public class GetContractDetailTest {

    @Autowired
    private ContractController contractController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: INV_DETAIL_01
    // Description: Test successful retrieval of contract details with valid id_contract
    // Input Conditions:
    //   - id_contract="1"
    //   - Database: Contract exists with id_contract=1, prop_owner_id=33, delete=0, tenants='[{"username":"tenant1"}]'
    // Expected Output:
    //   - HTTP Status: 200 OK
    //   - Response body: Contract object with matching fields, tenant_list containing 1 TenantInContract
    @Test
    public void testGetContractDetail_Success() throws SQLException {
        // Arrange: Insert contract
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tester.contract (id_contract, prop_owner_id, `delete`, tenants) " +
                     "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE `delete` = 0, tenants = ?")) {
            ps.setInt(1, 1);
            ps.setInt(2, 33);
            ps.setInt(3, 0);
            ps.setString(4, "[{\"username\":\"tenant1\"}]");
            ps.setString(5, "[{\"username\":\"tenant1\"}]");
            ps.executeUpdate();
            conn.commit();
        }

        String idContract = "1";

        // Act
        ResponseEntity<Contract> response = contractController.getContractDetail(null, idContract);

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        Contract contract = response.getBody();
        assertEquals(1, contract.getId_contract(), "Contract ID should be 1");
        assertEquals(33, contract.getProp_owner_id(), "Prop owner ID should be 33");
        List<TenantInContract> tenantList = contract.getTenant_list();
        assertNotNull(tenantList, "Tenant list should not be null");
        assertEquals(1, tenantList.size(), "Tenant list should contain 1 tenant");
        assertEquals("tenant1", tenantList.get(0).getUsername(), "Tenant username should be tenant1");
    }

    // Testcase ID: INV_DETAIL_01.1
    // Description: Rollback data for INV_DETAIL_01
    @Test
    public void testRollback_INV_DETAIL_01() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.contract WHERE id_contract = ?")) {
            ps.setInt(1, 1);
            ps.executeUpdate();
            conn.commit();

            // Verify rollback
            try (PreparedStatement verifyPs = conn.prepareStatement("SELECT COUNT(*) FROM tester.contract WHERE id_contract = ?")) {
                verifyPs.setInt(1, 1);
                try (ResultSet rs = verifyPs.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Contract id_contract=1 should be removed");
                }
            }
        }
    }

    // Testcase ID: INV_DETAIL_02
    // Description: Test retrieval when contract does not exist
    // Input Conditions:
    //   - id_contract="999"
    //   - Database: No contract with id_contract=999
    // Expected Output:
    //   - HTTP Status: 200 OK (current controller behavior)
    //   - Response body: Empty Contract object (id_contract=0, tenant_list=[])
    // Note: Controller should return 404 NOT_FOUND
    @Test
    public void testGetContractDetail_NonExistent() {
        // Arrange
        String idContract = "999";

        // Act
        ResponseEntity<Contract> response = contractController.getContractDetail(null, idContract);

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        Contract contract = response.getBody();
        assertEquals(0, contract.getId_contract(), "Contract ID should be 0");
        assertNotNull(contract.getTenant_list(), "Tenant list should not be null");
        assertTrue(contract.getTenant_list().isEmpty(), "Tenant list should be empty");
    }

    // Testcase ID: INV_DETAIL_02.1
    // Description: Rollback data for INV_DETAIL_02 (no change expected)
    @Test
    public void testRollback_INV_DETAIL_02() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.contract WHERE id_contract = ?")) {
            ps.setInt(1, 999);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No contract should exist for id_contract=999");
            }
            conn.commit();
        }
    }

    // Testcase ID: INV_DETAIL_03
    // Description: Test failure when id_contract is empty
    // Input Conditions:
    //   - id_contract=""
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST (desired behavior, current controller returns 500)
    //   - Response body: Empty or error message
    // Note: Controller should validate id_contract
    @Test
    public void testGetContractDetail_EmptyId() {
        // Arrange
        String idContract = "";

        // Act
        ResponseEntity<Contract> response = contractController.getContractDetail(null, idContract);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), 
                     "HTTP status should be BAD_REQUEST for empty id_contract");
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    // Testcase ID: INV_DETAIL_03.1
    // Description: Rollback data for INV_DETAIL_03 (no change expected)
    @Test
    public void testRollback_INV_DETAIL_03() {
        // No database changes expected, so no action needed
    }

    // Testcase ID: INV_DETAIL_04
    // Description: Test failure when id_contract is not a number
    // Input Conditions:
    //   - id_contract="abc"
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST (desired behavior, current controller returns 500)
    //   - Response body: Empty or error message
    // Note: Controller should validate id_contract
    @Test
    public void testGetContractDetail_InvalidId() {
        // Arrange
        String idContract = "abc";

        // Act
        ResponseEntity<Contract> response = contractController.getContractDetail(null, idContract);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), 
                     "HTTP status should be BAD_REQUEST for invalid id_contract");
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    // Testcase ID: INV_DETAIL_04.1
    // Description: Rollback data for INV_DETAIL_04 (no change expected)
    @Test
    public void testRollback_INV_DETAIL_04() {
        // No database changes expected, so no action needed
    }

    // Testcase ID: INV_DETAIL_05
    // Description: Test retrieval with invalid tenants JSON
    // Input Conditions:
    //   - id_contract="1"
    //   - Database: Contract exists with id_contract=1, prop_owner_id=33, delete=0, tenants="null"
    // Expected Output:
    //   - HTTP Status: 200 OK
    //   - Response body: Contract object with matching fields, tenant_list empty
    @Test
    public void testGetContractDetail_InvalidTenantsJson() throws SQLException {
        // Arrange: Insert contract with valid JSON that fails parsing
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tester.contract (id_contract, prop_owner_id, `delete`, tenants) " +
                     "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE `delete` = 0, tenants = ?")) {
            ps.setInt(1, 1);
            ps.setInt(2, 33);
            ps.setInt(3, 0);
            ps.setString(4, "null");
            ps.setString(5, "null");
            ps.executeUpdate();
            conn.commit();
        }

        String idContract = "1";

        // Act
        ResponseEntity<Contract> response = contractController.getContractDetail(null, idContract);

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        Contract contract = response.getBody();
        assertEquals(1, contract.getId_contract(), "Contract ID should be 1");
        assertEquals(33, contract.getProp_owner_id(), "Prop owner ID should be 33");
        assertNotNull(contract.getTenant_list(), "Tenant list should not be null");
        assertTrue(contract.getTenant_list().isEmpty(), "Tenant list should be empty due to unparsable JSON");
    }

    // Testcase ID: INV_DETAIL_05.1
    // Description: Rollback data for INV_DETAIL_05
    @Test
    public void testRollback_INV_DETAIL_05() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.contract WHERE id_contract = ?")) {
            ps.setInt(1, 1);
            ps.executeUpdate();
            conn.commit();

            // Verify rollback
            try (PreparedStatement verifyPs = conn.prepareStatement("SELECT COUNT(*) FROM tester.contract WHERE id_contract = ?")) {
                verifyPs.setInt(1, 1);
                try (ResultSet rs = verifyPs.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Contract id_contract=1 should be removed");
                }
            }
        }
    }

    // Testcase ID: INV_DETAIL_06
    // Description: Test retrieval of a soft-deleted contract
    // Input Conditions:
    //   - id_contract="1"
    //   - Database: Contract exists with id_contract=1, prop_owner_id=33, delete=1
    // Expected Output:
    //   - HTTP Status: 200 OK (current controller behavior)
    //   - Response body: Contract object with matching fields, delete=1
    // Note: Controller should return 404 NOT_FOUND for deleted contracts
    @Test
    public void testGetContractDetail_AlreadyDeleted() throws SQLException {
        // Arrange: Insert contract with delete=1
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tester.contract (id_contract, prop_owner_id, `delete`, tenants) " +
                     "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE `delete` = 1, tenants = ?")) {
            ps.setInt(1, 1);
            ps.setInt(2, 33);
            ps.setInt(3, 1);
            ps.setString(4, "[{\"username\":\"tenant1\"}]");
            ps.setString(5, "[{\"username\":\"tenant1\"}]");
            ps.executeUpdate();
            conn.commit();
        }

        String idContract = "1";

        // Act
        ResponseEntity<Contract> response = contractController.getContractDetail(null, idContract);

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        Contract contract = response.getBody();
        assertEquals(1, contract.getId_contract(), "Contract ID should be 1");
        assertEquals(33, contract.getProp_owner_id(), "Prop owner ID should be 33");
    }

    // Testcase ID: INV_DETAIL_06.1
    // Description: Rollback data for INV_DETAIL_06
    @Test
    public void testRollback_INV_DETAIL_06() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM tester.contract WHERE id_contract = ?")) {
            ps.setInt(1, 1);
            ps.executeUpdate();
            conn.commit();

            // Verify rollback
            try (PreparedStatement verifyPs = conn.prepareStatement("SELECT COUNT(*) FROM tester.contract WHERE id_contract = ?")) {
                verifyPs.setInt(1, 1);
                try (ResultSet rs = verifyPs.executeQuery()) {
                    assertTrue(rs.next(), "Result set should have a count");
                    assertEquals(0, rs.getInt(1), "Contract id_contract=1 should be removed");
                }
            }
        }
    }
}