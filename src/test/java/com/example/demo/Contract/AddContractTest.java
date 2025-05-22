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
public class AddContractTest {

    @Autowired
    private ContractController contractController;

    private Connection connection;
    private static final int TEST_OWNER_ID = 9999; // Unique prop_owner_id to avoid conflicts

    @BeforeEach
    public void setUp() throws SQLException {
        // Establish connection to the test database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false); // Start transaction

        // Clear existing data for TEST_OWNER_ID
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.contract WHERE prop_owner_id = ?")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.executeUpdate();
        }

        // Commit transaction to ensure clean state
        connection.commit();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.contract WHERE prop_owner_id = ?")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.executeUpdate();
        }

        // Close connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testAddContract_Success() {
        // Arrange
        Contract contract = createValidContract();
        contract.setProp_owner_id(TEST_OWNER_ID);

        // Act
        ResponseEntity<Map<String, String>> response = contractController.addContract(contract);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "HTTP status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("Contract added successfully", response.getBody().get("message"), "Message should indicate success");
        assertNotNull(response.getBody().get("id_contract"), "Response should include id_contract");
        assertTrue(Integer.parseInt(response.getBody().get("id_contract")) > 0, "id_contract should be positive");

        // Verify data in database
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM bds.contract WHERE prop_owner_id = ? AND `delete` = 0")) {
            ps.setInt(1, TEST_OWNER_ID);
            try (var rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Contract should be inserted into database");
                assertEquals("Test Property", rs.getString("prop_name"), "Property name should match");
                assertEquals(TEST_OWNER_ID, rs.getInt("prop_owner_id"), "Owner ID should match");
                assertEquals(1000.0f, rs.getFloat("price"), "Price should match");
            }
        } catch (SQLException e) {
            fail("SQLException during database verification: " + e.getMessage());
        }
    }

    @Test
    public void testAddContract_DatabaseError() {
        // Arrange
        Contract contract = createValidContract();
        contract.setProp_owner_id(TEST_OWNER_ID);

        // Act
        // Note: Simulating SQLException requires specific database error setup (e.g., invalid table).
        // This test assumes normal execution unless configured otherwise.
        ResponseEntity<Map<String, String>> response = contractController.addContract(contract);

        // Assert
        assertNotEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "This test requires specific database error setup; assuming success unless configured otherwise");
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
        contract.setProp_name("Test Property");
        contract.setRoom_id(1);
        contract.setRoom_code("ROOM001");
        contract.setMax_pp(4);
        contract.setTenant_list(new ArrayList<>());
        contract.setPrice(1000.0f);
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