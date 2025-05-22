package com.example.demo.HandOver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.example.demo.Model.HandOver;
@Transactional

/**
 * Lớp kiểm thử cho phương thức getContractListForTenant của HandOverController.
 */
@SpringBootTest
@ActiveProfiles("test")
public class HandOverControllerTestGetContractListForTenant {

    @Autowired
    private HandOverController handOverController;

    private Connection connection;
    private static final int TEST_OWNER_ID = 9999; // ID chủ sở hữu thử nghiệm
    private static final String TEST_USERNAME = "testuser"; // Tên người thuê thử nghiệm
    private int testHandOverId; // ID bàn giao thử nghiệm

    /**
     * Thiết lập trước mỗi test: Kết nối cơ sở dữ liệu, xóa dữ liệu cũ, chèn bản ghi thử nghiệm.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bds", "root", "1234");
        connection.setAutoCommit(false);

        // Xóa dữ liệu cũ
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.hand_over WHERE id_owner = ?")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.executeUpdate();
        }

        // Chèn bàn giao thử nghiệm với tenants chứa username
        String insertQuery = "INSERT INTO bds.hand_over (label, id_contract, id_property, property_name, id_room, room_code, id_owner, tenants, date, content, created_date, updated_date, `delete`) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Test HandOver");
            ps.setInt(2, 1);
            ps.setInt(3, 1);
            ps.setString(4, "Test Property");
            ps.setInt(5, 1);
            ps.setString(6, "ROOM001");
            ps.setInt(7, TEST_OWNER_ID);
            ps.setString(8, "[{\"username\":\"" + TEST_USERNAME + "\"}]");
            ps.setDate(9, Date.valueOf("2025-05-18"));
            ps.setString(10, "Test content");
            ps.setDate(11, Date.valueOf("2025-05-18"));
            ps.setDate(12, Date.valueOf("2025-05-18"));
            ps.setInt(13, 0);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                assertTrue(rs.next());
                testHandOverId = rs.getInt(1);
                assertTrue(testHandOverId > 0);
            }
        }
        connection.commit();
    }

    /**
     * Dọn dẹp sau mỗi test: Xóa dữ liệu thử nghiệm và đóng kết nối.
     */
    @AfterEach
    public void tearDown() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.hand_over WHERE id_owner = ?")) {
            ps.setInt(1, TEST_OWNER_ID);
            ps.executeUpdate();
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Kiểm tra thành công: Lấy danh sách bàn giao hợp lệ cho người thuê.
     */
    @Test
    public void testGetContractListForTenant_Success() {
        Model model = new ExtendedModelMap();
        try {
            ResponseEntity<List<HandOver>> response = handOverController.getContractListForTenant(model, TEST_USERNAME);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            List<HandOver> handOverList = response.getBody();
            assertNotNull(handOverList);
            assertEquals(1, handOverList.size());
            HandOver handOver = handOverList.get(0);
            assertEquals("Test HandOver", handOver.getLabel());
            assertEquals(TEST_OWNER_ID, handOver.getId_owner());
            assertEquals("Test Property", handOver.getProperty_name());
            assertEquals("ROOM001", handOver.getRoom_code());
            assertEquals(1, handOver.getTenant_list().size());
            assertEquals(TEST_USERNAME, handOver.getTenant_list().get(0).getUsername());
            assertEquals("Test content", handOver.getContent());
        } catch (IOException e) {
            fail("Lỗi IOException: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra khi không có bàn giao nào cho người thuê.
     */
    @Test
    public void testGetContractListForTenant_EmptyList() {
        // Xóa bản ghi thử nghiệm
        try {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM bds.hand_over WHERE id_owner = ?")) {
                ps.setInt(1, TEST_OWNER_ID);
                ps.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            fail("Lỗi SQL: " + e.getMessage());
        }

        Model model = new ExtendedModelMap();
        try {
            ResponseEntity<List<HandOver>> response = handOverController.getContractListForTenant(model, TEST_USERNAME);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            List<HandOver> handOverList = response.getBody();
            assertNotNull(handOverList);
            assertTrue(handOverList.isEmpty());
        } catch (IOException e) {
            fail("Lỗi IOException: " + e.getMessage());
        }
    }
}