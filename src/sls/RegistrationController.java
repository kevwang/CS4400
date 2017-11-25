package sls;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class RegistrationController {
    @FXML
    private void initialize() {

    }

    @FXML
    private void createClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.ADMIN_WELCOME);
    }
}
