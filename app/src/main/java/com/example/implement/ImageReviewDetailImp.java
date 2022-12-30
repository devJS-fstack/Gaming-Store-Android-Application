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

import entities.ImageReviewDetail;
import entities.PurchaseOrderDetail;


public class ImageReviewDetailImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();
    public ArrayList<ImageReviewDetail> findAll(String query) {
        ArrayList<ImageReviewDetail> list = new ArrayList<>();
        try {
            String qrExecute = "SELECT * FROM image_review_detail " + query;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(qrExecute);
            while(resultSet.next()){
                ImageReviewDetail item = this.parseData(resultSet);
                list.add(item);
            }
        } catch (Exception ex){
            System.out.println("Error while find image review detail" + ex);
        }

        return list;
    }

    public Boolean insertMany(ArrayList<ImageReviewDetail> listImgReview) {
        String query = "INSERT INTO image_review_detail(id, id_review_product, name_file, def_type)" +
                " VALUES";
        int count = 0;
        for (int i = 0; i < listImgReview.size(); i++) {
            if (i == listImgReview.size() - 1) {
                query += "(?,?,?,?)";
            } else {
                query += "(?,?,?,?),";
            }
        }

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            for (ImageReviewDetail imgReview: listImgReview) {
                ps.setString(++count, imgReview.getId());
                ps.setString(++count, imgReview.getIdReviewProduct());
                ps.setString(++count, imgReview.getNameFile());
                ps.setString(++count, imgReview.getDefType());
            }

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error while insert image review product: " + e);
        }

        return false;
    }

    public ImageReviewDetail parseData(ResultSet resultSet){
        ImageReviewDetail item = null;
        try {
            String id = resultSet.getString("id");
            String idReviewProduct = resultSet.getString("id_review_product");
            String nameFile = resultSet.getString("name_file");
            String defType = resultSet.getString("def_type");
            item = new ImageReviewDetail(id, idReviewProduct, nameFile, defType);
        } catch (Exception ex){
            System.out.println("Error while parse data" + ex);
        }

        return item;
    }

    public Boolean isExistId(String data, String query) {
        String qrExecute = "SELECT * FROM image_review_detail where " + query + " = ?";
        Boolean isExist = false;
        try {
            PreparedStatement preStatement = connection.prepareStatement(qrExecute);
            preStatement.setString(1, data);
            ResultSet resultSet = preStatement.executeQuery();
            isExist = resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error while query image_review_detail: " + e);
        }
        return isExist;
    }
}
