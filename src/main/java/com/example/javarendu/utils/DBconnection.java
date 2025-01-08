package com.example.javarendu.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/RenduDB", "postgres", "anass123");
    }
}
