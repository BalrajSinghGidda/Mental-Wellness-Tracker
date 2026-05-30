package app.ui;

import app.auth.AuthController;
import app.models.UserModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {

    private final AuthController authController = new AuthController();
    private final Stage stage;
    private static final String BACKGROUND_COLOR = "-fx-background-color: #f5f5f5;";
    private static final String BUTTON_STYLE = "-fx-font-size: 14; -fx-padding: 10 30; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;";
    private static final String SECONDARY_BUTTON_STYLE = "-fx-font-size: 14; -fx-padding: 10 30; -fx-background-color: #2196F3; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;";
    private static final String TEXT_FIELD_STYLE = "-fx-font-size: 13; -fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;";

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        VBox mainBox = new VBox(15);
        mainBox.setPadding(new Insets(40));
        mainBox.setStyle(BACKGROUND_COLOR);
        mainBox.setAlignment(Pos.CENTER);

        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("MindMosaic");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #4CAF50;");
        
        Label subtitleLabel = new Label("Mental Wellness Tracker");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setStyle("-fx-text-fill: #666;");
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);

        // Form Box
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10;");
        formBox.setMaxWidth(350);

        Label loginLabel = new Label("Login to Your Account");
        loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        loginLabel.setStyle("-fx-text-fill: #333;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle(TEXT_FIELD_STYLE);
        usernameField.setPrefHeight(40);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(TEXT_FIELD_STYLE);
        passwordField.setPrefHeight(40);

        Button loginButton = new Button("Login");
        loginButton.setStyle(BUTTON_STYLE);
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            
            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter both username and password.", ButtonType.OK);
                alert.setTitle("Missing Information");
                alert.showAndWait();
                return;
            }
            
            UserModel user = authController.loginUser(username, password);
            if (user != null) {
                DashboardView dashboard = new DashboardView(stage, user);
                stage.setScene(dashboard.createScene());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.", ButtonType.OK);
                alert.setTitle("Login Failed");
                alert.showAndWait();
            }
        });

        Button registerButton = new Button("Create New Account");
        registerButton.setStyle(SECONDARY_BUTTON_STYLE);
        registerButton.setPrefWidth(Double.MAX_VALUE);
        registerButton.setOnAction(e -> {
            RegistrationView registrationView = new RegistrationView(stage);
            stage.setScene(registrationView.createScene());
        });

        formBox.getChildren().addAll(
            loginLabel,
            new Separator(),
            usernameField,
            passwordField,
            loginButton,
            registerButton
        );

        mainBox.getChildren().addAll(headerBox, formBox);

        Scene scene = new Scene(mainBox, 500, 600);
        return scene;
    }
}
