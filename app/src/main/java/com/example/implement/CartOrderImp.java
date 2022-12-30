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

import entities.CartOrder;
import entities.Product;

public class CartOrderImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();

    public ArrayList<CartOrder> findAll(String query) {
        ArrayList <CartOrder> list = new ArrayList<>();
        try {
            String qrExecute = "SELECT * FROM purchase_order " + query;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(qrExecute);
            while(resultSet.next()){
                CartOrder item = this.parseData(resultSet);
                list.add(item);
            }
        } catch (Exception ex){
            System.out.println("Error while find purchase order" + ex);
        }

        return list;
    }

    public ArrayList<HashMap<String, Object>> queryCart(String idUser) {
        ArrayList<HashMap<String, Object>> listHash = new ArrayList<>();
        String query = "select * from cart_orders as c JOIN product as p ON c.id_product = p.id_product AND c.id_user = '" + idUser + "' AND c.status = 'ADDED'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                HashMap<String, Object> map = new HashMap<>();
                CartOrder cartOrder = this.parseData(resultSet);
                Product product = this.parseDataProduct(resultSet);
                map.put("cart", cartOrder);
                map.put("product", product);
                listHash.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listHash;
    }

    public Boolean updateQuantityCart(String prefix, String idCart) {
        try {
            String sql = "update cart_orders SET quantity_cart" + prefix + "=1 where id_cart_order = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, idCart);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error while insert cart order" + ex);
        }
        return false;
    }

    public Boolean updateManyStatusCard(String status, String[] listIdCart) {
        try {
            String sql = "update cart_orders SET status = ? where ";
            int count = 1;
            for (int i = 0; i < listIdCart.length; i++) {
                if (i == 0) {
                    sql += "id_cart_order = '" +  listIdCart[i] + "'";
                } else {
                    sql += " OR id_cart_order = '" +  listIdCart[i] + "'";
                }
            }
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error while insert cart order" + ex);
        }
        return false;
    }

    public Boolean delete(String idCart) {
        try {
            String sql = "Delete cart_orders where id_cart_order = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, idCart);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error while delete cart order" + ex);
        }
        return false;
    }

    public Boolean save(CartOrder cartOrder) {
        try {
            String sql = "INSERT INTO cart_orders(id_cart_order, id_user, id_product, quantity_cart, order_date, status)"
                    + " VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime now = LocalDateTime.now();
                String date = now.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss"));
                ps.setString(1, cartOrder.getIdCartOrder());
                ps.setString(2, cartOrder.getIdUser());
                ps.setString(3, cartOrder.getIdProduct());
                ps.setInt(4, cartOrder.getQuantityCart());
                ps.setString(5, date);
                ps.setString(6, cartOrder.getStatus());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception ex) {
            System.out.println("Error while insert cart order" + ex);
        }
        return false;
    }

    public int count(String idUser) {
        String query = "select COUNT(*) as count from cart_orders WHERE id_user = '" + idUser + "' AND status = 'ADDED'";
        int count = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    private CartOrder parseData(ResultSet resultSet) {
        CartOrder item = null;
        try {
            String idCart = resultSet.getString("id_cart_order");
            String idUser = resultSet.getString("id_user");
            String idProduct = resultSet.getString("id_product");
            int quantity = resultSet.getInt("quantity_cart");
            String dateOrder = resultSet.getString("order_date");
            String status = resultSet.getString("status");
            item = new CartOrder(idCart, idUser, idProduct, quantity, dateOrder, status);
        } catch (Exception ex){
            System.out.println("Error while parse data" + ex);
        }

        return item;
    }

    private Product parseDataProduct(ResultSet resultSet){
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

    public Boolean isExistId(String data, String query) {
        String qrExecute = "SELECT * FROM cart_orders where " + query + " = ?";
        Boolean isExist = false;
        try {
            PreparedStatement preStatement = connection.prepareStatement(qrExecute);
            preStatement.setString(1, data);
            ResultSet resultSet = preStatement.executeQuery();
            isExist = resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error while query data cart_orders: " + e);
        }
        return isExist;
    }
}
