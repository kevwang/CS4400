package sls;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 9/26/2017.
 *
 * Manages the main scene and transitions views
 */
public class ViewManager {
    private static Stage msPrimaryStage;
    private static final Map<ScreensEnum, ScreenSettings> msfScreenMap;

    static {
        msfScreenMap = new HashMap<>();
        msfScreenMap.put(ScreensEnum.LOGIN,
                new ScreenSettings("Login.fxml", "Login", 600, 400));
        msfScreenMap.put(ScreensEnum.REGISTRATION,
                new ScreenSettings("PassengerAccountRegistration.fxml", "Passenger Registration", 600, 400));
        msfScreenMap.put(ScreensEnum.PASSENGER_WELCOME,
                new ScreenSettings("PassengerWelcome.fxml", "Welcome!", 600, 400));
        msfScreenMap.put(ScreensEnum.ADMIN_WELCOME,
                new ScreenSettings("AdministratorWelcome.fxml", "Welcome!", 600, 400));
        msfScreenMap.put(ScreensEnum.STATION_MANAGEMENT,
                new ScreenSettings("StationManagement.fxml", "Welcome!", 600, 400));
        msfScreenMap.put(ScreensEnum.CREATE_NEW_STATION,
                new ScreenSettings("CreateNewStation.fxml", "Welcome!", 600, 400));
        msfScreenMap.put(ScreensEnum.STATION_DETAIL,
                new ScreenSettings("AdminStationDetail.fxml", "Welcome!", 600, 400));
        msfScreenMap.put(ScreensEnum.SUSPENDED_CARDS,
                new ScreenSettings("SuspendedCards.fxml", "Welcome!", 600, 400));
        msfScreenMap.put(ScreensEnum.ADMIN_CARD_MANAGEMENT,
                new ScreenSettings("AdminCardManagement.fxml", "Welcome!", 600, 400));
        msfScreenMap.put(ScreensEnum.FLOW_REPORT,
                new ScreenSettings("FlowReport.fxml", "Welcome!", 600, 400));
        msfScreenMap.put(ScreensEnum.PASSENGER_CARD_MANAGEMENT,
                new ScreenSettings("PassengerCardManagement.fxml", "Welcome!", 600, 400));
        msfScreenMap.put(ScreensEnum.TRIP_HISTORY,
                new ScreenSettings("PassengerTripHistory.fxml", "Welcome!", 600, 400));
    }

    public static void changeView(ScreensEnum aScreen) throws IOException {
        ScreenSettings fScreen = msfScreenMap.get(aScreen);
        Parent fRoot = FXMLLoader.load(ViewManager.class.getResource(fScreen.getFxmlLocation()));
        //Parent fMenu = FXMLLoader.load(ViewManager.class.getResource("MenuBar.fxml"));
        //msPrimaryStage.setScene(new Scene(fRoot, fScreen.getHeight(), fScreen.getLength()));
        msPrimaryStage.setScene(new Scene(fRoot));
        msPrimaryStage.setTitle(fScreen.getTitle());
        msPrimaryStage.show();
    }

    public Stage getPrimaryStage() {
        return msPrimaryStage;
    }

    public static void setPrimaryStage(Stage aStage) {
        msPrimaryStage = aStage;
    }

    private static class ScreenSettings {
        private String mFxmlLocation;
        private String mTitle;
        private int mWidth;
        private int mHeight;

        public ScreenSettings(String aFxmlLocation, String aTitle, int aWidth, int aHeight) {
            mFxmlLocation = aFxmlLocation;
            mTitle = aTitle;
            mWidth = aWidth;
            mHeight = aHeight;
        }

        public String getFxmlLocation() {
            return mFxmlLocation;
        }

        public String getTitle() {
            return mTitle;
        }

        public int getHeight() {
            return mWidth;
        }

        public int getLength() {
            return mHeight;
        }
    }
}
