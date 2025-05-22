package com.example.demo.HandOver;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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

import com.example.demo.Model.HandOver;
@Transactional

/**
 * Lớp kiểm thử cho phương thức deleteContract của HandOverController.
 */
@SpringBootTest
@ActiveProfiles("test")
public class HandOverControllerTestDeleteContract {

    @Autowired
    private HandOverController handOverController;

    private Connection connection;
    private static final int TEST_OWNER_ID = 9999; // ID chủ sở hữu thử nghiệm
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

        // Chèn bàn giao thử nghiệm
        String insertHandOverQuery = "INSERT INTO bds.hand_over (label, id_contract, id_property, property_name, id_room, room_code, id_owner, tenants, date, content, created_date, updated_date, `delete`) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertHandOverQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "Test HandOver");
            ps.setInt(2, 1);
            ps.setInt(3, 1);
            ps.setString(4, "Test Property");
            ps.setInt(5, 1);
            ps.setString(6, "ROOM001");
            ps.setInt(7, TEST_OWNER_ID);
            ps.setString(8, "[]");
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
     * Tạo đối tượng HandOver tối thiểu cho test.
     */
    private HandOver createMinimalHandOver(int idHandOver) {
        HandOver handOver = new HandOver();
        handOver.setId_hand_over(idHandOver);
        return handOver;
    }

    /**
     * Kiểm tra thành công: Xóa mềm bàn giao hợp lệ (đánh dấu delete=1).
     */
    @Test
    public void testDeleteContract_Success() {
        HandOver handOver = createMinimalHandOver(testHandOverId);
        ResponseEntity<Map<String, String>> response = handOverController.deleteContract(handOver, String.valueOf(testHandOverId));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Hand over deleted successfully!", responseBody.get("message"));
        try (PreparedStatement ps = connection.prepareStatement("SELECT `delete` FROM bds.hand_over WHERE id_hand_over = ?")) {
            ps.setInt(1, testHandOverId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(1, rs.getInt("delete"));
            }
        } catch (SQLException e) {
            fail("Lỗi SQL: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra khi id_hand_over không hợp lệ (controller không kiểm tra và vẫn trả về thành công).
     */
    @Test
    public void testDeleteContract_InvalidId() {
        // id_hand_over không hợp lệ trong body và path variable
        HandOver handOver = createMinimalHandOver(-1);
        ResponseEntity<Map<String, String>> response = handOverController.deleteContract(handOver, "invalid");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Hand over deleted successfully!", responseBody.get("message"));
    }
}