package edu.psu.sender.db;

import edu.psu.sender.PropertyReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDB {

    // private static final String url = "jdbc:sqlite:perm_city_polyclinic_7_registry.db";
    private static final String url = PropertyReader.LoadProperty("sqlite.jdbc");

    private static SQLiteDB instance;
    private Connection connection;

    private SQLiteDB(Connection connection) {
        this.connection = connection;
    }

    public static SQLiteDB getInstance() {
        if (instance == null) {
            try {
                DriverManager.registerDriver(new org.sqlite.JDBC());
                Connection connection = DriverManager.getConnection(url);
                instance = new SQLiteDB(connection);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
