package com.example.implement;

import com.example.middle_group2.LoginActivity;
import com.example.utils.ConnectDatabase;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import entities.User;

public class UserImp {
    private ConnectDatabase connectDatabase = new ConnectDatabase();
    private Connection connection = connectDatabase.connectSQL();

    public User checkUser(String username, String password) {
        User user = null;
        String qrExecute = "SELECT * FROM users where email = ?";
        try {
            PreparedStatement preStatement = connection.prepareStatement(qrExecute);
            preStatement.setString(1, username);
            ResultSet resultSet = preStatement.executeQuery();
            while (resultSet.next()) {
                user = this.parseData(resultSet);
                System.out.println("user2: " + user.getIdUser());
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                Boolean isMatchPass = bCryptPasswordEncoder.matches(password, user.getPassword());
                System.out.println("user2: " + isMatchPass);
                if(!isMatchPass) {
                    user = null;
                }
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println("Error while query data user: " + e);
        }
        return user;
    }

    public Boolean isExistUser(String email, String query) {
        String qrExecute = "SELECT * FROM users where " + query + " = ?";
        Boolean isExist = false;
        try {
            PreparedStatement preStatement = connection.prepareStatement(qrExecute);
            preStatement.setString(1, email);
            ResultSet resultSet = preStatement.executeQuery();
            while (resultSet.next()) {
                LoginActivity.idUserLogin = resultSet.getString("id_user");
                LoginActivity.emailUserLogin = resultSet.getString("email");
                System.out.println("email: " + LoginActivity.emailUserLogin);
                System.out.println("email 2: " + resultSet.getString("email"));
                isExist = true;
            }
        } catch (SQLException e) {
            System.out.println("Error while query data user: " + e);
        }
        return isExist;
    }

    public Boolean isExistUserFB(String email, String idUser) {
        String qrExecute = "SELECT * FROM users where email = ? and id_user = ?";
        Boolean isExist = false;
        try {
            PreparedStatement preStatement = connection.prepareStatement(qrExecute);
            preStatement.setString(1, email);
            preStatement.setString(2, idUser);
            ResultSet resultSet = preStatement.executeQuery();
            isExist = resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error while query data user: " + e);
        }
        return isExist;
    }

    public Boolean save(User user) {
        String query = "INSERT INTO users(id_user, username, password, first_name, last_name, name_file_avatar, def_type, type_login, email)" +
                " VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                ps.setString(1, user.getIdUser());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getFirstName());
                ps.setString(5, user.getLastName());
                ps.setString(6, user.getNameFile());
                ps.setString(7, user.getDefType());
                ps.setString(8, user.getTypeLogin());
                ps.setString(9, user.getEmail());

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private User parseData(ResultSet resultSet) {
        User user = null;
        try {
            String idUser = resultSet.getString("id_user");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            String name_file = resultSet.getString("name_file_avatar");
            String type_login = resultSet.getString("type_login");
            String email = resultSet.getString("email");
            String def_type = resultSet.getString("def_type");
            user = new User(idUser, username, password, first_name, last_name, name_file, def_type, type_login, email);
        } catch (Exception e) {
            System.out.println("Error while parse data user: " + e);
        }

        return user;
    }

}
