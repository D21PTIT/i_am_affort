package com.example.demo.HandOver;

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
 * Lớp kiểm thử cho phương thức updateHandOver của HandOverController.
 */
@SpringBootTest
@ActiveProfiles("test")
public class HandOverControllerTestUpdateHandOver {

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
     * Tạo đối tượng HandOver hợp lệ cho test.
     */
    private HandOver createValidHandOver() {
        HandOver handOver = new HandOver();
        handOver.setLabel("Updated HandOver");
        handOver.setId_contract(2);
        handOver.setId_property(2);
        handOver.setProperty_name("Updated Property");
        handOver.setId_room(2);
        handOver.setRoom_code("ROOM002");
        handOver.setId_owner(TEST_OWNER_ID);
        handOver.setTenant_list(new ArrayList<>());
        handOver.setDate(Date.valueOf("2025-05-19"));
        handOver.setContent("Updated content");
        return handOver;
    }

    /**
     * Kiểm tra thành công: Cập nhật bàn giao hợp lệ.
     */
    @Test
    public void testUpdateHandOver_Success() {
        HandOver handOver = createValidHandOver();
        ResponseEntity<Map<String, String>> response = handOverController.updateHandOver(handOver, String.valueOf(testHandOverId));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Hand over updated successfully hihihi!", responseBody.get("message"));

        // Kiểm tra cơ sở dữ liệu
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM bds.hand_over WHERE id_hand_over = ?")) {
            ps.setInt(1, testHandOverId);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Updated HandOver", rs.getString("label"));
                assertEquals("Updated Property", rs.getString("property_name"));
                assertEquals("ROOM002", rs.getString("room_code"));
                assertEquals(TEST_OWNER_ID, rs.getInt("id_owner"));
                assertEquals("[]", rs.getString("tenants"));
            }
        } catch (SQLException e) {
            fail("Lỗi SQL: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra lỗi khi label rỗng.
     */
    @Test
    public void testUpdateHandOver_EmptyLabel() {
        HandOver handOver = createValidHandOver();
        handOver.setLabel("");
        ResponseEntity<Map<String, String>> response = handOverController.updateHandOver(handOver, String.valueOf(testHandOverId));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Label must not be empty", responseBody.get("message"));
    }

    /**
     * Kiểm tra lỗi khi id_hand_over không hợp lệ (không phải số).
     */
    @Test
    public void testUpdateHandOver_InvalidId() {
        HandOver handOver = createValidHandOver();
        ResponseEntity<Map<String, String>> response = handOverController.updateHandOver(handOver, "invalid");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Invalid hand over ID", responseBody.get("message"));
    }

    /**
     * Kiểm tra lỗi khi tenant_list không thể serialize.
     */
    @Test
    public void testUpdateHandOver_InvalidJson() {
        HandOver handOver = createValidHandOver();
        // Gây lỗi serialization bằng cách đặt tenant_list=null
        handOver.setTenant_list(null);
        ResponseEntity<Map<String, String>> response = handOverController.updateHandOver(handOver, String.valueOf(testHandOverId));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Error occurred", responseBody.get("message"));
    }
}