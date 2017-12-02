package sls;

import db.CardManagementQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Breezecard;

import javax.xml.soap.Text;
import java.util.ArrayList;
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

        breezecardList = CardManagementQueries.getCards();
        final ObservableList<Breezecard> data = FXCollections.observableArrayList();
        data.addAll(breezecardList);

        table.setItems(data);
    }

    @FXML
    private void addCardSelected() {
        System.out.println("test");
    }

    @FXML
    private void addValueSelected() {

    }
}
