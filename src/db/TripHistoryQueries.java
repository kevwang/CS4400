package db;

import javafx.scene.control.Alert;
import models.Breezecard;
import models.Trip;
import models.User;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * For passenger trip history
 *
 * Created by Kevin on 12/2/2017.
 */
public class TripHistoryQueries {

    /**
     * For manage cards controller
     * @return
     */
    public static List<Trip> getTrips(Date start, Date end) {
        try {
            String endString = end == null ? "NOW()" : "'" +
                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(end) + "'";
            String startString = start == null ? "1900-01-01 01:01:01" :new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(start);

            User currentUser = User.getCurrentUser();
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT * FROM Trip\n" +
                "WHERE (\n" +
                "        StartTime\n" +
                "        BETWEEN '" + startString + "'\n" +
                "AND " + endString + "\n" +
                ")\n" +
                "AND BreezecardNum NOT\n" +
                "IN (\n" +
                "        SELECT BreezecardNum\n" +
                "        FROM Conflict\n" +
                ")\n" +
                "AND BreezecardNum\n" +
                "IN (\n" +
                "        SELECT BreezecardNum\n" +
                "        FROM Trip AS T\n" +
                "        WHERE T.BreezecardNUM\n" +
                "        IN (\n" +
                "                SELECT B.BreezecardNum\n" +
                "                FROM Breezecard AS B\n" +
                "                WHERE B.BelongsTo = '" + currentUser.getUsername() + "'\n" +
                "        )\n" +
                "        ORDER BY `StartTime` DESC\n" +
                ");";

            ResultSet rs = stmt.executeQuery(query);

            // Take the resultset and add into breezecard list
            List<Trip> trips = new ArrayList<>();
            while (rs.next()) {
                trips.add(new Trip(
                        rs.getDouble("Tripfare"),
                        rs.getTimestamp("StartTime"),
                        rs.getString("BreezecardNum"),
                        rs.getString("StartsAt"),
                        rs.getString("EndsAt")
                ));
            }

            System.out.println("Query returned " + trips.size() + " trips");
            return trips;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect parameters!");
            alert.showAndWait();
        }

        return null;
    }

}
