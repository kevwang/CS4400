package sls;

import db.CardQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.StationFlow;
import models.SuspendedCard;

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
        this.refresh();
    }

    private void refresh() {
        List<StationFlow> flows = CardQueries.getFlowReport();

        if (flows != null && !flows.isEmpty()) {
            final ObservableList<StationFlow> data = FXCollections.observableArrayList();
            data.addAll(flows);

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
