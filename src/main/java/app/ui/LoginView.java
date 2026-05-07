package app.ui;

import app.auth.AuthController;
import app.models.UserModel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    private final AuthController authController = new AuthController();
    private final Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            UserModel user = authController.loginUser(usernameField.getText(), passwordField.getText());
            if (user != null) {
                // Navigate to Dashboard
                DashboardView dashboard = new DashboardView(stage, user);
                stage.setScene(dashboard.createScene());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            RegistrationView registrationView = new RegistrationView(stage);
            stage.setScene(registrationView.createScene());
        });

        vbox.getChildren().addAll(new Label("Login"), usernameField, passwordField, loginButton, registerButton);
        return new Scene(vbox, 300, 250);
    }
}
