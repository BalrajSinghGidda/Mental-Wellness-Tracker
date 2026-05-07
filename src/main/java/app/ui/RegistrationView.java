package app.ui;

import app.auth.AuthController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrationView {

    private final AuthController authController = new AuthController();
    private final Stage stage;

    public RegistrationView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            boolean success = authController.registerUser(usernameField.getText(), emailField.getText(), passwordField.getText());
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration successful!", ButtonType.OK);
                alert.showAndWait();
                LoginView loginView = new LoginView(stage);
                stage.setScene(loginView.createScene());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Registration failed.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Button backButton = new Button("Back to Login");
        backButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            stage.setScene(loginView.createScene());
        });

        vbox.getChildren().addAll(new Label("Register"), usernameField, emailField, passwordField, registerButton, backButton);
        return new Scene(vbox, 300, 300);
    }
}
