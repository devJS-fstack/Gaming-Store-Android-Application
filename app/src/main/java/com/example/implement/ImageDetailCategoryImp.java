package com.example.implement;

import com.example.utils.ConnectDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import entities.ImageDetailCategory;

public class ImageDetailCategoryImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();
    public ArrayList<ImageDetailCategory> findAll(String query) {
        ArrayList<ImageDetailCategory> list = new ArrayList<>();
        try {
            String qrExecute = "SELECT * FROM image_detail_category " + query;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(qrExecute);
            while(resultSet.next()){
                ImageDetailCategory item = this.parseData(resultSet);
                list.add(item);
            }
        } catch (Exception ex){
            System.out.println("Error while find all image detail by category");
        }

        return list;
    }

    public ImageDetailCategory parseData(ResultSet resultSet){
        ImageDetailCategory item = null;
        try {
            String id = resultSet.getString("id");
            String nameFile = resultSet.getString("name_file");
            String defType = resultSet.getString("def_type");
            String idCategory = resultSet.getString("id_category");
            String status = resultSet.getString("status");
            item = new ImageDetailCategory(id, nameFile, defType, idCategory, status);
        } catch (Exception ex){
            System.out.println("Error while parse data" + ex);
        }

        return item;
    }
}
