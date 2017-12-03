package sls;

import db.StationManagementQueries;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.Station;

import java.io.IOException;
import java.util.List;

public class StationManagementController {
    @FXML private Button create;
    @FXML private Button view;

    @FXML
    private TableView<Station> table;
    @FXML private TableColumn nameCol;
    @FXML private TableColumn stopIdCol;
    @FXML private TableColumn fareCol;
    @FXML private TableColumn statusCol;

    private List<Station> stationList;

    @FXML
    private void initialize() {
        // Set column properties
        nameCol.setCellValueFactory(new PropertyValueFactory<Station, String>("name"));
        stopIdCol.setCellValueFactory(new PropertyValueFactory<Station, String>("stopId"));
        fareCol.setCellValueFactory(new PropertyValueFactory<Station, String>("fare"));
        statusCol.setCellValueFactory(new PropertyValueFactory<Station, String>("closedString"));

        stationList = StationManagementQueries.getStations();
        for (Station s : stationList) {
            s.setClosedString(
                    s.getClosed() ? "Closed" : "Open");
        }

        if (stationList != null) {
            final ObservableList<Station> data = FXCollections.observableArrayList();
            data.addAll(stationList);

            table.setItems(data);
        }
    }

    @FXML
    private void createClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.CREATE_NEW_STATION);
    }

    @FXML
    private void viewClicked() throws IOException {
        try {
            AdminStationDetailController.setMsStopId(
                    table.getSelectionModel().getSelectedItem().getStopId()
            );
            ViewManager.changeView(ScreensEnum.STATION_DETAIL);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "No stop selected");
            alert.showAndWait();
        }
    }
}
