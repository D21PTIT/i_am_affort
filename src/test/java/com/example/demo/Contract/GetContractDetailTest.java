package com.example.demo.Contract;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
public class GetContractDetailTest {

    @Autowired
    private ContractController contractController;

    private Connection connection;
    private static final int TEST_CONTRACT_ID = 9999;
    private static final String TEST_USERNAME = "test_user";

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);

        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM contract WHERE id_contract = ?")) {
            ps.setInt(1, TEST_CONTRACT_ID);
            ps.executeUpdate();
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM contract WHERE id_contract = ?")) {
            ps.setInt(1, TEST_CONTRACT_ID);
            ps.executeUpdate();
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testGetContractDetail_Success() throws Exception {
        insertTestContract(TEST_CONTRACT_ID, TEST_USERNAME);
        String idContract = String.valueOf(TEST_CONTRACT_ID);

        ResponseEntity<Contract> response = contractController.getContractDetail(null, idContract);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Contract contract = response.getBody();
        assertNotNull(contract);
        assertEquals(TEST_CONTRACT_ID, contract.getId_contract());
        assertEquals("Test Property", contract.getProp_name());
        assertEquals(1000.0f, contract.getPrice());
    }

    @Test
    public void testGetContractDetail_NoDataFound() {
        String idContract = "999"; // Không tồn tại
        ResponseEntity<Contract> response = contractController.getContractDetail(null, idContract);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Contract contract = response.getBody();
        assertNotNull(contract);
        assertEquals(0, contract.getId_contract());
    }

    @Test
    public void testGetContractDetail_InvalidContractId() {
        String idContract = "invalid"; // Không hợp lệ
        ResponseEntity<Contract> response = contractController.getContractDetail(null, idContract);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetContractDetail_DatabaseError() {
        // Trường hợp này chưa có mô phỏng lỗi DB, chỉ đảm bảo không lỗi bất ngờ
        String idContract = String.valueOf(TEST_CONTRACT_ID);
        ResponseEntity<Contract> response = contractController.getContractDetail(null, idContract);

        assertNotEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private void insertTestContract(int contractId, String username) throws SQLException {
        String tenantsJson = "[{\"username\": \"" + username + "\"}]";
        String insertQuery = "INSERT INTO contract (id_contract, prop_owner_id, prop_owner_name, owner_gender, owner_email, owner_phone, owner_dob, prop_id, prop_name, room_id, room_code, max_pp, tenants, price, price_type, rule, status, start_date, end_date, electric, water, water_type, internet, clean, elevator, other_service, deposit) " +
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
