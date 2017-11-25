package sls;

import javafx.fxml.FXML;

import java.io.IOException;

public class StationManagementController {
    @FXML
    private void initialize() {

    }

    @FXML
    private void createClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.CREATE_NEW_STATION);
    }

    @FXML
    private void viewClicked() throws IOException {
        ViewManager.changeView(ScreensEnum.STATION_DETAIL);
    }
}
