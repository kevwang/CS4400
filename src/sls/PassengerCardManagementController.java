package sls;

import db.UserCardManagementQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import models.Breezecard;

import java.util.List;

/**
 * Created by Kevin on 12/2/2017.
 */
public class PassengerCardManagementController {
    @FXML
    private TextField breezeCardNum;

    @FXML
    private TextField creditCardNum;

    @FXML
    private TextField valueToAdd;

    @FXML
    private TableView<Breezecard> table;

    @FXML private TableColumn<Breezecard, String> breezeNumCol;
    @FXML private TableColumn<Breezecard, String> breezeValueCol;

    private List<Breezecard> breezecardList;

    @FXML
    private void initialize() {
        // Set column properties
        ContextMenu cm = new ContextMenu();
        MenuItem mi = new MenuItem("Remove");
        mi.setOnAction(event -> remove());
        cm.getItems().add(mi);

        breezeNumCol.setCellValueFactory(new PropertyValueFactory<Breezecard, String>("cardNumber"));
        breezeValueCol.setCellValueFactory(new PropertyValueFactory<Breezecard, String>("value"));
        table.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            if(t.getButton() == MouseButton.SECONDARY)
            {
                cm.show(table , t.getScreenX() , t.getScreenY());
            }
        });

        this.refresh();
    }

    private void refresh() {
        breezecardList = UserCardManagementQueries.getCards();
        if (breezecardList != null) {
            final ObservableList<Breezecard> data = FXCollections.observableArrayList();
            data.addAll(breezecardList);

            table.setItems(data);
        }
    }

    @FXML
    private void addCardSelected() {
        if (breezeCardNum.getText().replaceAll("\\s+","").length() != 16) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Bad value");
            alert.showAndWait();
            return;
        }
        if (UserCardManagementQueries.addCard(
                breezeCardNum.getText().replaceAll("\\s+","")
        )) {
            breezeCardNum.setText("");
            this.refresh();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Card added");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding card");
            alert.showAndWait();
        }
    }

    @FXML
    private void addValueSelected() {
        try {
            Double v = Double.parseDouble(valueToAdd.getText());
            if (v < 0.0 || v > 1000.0 ||
                    creditCardNum.getText().replaceAll("\\s+","").length() != 16) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Bad value");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Bad value");
            alert.showAndWait();
            return;
        }

        if (UserCardManagementQueries.addValue(
                table.getSelectionModel().getSelectedItem().getCardNumber(),
                Double.parseDouble(valueToAdd.getText())
        )) {
            this.refresh();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Card value added successfully");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding value");
            alert.showAndWait();
        }
    }

    private void remove() {
        if (UserCardManagementQueries.removeCard(
                table.getSelectionModel().getSelectedItem().getCardNumber()
        )) {
            this.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error removing card");
            alert.showAndWait();
        }
    }
}
