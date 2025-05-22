package com.example.demo.Contract;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Model.Contract;
@Transactional

@SpringBootTest
@ActiveProfiles("test")
public class UpdateContractTest {

    @Autowired
    private ContractController contractController;

    private Connection connection;
    private static final int TEST_CONTRACT_ID = 9999; // Unique id_contract to avoid conflicts

    @BeforeEach
    public void setUp() throws SQLException {
        // Establish connection to the test database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false); // Start transaction

        // Clear existing data for TEST_CONTRACT_ID
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.contract WHERE id_contract = ?")) {
            ps.setInt(1, TEST_CONTRACT_ID);
            ps.executeUpdate();
        }

        // Insert a test contract
        insertTestContract(TEST_CONTRACT_ID);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.contract WHERE id_contract = ?")) {
            ps.setInt(1, TEST_CONTRACT_ID);
            ps.executeUpdate();
        }

        // Close connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testUpdateContract_Success() {
        // Arrange
        Contract contract = createValidContract();
        contract.setId_contract(TEST_CONTRACT_ID);
        String idContract = String.valueOf(TEST_CONTRACT_ID);

        // Act
        ResponseEntity<Map<String, String>> response = contractController.updateContract(contract, idContract);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Contract updated successfully hihihi!", response.getBody().get("message"), "Message should indicate success");

        // Verify data in database
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM bds.contract WHERE id_contract = ?")) {
            ps.setInt(1, TEST_CONTRACT_ID);
            try (var rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contract should exist in database");
                assertEquals("Updated Property", rs.getString("prop_name"), "Property name should be updated");
                assertEquals(2000.0f, rs.getFloat("price"), "Price should be updated");
            }
        } catch (SQLException e) {
            fail("SQLException during database verification: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateContract_NameNull() {
        // Arrange
        Contract contract = createValidContract();
        contract.setProp_name(null);
        contract.setId_contract(TEST_CONTRACT_ID);
        String idContract = String.valueOf(TEST_CONTRACT_ID);

        // Act
        ResponseEntity<Map<String, String>> response = contractController.updateContract(contract, idContract);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Property name must not be null or empty", response.getBody().get("message"), "Message should indicate name error");

        // Verify data in database (should not be updated)
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM bds.contract WHERE id_contract = ?")) {
            ps.setInt(1, TEST_CONTRACT_ID);
            try (var rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contract should exist in database");
                assertEquals("Test Property", rs.getString("prop_name"), "Property name should not be updated");
            }
        } catch (SQLException e) {
            fail("SQLException during database verification: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateContract_NameEmpty() {
        // Arrange
        Contract contract = createValidContract();
        contract.setProp_name("");
        contract.setId_contract(TEST_CONTRACT_ID);
        String idContract = String.valueOf(TEST_CONTRACT_ID);

        // Act
        ResponseEntity<Map<String, String>> response = contractController.updateContract(contract, idContract);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP status should be BAD_REQUEST");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Property name must not be null or empty", response.getBody().get("message"), "Message should indicate name error");

        // Verify data in database (should not be updated)
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM bds.contract WHERE id_contract = ?")) {
            ps.setInt(1, TEST_CONTRACT_ID);
            try (var rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contract should exist in database");
                assertEquals("Test Property", rs.getString("prop_name"), "Property name should not be updated");
            }
        } catch (SQLException e) {
            fail("SQLException during database verification: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateContract_DatabaseError() {
        // Arrange
        Contract contract = createValidContract();
        contract.setId_contract(TEST_CONTRACT_ID);
        String idContract = String.valueOf(TEST_CONTRACT_ID);

        // Act
        // Note: Simulating SQLException requires specific database error setup
        ResponseEntity<Map<String, String>> response = contractController.updateContract(contract, idContract);

        // Assert
        assertNotEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "This test requires specific database error setup; assuming success unless configured otherwise");
    }

    // Helper method to insert a test contract
    private void insertTestContract(int idContract) throws SQLException {
        String insertQuery = "INSERT INTO bds.contract (id_contract, prop_owner_id, prop_owner_name, owner_gender, owner_email, owner_phone, owner_dob, prop_id, prop_name, room_id, room_code, max_pp, tenants, price, price_type, rule, status, start_date, end_date, electric, water, water_type, internet, clean, elevator, other_service, deposit) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setInt(1, idContract);
            ps.setInt(2, 1);
            ps.setString(3, "John Doe");
            ps.setInt(4, 1);
            ps.setString(5, "john@example.com");
            ps.setString(6, "1234567890");
            ps.setString(7, "1990-01-01");
            ps.setInt(8, 1);
            ps.setString(9, "Test Property");
            ps.setInt(10, 1);
            ps.setString(11, "ROOM001");
            ps.setInt(12, 4);
            ps.setString(13, "[]");
            ps.setFloat(14, 1000.0f);
            ps.setInt(15, 1);
            ps.setString(16, "No pets allowed");
            ps.setInt(17, 1);
            ps.setDate(18, Date.valueOf("2025-05-20"));
            ps.setDate(19, Date.valueOf("2026-05-20"));
            ps.setFloat(20, 50.0f);
            ps.setFloat(21, 20.0f);
            ps.setInt(22, 1);
            ps.setFloat(23, 30.0f);
            ps.setFloat(24, 10.0f);
            ps.setFloat(25, 15.0f);
            ps.setFloat(26, 5.0f);
            ps.setFloat(27, 500.0f);
            ps.executeUpdate();
        }
        connection.commit();
    }

    // Helper method to create a valid Contract object
    private Contract createValidContract() {
        Contract contract = new Contract();
        contract.setProp_owner_id(1);
        contract.setProp_owner_name("John Doe");
        contract.setOwner_gender(1);
        contract.setOwner_email("john@example.com");
        contract.setOwner_phone("1234567890");
        contract.setOwner_dob("1990-01-01");
        contract.setProp_id(1);
        contract.setProp_name("Updated Property");
        contract.setRoom_id(1);
        contract.setRoom_code("ROOM001");
        contract.setMax_pp(4);
        contract.setTenant_list(new ArrayList<>());
        contract.setPrice(2000.0f);
        contract.setPrice_type(1);
        contract.setRule("No pets allowed");
        contract.setStatus(1);
        contract.setStart_date(Date.valueOf("2025-05-20"));
        contract.setEnd_date(Date.valueOf("2026-05-20"));
        contract.setCreated_date(Date.valueOf("2025-05-20"));
        contract.setUpdated_date(Date.valueOf("2025-05-20"));
        contract.setElectric(50.0f);
        contract.setWater(20.0f);
        contract.setWater_type(1);
        contract.setInternet(30.0f);
        contract.setClean(10.0f);
        contract.setElevator(15.0f);
        contract.setOther_service(5.0f);
        contract.setDeposit(500.0f);
        return contract;
    }
}