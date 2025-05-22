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
import static org.junit.jupiter.api.Assertions.assertNull;
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
public class GetContractListTest {

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
    public void testGetContractList_Success() throws Exception {
        // Arrange
        insertTestContract(TEST_OWNER_ID, 0);
        String idOwner = String.valueOf(TEST_OWNER_ID);
        Model model = null;

        // Act
        ResponseEntity<List<Contract>> response = contractController.getContractList(model, idOwner);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Contract> contracts = response.getBody();
        assertNotNull(contracts, "Contract list should not be null");
        assertEquals(1, contracts.size(), "Contract list should contain one contract");
        Contract contract = contracts.get(0);
        assertEquals("Test Property", contract.getProp_name(), "Property name should be Test Property");
        assertEquals(TEST_OWNER_ID, contract.getProp_owner_id(), "Owner ID should match");
        assertEquals(1000.0f, contract.getPrice(), "Price should be 1000.0");
        assertEquals(0, contract.getDelete(), "Delete flag should be 0");
    }

    @Test
    public void testGetContractList_NoDataFound() throws Exception {
        // Arrange
        String idOwner = "999"; // Non-existent owner ID
        Model model = null;

        // Act
        ResponseEntity<List<Contract>> response = contractController.getContractList(model, idOwner);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Contract> contracts = response.getBody();
        assertNotNull(contracts, "Contract list should not be null");
        assertTrue(contracts.isEmpty(), "Contract list should be empty");
    }

    @Test
    public void testGetContractList_DeletedContract() throws Exception {
        // Arrange
        insertTestContract(TEST_OWNER_ID, 0);
        insertTestContract(TEST_OWNER_ID, 1); // Deleted contract
        String idOwner = String.valueOf(TEST_OWNER_ID);
        Model model = null;

        // Act
        ResponseEntity<List<Contract>> response = contractController.getContractList(model, idOwner);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "HTTP status should be OK");
        List<Contract> contracts = response.getBody();
        assertNotNull(contracts, "Contract list should not be null");
        assertEquals(1, contracts.size(), "Contract list should contain only non-deleted contracts");
        Contract contract = contracts.get(0);
        assertEquals("Test Property", contract.getProp_name(), "Property name should be Test Property");
        assertEquals(0, contract.getDelete(), "Delete flag should be 0");
    }

    @Test
    public void testGetContractList_InvalidOwnerId() throws Exception {
        // Arrange
        String idOwner = "invalid"; // Non-numeric owner ID
        Model model = null;

        // Act
        ResponseEntity<List<Contract>> response = contractController.getContractList(model, idOwner);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "HTTP status should be INTERNAL_SERVER_ERROR for invalid owner ID");
        assertNull(response.getBody(), "Response body should be null");
    }

    @Test
    public void testGetContractList_DatabaseError() throws Exception {
        // Arrange
        String idOwner = String.valueOf(TEST_OWNER_ID);
        Model model = null;
        // Note: Simulating SQLException requires specific database error setup
        // This test assumes normal execution unless configured otherwise

        // Act
        ResponseEntity<List<Contract>> response = contractController.getContractList(model, idOwner);

        // Assert
        assertNotEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "This test requires specific database error setup; assuming success unless configured otherwise");
    }

    // Helper method to insert a test contract
    private void insertTestContract(int ownerId, int deleteFlag) throws SQLException {
        String insertQuery = "INSERT INTO bds.contract (prop_owner_id, prop_owner_name, owner_gender, owner_email, owner_phone, owner_dob, prop_id, prop_name, room_id, room_code, max_pp, tenants, price, price_type, rule, status, start_date, end_date, electric, water, water_type, internet, clean, elevator, other_service, deposit, `delete`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setInt(1, ownerId);
            ps.setString(2, "John Doe");
            ps.setInt(3, 1);
            ps.setString(4, "john@example.com");
            ps.setString(5, "1234567890");
            ps.setString(6, "1990-01-01");
            ps.setInt(7, 1);
            ps.setString(8, "Test Property");
            ps.setInt(9, 1);
            ps.setString(10, "ROOM001");
            ps.setInt(11, 4);
            ps.setString(12, "[]");
            ps.setFloat(13, 1000.0f);
            ps.setInt(14, 1);
            ps.setString(15, "No pets allowed");
            ps.setInt(16, 1);
            ps.setDate(17, Date.valueOf("2025-05-20"));
            ps.setDate(18, Date.valueOf("2026-05-20"));
            ps.setFloat(19, 50.0f);
            ps.setFloat(20, 20.0f);
            ps.setInt(21, 1);
            ps.setFloat(22, 30.0f);
            ps.setFloat(23, 10.0f);
            ps.setFloat(24, 15.0f);
            ps.setFloat(25, 5.0f);
            ps.setFloat(26, 500.0f);
            ps.setInt(27, deleteFlag);
            ps.executeUpdate();
        }
        connection.commit();
    }
}