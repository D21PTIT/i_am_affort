package com.example.demo.Contract;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.example.demo.Model.Contract;
@Transactional

@SpringBootTest
@ActiveProfiles("test")
public class DeleteContractTest {

    @Autowired
    private ContractController contractController;

    private Connection connection;
    private static final int TEST_CONTRACT_ID = 9999;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);
        deleteTestContract(TEST_CONTRACT_ID);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        deleteTestContract(TEST_CONTRACT_ID);
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testDeleteContract_Success() throws SQLException {
        insertTestContract(TEST_CONTRACT_ID);
        String idContract = String.valueOf(TEST_CONTRACT_ID);
        Contract contract = createValidContract(TEST_CONTRACT_ID);

        ResponseEntity<Map<String, String>> response = contractController.deleteContract(contract, idContract);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Contract deleted successfully!", response.getBody().get("message"));

        try (PreparedStatement ps = connection.prepareStatement("SELECT `delete` FROM contract WHERE id_contract = ?")) {
            ps.setInt(1, TEST_CONTRACT_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Should find the contract");
                assertEquals(1, rs.getInt("delete"), "Delete flag should be 1");
            }
        }
    }

    @Test
    public void testDeleteContract_NotExist_ShouldStillReturnSuccess() {
        // Lưu ý: Logic hiện tại của controller luôn trả CREATED kể cả khi contract không tồn tại
        String idContract = String.valueOf(TEST_CONTRACT_ID);
        Contract contract = createValidContract(TEST_CONTRACT_ID);

        ResponseEntity<Map<String, String>> response = contractController.deleteContract(contract, idContract);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Contract deleted successfully!", response.getBody().get("message"));
    }

    

    // === Helper methods ===

    private void insertTestContract(int contractId) throws SQLException {
        String tenantsJson = "[{\"username\": \"test_user\"}]";
        String query = "INSERT INTO contract (id_contract, prop_owner_id, prop_owner_name, owner_gender, owner_email, owner_phone, owner_dob, prop_id, prop_name, room_id, room_code, max_pp, tenants, price, price_type, rule, status, start_date, end_date, electric, water, water_type, internet, clean, elevator, other_service, deposit, `delete`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
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

    private void deleteTestContract(int contractId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM contract WHERE id_contract = ?")) {
            ps.setInt(1, contractId);
            ps.executeUpdate();
        }
        connection.commit();
    }

    private Contract createValidContract(int contractId) {
        Contract contract = new Contract();
        contract.setId_contract(contractId);
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
