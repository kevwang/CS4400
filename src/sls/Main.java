package sls;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ViewManager.setPrimaryStage(primaryStage);
        ViewManager.changeView(ScreensEnum.LOGIN);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
