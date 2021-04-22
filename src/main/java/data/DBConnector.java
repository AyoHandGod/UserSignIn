package data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class DBConnector {

    private static DBConnector instance = new DBConnector();
    private String dbURL;
    private String dbUsername;
    private String dbPassword;

    private DBConnector() {

        try(InputStream propertyStream = DBConnector.class.getClassLoader().getResourceAsStream("config.properties")) {
            Class.forName("com.mysql.jdbc.Driver");
            Properties properties = new Properties();
            properties.load(propertyStream);
            dbURL = properties.getProperty("db.url");
            dbPassword = properties.getProperty("db.password");
            dbUsername = properties.getProperty("db.username");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private DBConnector(String dbURL, String dbUsername, String dbPassword) {
        this.dbURL=dbURL;
        this.dbUsername=dbUsername;
        this.dbPassword=dbPassword;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbURL, dbUsername, dbPassword);
    }

    public static DBConnector getInstance() {
        if(instance!=null) {
            return instance;
        }
        instance = new DBConnector();
        return instance;
    }

    public static void main(String[] args) {
        System.out.println("Dontae what are you talking about?");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        System.out.println(response);
    }
}
