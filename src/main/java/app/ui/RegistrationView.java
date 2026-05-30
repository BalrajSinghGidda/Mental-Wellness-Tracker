package app.ui;

import app.auth.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RegistrationView {

    private final AuthController authController = new AuthController();
    private final Stage stage;
    private static final String BACKGROUND_COLOR = "-fx-background-color: #f5f5f5;";
    private static final String BUTTON_STYLE = "-fx-font-size: 14; -fx-padding: 10 30; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;";
    private static final String BACK_BUTTON_STYLE = "-fx-font-size: 14; -fx-padding: 10 30; -fx-background-color: #999; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;";
    private static final String TEXT_FIELD_STYLE = "-fx-font-size: 13; -fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;";

    public RegistrationView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        VBox mainBox = new VBox(15);
        mainBox.setPadding(new Insets(40));
        mainBox.setStyle(BACKGROUND_COLOR);
        mainBox.setAlignment(Pos.TOP_CENTER);

        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("MindMosaic");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #4CAF50;");
        
        Label subtitleLabel = new Label("Create Your Account");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setStyle("-fx-text-fill: #666;");
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);

        // Form Box
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10;");
        formBox.setMaxWidth(350);

        Label registerLabel = new Label("Register New Account");
        registerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        registerLabel.setStyle("-fx-text-fill: #333;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle(TEXT_FIELD_STYLE);
        usernameField.setPrefHeight(40);

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.setStyle(TEXT_FIELD_STYLE);
        emailField.setPrefHeight(40);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password (min 4 characters)");
        passwordField.setStyle(TEXT_FIELD_STYLE);
        passwordField.setPrefHeight(40);

        Button registerButton = new Button("Register");
        registerButton.setStyle(BUTTON_STYLE);
        registerButton.setPrefWidth(Double.MAX_VALUE);
        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all fields.", ButtonType.OK);
                alert.setTitle("Missing Information");
                alert.showAndWait();
                return;
            }
            
            if (!email.contains("@") || !email.contains(".")) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid email address.", ButtonType.OK);
                alert.setTitle("Invalid Email");
                alert.showAndWait();
                return;
            }
            
            if (password.length() < 4) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Password must be at least 4 characters long.", ButtonType.OK);
                alert.setTitle("Weak Password");
                alert.showAndWait();
                return;
            }
            
            boolean success = authController.registerUser(username, email, password);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account created successfully! You can now login.", ButtonType.OK);
                alert.setTitle("Registration Successful");
                alert.showAndWait();
                LoginView loginView = new LoginView(stage);
                stage.setScene(loginView.createScene());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Registration failed. Username or email may already exist.", ButtonType.OK);
                alert.setTitle("Registration Failed");
                alert.showAndWait();
            }
        });

        Button backButton = new Button("Back to Login");
        backButton.setStyle(BACK_BUTTON_STYLE);
        backButton.setPrefWidth(Double.MAX_VALUE);
        backButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            stage.setScene(loginView.createScene());
        });

        formBox.getChildren().addAll(
            registerLabel,
            new Separator(),
            usernameField,
            emailField,
            passwordField,
            registerButton,
            backButton
        );

        mainBox.getChildren().add(formBox);

        Scene scene = new Scene(mainBox, 500, 650);
        return scene;
    }
}
