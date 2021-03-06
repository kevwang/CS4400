package db;

import models.Breezecard;
import models.Station;
import models.Trip;
import models.User;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                    String emailQuery = "SELECT Email FROM Passenger WHERE Username = '" + username + "'";
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

    /**
     * Registration for a new user
     * @param username
     * @param email
     * @param password
     * @param breezeNum
     * @return
     */
    public static boolean register(String username, String email, String password, String breezeNum) {
        try {
            // First see if user exists, if so, return false
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT *\n" +
                    "FROM User\n" +
                    "WHERE Username='" + username + "';";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                return false;
            }

            // If user doesnt exist, insert
            query = "INSERT INTO `User` (`Username`, `Password`, `IsAdmin`) VALUES ('" + username + "',  MD5('"
                    + password + "'), '0');";
            stmt.executeUpdate(query);
            query = "INSERT INTO `Passenger` (`Username`, `Email`) VALUES ('" + username + "', '" + email + "');";
            stmt.executeUpdate(query);

            // Set new user instance
            User currentUser = new User(username, false, email);
            User.setCurrentUser(currentUser);
            if (!UserCardManagementQueries.addCard(breezeNum)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    /**
     * For passenger welcome controller
     * @param breezeNum
     * @return
     */
    public static Trip getCurrentTrip(String breezeNum) {
        try {
            // First see if user exists, if so, return false
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT *" +
                    "FROM `Trip`" +
                    "WHERE `BreezecardNum` = " + breezeNum + "\n" +
                    "ORDER BY StartTime DESC" +
                    ";";

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                if (rs.getString("EndsAt") != null) {
                    return null;
                } else {
                    return new Trip(null,
                            null,
                            null,
                            rs.getString("StartsAt"),
                            rs.getString("EndsAt"));
                }
            }

            return null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    /**
     * For passenger welcome controller
     * @param breezeNum
     * @return
     */
    public static boolean startTrip(String breezeNum, String stopId, Double fare) {
        try {
            // First see if user exists, if so, return false
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "INSERT INTO `Trip` (`Tripfare`, `StartTime`, `BreezecardNum`, `StartsAt`, `EndsAt`)" +
                "VALUES ('" + fare + "', NOW(), '"+ breezeNum + "', '" + stopId + "', NULL);";

            int rs = stmt.executeUpdate(query);

            if (rs == 0) {
                return false;
            }

            query = "UPDATE CardValue\n" +
                "SET Value=Value-EnterFare\n" +
                "WHERE EndsAt IS NULL AND BreezecardNum= '" + breezeNum + "';";
            rs = stmt.executeUpdate(query);

            if (rs == 0) {
                return false;
            }


            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    /**
     * For passenger welcome controller
     * @param breezeNum
     * @return
     */
    public static boolean endTrip(String stopId, String breezeNum) {
        try {
            // First see if user exists, if so, return false
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "UPDATE `Trip` SET  `EndsAt` = '" + stopId + "' WHERE\n" +
                "`Trip`.`EndsAt` IS NULL AND `Trip`.`BreezecardNum` ='" + breezeNum + "'";

            int rs = stmt.executeUpdate(query);

            if (rs == 0) { // If nothing updated..
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    /**
     * For welcome screen controller
     * @return
     */
    public static List<Breezecard> getUserBreezecards() {
        try {
            User currentUser = User.getCurrentUser();
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT B.BreezecardNum, B.Value\n" +
                "FROM Breezecard AS B\n" +
                "WHERE BelongsTo = '" + currentUser.getUsername() + "'\n" +
                "AND B.BreezecardNum NOT\n" +
                "IN (\n" +
                "SELECT C.BreezecardNum\n" +
                "FROM Conflict AS C" +
                ");";
            ResultSet rs = stmt.executeQuery(query);

            // Take the resultset and add into breezecard list
            List<Breezecard> cards = new ArrayList<>();
            while (rs.next()) {
                cards.add(new Breezecard(
                        rs.getString("BreezecardNum"),
                        rs.getDouble("Value"),
                        currentUser.getUsername()
                ));
            }

            return cards;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    /**
     * For welcome screen controller
     * @return
     */
    public static List<Station> getStartingStations() {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT StopID, Name, EnterFare, IsTrain\n" +
                "FROM Station\n" +
                "WHERE ClosedStatus =0 ORDER BY Name ASC;";
            ResultSet rs = stmt.executeQuery(query);

            // Take the resultset and add into breezecard list
            List<Station> stations = new ArrayList<>();
            while (rs.next()) {
                stations.add(new Station(
                        rs.getString("StopID"),
                        rs.getString("Name"),
                        rs.getDouble("EnterFare"),
                        false,
                        (rs.getInt("IsTrain") != 0)
                ));
            }

            return stations;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    /**
     * For welcome screen controller
     * @return
     */
    public static List<Station> getEndingStations() {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT StopID,Name,EnterFare, IsTrain\n" +
                "FROM Station\n" +
                "WHERE ClosedStatus=0 AND IsTrain=1";
            ResultSet rs = stmt.executeQuery(query);

            // Take the resultset and add into breezecard list
            List<Station> stations = new ArrayList<>();
            while (rs.next()) {
                stations.add(new Station(
                        rs.getString("StopID"),
                        rs.getString("Name"),
                        rs.getDouble("EnterFare"),
                        false,
                        rs.getInt("IsTrain") != 0
                ));
            }

            return stations;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }
}
