package sls;

import db.DBUserQueries;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Random;

public class RegistrationController {
    @FXML
    private TextField user;

    @FXML
    private TextField email;

    @FXML
    private TextField pass;

    @FXML
    private TextField confirmPass;

    @FXML
    private RadioButton existingCard;

    @FXML
    private TextField cardNumber;

    @FXML
    private RadioButton newCard;

    @FXML
    private void initialize() {
        newCard.selectedProperty().set(true);
        Random rand = new Random();
        cardNumber.setText((1000 + rand.nextInt(9000)) + " " +
                (1000 + rand.nextInt(9000)) + " " +
                (1000 + rand.nextInt(9000)) + " " +
                (1000 + rand.nextInt(9000)) + " ");
    }

    @FXML
    private void createClicked() throws IOException {
        if (DBUserQueries.register(user.getText(), email.getText(), pass.getText(),
                cardNumber.getText().replaceAll("\\s+",""))) {
            ViewManager.changeView(ScreensEnum.ADMIN_WELCOME);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "There was a problem making your account");
            alert.showAndWait();
        }
    }

    @FXML
    private void existingCardSelected() {
        cardNumber.setEditable(false);
        cardNumber.setText("");
        newCard.setSelected(false);
    }

    @FXML
    private void newCardSelected() {
        Random rand = new Random();
        cardNumber.setEditable(true);cardNumber.setText((1000 + rand.nextInt(9000)) + " " +
                (1000 + rand.nextInt(9000)) + " " +
                (1000 + rand.nextInt(9000)) + " " +
                (1000 + rand.nextInt(9000)) + " ");
        existingCard.setSelected(false);
    }
}
