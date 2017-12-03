package db;

import models.Breezecard;
import models.Station;
import models.StationFlow;
import models.SuspendedCard;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
            String query = "UPDATE `cs4400_Group_45`.`Breezecard` SET `Value` = " + value + " WHERE" +
                "CONVERT( `Breezecard`.`BreezecardNum`USING utf8 ) = " + breezeNum + " LIMIT 1 ;";

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
     * For admin card management controller
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


            int rs = stmt.executeUpdate(query);

            // If card is in trip, there is problem, else transfer
            if (rs == 0) {
                query = "UPDATE `cs4400_Group_45`.`Breezecard` SET `BelongsTo` = " + newOwner + " WHERE" +
                    "CONVERT( `Breezecard`.`BreezecardNum`USING utf8 ) = " + breezeNum + " LIMIT 1 ;";
                rs = stmt.executeUpdate(query);

                if (rs == 0) {
                    System.out.println("There was a problem transferring the card");
                    return false;
                }
            } else {
                System.out.println("There was a problem transfering the card");
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
    public static List<StationFlow> getFlowReport() {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query =
                    "SELECT Name AS  'Station Name', COALESCE( Cin, 0 ) AS  '# Passengers In', COALESCE( Cout, 0 ) AS  '# Passengers Out', \n" +
                    "COALESCE( Cin, 0 ) - COALESCE( Cout, 0 ) AS Flow, COALESCE( Revenue, 0 ) AS Revenue\n" +
                    "FROM PassengerFlow\n" +
                    "LEFT JOIN Station ON PassengerFlow.StationName = Station.StopID\n" +
                    "ORDER BY COALESCE( Cin, 0 ) DESC";

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
            if (cardNum != null) { sb.append("AND `BreezecardNum` = '2792083965359460'\n"); }
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
