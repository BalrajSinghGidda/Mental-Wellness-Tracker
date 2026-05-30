package app;

import app.database.DatabaseManager;
import app.ui.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.initializeDatabase();
        primaryStage.setTitle("MindMosaic - Mental Wellness Tracker");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(500);
        LoginView loginView = new LoginView(primaryStage);
        primaryStage.setScene(loginView.createScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
