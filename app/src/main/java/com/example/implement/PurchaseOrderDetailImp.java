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

import entities.PurchaseOrderDetail;

public class PurchaseOrderDetailImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();
    public ArrayList<PurchaseOrderDetail> findAll(String query) {
        ArrayList<PurchaseOrderDetail> list = new ArrayList<>();
        try {
            String qrExecute = "SELECT * FROM purchase_order_detail " + query;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(qrExecute);
            while(resultSet.next()){
                PurchaseOrderDetail item = this.parseData(resultSet);
                list.add(item);
            }
        } catch (Exception ex){
            System.out.println("Error while find purchase order" + ex);
        }

        return list;
    }

    public int count(String query) {
        int count = 0;
        try {
            String qrExecute = "SELECT Count(*) as count FROM purchase_order_detail " + query;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(qrExecute);
            while(resultSet.next()){
                count = resultSet.getInt("count");
            }
        } catch (Exception ex){
            System.out.println("Error while find purchase order" + ex);
        }

        return count;
    }

    public boolean insertMany(ArrayList<PurchaseOrderDetail> listPODetail) {
        try {
            String sql = "INSERT INTO purchase_order_detail(po_num_detail, po_quantity, total_price, id_product, po_number) VALUES";
            int count = 0;
            for (int i = 0; i < listPODetail.size(); i++) {
                if (i == listPODetail.size() - 1) {
                    sql += "(?,?,?,?,?)";
                } else {
                    sql += "(?,?,?,?,?),";
                }
            }

            PreparedStatement ps = connection.prepareStatement(sql);

            for (PurchaseOrderDetail purchaseOrderDetail: listPODetail) {
                ps.setString(++count, purchaseOrderDetail.getPoNumberDetail());
                ps.setInt(++count, purchaseOrderDetail.getPoQuantity());
                ps.setInt(++count, purchaseOrderDetail.getTotalPrice());
                ps.setString(++count, purchaseOrderDetail.getIdProduct());
                ps.setString(++count, purchaseOrderDetail.getPoNumber());
            }
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error while insert many purchase order detail" + ex);
        }
        return false;
    }

    public Boolean isExistId(String value, String query) {
        String qrExecute = "SELECT * FROM purchase_order_detail where " + query + " = ?";
        Boolean isExist = false;
        try {
            PreparedStatement preStatement = connection.prepareStatement(qrExecute);
            preStatement.setString(1, value);
            ResultSet resultSet = preStatement.executeQuery();
            isExist = resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error while query data purchase_order_detail: " + e);
        }
        return isExist;
    }

    public PurchaseOrderDetail parseData(ResultSet resultSet){
        PurchaseOrderDetail item = null;
        try {
            String poNumDetail = resultSet.getString("po_num_detail");
            int poQuantity = resultSet.getInt("po_quantity");
            String idProduct = resultSet.getString("id_product");
            int totalPrice = resultSet.getInt("total_price");
            String poNum = resultSet.getString("po_number");
            item = new PurchaseOrderDetail(poNumDetail, poQuantity, totalPrice, idProduct, poNum);
        } catch (Exception ex){
            System.out.println("Error while parse data purchase order" + ex);
        }

        return item;
    }
}
