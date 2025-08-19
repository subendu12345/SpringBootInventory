package com.prod.GreenValley.config;
import java.sql.*;



public class DataBaseConnection {
    
    private final String jdbcUrl;
    private final String jdbcUser;
    private final String jdbcPassword;

    public DataBaseConnection(String jdbcUrl, String jdbcUser, String jdbcPassword) {
        this.jdbcUrl = jdbcUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPassword = jdbcPassword;
    }

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
    }
}
