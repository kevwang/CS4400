package sls;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class LoginController {
    @FXML
    private void initialize() {

    }

    @FXML
    private void loginClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.PASSENGER_WELCOME);
    }

    @FXML
    private void registerClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.REGISTRATION);
    }
}
