package sls;

import db.DBUserQueries;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import models.Breezecard;
import models.Station;
import models.Trip;

import java.io.IOException;
import java.util.List;

public class PassengerWelcomeController {
    @FXML
    private ComboBox<Breezecard> breezeCards;

    @FXML
    private Text cardBalance;

    @FXML
    private ComboBox<Station> startsAt;

    @FXML
    private ComboBox<String> endsAt;

    @FXML
    private Hyperlink startTrip;

    @FXML
    private Hyperlink manageCards;

    @FXML
    private Hyperlink endTrip;

    private List<Breezecard> breezecardList;
    private List<Station> startStationList;
    private List<Station> endStationList;
    private boolean tripInProgress = false;

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
                startsAt.getItems().add(s);
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

        breezeCardSelected();
    }

    @FXML
    private void breezeCardSelected() {
        cardBalance.setText(
                breezeCards.getValue().getValue().toString());
        Trip currTrip = DBUserQueries.getCurrentTrip(
                breezeCards.getValue().getCardNumber());
        if (currTrip != null) {
            startTrip.setText("Trip currently in progress");
            //startsAt.getSelectionModel().select();
            tripInProgress = true;
        } else {
            startTrip.setText("Start Trip");
            tripInProgress = false;
        }
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

    @FXML
    private void startTripClicked() {
        if (!tripInProgress) {
            if (DBUserQueries.startTrip(
                    breezeCards.getValue().getCardNumber(),
                    startsAt.getValue().getStopId()
            )) {
                startTrip.setText("Trip currently in progress");
                breezeCards.setEditable(false);
                startsAt.setEditable(false);
                tripInProgress = true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error starting trip");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void endTripClicked() {
        if (tripInProgress) {
            if (DBUserQueries.endTrip(
                    endsAt.getValue(),
                    breezeCards.getValue().getCardNumber()
            )) {
                startTrip.setText("Start Trip");
                breezeCards.setEditable(true);
                startsAt.setEditable(true);
                tripInProgress = false;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error ending trip");
                alert.showAndWait();
            }
        }
    }
}
