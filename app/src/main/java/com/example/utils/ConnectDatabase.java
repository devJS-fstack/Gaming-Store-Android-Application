package com.example.utils;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDatabase {
    private String username, password, ip, port, database;

    public Connection connectSQL() {
        ip = "192.168.2.26";
        database = "android_project";
        username = "sa";
        password = "215531622";
        port = "1433";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection cnn = null;
        String connectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database +
                    ";user=" + username + ";password=" + password;
            Log.d("Connection URL", connectionURL);
            cnn = DriverManager.getConnection(connectionURL);
            if(cnn != null){
                System.out.println("Connect database successfully");
            }
        } catch(Exception ex) {
            Log.d("Error connect database", ex.toString());
        }
        return cnn;
    }
}
