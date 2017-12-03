package sls;

import db.StationManagementQueries;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.Station;

/**
 * Created by Kevin on 12/2/2017.
 */
public class AdminStationDetailController {
    @FXML private Text stationName;
    @FXML private Text stopId;
    @FXML private TextField fare;
    @FXML private Hyperlink updateFare;
    @FXML private Text intersection;
    @FXML private CheckBox openStation;

    private static String msStopId = null;

    @FXML
    private void initialize() {
        if (msStopId != null) {
            Station station = StationManagementQueries.viewStationDetail(this.msStopId);

            // Set the screen.. up!
            stationName.setText(station.getName());
            stopId.setText(station.getStopId());
            fare.setText(station.getFare().toString());
            intersection.setText(
                    station.getIntersection() != null ?
                            station.getIntersection() : "No intersection");
            openStation.setSelected(
                    !station.getClosed());
        }
    }

    @FXML
    private void updateFareClicked() {
        if (StationManagementQueries.setFare(
                stopId.getText(), Double.parseDouble(fare.getText()))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Fare updated");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating fare");
            alert.showAndWait();
        }
    }

    @FXML
    private void tryToSelectYo() {
        if (StationManagementQueries.setClosedStatus(
                stopId.getText(), !openStation.isSelected())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Fare updated");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating fare");
            alert.showAndWait();
        }
    }

    public static String getMsStopId() {
        return msStopId;
    }

    public static void setMsStopId(String msStopId) {
        AdminStationDetailController.msStopId = msStopId;
    }
}
