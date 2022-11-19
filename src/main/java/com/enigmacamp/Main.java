package com.enigmacamp;

import com.enigmacamp.shared.utils.DBManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DBManager.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}