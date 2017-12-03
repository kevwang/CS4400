package sls;

import db.TripHistoryQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Trip;

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

        this.refresh();
    }

    private void refresh() {
        tripList = TripHistoryQueries.getTrips();

        if (tripList != null) {
            final ObservableList<Trip> data = FXCollections.observableArrayList();
            data.addAll(tripList);
            table.setItems(data);
        }
    }

    @FXML
    private void updateClicked() {
        this.refresh();
    }

    @FXML
    private void resetClicked() {
        this.refresh();
    }
}
