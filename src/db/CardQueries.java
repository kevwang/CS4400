package db;

import javafx.scene.control.Alert;
import models.Breezecard;
import models.Station;
import models.StationFlow;
import models.SuspendedCard;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kevin on 12/2/2017.
 */
public class CardQueries {

    /**
     * For admin card management controller
     * @param breezeNum
     * @return
     */
    public static boolean setValue(String breezeNum, Double value) {
        try {
            // First see if user exists, if so, return false
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "UPDATE `cs4400_Group_45`.`Breezecard` SET `Value` = " + value + " WHERE\n" +
                "CONVERT( `Breezecard`.`BreezecardNum`USING utf8 ) = '" + breezeNum + "' LIMIT 1 ;";

            int rs = stmt.executeUpdate(query);

            if (rs == 0) {
                return false;
            }

            System.out.println(breezeNum + " successfully updated");
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    /**
     * For admin card management controller and suspendedcard
     * @param breezeNum
     * @return
     */
    public static boolean transferCard(String breezeNum, String newOwner) {
        try {
            // First see if card exists, if so, return false
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT `EndsAt`" +
                "FROM `Trip`" +
                "WHERE `BreezecardNum` = " + breezeNum + ";";

            ResultSet rs = stmt.executeQuery(query);

            // If card is in trip, there is problem, else transfer
            if (!rs.next() || rs.getString("EndsAt") != null) {
                query = "UPDATE `cs4400_Group_45`.`Breezecard` SET `BelongsTo` = '" + newOwner + "' WHERE\n" +
                    "CONVERT( `Breezecard`.`BreezecardNum`USING utf8 ) = '" + breezeNum + "' LIMIT 1 ;";
                int r = stmt.executeUpdate(query);

                if (r == 0) {
                    System.out.println("There was a problem transferring the card");
                    return false;
                }
                // Also delete from conflict table if it's there
                query = "DELETE FROM `cs4400_Group_45`.`Conflict`\n" +
                        "WHERE `BreezecardNum` = '" + breezeNum + "';";
                stmt.executeUpdate(query);
            } else {
                System.out.println("Card is currently in a trip");
                return false;
            }

            System.out.println(breezeNum + " successfully transfered");
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
    public static List<SuspendedCard> getSuspendedCards() {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query =
                "SELECT `BreezecardNum` AS `Card #` , `Username` AS `New Owner` , `DateTime` AS`Date Suspended` , (\n" +
                "SELECT `BelongsTo`\n" +
                "FROM `Breezecard`\n" +
                "WHERE Conflict.BreezecardNum = Breezecard.BreezecardNum\n" +
                ") AS `Previous Owner`\n" +
                "FROM `Conflict` ;";

            ResultSet rs = stmt.executeQuery(query);

            // If no result, return false
            List<SuspendedCard> cards = new ArrayList<>();
            while (rs.next()) {
                cards.add(new SuspendedCard(
                        rs.getString("Card #"),
                        rs.getString("Previous Owner"),
                        rs.getString("New Owner"),
                        rs.getTimestamp("Date Suspended")
                ));
            }

            System.out.println("Query returned " + cards.size() + "suspended cards");
            return cards;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    /**
     * For Flow report controller
     * @return
     */
    public static List<StationFlow> getFlowReport(Date start, Date end) {
        try {
            String endString = end == null ? "NOW()" : "'" +
                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(end) + "'";
            String startString = start == null ? "1900-01-01 01:01:01" : new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(start);

            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query =
                    "SELECT Name AS  'Station Name', COALESCE( Cin, 0 ) AS  '# Passengers In', COALESCE( Cout, 0 ) AS  '# Passengers Out', \n" +
                    "COALESCE( Cin, 0 ) - COALESCE( Cout, 0 ) AS Flow, COALESCE( Revenue, 0 ) AS Revenue\n" +
                    "FROM PassengerFlow\n" +
                    "LEFT JOIN Station ON PassengerFlow.StationName = Station.StopID\n" +
                    "ORDER BY COALESCE( Cin, 0 ) DESC";
            /*query =
                "DROP VIEW CusIn;\n" +
                "CREATE VIEW `CusIn` AS SELECT StartsAt, COUNT( StartsAt ) AS Cin\n" +
                "FROM Trip\n" +
                "WHERE StartTime\n" +
                "BETWEEN  '2017-10-31 22:31:10'\n" +
                "AND NOW( )\n" +
                "GROUP BY StartsAt ;\n" +

                "DROP VIEW CusOut;\n" +
                "CREATE VIEW `CusOut`\n" +
                "AS SELECT EndsAt, COUNT( EndsAt ) AS Cout\n" +
                "FROM Trip\n" +
                "WHERE StartTime\n" +
                "BETWEEN '2017-10-31 22:31:10'\n" +
                "AND NOW( )\n" +
                "GROUP BY EndsAt;\n" +

                "DROP VIEW Revenue;\n" +

                "CREATE VIEW `Revenue`\n" +
                "AS SELECT StartsAt, SUM( TripFare ) AS Revenue\n" +
                "FROM Trip\n" +
                "WHERE StartTime\n" +
                "BETWEEN '2017-10-31 22:31:10'\n" +
                "AND NOW( )\n" +
                "GROUP BY StartsAt;\n" +

                "DROP VIEW StationFilter;\n" +
                "CREATE VIEW `StationFilter` AS SELECT StartsAt\n" +
                "FROM Trip\n" +
                "WHERE StartTime\n" +
                "BETWEEN '2017-10-31 22:31:10'\n" +
                "AND NOW( )\n" +
                "UNION\n" +
                "SELECT EndsAt\n" +
                "FROM Trip\n" +
                "WHERE StartTime\n" +
                "BETWEEN '2017-10-31 22:31:10'\n" +
                "AND NOW( ) \n;" +

                "DROP VIEW PassengerFlow;\n" +
                "CREATE VIEW `PassengerFlow` AS SELECT StationFilter.StartsAt, Cin, Cout, Cin - Cout AS Flow, Revenue\n" +
                "FROM StationFilter\n" +
                "LEFT JOIN CusIn ON StationFilter.StartsAt = CusIn.StartsAt\n" +
                "LEFT JOIN CusOut ON StationFilter.StartsAt = CusOut.EndsAt\n" +
                "LEFT JOIN Revenue ON StationFilter.StartsAt = Revenue.StartsAt;\n";
            stmt.executeUpdate(query);*/

             query = "SELECT StartsAt AS 'Station Name', COALESCE( Cin, 0 ) AS '# Passengers In', COALESCE( Cout, 0 ) AS '# Passengers Out', \n" +
                     "COALESCE( Cin, 0 ) - COALESCE( Cout, 0 ) AS Flow, COALESCE( Revenue, 0 ) AS Revenue\n" +
                "FROM (\n" +
                "SELECT DISTINCT (\n" +
                "PF.StartsAt\n" +
                "), Cin, Cout, Flow, Revenue\n" +
                "FROM PassengerFlow AS PF\n" +
                "RIGHT JOIN Start AS S ON PF.StartsAt = S.StartsAt\n" +
                "WHERE StartTime\n" +
                "BETWEEN '" + startString + "'\n" +
                "AND " + endString + "\n" +
                ") AS PF2\n" +
                "LEFT JOIN Station ON PF2.StartsAt = Station.StopID\n" +
                "ORDER BY StartsAt ASC\n";


            ResultSet rs = stmt.executeQuery(query);

            // If no result, return false
            List<StationFlow> flows = new ArrayList<>();
            while (rs.next()) {
                flows.add(new StationFlow(
                        rs.getString("Station Name"),
                        rs.getInt("# Passengers In"),
                        rs.getInt("# Passengers Out"),
                        rs.getInt("Flow"),
                        rs.getDouble("Revenue")
                ));
            }

            System.out.println("Query returned " + flows.size() + "station flows");
            return flows;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    /**
     * For Create new station controller
     * @return
     */
    public static List<Breezecard> getBreezecards(
            String cardOwner, String cardNum, Double lowValue, Double highValue, Boolean inclSuspendedCards) {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder sb = new StringBuilder(
                    "SELECT `BreezecardNum` AS `Card #` , `Value` AS `Value` , `BelongsTo` AS `Owner`\n" +
                    "FROM `Breezecard`\n");

            // Append to string based on inputs
            if (highValue != null) {
                sb.append("WHERE `Value` <= '" + highValue + "'\n");
            } else {
                sb.append("WHERE `Value` <= '10000'\n");
            }
            if (lowValue != null) {
                sb.append("AND `Value` >= '" + lowValue + "'\n");
            } else {
                sb.append("AND `Value` >= '-1000'\n");
            }
            if (cardNum != null) { sb.append("AND `BreezecardNum` = '" + cardNum + "'\n"); }
            if (cardOwner != null) { sb.append("AND `BelongsTo` = '" + cardOwner + "'\n"); }
            if (inclSuspendedCards) {
                sb.append(
                        "AND NOT\n" +
                        "EXISTS (\n" +
                        "SELECT `BreezecardNum`\n" +
                        "FROM `Conflict`\n" +
                        "WHERE Conflict.BreezecardNum = Breezecard.BreezecardNum\n" +
                        ");");
            }
            String query = sb.toString();

            ResultSet rs = stmt.executeQuery(query);

            // If no result, return false
            List<Breezecard> cards = new ArrayList<>();
            while (rs.next()) {
                cards.add(new Breezecard(
                        rs.getString("Card #"),
                        rs.getDouble("Value"),
                        rs.getString("Owner")
                ));
            }

            System.out.println("Query returned " + cards.size() + " breezecards");
            return cards;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }
}
