package edu.psu.receiver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDB {

    private static final String url = "jdbc:postgresql://127.0.0.1:5432/perm_city_polyclinic_7_registry";
    private static final String user = "postgres";
    private static final String password = "postgres";

    private static PostgresDB instance;
    private Connection connection;

    private PostgresDB(Connection connection) {
        this.connection = connection;
    }

    public static PostgresDB getInstance() {
        if (instance == null) {
            try {
                DriverManager.registerDriver(new org.postgresql.Driver());
                Connection connection = DriverManager.getConnection(url, user, password);
                instance = new PostgresDB(connection);
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
