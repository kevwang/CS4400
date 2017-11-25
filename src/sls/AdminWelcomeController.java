package sls;

import javafx.fxml.FXML;

import java.io.IOException;

public class AdminWelcomeController {
    @FXML
    private void initialize() {

    }

    @FXML
    private void stationManagementClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.STATION_MANAGEMENT);
    }

    @FXML
    private void suspendedCardsClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.SUSPENDED_CARDS);
    }

    @FXML
    private void cardManagementClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.ADMIN_CARD_MANAGEMENT);
    }

    @FXML
    private void flowReportClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.FLOW_REPORT);
    }

    @FXML
    private void logOutClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.LOGIN);
    }
}
