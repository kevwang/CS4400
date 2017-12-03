package sls;

import db.CardQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Station;
import models.SuspendedCard;

import java.io.IOException;
import java.util.List;

public class SuspendedCardsController {
    @FXML private TableView<SuspendedCard> table;
    @FXML private TableColumn cardNumCol;
    @FXML private TableColumn newOwnerCol;
    @FXML private TableColumn dateSuspendedCol;
    @FXML private TableColumn previousOwnerCol;

    @FXML
    private void initialize() {
        cardNumCol.setCellValueFactory(new PropertyValueFactory<SuspendedCard, String>("breezecardNumber"));
        newOwnerCol.setCellValueFactory(new PropertyValueFactory<SuspendedCard, String>("newOwner"));
        dateSuspendedCol.setCellValueFactory(new PropertyValueFactory<SuspendedCard, String>("dateSuspended"));
        previousOwnerCol.setCellValueFactory(new PropertyValueFactory<SuspendedCard, String>("previousOwner"));
        this.refresh();
    }

    private void refresh() {
        List<SuspendedCard> cardsList = CardQueries.getSuspendedCards();

        if (cardsList != null && !cardsList.isEmpty()) {
            final ObservableList<SuspendedCard> data = FXCollections.observableArrayList();
            data.addAll(cardsList);

            table.setItems(data);
        }
    }

    @FXML
    private void newOwnerClicked() throws IOException {
        try {
            SuspendedCard sc = table.getSelectionModel().getSelectedItem();
            if (CardQueries.transferCard(
                    sc.getBreezecardNumber(), sc.getNewOwner())) {
                this.refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error transfering card");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "No row selected");
            alert.showAndWait();
        }

    }

    @FXML
    private void prevOwnerClicked() throws IOException {
        try {
            SuspendedCard sc = table.getSelectionModel().getSelectedItem();
            if (CardQueries.transferCard(
                    sc.getBreezecardNumber(), sc.getPreviousOwner())) {
                this.refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error transfering card");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "No row selected");
            alert.showAndWait();
        }
    }
}
