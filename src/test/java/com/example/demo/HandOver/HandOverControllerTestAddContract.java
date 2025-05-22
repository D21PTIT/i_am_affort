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
 * Lớp kiểm thử cho phương thức addContract của HandOverController.
 */
@SpringBootTest
@ActiveProfiles("test")
public class HandOverControllerTestAddContract {

    @Autowired
    private HandOverController handOverController;

    private Connection connection;
    private static final int TEST_OWNER_ID = 9999; // ID chủ sở hữu thử nghiệm

    /**
     * Thiết lập trước mỗi test: Kết nối cơ sở dữ liệu, xóa dữ liệu cũ.
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
        handOver.setLabel("New HandOver");
        handOver.setId_contract(1);
        handOver.setId_property(1);
        handOver.setProperty_name("New Property");
        handOver.setId_room(1);
        handOver.setRoom_code("ROOM002");
        handOver.setId_owner(TEST_OWNER_ID);
        handOver.setTenant_list(new ArrayList<>());
        handOver.setDate(Date.valueOf("2025-05-18"));
        handOver.setContent("New content");
        return handOver;
    }

    /**
     * Kiểm tra thành công: Thêm bàn giao hợp lệ.
     */
    @Test
    public void testAddContract_Success() {
        HandOver handOver = createValidHandOver();
        ResponseEntity<Map<String, String>> response = handOverController.addContract(handOver);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Hand over added successfully", responseBody.get("message"));
        String newId = responseBody.get("id_hand_over");
        assertNotNull(newId);
        assertTrue(Integer.parseInt(newId) > 0);

        // Kiểm tra cơ sở dữ liệu
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM bds.hand_over WHERE id_hand_over = ?")) {
            ps.setInt(1, Integer.parseInt(newId));
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("New HandOver", rs.getString("label"));
                assertEquals("New Property", rs.getString("property_name"));
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
    public void testAddContract_EmptyLabel() {
        HandOver handOver = createValidHandOver();
        handOver.setLabel("");
        ResponseEntity<Map<String, String>> response = handOverController.addContract(handOver);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Label must not be empty", responseBody.get("message"));
    }

    /**
     * Kiểm tra lỗi khi id_owner không hợp lệ (âm).
     */
    @Test
    public void testAddContract_InvalidOwnerId() {
        HandOver handOver = createValidHandOver();
        handOver.setId_owner(-1);
        ResponseEntity<Map<String, String>> response = handOverController.addContract(handOver);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Invalid owner ID", responseBody.get("message"));
    }

    /**
     * Kiểm tra lỗi khi tenant_list không thể serialize.
     */
    @Test
    public void testAddContract_InvalidJson() {
        HandOver handOver = createValidHandOver();
        // Tạo tenant_list không thể serialize (giả lập bằng cách gây lỗi serialization)
        // Lưu ý: Cần mock ObjectMapper để ném JsonProcessingException, nhưng vì không mock được, giả lập bằng dữ liệu không hợp lệ
        handOver.setTenant_list(null); // Gây lỗi serialization
        ResponseEntity<Map<String, String>> response = handOverController.addContract(handOver);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Error occurred", responseBody.get("message"));
    }
}