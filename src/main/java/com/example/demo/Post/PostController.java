package com.example.demo.Post;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Direction;
import com.example.demo.Model.Doc;
import com.example.demo.Model.Img;
import com.example.demo.Model.Post;
import com.example.demo.Model.Price;
import com.example.demo.Model.RoomInPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class PostController {
	@GetMapping("/post_list")
    public ResponseEntity<List<Post>> getPosts(Model model) {
        List<Post> post_list = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester",
                "root", "123456");
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT * FROM `tester`.`post` WHERE `delete` = 0 AND `status` = 1 order by `id_post` DESC");) {

            ObjectMapper objectMapper = new ObjectMapper();
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Post post = new Post();

                    // Basic post details
                    post.setId_post(resultSet.getInt("id_post"));
                    post.setHeader(resultSet.getString("header"));
                    post.setProvince(resultSet.getString("province"));
                    post.setDistrict(resultSet.getString("district"));
                    post.setWard(resultSet.getString("ward"));
                    post.setDetail_address(resultSet.getString("detail_address"));

                    // Legal documents
                    List<Doc> doc_list = parseJsonList(resultSet.getString("legal_doc"), objectMapper, Doc.class);
                    post.setDoc_list(doc_list);

                    // Property details
                    post.setSurface_area(resultSet.getFloat("surface_area"));
                    post.setUseable_area(resultSet.getFloat("useable_area"));
                    post.setWidth(resultSet.getFloat("width"));
                    post.setLength(resultSet.getFloat("length"));
                    post.setFlours(resultSet.getInt("flours"));
                    post.setBedrooms(resultSet.getInt("bedrooms"));
                    post.setToilets(resultSet.getInt("toilets"));

                    // Directions
                    List<Direction> direction_list = parseJsonList(resultSet.getString("direction"), objectMapper, Direction.class);
                    post.setDirection_list(direction_list);

                    // Prices
                    List<Price> price_month = createPriceList(resultSet, objectMapper, "price_month");
                    List<Price> price_quater = createPriceList(resultSet, objectMapper, "price_quater");
                    List<Price> price_half_year = createPriceList(resultSet, objectMapper, "price_half_year");
                    List<Price> price_year = createPriceList(resultSet, objectMapper, "price_year");

                    post.setPrice_month(price_month);
                    post.setPrice_quater(price_quater);
                    post.setPrice_half_year(price_half_year);
                    post.setPrice_year(price_year);

                    // Status and descriptions
                    post.setStatus(resultSet.getInt("status"));
                    post.setShort_des(resultSet.getString("short_description"));
                    post.setDetail_des(resultSet.getString("detail_des"));

                    // Images
                    List<Img> image_list = parseJsonList(resultSet.getString("img"), objectMapper, Img.class);
                    post.setImage_list(image_list);

                    // Rooms
                    List<RoomInPost> room_list = parseJsonList(resultSet.getString("rooms"), objectMapper, RoomInPost.class);
                    post.setRoom_list(room_list);

                    // Creator and ownership details
                    post.setCreated_by_staff(resultSet.getInt("created_by_staff"));
                    post.setCreated_by_user(resultSet.getInt("created_by_user"));
                    post.setType(resultSet.getInt("type"));
                    post.setOwner(resultSet.getString("owner"));
                    post.setPhone_number(resultSet.getString("phone_number"));
                    post.setEmail(resultSet.getString("email"));
                    post.setCompany_name(resultSet.getString("company_name"));

                    // Dates
                    post.setExp_date(resultSet.getDate("exp_date"));
                    post.setCreated_at(resultSet.getDate("created_at"));
                    post.setUpdated_at(resultSet.getDate("updated_at"));

                    // Additional details
                    post.setDelete(resultSet.getInt("delete"));
                    post.setId_property(resultSet.getInt("id_property"));
                    post.setPublic_price(resultSet.getInt("public_price"));
                    post.setElectric(resultSet.getFloat("electric"));
                    post.setWater(resultSet.getFloat("water"));
                    post.setWater_type(resultSet.getInt("water_type"));
                    post.setInternet(resultSet.getFloat("internet"));
                    post.setClean(resultSet.getFloat("clean"));
                    post.setElevator(resultSet.getFloat("elevator"));
                    post.setOther_service(resultSet.getFloat("other_service"));
                    post.setLongitude(resultSet.getFloat("longitude"));
                    post.setLatitude(resultSet.getFloat("latitude"));

                    post_list.add(post);
                }
            }
            return ResponseEntity.ok(post_list);

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }
	
    @GetMapping("/post_list/{id_user}")
    public ResponseEntity<List<Post>> getPostUserList(@PathVariable String id_user) {
        List<Post> post_list = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester",
                "root", "123456");
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT * FROM `tester`.`post` WHERE `delete` = 0 AND `created_by_user` = ? order by `id_post` DESC" );) {
            ps.setInt(1, Integer.valueOf(id_user));

            ObjectMapper objectMapper = new ObjectMapper();
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Post post = new Post();

                    // Basic post details
                    post.setId_post(resultSet.getInt("id_post"));
                    post.setHeader(resultSet.getString("header"));
                    post.setProvince(resultSet.getString("province"));
                    post.setDistrict(resultSet.getString("district"));
                    post.setWard(resultSet.getString("ward"));
                    post.setDetail_address(resultSet.getString("detail_address"));

                    // Legal documents
                    List<Doc> doc_list = parseJsonList(resultSet.getString("legal_doc"), objectMapper, Doc.class);
                    post.setDoc_list(doc_list);

                    // Property details
                    post.setSurface_area(resultSet.getFloat("surface_area"));
                    post.setUseable_area(resultSet.getFloat("useable_area"));
                    post.setWidth(resultSet.getFloat("width"));
                    post.setLength(resultSet.getFloat("length"));
                    post.setFlours(resultSet.getInt("flours"));
                    post.setBedrooms(resultSet.getInt("bedrooms"));
                    post.setToilets(resultSet.getInt("toilets"));

                    // Directions
                    List<Direction> direction_list = parseJsonList(resultSet.getString("direction"), objectMapper, Direction.class);
                    post.setDirection_list(direction_list);

                    // Prices
                    List<Price> price_month = createPriceList(resultSet, objectMapper, "price_month");
                    List<Price> price_quater = createPriceList(resultSet, objectMapper, "price_quater");
                    List<Price> price_half_year = createPriceList(resultSet, objectMapper, "price_half_year");
                    List<Price> price_year = createPriceList(resultSet, objectMapper, "price_year");

                    post.setPrice_month(price_month);
                    post.setPrice_quater(price_quater);
                    post.setPrice_half_year(price_half_year);
                    post.setPrice_year(price_year);

                    // Status and descriptions
                    post.setStatus(resultSet.getInt("status"));
                    post.setShort_des(resultSet.getString("short_description"));
                    post.setDetail_des(resultSet.getString("detail_des"));

                    // Images
                    List<Img> image_list = parseJsonList(resultSet.getString("img"), objectMapper, Img.class);
                    post.setImage_list(image_list);

                    // Rooms
                    List<RoomInPost> room_list = parseJsonList(resultSet.getString("rooms"), objectMapper, RoomInPost.class);
                    post.setRoom_list(room_list);

                    // Creator and ownership details
                    post.setCreated_by_staff(resultSet.getInt("created_by_staff"));
                    post.setCreated_by_user(resultSet.getInt("created_by_user"));
                    post.setType(resultSet.getInt("type"));
                    post.setOwner(resultSet.getString("owner"));
                    post.setPhone_number(resultSet.getString("phone_number"));
                    post.setEmail(resultSet.getString("email"));
                    post.setCompany_name(resultSet.getString("company_name"));

                    // Dates
                    post.setExp_date(resultSet.getDate("exp_date"));
                    post.setCreated_at(resultSet.getDate("created_at"));
                    post.setUpdated_at(resultSet.getDate("updated_at"));

                    // Additional details
                    post.setDelete(resultSet.getInt("delete"));
                    post.setId_property(resultSet.getInt("id_property"));
                    post.setPublic_price(resultSet.getInt("public_price"));
                    post.setElectric(resultSet.getFloat("electric"));
                    post.setWater(resultSet.getFloat("water"));
                    post.setWater_type(resultSet.getInt("water_type"));
                    post.setInternet(resultSet.getFloat("internet"));
                    post.setClean(resultSet.getFloat("clean"));
                    post.setElevator(resultSet.getFloat("elevator"));
                    post.setOther_service(resultSet.getFloat("other_service"));
                    post.setLongitude(resultSet.getFloat("longitude"));
                    post.setLatitude(resultSet.getFloat("latitude"));

                    post_list.add(post);
                }
            }
            return ResponseEntity.ok(post_list);

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }

    // Helper method to parse JSON lists safely
    private <T> List<T> parseJsonList(String jsonString, ObjectMapper objectMapper, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return resultList;
        }

        try {
            // If it's a single integer, return an empty list
            if (jsonString.matches("\\d+")) {
                return resultList;
            }

            // Try parsing as a list first
            try {
                resultList = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            } catch (JsonProcessingException arrayError) {
                // If not a list, try parsing as a single object
                try {
                    T singleObject = objectMapper.readValue(jsonString, clazz);
                    resultList.add(singleObject);
                } catch (JsonProcessingException objectError) {
                    objectError.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    // Helper method to create price lists
    private List<Price> createPriceList(ResultSet resultSet, ObjectMapper objectMapper, String columnName) {
        try {
            String priceJson = resultSet.getString(columnName);
            return parseJsonList(priceJson, objectMapper, Price.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @GetMapping("/post_detail/{id_post}")
    public ResponseEntity<Post> getPostDetail(Model model, @PathVariable String id_post) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
            ps = connection.prepareStatement("SELECT * FROM `tester`.`post` WHERE id_post = ?");
            ps.setInt(1, Integer.valueOf(id_post));
            
            ObjectMapper objectMapper = new ObjectMapper();
            resultSet = ps.executeQuery();
            
            if (resultSet.next()) {
                Post post = new Post();

                // Basic post details
                post.setId_post(resultSet.getInt("id_post"));
                post.setHeader(resultSet.getString("header"));
                post.setProvince(resultSet.getString("province"));
                post.setDistrict(resultSet.getString("district"));
                post.setWard(resultSet.getString("ward"));
                post.setDetail_address(resultSet.getString("detail_address"));

                // Legal documents
                List<Doc> doc_list = parseJsonList(resultSet.getString("legal_doc"), objectMapper, Doc.class);
                post.setDoc_list(doc_list);

                // Property details
                post.setSurface_area(resultSet.getFloat("surface_area"));
                post.setUseable_area(resultSet.getFloat("useable_area"));
                post.setWidth(resultSet.getFloat("width"));
                post.setLength(resultSet.getFloat("length"));
                post.setFlours(resultSet.getInt("flours"));
                post.setBedrooms(resultSet.getInt("bedrooms"));
                post.setToilets(resultSet.getInt("toilets"));

                // Directions
                List<Direction> direction_list = parseJsonList(resultSet.getString("direction"), objectMapper, Direction.class);
                post.setDirection_list(direction_list);

                // Prices
                List<Price> price_month = createPriceList(resultSet, objectMapper, "price_month");
                List<Price> price_quater = createPriceList(resultSet, objectMapper, "price_quater");
                List<Price> price_half_year = createPriceList(resultSet, objectMapper, "price_half_year");
                List<Price> price_year = createPriceList(resultSet, objectMapper, "price_year");

                post.setPrice_month(price_month);
                post.setPrice_quater(price_quater);
                post.setPrice_half_year(price_half_year);
                post.setPrice_year(price_year);

                // Status and descriptions
                post.setStatus(resultSet.getInt("status"));
                post.setShort_des(resultSet.getString("short_description"));
                post.setDetail_des(resultSet.getString("detail_des"));

                // Images
                List<Img> image_list = parseJsonList(resultSet.getString("img"), objectMapper, Img.class);
                post.setImage_list(image_list);

                // Rooms
                List<RoomInPost> room_list = parseJsonList(resultSet.getString("rooms"), objectMapper, RoomInPost.class);
                post.setRoom_list(room_list);

                // Creator and ownership details
                post.setCreated_by_staff(resultSet.getInt("created_by_staff"));
                post.setCreated_by_user(resultSet.getInt("created_by_user"));
                post.setType(resultSet.getInt("type"));
                post.setOwner(resultSet.getString("owner"));
                post.setPhone_number(resultSet.getString("phone_number"));
                post.setEmail(resultSet.getString("email"));
                post.setCompany_name(resultSet.getString("company_name"));

                // Dates
                post.setExp_date(resultSet.getDate("exp_date"));
                post.setCreated_at(resultSet.getDate("created_at"));
                post.setUpdated_at(resultSet.getDate("updated_at"));

                // Additional details
                post.setDelete(resultSet.getInt("delete"));
                post.setId_property(resultSet.getInt("id_property"));
                post.setPublic_price(resultSet.getInt("public_price"));
                post.setElectric(resultSet.getFloat("electric"));
                post.setWater(resultSet.getFloat("water"));
                post.setWater_type(resultSet.getInt("water_type"));
                post.setInternet(resultSet.getFloat("internet"));
                post.setClean(resultSet.getFloat("clean"));
                post.setElevator(resultSet.getFloat("elevator"));
                post.setOther_service(resultSet.getFloat("other_service"));
                post.setLongitude(resultSet.getFloat("longitude"));
                post.setLatitude(resultSet.getFloat("latitude"));

                return new ResponseEntity<>(post, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/post/create")
    public ResponseEntity<Map<String, String>> createPost(@RequestBody Post post) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> response = new HashMap<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
            
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert complex objects to JSON
            String legalDocJson = objectMapper.writeValueAsString(post.getDoc_list());
            String directionJson = objectMapper.writeValueAsString(post.getDirection_list());
            String priceMonthJson = objectMapper.writeValueAsString(post.getPrice_month());
            String priceQuaterJson = objectMapper.writeValueAsString(post.getPrice_quater());
            String priceHalfYearJson = objectMapper.writeValueAsString(post.getPrice_half_year());
            String priceYearJson = objectMapper.writeValueAsString(post.getPrice_year());
            String imgJson = objectMapper.writeValueAsString(post.getImage_list());
            String roomsJson = objectMapper.writeValueAsString(post.getRoom_list());

            String sql = "INSERT INTO `tester`.`post` " +
                    "(`header`, `province`, `district`, `ward`, `detail_address`, " +
                    "`legal_doc`, `surface_area`, `useable_area`, `width`, `length`, " +
                    "`flours`, `bedrooms`, `toilets`, `direction`, `price_month`, " +
                    "`price_quater`, `price_half_year`, `price_year`, `status`, " +
                    "`short_description`, `detail_des`, `img`, `rooms`, `created_by_staff`, " +
                    "`created_by_user`, `type`, `owner`, `phone_number`, `email`, " +
                    "`company_name`, `exp_date`, `id_property`, " +
                    "`other_service`, `longitude`, `latitude`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, post.getHeader());
            ps.setString(2, post.getProvince());
            ps.setString(3, post.getDistrict());
            ps.setString(4, post.getWard());
            ps.setString(5, post.getDetail_address());
            ps.setString(6, legalDocJson);
            ps.setFloat(7, post.getSurface_area());
            ps.setFloat(8, post.getUseable_area());
            ps.setFloat(9, post.getWidth());
            ps.setFloat(10, post.getLength());
            ps.setInt(11, post.getFlours());
            ps.setInt(12, post.getBedrooms());
            ps.setInt(13, post.getToilets());
            ps.setString(14, directionJson);
            ps.setString(15, priceMonthJson);
            ps.setString(16, priceQuaterJson);
            ps.setString(17, priceHalfYearJson);
            ps.setString(18, priceYearJson);
            ps.setInt(19, post.getStatus());
            ps.setString(20, post.getShort_des());
            ps.setString(21, post.getDetail_des());
            ps.setString(22, imgJson);
            ps.setString(23, roomsJson);
            ps.setInt(24, post.getCreated_by_staff());
            ps.setInt(25, post.getCreated_by_user());
            ps.setInt(26, post.getType());
            ps.setString(27, post.getOwner());
            ps.setString(28, post.getPhone_number());
            ps.setString(29, post.getEmail());
            ps.setString(30, post.getCompany_name());
            ps.setDate(31, post.getExp_date());
            ps.setInt(32, post.getId_property());
            ps.setFloat(33, post.getOther_service());
            ps.setFloat(34, post.getLongitude());
            ps.setFloat(35, post.getLatitude());

            ps.executeUpdate();

            response.put("message", "Post created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error occurred while creating post");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @PutMapping("/post/update/{id_post}")
    public ResponseEntity<Map<String, String>> updatePost(@RequestBody Post post, @PathVariable String id_post) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> response = new HashMap<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");
            
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert complex objects to JSON
            String legalDocJson = objectMapper.writeValueAsString(post.getDoc_list());
            String directionJson = objectMapper.writeValueAsString(post.getDirection_list());
            String priceMonthJson = objectMapper.writeValueAsString(post.getPrice_month());
            String priceQuaterJson = objectMapper.writeValueAsString(post.getPrice_quater());
            String priceHalfYearJson = objectMapper.writeValueAsString(post.getPrice_half_year());
            String priceYearJson = objectMapper.writeValueAsString(post.getPrice_year());
            String imgJson = objectMapper.writeValueAsString(post.getImage_list());
            String roomsJson = objectMapper.writeValueAsString(post.getRoom_list());

            String sql = "UPDATE `tester`.`post` SET " +
                    "`header` = ?, `province` = ?, `district` = ?, `ward` = ?, `detail_address` = ?, " +
                    "`legal_doc` = ?, `surface_area` = ?, `useable_area` = ?, `width` = ?, `length` = ?, " +
                    "`flours` = ?, `bedrooms` = ?, `toilets` = ?, `direction` = ?, `price_month` = ?, " +
                    "`price_quater` = ?, `price_half_year` = ?, `price_year` = ?, `status` = ?, " +
                    "`short_description` = ?, `detail_des` = ?, `img` = ?, `rooms` = ?, `created_by_staff` = ?, " +
                    "`created_by_user` = ?, `type` = ?, `owner` = ?, `phone_number` = ?, `email` = ?, " +
                    "`company_name` = ?, `exp_date` = ?, `id_property` = ?, " +
                    "`public_price` = ?, `electric` = ?, `water` = ?, `water_type` = ?, "+ 
                    "`internet` = ?, `clean` = ?, `elevator` = ?, `other_service` = ?, " +
                    "`longitude` = ?, `latitude` = ? " +
                    "WHERE (`id_post` = ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, post.getHeader());
            ps.setString(2, post.getProvince());
            ps.setString(3, post.getDistrict());
            ps.setString(4, post.getWard());
            ps.setString(5, post.getDetail_address());
            ps.setString(6, legalDocJson);
            ps.setFloat(7, post.getSurface_area());
            ps.setFloat(8, post.getUseable_area());
            ps.setFloat(9, post.getWidth());
            ps.setFloat(10, post.getLength());
            ps.setInt(11, post.getFlours());
            ps.setInt(12, post.getBedrooms());
            ps.setInt(13, post.getToilets());
            ps.setString(14, directionJson);
            ps.setString(15, priceMonthJson);
            ps.setString(16, priceQuaterJson);
            ps.setString(17, priceHalfYearJson);
            ps.setString(18, priceYearJson);
            ps.setInt(19, post.getStatus());
            ps.setString(20, post.getShort_des());
            ps.setString(21, post.getDetail_des());
            ps.setString(22, imgJson);
            ps.setString(23, roomsJson);
            ps.setInt(24, post.getCreated_by_staff());
            ps.setInt(25, post.getCreated_by_user());
            ps.setInt(26, post.getType());
            ps.setString(27, post.getOwner());
            ps.setString(28, post.getPhone_number());
            ps.setString(29, post.getEmail());
            ps.setString(30, post.getCompany_name());
            ps.setDate(31, post.getExp_date());
            ps.setInt(32, post.getId_property());
            ps.setInt(33, post.getPublic_price());
            ps.setFloat(34, post.getElectric());
            ps.setFloat(35, post.getWater());
            ps.setInt(36, post.getWater_type());
            ps.setFloat(37, post.getInternet());
            ps.setFloat(38, post.getClean());
            ps.setFloat(39, post.getElevator());
            ps.setFloat(40, post.getOther_service());
            ps.setFloat(41, post.getLongitude());
            ps.setFloat(42, post.getLatitude());
            ps.setInt(43, Integer.valueOf(id_post));

            ps.executeUpdate();
            response.put("message", "Post updated successfully!");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error occurred");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @PutMapping("/post/delete/{id_post}")
    public ResponseEntity<Map<String, String>> deletePost(@RequestBody Post post,
            @PathVariable String id_post) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> response = new HashMap<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root", "123456");

            ps = connection.prepareStatement(
                    "UPDATE post SET `delete` = 1 WHERE id_post = ?");
            ps.setInt(1, Integer.valueOf(id_post));

            ps.executeUpdate();
            response.put("message", "Delete successfully!");

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error occurred");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @GetMapping("/suggest_post_list/{id_tenant}")
    public ResponseEntity<List<Post>> getSuggestList(@PathVariable String id_tenant) {
        List<Post> post_list = new ArrayList<>();
        String query = 
                "SELECT " +
                "    p.*, " +
                "    CASE " +
                "        WHEN `p`.`province` = `s`.`province` " +
                "            AND `p`.`district` = `s`.`district` " +
                "            AND `p`.`ward` = `s`.`ward` " +
                "            AND LOWER(`p`.`detail_address`) LIKE CONCAT('%', LOWER(`s`.`detail_address`), '%') THEN 4 " +
                "        WHEN `p`.`province` = `s`.`province` " +
                "            AND `p`.`district` = `s`.`district` " +
                "            AND `p`.`ward` = `s`.`ward` THEN 3 " +
                "        WHEN `p`.`province` = `s`.`province` " +
                "            AND `p`.`district` = `s`.`district` " +
                "            AND LOWER(`p`.`detail_address`) LIKE CONCAT('%', LOWER(`s`.`detail_address`), '%') THEN 2 " +
                "        WHEN `p`.`province` = `s`.`province` " +
                "            AND `p`.`district` = `s`.`district` THEN 1 " +
                "    END as match_score " +
                "FROM post `p` " +
                "INNER JOIN `suggest_form` s ON s.`id_tenant` = ? " +  // Changed CROSS JOIN to INNER JOIN with condition
                "WHERE `p`.`province` = `s`.`province` " +
                "    AND `p`.`district` = `s`.`district` " +
                "    AND `p`.`status` = 1 " +
                "HAVING match_score IS NOT NULL " +
                "ORDER BY match_score DESC, `id_post` DESC " +
                "LIMIT 20";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester",
                "root", "123456");
                PreparedStatement ps = connection.prepareStatement(query);) {
            ps.setInt(1, Integer.valueOf(id_tenant));
//            System.out.println(ps);

            ObjectMapper objectMapper = new ObjectMapper();
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Post post = new Post();

                    // Basic post details
                    post.setId_post(resultSet.getInt("id_post"));
                    post.setHeader(resultSet.getString("header"));
                    post.setProvince(resultSet.getString("province"));
                    post.setDistrict(resultSet.getString("district"));
                    post.setWard(resultSet.getString("ward"));
                    post.setDetail_address(resultSet.getString("detail_address"));

                    // Legal documents
                    List<Doc> doc_list = parseJsonList(resultSet.getString("legal_doc"), objectMapper, Doc.class);
                    post.setDoc_list(doc_list);

                    // Property details
                    post.setSurface_area(resultSet.getFloat("surface_area"));
                    post.setUseable_area(resultSet.getFloat("useable_area"));
                    post.setWidth(resultSet.getFloat("width"));
                    post.setLength(resultSet.getFloat("length"));
                    post.setFlours(resultSet.getInt("flours"));
                    post.setBedrooms(resultSet.getInt("bedrooms"));
                    post.setToilets(resultSet.getInt("toilets"));

                    // Directions
                    List<Direction> direction_list = parseJsonList(resultSet.getString("direction"), objectMapper, Direction.class);
                    post.setDirection_list(direction_list);

                    // Prices
                    List<Price> price_month = createPriceList(resultSet, objectMapper, "price_month");
                    List<Price> price_quater = createPriceList(resultSet, objectMapper, "price_quater");
                    List<Price> price_half_year = createPriceList(resultSet, objectMapper, "price_half_year");
                    List<Price> price_year = createPriceList(resultSet, objectMapper, "price_year");

                    post.setPrice_month(price_month);
                    post.setPrice_quater(price_quater);
                    post.setPrice_half_year(price_half_year);
                    post.setPrice_year(price_year);

                    // Status and descriptions
                    post.setStatus(resultSet.getInt("status"));
                    post.setShort_des(resultSet.getString("short_description"));
                    post.setDetail_des(resultSet.getString("detail_des"));

                    // Images
                    List<Img> image_list = parseJsonList(resultSet.getString("img"), objectMapper, Img.class);
                    post.setImage_list(image_list);

                    // Rooms
                    List<RoomInPost> room_list = parseJsonList(resultSet.getString("rooms"), objectMapper, RoomInPost.class);
                    post.setRoom_list(room_list);

                    // Creator and ownership details
                    post.setCreated_by_staff(resultSet.getInt("created_by_staff"));
                    post.setCreated_by_user(resultSet.getInt("created_by_user"));
                    post.setType(resultSet.getInt("type"));
                    post.setOwner(resultSet.getString("owner"));
                    post.setPhone_number(resultSet.getString("phone_number"));
                    post.setEmail(resultSet.getString("email"));
                    post.setCompany_name(resultSet.getString("company_name"));

                    // Dates
                    post.setExp_date(resultSet.getDate("exp_date"));
                    post.setCreated_at(resultSet.getDate("created_at"));
                    post.setUpdated_at(resultSet.getDate("updated_at"));

                    // Additional details
                    post.setDelete(resultSet.getInt("delete"));
                    post.setId_property(resultSet.getInt("id_property"));
                    post.setPublic_price(resultSet.getInt("public_price"));
                    post.setElectric(resultSet.getFloat("electric"));
                    post.setWater(resultSet.getFloat("water"));
                    post.setWater_type(resultSet.getInt("water_type"));
                    post.setInternet(resultSet.getFloat("internet"));
                    post.setClean(resultSet.getFloat("clean"));
                    post.setElevator(resultSet.getFloat("elevator"));
                    post.setOther_service(resultSet.getFloat("other_service"));
                    post.setLongitude(resultSet.getFloat("longitude"));
                    post.setLatitude(resultSet.getFloat("latitude"));

                    post_list.add(post);
                }
            }
            return ResponseEntity.ok(post_list);

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }

}