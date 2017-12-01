package db;

import models.User;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Kevin on 12/1/2017.
 */
public class DBUserQueries {

    /**
     * Login and set user instance
     * @param username
     * @param password
     * @return
     */
    public static boolean login(String username, String password) {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT IsAdmin FROM User WHERE Username = '" + username + "' AND Password = MD5('" + password + "')";
            ResultSet rs = stmt.executeQuery(query);

            // If there is a result, then success
            if (rs.next()) {
                System.out.println("Found user and pass of " + username);
                Integer isAdminResult = rs.getInt("isAdmin");
                String emailAddress = null;

                // Set an email if it's an admin
                if (isAdminResult == 0) {
                    stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    String emailQuery = "SELECT Email FROM Passenger WHERE Username = 'sandrapatel'";
                    rs = stmt.executeQuery(emailQuery);
                    rs.next(); // There should def be here.. key constraints will support
                    emailAddress = rs.getString("Email");
                }

                // Set new user instance
                User currentUser = new User(username, isAdminResult == 0 ? false : true, emailAddress);
                User.setCurrentUser(currentUser);

                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return false;
    }
}
