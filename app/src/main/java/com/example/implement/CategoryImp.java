package com.example.implement;

import com.example.utils.ConnectDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import entities.Category;


public class CategoryImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();
    public ArrayList<Category> findAll() {
        ArrayList<Category> listCategory = new ArrayList<>();
        try {
            String query = "SELECT * FROM category";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                String idCategory = resultSet.getString("id_category");
                String nameCategory = resultSet.getString("name_category");
                String nameFile = resultSet.getString("name_file");
                String defType = resultSet.getString("def_type");
                Category category = new Category(idCategory, nameCategory, nameFile, defType);
                listCategory.add(category);
            }
        } catch (Exception ex){
            System.out.println("Error while find all product" + ex);
        }

        return listCategory;
    }
}
