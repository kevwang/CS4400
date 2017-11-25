package sls;

import javafx.fxml.FXML;

import java.io.IOException;

public class PassengerWelcomeController {
    @FXML
    private void initialize() {

    }

    @FXML
    private void manageClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.PASSENGER_CARD_MANAGEMENT);
    }

    @FXML
    private void viewClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.TRIP_HISTORY);
    }

    @FXML
    private void logOutClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.LOGIN);
    }
}
