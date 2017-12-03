package sls;

import db.CardQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Breezecard;
import models.SuspendedCard;

import java.util.List;

/**
 * Created by Kevin on 12/2/2017.
 */
public class AdminCardManagementController {
    @FXML private TextField owner;
    @FXML private TextField cardNum;
    @FXML private TextField lowValue;
    @FXML private TextField highValue;
    @FXML private TextField setValue;
    @FXML private TextField transferUser;
    @FXML private CheckBox showSuspended;

    @FXML
    private TableView<Breezecard> table;
    @FXML private TableColumn cardNumCol;
    @FXML private TableColumn valueCol;
    @FXML private TableColumn ownerCol;


    @FXML
    private void initialize() {
        lowValue.setText("0.0");
        highValue.setText("1000.0");
        owner.setText("sandrapatel");
        cardNum.setText("2792083965359460");

        cardNumCol.setCellValueFactory(new PropertyValueFactory<Breezecard, String>("cardNumber"));
        valueCol.setCellValueFactory(new PropertyValueFactory<Breezecard, String>("value"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<Breezecard, String>("user"));
        this.refresh();
    }

    private void refresh() {
        List<Breezecard> cardsList = CardQueries.getBreezecards(
                owner.getText(),
                cardNum.getText(),
                Double.parseDouble(lowValue.getText()),
                Double.parseDouble(highValue.getText()),
                showSuspended.isSelected()
        );

        if (cardsList != null && !cardsList.isEmpty()) {
            final ObservableList<Breezecard> data = FXCollections.observableArrayList();
            data.addAll(cardsList);

            table.setItems(data);
        }
    }

    @FXML
    private void resetClicked() {
        this.refresh();
    }

    @FXML
    private void updateClicked() {
        this.refresh();
    }

    @FXML
    private void setValueClicked() {
        if (CardQueries.setValue(
                table.getSelectionModel().getSelectedItem().getCardNumber(),
                Double.parseDouble(setValue.getText())
        )) {
            this.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating card");
            alert.showAndWait();
        }
    }

    @FXML
    private void transferCardClicked() {
        if (CardQueries.transferCard(
                table.getSelectionModel().getSelectedItem().getCardNumber(),
                transferUser.getText()
        )) {
            this.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating card");
            alert.showAndWait();
        }
    }
}
