package com.example.demo.Invoice;

import com.example.demo.Contract.ContractController;
import com.example.demo.Model.Contract;
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
 * Test class for deleteContract method in ContractController.
 * Assumes database may have:
 * - A contract with id_contract=1, prop_owner_id=33, delete=0.
 * - A contract with id_contract=2, prop_owner_id=33, delete=0 or delete=1 (inserted as needed).
 * Tests successful soft delete, invalid id_contract, non-existent id_contract, and already deleted contract.
 * Includes rollback tests to restore original data after each test.
 */
@SpringBootTest
@ActiveProfiles("test")
public class DeleteContractTest {

    @Autowired
    private ContractController contractController;

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
        conn.setAutoCommit(false); // Start transaction
        return conn;
    }

    // Testcase ID: INV_DEL_01
    // Description: Test successful soft delete of a contract with valid id_contract
    // Input Conditions:
    //   - id_contract="1"
    //   - Contract object with id_contract=1
    //   - Database: Contract exists with id_contract=1, delete=0
    // Expected Output:
    //   - HTTP Status: 201 CREATED
    //   - Response body: Map with "message": "Contract deleted successfully!"
    //   - Database: Contract id_contract=1 has delete=1
    @Test
    public void testDeleteContract_Success() throws SQLException {
        // Arrange: Insert contract
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tester.contract (id_contract, prop_owner_id, `delete`) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE `delete` = 0")) {
            ps.setInt(1, 1);
            ps.setInt(2, 33);
            ps.setInt(3, 0);
            ps.executeUpdate();
            conn.commit();
        }

        Contract contract = new Contract();
        contract.setId_contract(1);
        String idContract = "1";

        // Act
        ResponseEntity<Map<String, String>> response = contractController.deleteContract(contract, idContract);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Contract deleted successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT `delete` FROM tester.contract WHERE id_contract = ?")) {
            ps.setInt(1, 1);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contract should exist");
                assertEquals(1, rs.getInt("delete"), "Contract should be soft deleted");
            }
        }
    }

    // Testcase ID: INV_DEL_01.1
    // Description: Rollback data for INV_DEL_01
    @Test
    public void testRollback_INV_DEL_01() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE tester.contract SET `delete` = 0 WHERE id_contract = ?")) {
            ps.setInt(1, 1);
            ps.executeUpdate();
            conn.commit();

            // Verify rollback
            try (PreparedStatement verifyPs = conn.prepareStatement("SELECT `delete` FROM tester.contract WHERE id_contract = ?")) {
                verifyPs.setInt(1, 1);
                try (ResultSet rs = verifyPs.executeQuery()) {
                    if (rs.next()) {
                        assertEquals(0, rs.getInt("delete"), "Contract should have delete=0");
                    }
                }
            }
        }
    }

    // Testcase ID: INV_DEL_02
    // Description: Test delete when id_contract does not exist
    // Input Conditions:
    //   - id_contract="999"
    //   - Contract object with id_contract=999
    //   - Database: No contract with id_contract=999
    // Expected Output:
    //   - HTTP Status: 201 CREATED (current controller behavior)
    //   - Response body: Map with "message": "Contract deleted successfully!"
    //   - Database: No changes
    @Test
    public void testDeleteContract_NonExistent() {
        // Arrange
        Contract contract = new Contract();
        contract.setId_contract(999);
        String idContract = "999";

        // Act
        ResponseEntity<Map<String, String>> response = contractController.deleteContract(contract, idContract);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Contract deleted successfully!", response.getBody().get("message"), "Message should indicate success");
    }

    // Testcase ID: INV_DEL_02.1
    // Description: Rollback data for INV_DEL_02 (no change expected)
    @Test
    public void testRollback_INV_DEL_02() throws SQLException {
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

    // Testcase ID: INV_DEL_03
    // Description: Test delete when id_contract from @PathVariable differs from contract.getId_contract()
    // Input Conditions:
    //   - id_contract="1"
    //   - Contract object with id_contract=2
    //   - Database: Contract exists with id_contract=2, delete=0
    // Expected Output:
    //   - HTTP Status: 201 CREATED
    //   - Response body: Map with "message": "Contract deleted successfully!"
    //   - Database: Contract id_contract=2 has delete=1, id_contract=1 unchanged
    @Test
    public void testDeleteContract_MismatchedId() throws SQLException {
        // Arrange: Insert contract
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tester.contract (id_contract, prop_owner_id, `delete`) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE `delete` = 0")) {
            ps.setInt(1, 2);
            ps.setInt(2, 33);
            ps.setInt(3, 0);
            ps.executeUpdate();
            conn.commit();
        }

        Contract contract = new Contract();
        contract.setId_contract(2);
        String idContract = "1";

        // Act
        ResponseEntity<Map<String, String>> response = contractController.deleteContract(contract, idContract);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Contract deleted successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT `delete` FROM tester.contract WHERE id_contract = ?")) {
            ps.setInt(1, 2);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contract should exist");
                assertEquals(1, rs.getInt("delete"), "Contract id_contract=2 should be soft deleted");
            }
        }
    }

    // Testcase ID: INV_DEL_03.1
    // Description: Rollback data for INV_DEL_03
    @Test
    public void testRollback_INV_DEL_03() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE tester.contract SET `delete` = 0 WHERE id_contract = ?")) {
            ps.setInt(1, 2);
            ps.executeUpdate();
            conn.commit();

            // Verify rollback
            try (PreparedStatement verifyPs = conn.prepareStatement("SELECT `delete` FROM tester.contract WHERE id_contract = ?")) {
                verifyPs.setInt(1, 2);
                try (ResultSet rs = verifyPs.executeQuery()) {
                    if (rs.next()) {
                        assertEquals(0, rs.getInt("delete"), "Contract should have delete=0");
                    }
                }
            }
        }
    }

    // Testcase ID: INV_DEL_04
    // Description: Test failure when id_contract is empty
    // Input Conditions:
    //   - id_contract=""
    //   - Contract object with id_contract=0
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST (desired behavior, current controller returns 201 CREATED)
    //   - Response body: Map with "message": "id_contract cannot be empty"
    // Note: Current controller uses contract.getId_contract(), so it returns 201 CREATED
    @Test
    public void testDeleteContract_EmptyId() {
        // Arrange
        Contract contract = new Contract();
        contract.setId_contract(0);
        String idContract = "";

        // Act
        ResponseEntity<Map<String, String>> response = contractController.deleteContract(contract, idContract);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), 
                     "HTTP status should be BAD_REQUEST for empty id_contract");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("id_contract cannot be empty", response.getBody().get("message"), 
                     "Message should indicate empty id_contract");
    }

    // Testcase ID: INV_DEL_04.1
    // Description: Rollback data for INV_DEL_04 (no change expected)
    @Test
    public void testRollback_INV_DEL_04() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.contract WHERE id_contract = ?")) {
            ps.setInt(1, 0);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No contract should exist for id_contract=0");
            }
            conn.commit();
        }
    }

    // Testcase ID: INV_DEL_05
    // Description: Test failure when id_contract is not a number
    // Input Conditions:
    //   - id_contract="abc"
    //   - Contract object with id_contract=0
    // Expected Output:
    //   - HTTP Status: 400 BAD_REQUEST (desired behavior, current controller returns 201 CREATED)
    //   - Response body: Map with "message": "Invalid id_contract format"
    // Note: Current controller uses contract.getId_contract(), so it returns 201 CREATED
    @Test
    public void testDeleteContract_InvalidId() {
        // Arrange
        Contract contract = new Contract();
        contract.setId_contract(0);
        String idContract = "abc";

        // Act
        ResponseEntity<Map<String, String>> response = contractController.deleteContract(contract, idContract);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), 
                     "HTTP status should be BAD_REQUEST for invalid id_contract");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Invalid id_contract format", response.getBody().get("message"), 
                     "Message should indicate invalid id_contract");
    }

    // Testcase ID: INV_DEL_05.1
    // Description: Rollback data for INV_DEL_05 (no change expected)
    @Test
    public void testRollback_INV_DEL_05() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tester.contract WHERE id_contract = ?")) {
            ps.setInt(1, 0);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Result set should have a count");
                assertEquals(0, rs.getInt(1), "No contract should exist for id_contract=0");
            }
            conn.commit();
        }
    }

    // Testcase ID: INV_DEL_06
    // Description: Test delete when contract is already soft deleted
    // Input Conditions:
    //   - id_contract="1"
    //   - Contract object with id_contract=1
    //   - Database: Contract exists with id_contract=1, delete=1
    // Expected Output:
    //   - HTTP Status: 201 CREATED (current controller behavior)
    //   - Response body: Map with "message": "Contract deleted successfully!"
    //   - Database: Contract id_contract=1 remains delete=1
    @Test
    public void testDeleteContract_AlreadyDeleted() throws SQLException {
        // Arrange: Insert contract with delete=1
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO tester.contract (id_contract, prop_owner_id, `delete`) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE `delete` = 1")) {
            ps.setInt(1, 1);
            ps.setInt(2, 33);
            ps.setInt(3, 1);
            ps.executeUpdate();
            conn.commit();
        }

        Contract contract = new Contract();
        contract.setId_contract(1);
        String idContract = "1";

        // Act
        ResponseEntity<Map<String, String>> response = contractController.deleteContract(contract, idContract);

        // Assert response
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Contract deleted successfully!", response.getBody().get("message"), "Message should indicate success");

        // Verify database
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT `delete` FROM tester.contract WHERE id_contract = ?")) {
            ps.setInt(1, 1);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contract should exist");
                assertEquals(1, rs.getInt("delete"), "Contract should remain soft deleted");
            }
        }
    }

    // Testcase ID: INV_DEL_06.1
    // Description: Rollback data for INV_DEL_06
    @Test
    public void testRollback_INV_DEL_06() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE tester.contract SET `delete` = 0 WHERE id_contract = ?")) {
            ps.setInt(1, 1);
            ps.executeUpdate();
            conn.commit();

            // Verify rollback
            try (PreparedStatement verifyPs = conn.prepareStatement("SELECT `delete` FROM tester.contract WHERE id_contract = ?")) {
                verifyPs.setInt(1, 1);
                try (ResultSet rs = verifyPs.executeQuery()) {
                    if (rs.next()) {
                        assertEquals(0, rs.getInt("delete"), "Contract should have delete=0");
                    }
                }
            }
        }
    }
}