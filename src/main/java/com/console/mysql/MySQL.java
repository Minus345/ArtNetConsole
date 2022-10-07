package com.console.mysql;

import java.sql.*;

public class MySQL {

    String host;
    String port;
    String database;
    String username;
    String password;
    Connection con;

    public MySQL(String host, String port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect(){
        if(con == null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println(host + " " + port + " " + database + " " + username + " " + password);
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void disconnect(){
        if(con != null){
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isConnected(){
        return con != null;
    }

    public void executeUpdate(String query){
        if(isConnected()){
            try {
                PreparedStatement ps = con.prepareStatement(query);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ResultSet executeQuery(String query){
        if(isConnected()){
            try {
                PreparedStatement ps = con.prepareStatement(query);
                return ps.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}