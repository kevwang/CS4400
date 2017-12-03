package sls;

import db.CardQueries;
import db.DBUserQueries;
import db.UserCardManagementQueries;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import models.Breezecard;
import models.SuspendedCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        if (user.getText().isEmpty() ||
            email.getText().isEmpty() ||
            pass.getText().isEmpty() ||
            confirmPass.getText().isEmpty() ||
            //pass.getText().equals(confirmPass.getText()) ||
            cardNumber.getText().replaceAll("\\s+","").length() != 16) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect parameters!");
            alert.showAndWait();
            return;
        }

        if (DBUserQueries.register(user.getText(), email.getText(), pass.getText(),
                cardNumber.getText().replaceAll("\\s+",""))) {

            // Check if the created card is now suspended
            List<SuspendedCard> susCards = CardQueries.getSuspendedCards();
            List<Breezecard> brCards = UserCardManagementQueries.getCards();
            /*for (SuspendedCard susCard : susCards) {
                for (Breezecard brCard : brCards) {
                    if (susCard.getBreezecardNumber().equals(brCard.getCardNumber())) {
                        Random rand = new Random();
                        cardNumber.setText((1000 + rand.nextInt(9000)) + " " +
                                (1000 + rand.nextInt(9000)) + " " +
                                (1000 + rand.nextInt(9000)) + " " +
                                (1000 + rand.nextInt(9000)) + " ");
                        UserCardManagementQueries.addCard(cardNumber.getText());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                                "That card is suspended. New card created toO");
                        alert.showAndWait();
                    }
                }
            }*/
            if (brCards.isEmpty()) {
                Random rand = new Random();
                cardNumber.setText((1000 + rand.nextInt(9000)) + " " +
                        (1000 + rand.nextInt(9000)) + " " +
                        (1000 + rand.nextInt(9000)) + " " +
                        (1000 + rand.nextInt(9000)) + " ");
                UserCardManagementQueries.addCard(
                        cardNumber.getText().replaceAll("\\s+",""));
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "That card is suspended. New card created toO");
                alert.showAndWait();
            }

            ViewManager.changeView(ScreensEnum.PASSENGER_WELCOME);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "There was a problem making your account");
            alert.showAndWait();
        }
    }

    @FXML
    private void existingCardSelected() {
        cardNumber.setText("");
        newCard.setSelected(false);
    }

    @FXML
    private void newCardSelected() {
        Random rand = new Random();
        cardNumber.setEditable(false);
        cardNumber.setText((1000 + rand.nextInt(9000)) + " " +
                (1000 + rand.nextInt(9000)) + " " +
                (1000 + rand.nextInt(9000)) + " " +
                (1000 + rand.nextInt(9000)) + " ");
        existingCard.setSelected(false);
    }
}
