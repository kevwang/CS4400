package db;

import models.Breezecard;
import models.User;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
    public static List<Breezecard> getCards() {
        try {
            User currentUser = User.getCurrentUser();
            Statement stmt = DatabaseConnection.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT  `BreezecardNum` AS  `Card Number` ,  `Value`\n" +
                    "FROM  `Breezecard`\n" +
                    "WHERE  `BelongsTo` =  '" + currentUser.getUsername() + "';";

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
