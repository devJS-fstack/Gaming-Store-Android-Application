package com.example.implement;

import com.example.utils.ConnectDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Product;
import entities.ReviewProduct;


public class ReviewProductImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();
    public ArrayList<ReviewProduct> findAll(String query) {
        ArrayList<ReviewProduct> list = new ArrayList<>();
        try {
            String qrExecute = "SELECT * FROM product_reviews " + query;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(qrExecute);
            while(resultSet.next()){
                ReviewProduct item = this.parseData(resultSet);
                list.add(item);
            }
        } catch (Exception ex){
            System.out.println("Error while find all product");
        }

        return list;
    }

    public ArrayList<HashMap<String, Object>> findManyType(String query) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        try {
            String qrExecute = "SELECT * FROM product_reviews " + query;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(qrExecute);
            while(resultSet.next()){
                HashMap<String, Object> map = new HashMap<>();
                ReviewProduct reviewProduct = parseData(resultSet);
                map.put("review", reviewProduct);
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String nameFile = resultSet.getString("name_file_avatar");
                String defType = resultSet.getString("def_type");
                map.put("firstName", firstName);
                map.put("lastName", lastName);
                map.put("nameFile", nameFile);
                map.put("defType", defType);
                list.add(map);
            }
        } catch (Exception ex){
            System.out.println("Error while find review product" + ex);
        }

        return list;
    }

    public Boolean save(ReviewProduct review) {
        String query = "INSERT INTO product_reviews(id_review_product, id_user, description, star_number, id_product, date)" +
                " VALUES(?,?,?,?,?,?)";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime now = LocalDateTime.now();
                String date = now.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss"));
                ps.setString(1, review.getIdReviewProduct());
                ps.setString(2, review.getIdUser());
                ps.setString(3, review.getDescription());
                ps.setInt(4, review.getStarNumber());
                ps.setString(5, review.getIdProduct());
                ps.setString(6, date);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            System.out.println("Error while insert review product: " + e);
        }

        return false;
    }

    public ReviewProduct parseData(ResultSet resultSet){
        ReviewProduct item = null;
        try {
            String idReviewProduct = resultSet.getString("id_review_product");
            String idUser = resultSet.getString("id_user");
            String description = resultSet.getString("description");
            int starNumber = resultSet.getInt("star_number");
            String idProduct = resultSet.getString("id_product");
            String date = resultSet.getString("date");
            item = new ReviewProduct(idReviewProduct, idUser, description, starNumber, idProduct, date);
        } catch (Exception ex){
            System.out.println("Error while parse data" + ex);
        }

        return item;
    }

    public Boolean isExistId(String data, String query) {
        String qrExecute = "SELECT * FROM product_reviews where " + query + " = ?";
        Boolean isExist = false;
        try {
            PreparedStatement preStatement = connection.prepareStatement(qrExecute);
            preStatement.setString(1, data);
            ResultSet resultSet = preStatement.executeQuery();
            isExist = resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error while query data product_reviews: " + e);
        }
        return isExist;
    }
}
