package sls;

import db.StationManagementQueries;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CreateNewStationController {
    @FXML private TextField stationName;
    @FXML private TextField stopId;
    @FXML private TextField fare;
    @FXML private RadioButton busButton;
    @FXML private RadioButton trainButton;
    @FXML private TextField intersection;
    @FXML private CheckBox openStation;

    @FXML
    private void initialize() {
        this.trainClicked();
    }

    @FXML
    private void createClicked() throws IOException {
        if (StationManagementQueries.createNewStation(
                stationName.getText(),
                stopId.getText(),
                fare.getText(),
                intersection.getText(),
                busButton.isSelected(),
                openStation.isSelected()
        )) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Station successfully created");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "There was an error in adding the station");
            alert.showAndWait();
        }
    }

    @FXML
    private void busClicked() {
        trainButton.setSelected(false);
        busButton.setSelected(true);
        intersection.setEditable(true);
    }

    @FXML
    private void trainClicked() {
        busButton.setSelected(false);
        trainButton.setSelected(true);
        intersection.setEditable(false);
    }
}
