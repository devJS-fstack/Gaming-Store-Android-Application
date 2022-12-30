package com.example.implement;

import com.example.utils.ConnectDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import entities.ImageDetailProduct;


public class DetailImageProductImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();
    public ArrayList<ImageDetailProduct> findAll(String query) {
        ArrayList<ImageDetailProduct> list = new ArrayList<>();
        try {
            String qrExecute = "SELECT * FROM product_image_detail " + query;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(qrExecute);
            while(resultSet.next()){
                ImageDetailProduct item = this.parseData(resultSet);
                list.add(item);
            }
        } catch (Exception ex){
            System.out.println("Error while find all product");
        }

        return list;
    }

    public ImageDetailProduct parseData(ResultSet resultSet){
        ImageDetailProduct item = null;
        try {
            String id = resultSet.getString("id");
            String nameFile = resultSet.getString("name_file");
            String defType = resultSet.getString("def_type");
            String idProduct = resultSet.getString("id_product");
            item = new ImageDetailProduct(id, nameFile, defType, idProduct);
        } catch (Exception ex){
            System.out.println("Error while parse data" + ex);
        }

        return item;
    }
}
