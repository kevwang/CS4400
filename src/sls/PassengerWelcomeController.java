package sls;

import db.DBUserQueries;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import models.Breezecard;
import models.Station;

import java.io.IOException;
import java.util.List;

public class PassengerWelcomeController {
    @FXML
    private ComboBox<Breezecard> breezeCards;

    @FXML
    private Text cardBalance;

    @FXML
    private ComboBox startsAt;

    @FXML
    private ComboBox endsAt;

    @FXML
    private Hyperlink startTrip;

    @FXML
    private Hyperlink manageCards;

    @FXML
    private Hyperlink endTrip;

    private List<Breezecard> breezecardList;
    private List<Station> startStationList;
    private List<Station> endStationList;

    @FXML
    private void initialize() {
        breezecardList = DBUserQueries.getUserBreezecards();
        startStationList = DBUserQueries.getStartingStations();
        endStationList = DBUserQueries.getEndingStations();

        // Populate lists from query
        if (breezecardList != null) {
            for (Breezecard bc : breezecardList) {
                breezeCards.getItems().add(bc);
            }
        }

        if (startStationList != null) {
            for (Station s : startStationList) {
                startsAt.getItems().add(s.getName());
            }
        }

        if (endStationList != null) {
            for (Station s : endStationList) {
                endsAt.getItems().add(s.getName());
            }
        }

        // Default selection
        breezeCards.getSelectionModel().selectFirst();
        startsAt.getSelectionModel().selectFirst();
        endsAt.getSelectionModel().selectFirst();

        cardBalance.setText(
                breezeCards.getValue().getValue().toString());
    }

    @FXML
    private void breezeCardSelected() {
        cardBalance.setText(
                breezeCards.getValue().getValue().toString());
    }

    @FXML
    private void manageClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.PASSENGER_CARD_MANAGEMENT);
    }

    @FXML
    private void viewClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.TRIP_HISTORY);
    }

    @FXML
    private void logOutClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.LOGIN);
    }
}
