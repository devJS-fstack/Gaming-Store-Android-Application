package com.example.implement;

import com.example.utils.ConnectDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Address;

public class AddressImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();

    public ArrayList<Address> findAll(String idUser) {
        ArrayList<Address> list = new ArrayList<>();
        String query = "select * from address where id_user = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                list.add(parseData(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Boolean save(Address address) {
        ArrayList<Address> list = new ArrayList<>();
        String query = "INSERT INTO address(id_address, id_user, phone_number, id_city, city, id_district, district, id_ward, ward, street, is_default, full_name)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, address.getIdAddress());
            preparedStatement.setString(2, address.getIdUser());
            preparedStatement.setString(3, address.getPhone());
            preparedStatement.setInt(4, address.getIdCity());
            preparedStatement.setString(5, address.getCity());
            preparedStatement.setInt(6, address.getIdDistrict());
            preparedStatement.setString(7, address.getDistrict());
            preparedStatement.setInt(8, address.getIdWard());
            preparedStatement.setString(9, address.getWard());
            preparedStatement.setString(10, address.getStreet());
            preparedStatement.setString(11, String.valueOf(address.getDefault()));
            preparedStatement.setString(12, address.getFullName());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

       return false;
    }

    private Address parseData(ResultSet resultSet) {
        Address item = null;
        try {
            String idAddress = resultSet.getString("id_address");
            String idUser = resultSet.getString("id_address");
            String phone = resultSet.getString("phone_number");
            int idCity = resultSet.getInt("id_city");
            String city = resultSet.getString("city");
            int idDistrict = resultSet.getInt("id_district");
            String district = resultSet.getString("district");
            int idWard = resultSet.getInt("id_ward");
            String ward = resultSet.getString("ward");
            Boolean isDefault = resultSet.getString("is_default").equals("true");
            String street = resultSet.getString("street");
            String fullName = resultSet.getString("full_name");
            item = new Address(idAddress, idUser, phone, idCity, city, idDistrict, district, idWard, ward, isDefault, street, fullName);
        } catch (Exception e) {
            System.out.println("Error while parse data address: " + e);
        }

        return item;
    }

    public Boolean isExistId(String data, String query) {
        String qrExecute = "SELECT * FROM address where " + query + " = ?";
        Boolean isExist = false;
        try {
            PreparedStatement preStatement = connection.prepareStatement(qrExecute);
            preStatement.setString(1, data);
            ResultSet resultSet = preStatement.executeQuery();
            isExist = resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error while query data address: " + e);
        }
        return isExist;
    }
}
