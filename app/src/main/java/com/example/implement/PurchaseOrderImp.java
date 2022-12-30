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

import entities.PurchaseOrder;

public class PurchaseOrderImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();
    public ArrayList<PurchaseOrder> findAll(String query) {
        ArrayList<PurchaseOrder> list = new ArrayList<>();
        try {
            String qrExecute = "SELECT * FROM purchase_order " + query;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(qrExecute);
            while(resultSet.next()){
                PurchaseOrder item = this.parseData(resultSet);
                list.add(item);
            }
        } catch (Exception ex){
            System.out.println("Error while find purchase order" + ex);
        }

        return list;
    }

    public boolean save(PurchaseOrder purchaseOrder) {
        try {
            String sql = "INSERT INTO purchase_order(purchase_order_number, id_user, status, payment, order_date, id_address)"
                    + " VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime now = LocalDateTime.now();
                String date = now.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss"));
                ps.setString(1, purchaseOrder.getPurchaseOrderNumber());
                ps.setString(2, purchaseOrder.getIdUser());
                ps.setString(3, purchaseOrder.getStatus());
                ps.setInt(4, purchaseOrder.getPayment());
                ps.setString(5, date);
                ps.setString(6, purchaseOrder.getIdAddress());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception ex) {
            System.out.println("Error while insert purchase order" + ex);
        }
        return false;
    }

    public Boolean isExistId(String value, String query) {
        String qrExecute = "SELECT * FROM purchase_order where " + query + " = ?";
        Boolean isExist = false;
        try {
            PreparedStatement preStatement = connection.prepareStatement(qrExecute);
            preStatement.setString(1, value);
            ResultSet resultSet = preStatement.executeQuery();
            isExist = resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error while query data purchase_order: " + e);
        }
        return isExist;
    }

    public PurchaseOrder parseData(ResultSet resultSet){
        PurchaseOrder item = null;
        try {
            String purchaseOrderNumber = resultSet.getString("purchase_order_number");
            String idUser = resultSet.getString("id_user");
            String status = resultSet.getString("status");
            int payment = resultSet.getInt("payment");
            String orderDate = resultSet.getString("order_date");
            String idAddress = resultSet.getString("id_address");
            item = new PurchaseOrder(purchaseOrderNumber, idUser, status, payment, orderDate, idAddress);
        } catch (Exception ex){
            System.out.println("Error while parse data purchase order" + ex);
        }

        return item;
    }
}
