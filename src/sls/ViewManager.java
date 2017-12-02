package sls;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
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
    private static ScreensEnum currentScreen = ScreensEnum.LOGIN;

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

    /**
     * Change scene based on screen parameter
     *
     * @param aScreen
     * @throws IOException
     */
    public static void changeView(ScreensEnum aScreen) throws IOException {
        currentScreen = aScreen;
        ScreenSettings fScreen = msfScreenMap.get(aScreen);
        Parent fRoot = FXMLLoader.load(ViewManager.class.getResource(fScreen.getFxmlLocation()));

        Scene scene = new Scene(fRoot);
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.B) {
                try {
                    changeToPreviousScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        msPrimaryStage.setScene(scene);
        msPrimaryStage.setTitle(fScreen.getTitle());
        msPrimaryStage.show();
    }

    public static void changeToPreviousScreen() throws IOException {
        switch (currentScreen) {
            case LOGIN:
                break;
            case FLOW_REPORT:
                changeView(ScreensEnum.LOGIN);
                break;
            case REGISTRATION:
                changeView(ScreensEnum.LOGIN);
                break;
            case TRIP_HISTORY:
                changeView(ScreensEnum.LOGIN);
                break;
            case ADMIN_WELCOME:
                changeView(ScreensEnum.LOGIN);
                break;
            case STATION_DETAIL:
                changeView(ScreensEnum.LOGIN);
                break;
            case SUSPENDED_CARDS:
                changeView(ScreensEnum.LOGIN);
                break;
            case PASSENGER_WELCOME:
                changeView(ScreensEnum.LOGIN);
                break;
            case CREATE_NEW_STATION:
                changeView(ScreensEnum.LOGIN);
                break;
            case STATION_MANAGEMENT:
                changeView(ScreensEnum.LOGIN);
                break;
            case ADMIN_CARD_MANAGEMENT:
                changeView(ScreensEnum.LOGIN);
                break;
            case PASSENGER_CARD_MANAGEMENT:
                changeView(ScreensEnum.LOGIN);
                break;
            default:
                break;
        }
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
