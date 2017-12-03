package db;

import models.Breezecard;
import models.Station;
import models.User;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 12/2/2017.
 */
public class UserCardManagementQueries {

    /**
     * For passenger welcome controller
     * @param breezeNum
     * @return
     */
    public static boolean addCard(String breezeNum) {
        try {
            User currentUser = User.getCurrentUser();
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT `BelongsTo`" +
                "FROM `Breezecard`" +
                "WHERE `BreezecardNum` = '" + breezeNum + "';";

            ResultSet rs = stmt.executeQuery(query);

            // Two cases: card found and suspend, none found and insert
            if (rs.next()) {
                // If card name is not null, suspend, else assign to new user
                if (rs.getString("BelongsTo") != null) {
                    System.out.println("Breezecard found, suspending card..");
                    query = "INSERT INTO `Conflict` (`Username`, `BreezecardNum`, DateTime)" +
                            "VALUES ('" + currentUser.getUsername() + "', '" + breezeNum + "', Now())";
                    int susInt = stmt.executeUpdate(query);

                    if (susInt == 0) {
                        System.out.println("There was a problem inserting the suspended card");
                        return false;
                    } else {
                        System.out.println("Suspended card inserted!");
                    }
                } else {
                    System.out.println("Breezecard found with no owner, updating");
                    query = "UPDATE `cs4400_Group_45`.`Breezecard` SET `BelongsTo` ='" + currentUser.getUsername() + "' WHERE" +
                        "CONVERT( `Breezecard`.`BreezecardNum`USING utf8 ) = '" + breezeNum + "' LIMIT 1 ;";
                    int susInt = stmt.executeUpdate(query);

                    if (susInt == 0) {
                        System.out.println("There was a problem updating the card owner");
                        return false;
                    } else {
                        System.out.println("Card updated!");
                    }
                }

            // None found, insert breezecard
            } else {
                query = "INSERT INTO `Breezecard` (`BreezecardNum`, `Value`, `BelongsTo`)" +
                    "VALUES ('" + breezeNum + "', 0, '" + currentUser.getUsername() + "')";
                int insInt = stmt.executeUpdate(query);

                if (insInt == 0) {
                    System.out.println("There was a problem inserting the new card");
                    return false;
                } else {
                    System.out.println("New card inserted!");
                }
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
    public static boolean removeCard(String breezeNum) {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "UPDATE `cs4400_Group_45`.`Breezecard` SET `BelongsTo` = NULL WHERE\n" +
                "CONVERT( `Breezecard`.`BreezecardNum`USING utf8 ) = '" + breezeNum + "' LIMIT 1 ;";

            int rs = stmt.executeUpdate(query);

            if (rs == 0) {
                return false;
            }

            // Also delete from conflict table if it's there
            query = "DELETE FROM `cs4400_Group_45`.`Conflict`\n" +
                    "WHERE `BreezecardNum` = '" + breezeNum + "';";
            stmt.executeUpdate(query);

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
    public static boolean addValue(String breezeNum, Double value) {
        try {
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "UPDATE `cs4400_Group_45`.`Breezecard` SET `Value` = (`Value`+" + value + ")" +
                "WHERE CONVERT( `Breezecard`.`BreezecardNum`USING utf8 ) = '" + breezeNum + "' LIMIT 1 ;";

            int rs = stmt.executeUpdate(query);

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
     * For manage cards controller
     * @return
     */
    public static List<Breezecard> getCards() {
        try {
            User currentUser = User.getCurrentUser();
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT  `BreezecardNum` AS  `Card Number` ,  `Value` \n" +
                "FROM  `Breezecard`\n" +
                "WHERE  `BelongsTo` = '" + currentUser.getUsername() + "';";

            ResultSet rs = stmt.executeQuery(query);

            // Take the resultset and add into breezecard list
            List<Breezecard> cards = new ArrayList<>();
            while (rs.next()) {
                cards.add(new Breezecard(
                        rs.getString("Card Number"),
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
}
