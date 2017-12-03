package sls;

import db.UserCardManagementQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
        breezeNumCol.setCellValueFactory(new PropertyValueFactory<Breezecard, String>("cardNumber"));
        breezeValueCol.setCellValueFactory(new PropertyValueFactory<Breezecard, String>("value"));
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
        if (UserCardManagementQueries.addCard(
                breezeCardNum.getText()
        )) {
            breezeCardNum.setText("");
            this.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding card");
            alert.showAndWait();
        }
    }

    @FXML
    private void addValueSelected() {
        if (UserCardManagementQueries.addValue(
                table.getSelectionModel().getSelectedItem().getCardNumber(),
                Double.parseDouble(valueToAdd.getText())
        )) {
            this.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding value");
            alert.showAndWait();
        }
    }
}
