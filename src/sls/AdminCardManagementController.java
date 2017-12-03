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
        cardNum.setText("2792 0839 6535 9460");

        cardNumCol.setCellValueFactory(new PropertyValueFactory<Breezecard, String>("cardNumber"));
        valueCol.setCellValueFactory(new PropertyValueFactory<Breezecard, String>("value"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<Breezecard, String>("user"));
        this.refresh();
    }

    private void refresh() {
        if (!validate()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid fields!");
            alert.showAndWait();
            return;
        }
        List<Breezecard> cardsList = CardQueries.getBreezecards(
                owner.getText().isEmpty() ? null : owner.getText(),
                cardNum.getText().replaceAll("\\s+","").isEmpty()
                        ? null : cardNum.getText().replaceAll("\\s+",""),
                lowValue.getText().isEmpty() ? null : Double.parseDouble(lowValue.getText()),
                highValue.getText().isEmpty() ? null : Double.parseDouble(highValue.getText()),
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
        lowValue.setText("0.0");
        highValue.setText("1000.0");
        owner.setText("");
        cardNum.setText("");

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

    /**
     * Validate the fields
     * @return
     */
    private boolean validate() {
        try {
            Double.parseDouble(lowValue.getText());
            Double.parseDouble(highValue.getText());
            if (cardNum.getText().replaceAll("\\s+","").length() != 16 &&
                    !cardNum.getText().isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
