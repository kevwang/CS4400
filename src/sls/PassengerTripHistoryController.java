package sls;

import db.TripHistoryQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Trip;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kevin on 12/2/2017.
 */
public class PassengerTripHistoryController {
    @FXML private TextField startTime;
    @FXML private TextField endTime;
    @FXML private Button update;
    @FXML private Button reset;

    @FXML
    private TableView table;
    @FXML private TableColumn timeCol;
    @FXML private TableColumn sourceCol;
    @FXML private TableColumn destCol;
    @FXML private TableColumn fareCol;
    @FXML private TableColumn cardCol;

    private List<Trip> tripList;

    @FXML
    private void initialize() {
        timeCol.setCellValueFactory(new PropertyValueFactory<Trip, String>("startTime"));
        sourceCol.setCellValueFactory(new PropertyValueFactory<Trip, String>("startsAt"));
        destCol.setCellValueFactory(new PropertyValueFactory<Trip, String>("endsAt"));
        fareCol.setCellValueFactory(new PropertyValueFactory<Trip, String>("tripFare"));
        cardCol.setCellValueFactory(new PropertyValueFactory<Trip, String>("cardNum"));

        startTime.setText("2017-10-02 13:11:11");
        endTime.setPromptText("Empty = NOW()");
        this.refresh();
    }

    private void refresh() {
        try {
            tripList = TripHistoryQueries.getTrips(
                    startTime.getText().isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startTime.getText()),
                    endTime.getText().isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endTime.getText()));

            if (tripList != null) {
                final ObservableList<Trip> data = FXCollections.observableArrayList();
                data.addAll(tripList);
                table.setItems(data);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect parameters!");
                alert.showAndWait();
            }
        } catch (ParseException p) {
            System.err.println(p.getMessage());
        }
    }

    @FXML
    private void updateClicked() {
        this.refresh();
    }

    @FXML
    private void resetClicked() {
        startTime.setText("2017-10-02 13:11:11");
        endTime.setText("");
        this.refresh();
    }
}
