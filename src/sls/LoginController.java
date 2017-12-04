package sls;

import db.DBUserQueries;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField user;

    @FXML
    private PasswordField pass;

    @FXML
    private void initialize() {
    }

    @FXML
    private void loginClicked() throws IOException {
        if (DBUserQueries.login(user.getText(), pass.getText())) {
            if (User.getCurrentUser().getAdmin()) {
                ViewManager.changeView(ScreensEnum.ADMIN_WELCOME);
            } else {
                ViewManager.changeView(ScreensEnum.PASSENGER_WELCOME);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect username/password");
            alert.showAndWait();
        }
    }

    @FXML
    private void registerClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.REGISTRATION);
    }
}
