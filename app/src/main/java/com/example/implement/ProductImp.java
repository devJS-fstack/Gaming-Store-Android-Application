package com.example.implement;

import com.example.utils.ConnectDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import entities.Product;

public class ProductImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();
    public ArrayList<Product> findAll(String query) {
        ArrayList<Product> listProduct = new ArrayList<>();
        try {
            String qrExecute = "SELECT * FROM product " + query;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(qrExecute);
            while(resultSet.next()){
                Product product = this.parseData(resultSet);
                listProduct.add(product);
            }
        } catch (Exception ex){
            System.out.println("Error while find all product" + ex);
        }

        return listProduct;
    }

    public Product parseData(ResultSet resultSet){
        Product product = null;
        try {
            String idProduct = resultSet.getString("id_product");
            String nameProduct = resultSet.getString("name_product");
            int currentPrice = resultSet.getInt("current_price");
            int oldPrice = resultSet.getInt("old_price");
            int sale = resultSet.getInt("sale");
            String currency = resultSet.getString("currency");
            String nameMainFile = resultSet.getString("name_file_main");
            String defType = resultSet.getString("def_type");
            String idCategory = resultSet.getString("id_category");
            String desc = resultSet.getString("description");
            product = new Product(idProduct, nameProduct, currentPrice, oldPrice, sale, currency, nameMainFile, defType, idCategory, desc);
        } catch (Exception ex){
            System.out.println("Error while parse data" + ex);
        }

        return product;
    }
}
