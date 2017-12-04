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
    private ComboBox<Station> endsAt;

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
        endStationList = DBUserQueries.getStartingStations();

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
                endsAt.getItems().add(s);
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
            if (Double.parseDouble(cardBalance.getText()) < startsAt.getValue().getFare()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Card balance too low");
                alert.showAndWait();
                return;
            }

            if (DBUserQueries.startTrip(
                    breezeCards.getValue().getCardNumber(),
                    startsAt.getValue().getStopId(),
                    startsAt.getValue().getFare()
            )) {
                breezeCards.getValue().setValue(
                        breezeCards.getValue().getValue() - startsAt.getValue().getFare());
                breezeCardSelected();
                startTrip.setText("Trip currently in progress");
                tripInProgress = true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error starting trip");
                alert.showAndWait();
            }

            // Update ends at stations
            if (endStationList != null) {
                endsAt.getItems().clear();

                if (startsAt.getValue().getTrain()) {
                    for (Station s : endStationList) {
                        if (s.getTrain()) {
                            endsAt.getItems().add(s);
                        }
                    }
                } else {
                    for (Station s : endStationList) {
                        if (!s.getTrain()) {
                            endsAt.getItems().add(s);
                        }
                    }
                }
                endsAt.getSelectionModel().selectFirst();
            }
        }
    }

    @FXML
    private void endTripClicked() {
        if (tripInProgress) {
            if (DBUserQueries.endTrip(
                    endsAt.getValue().getStopId(),
                    breezeCards.getValue().getCardNumber()
            )) {
                startTrip.setText("Start Trip");
                tripInProgress = false;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error ending trip");
                alert.showAndWait();
            }
        }
    }
}
