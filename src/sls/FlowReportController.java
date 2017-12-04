package sls;

import db.CardQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.StationFlow;
import models.SuspendedCard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kevin on 12/2/2017.
 */
public class FlowReportController {
    @FXML private TextField startTime;
    @FXML private TextField endTime;

    @FXML
    private TableView<StationFlow> table;
    @FXML private TableColumn stationNameCol;
    @FXML private TableColumn passengersInCol;
    @FXML private TableColumn passengersOutCol;
    @FXML private TableColumn flowCol;
    @FXML private TableColumn revenueCol;

    @FXML
    private void initialize() {
        stationNameCol.setCellValueFactory(new PropertyValueFactory<StationFlow, String>("stationName"));
        passengersInCol.setCellValueFactory(new PropertyValueFactory<StationFlow, String>("passengersIn"));
        passengersOutCol.setCellValueFactory(new PropertyValueFactory<StationFlow, String>("passengersOut"));
        flowCol.setCellValueFactory(new PropertyValueFactory<StationFlow, String>("flow"));
        revenueCol.setCellValueFactory(new PropertyValueFactory<StationFlow, String>("revenue"));

        startTime.setText("2017-10-02 13:11:11");
        endTime.setPromptText("Empty = NOW()");
        this.refresh();
    }

    private void refresh() {
        try {
            List<StationFlow> flows = CardQueries.getFlowReport(
                    startTime.getText().isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startTime.getText()),
                    endTime.getText().isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endTime.getText()));

            if (flows != null) {
                final ObservableList<StationFlow> data = FXCollections.observableArrayList();
                data.addAll(flows);

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
