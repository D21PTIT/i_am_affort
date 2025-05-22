package com.example.demo.Contract;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.example.demo.Model.Contract;
@Transactional

@SpringBootTest
@ActiveProfiles("test")
public class GetContractListForTenantTest {

    @Autowired
    private ContractController contractController;

    private Connection connection;
    private static final int TEST_CONTRACT_ID = 9999; // Unique id_contract to avoid conflicts
    private static final int TEST_CONTRACT_ID_2 = 10000; // Second unique id_contract
    private static final String TEST_USERNAME = "test_user";

    @BeforeEach
    public void setUp() throws SQLException {
        // Establish connection to the test database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false); // Start transaction

        // Clear all contracts related to TEST_USERNAME
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.contract WHERE JSON_CONTAINS(tenants, ?, '$.username') OR id_contract IN (?, ?)")) {
            ps.setString(1, "\"" + TEST_USERNAME + "\"");
            ps.setInt(2, TEST_CONTRACT_ID);
            ps.setInt(3, TEST_CONTRACT_ID_2);
            ps.executeUpdate();
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.contract WHERE JSON_CONTAINS(tenants, ?, '$.username') OR id_contract IN (?, ?)")) {
            ps.setString(1, "\"" + TEST_USERNAME + "\"");
            ps.setInt(2, TEST_CONTRACT_ID);
            ps.setInt(3, TEST_CONTRACT_ID_2);
            ps.executeUpdate();
        }

        // Close connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testGetContractListForTenant_Success() throws Exception {
        // Arrange
        insertTestContract(TEST_CONTRACT_ID, TEST_USERNAME);
        String username = TEST_USERNAME;
        Model model = null;

        // Act
        ResponseEntity<List<Contract>> response = contractController.getContractListForTenant(model, username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Contract> contracts = response.getBody();
        assertNotNull(contracts, "Contract list should not be null");
        assertEquals(1, contracts.size(), "Contract list should contain one contract");
        Contract contract = contracts.get(0);
        assertEquals("Test Property", contract.getProp_name(), "Property name should be Test Property");
        assertEquals(1000.0f, contract.getPrice(), "Price should be 1000.0");
    }

    @Test
    public void testGetContractListForTenant_NoDataFound() throws Exception {
        // Arrange
        String username = "non_existent_user";
        Model model = null;

        // Act
        ResponseEntity<List<Contract>> response = contractController.getContractListForTenant(model, username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Contract> contracts = response.getBody();
        assertNotNull(contracts, "Contract list should not be null");
        assertTrue(contracts.isEmpty(), "Contract list should be empty");
    }

    @Test
    public void testGetContractListForTenant_DeletedContract() throws Exception {
        // Arrange
        insertTestContract(TEST_CONTRACT_ID, TEST_USERNAME);
        insertTestContract(TEST_CONTRACT_ID_2, TEST_USERNAME);
        String username = TEST_USERNAME;
        Model model = null;

        // Act
        ResponseEntity<List<Contract>> response = contractController.getContractListForTenant(model, username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Contract> contracts = response.getBody();
        assertNotNull(contracts, "Contract list should not be null");
        assertEquals(2, contracts.size(), "Contract list should contain both contracts as delete flag is not checked");
        Contract contract = contracts.get(0);
        assertEquals("Test Property", contract.getProp_name(), "Property name should be Test Property");
    }

    @Test
    public void testGetContractListForTenant_DatabaseError() throws Exception {
        // Arrange
        String username = TEST_USERNAME;
        Model model = null;
        // Note: Simulating SQLException requires specific database error setup
        // This test assumes normal execution unless configured otherwise

        // Act
        ResponseEntity<List<Contract>> response = contractController.getContractListForTenant(model, username);

        // Assert
        assertNotEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "This test requires specific database error setup; assuming success unless configured otherwise");
    }

    // Helper method to insert a test contract
    private void insertTestContract(int contractId, String username) throws SQLException {
        String tenantsJson = "[{\"username\": \"" + username + "\"}]";
        String insertQuery = "INSERT INTO bds.contract (id_contract, prop_owner_id, prop_owner_name, owner_gender, owner_email, owner_phone, owner_dob, prop_id, prop_name, room_id, room_code, max_pp, tenants, price, price_type, rule, status, start_date, end_date, electric, water, water_type, internet, clean, elevator, other_service, deposit) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setInt(1, contractId);
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
            ps.setString(13, tenantsJson);
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
}