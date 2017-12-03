package db;

import models.Station;
import models.Trip;
import models.User;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 12/2/2017.
 */
public class StationManagementQueries {

    /**
     * For manage cards controller
     * @return
     */
    public static List<Station> getStations() {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT `Name` AS `Station Name` , `StopID` AS `Stop ID` , `EnterFare` AS `Fare`, (\n" +
                    "\n" +
                    "SELECT CASE WHEN ClosedStatus <>0\n" +
                    "THEN \"Closed\"\n" +
                    "ELSE \"Open\"\n" +
                    "END\n" +
                    ") AS 'Status'\n" +
                    "FROM `Station`;";

            ResultSet rs = stmt.executeQuery(query);

            // Take the resultset and add into breezecard list
            List<Station> stations = new ArrayList<>();
            while (rs.next()) {
                stations.add(new Station(
                        rs.getString("Stop ID"),
                        rs.getString("Station Name"),
                        rs.getDouble("Fare"),
                        rs.getString("Status").equals("Closed"),
                        false
                ));
            }

            System.out.println("Query returned " + stations.size() + " trips");
            return stations;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    /**
     * For Create new station controller
     * @return
     */
    public static boolean createNewStation(
            String stationName, String stopId, String fare, String intersection, Boolean isBusStation, Boolean openStation) {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "INSERT IGNORE INTO  `Station` (" +
                "`StopID` ," +
                "`Name` ," +
                "`EnterFare` ," +
                "`ClosedStatus` ," +
                "`IsTrain`" +
                ")" +
                "VALUES (" +
                "        'N64',  'tested',  '13.64',  '1',  '1'" +
                ");";

            int result = stmt.executeUpdate(query);
            System.out.println("Create station added " + result + " rows");

            // If no result, return false
            if (result == 0) {
                return false;
            }

            query = "INSERT INTO  `BusStationIntersection`(\n" +
                "`StopID` ,\n" +
                "`Intersection`\n" +
                ")\n" +
                "VALUES (\n" +
                "        'N64',  '36 St'\n" +
                ")\n";

            result = stmt.executeUpdate(query);
            System.out.println("Intersection added " + result + " rows");
            if (result == 0) {
                return false; // Again, if nothing added here
            }

            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    /**
     * For Create new station controller
     * @return
     */
    public static Station viewStationDetail(String stopId) {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT  `Name` , Station.StopID,  `EnterFare` ,  `ClosedStatus` , BusStationIntersection.Intersection\n" +
                "FROM Station\n" +
                "LEFT JOIN BusStationIntersection ON Station.StopID = BusStationIntersection.StopID\n" +
                "WHERE Station.StopID =  '"+ stopId + "'";

            ResultSet rs = stmt.executeQuery(query);

            // If no result, return false
            if (rs.next()) {
                System.out.println("Station fetched !");
                return new Station(
                        rs.getString("StopID"),
                        rs.getString("Name"),
                        rs.getDouble("EnterFare"),
                        rs.getInt("ClosedStatus") == 0,
                        false,
                        rs.getString("Intersection")
                );
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    /**
     * For admin station detail controller
     */
    public static boolean setFare(String stopId, Double value) {
        try {
            // First see if user exists, if so, return false
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "UPDATE `cs4400_Group_45`.`Station` SET `EnterFare` = " + value + " WHERE" +
                "CONVERT( `Station`.`StopID` USING utf8 ) = " + stopId + " LIMIT 1 ;";

            int rs = stmt.executeUpdate(query);

            if (rs == 0) {
                return false;
            }

            System.out.println(stopId + " successfully updated");
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    /**
     * For admin station detail controller
     */
    public static boolean setClosedStatus(String stopId, boolean closedStatus) {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String closedStr = closedStatus ? "1" : "0";
            String query = "UPDATE  `cs4400_Group_45`.`Station` SET  `ClosedStatus` = " + closedStr + " WHERE" +
                "CONVERT(  `Station`.`StopID` USING utf8 ) = " + stopId + " LIMIT 1 ;";

            int rs = stmt.executeUpdate(query);

            if (rs == 0) {
                return false;
            }

            System.out.println(stopId + " successfully updated");
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return false;
    }
}
