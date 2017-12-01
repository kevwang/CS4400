package db;

import java.sql.*;

/**
 * Created by Kevin on 11/30/2017.
 */
public class DatabaseConnection {
    public static Connection conn = null;

    public static void ConnectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_45";
            conn = DriverManager.getConnection(url, "cs4400_Group_45", "IH7JFn2G");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
