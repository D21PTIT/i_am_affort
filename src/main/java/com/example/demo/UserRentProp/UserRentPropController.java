package com.example.demo.UserRentProp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.UserRentProp;


@RestController
@CrossOrigin
public class UserRentPropController {
	@GetMapping("/user_rent_prop_list/{id_tenant}")
	public ResponseEntity<List<UserRentProp>> getRentList(Model model, @PathVariable String id_tenant)
			throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PreparedStatement ps = null;
		List<UserRentProp> rent_list = new ArrayList<UserRentProp>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tester", "root",
					"123456");
			statement = connection.createStatement();
			ps = connection.prepareStatement("SELECT * FROM `tester`.`user_rent_prop` WHERE `delete` = 0 AND `id_tenant` = ?");
			ps.setInt(1, Integer.valueOf(id_tenant));

			resultSet = ps.executeQuery();

			while (resultSet.next()) {

				int id_user_rent_prop = resultSet.getInt("id_user_rent_prop");
				int id_tenant1 = resultSet.getInt("id_tenant");
				int id_property = resultSet.getInt("id_property");
				int id_user = resultSet.getInt("id_user");
				String username = resultSet.getString("username");
				String tenant_name = resultSet.getString("tenant_name");
				int id_room = resultSet.getInt("id_room");
				Date start_date = resultSet.getDate("start_date");
				Date end_date = resultSet.getDate("end_date");
				int status = resultSet.getInt("status");
				float price = resultSet.getFloat("price");
				int price_type = resultSet.getInt("price_type");
				int rent_type = resultSet.getInt("rent_type");
				Date created_at = resultSet.getDate("created_at");
				Date updated_at = resultSet.getDate("updated_at");
								

				rent_list.add(new UserRentProp(id_user_rent_prop, id_tenant1, id_property, id_user, username,
						tenant_name, id_room, start_date, end_date, status, price, price_type,
						rent_type, updated_at, created_at));
			}
			return new ResponseEntity<>(rent_list, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
