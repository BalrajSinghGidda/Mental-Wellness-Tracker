package app.ui;

import app.models.UserModel;
import app.services.MoodService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MoodEntryView {

    private final MoodService moodService = new MoodService();
    private final UserModel user;
    private static final String LABEL_STYLE = "-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #333;";
    private static final String BUTTON_STYLE = "-fx-font-size: 14; -fx-padding: 10 20; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;";
    private static final String CANCEL_BUTTON_STYLE = "-fx-font-size: 14; -fx-padding: 10 20; -fx-background-color: #f44336; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;";

    public MoodEntryView(UserModel user) {
        this.user = user;
    }

    public void display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add New Mood Entry");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = new Label("New Mood Entry");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #4CAF50;");

        // Mood Selection
        VBox moodBox = new VBox(8);
        Label moodLabel = new Label("How are you feeling?");
        moodLabel.setStyle(LABEL_STYLE);
        ComboBox<String> moodComboBox = new ComboBox<>();
        moodComboBox.getItems().addAll("😊 Happy", "😢 Sad", "😠 Angry", "💪 Productive");
        moodComboBox.setPromptText("Select your mood");
        moodComboBox.setPrefWidth(Double.MAX_VALUE);
        moodComboBox.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        moodBox.getChildren().addAll(moodLabel, moodComboBox);

        // Stress Level
        VBox stressBox = new VBox(8);
        HBox stressLabelBox = new HBox(10);
        Label stressLabel = new Label("Stress Level:");
        stressLabel.setStyle(LABEL_STYLE);
        Label stressValueLabel = new Label("5/10");
        stressValueLabel.setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;");
        stressLabelBox.getChildren().addAll(stressLabel, stressValueLabel);

        Slider stressSlider = new Slider(0, 10, 5);
        stressSlider.setShowTickLabels(true);
        stressSlider.setShowTickMarks(true);
        stressSlider.setMajorTickUnit(1);
        stressSlider.setBlockIncrement(1);
        stressSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            stressValueLabel.setText(newVal.intValue() + "/10")
        );
        stressBox.getChildren().addAll(stressLabelBox, stressSlider);

        // Sleep Hours
        VBox sleepBox = new VBox(8);
        HBox sleepLabelBox = new HBox(10);
        Label sleepLabel = new Label("Sleep Hours:");
        sleepLabel.setStyle(LABEL_STYLE);
        Label sleepValueLabel = new Label("8h");
        sleepValueLabel.setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;");
        sleepLabelBox.getChildren().addAll(sleepLabel, sleepValueLabel);

        Slider sleepSlider = new Slider(0, 12, 8);
        sleepSlider.setShowTickLabels(true);
        sleepSlider.setShowTickMarks(true);
        sleepSlider.setMajorTickUnit(2);
        sleepSlider.setBlockIncrement(1);
        sleepSlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            sleepValueLabel.setText(String.format("%.1fh", newVal.doubleValue()))
        );
        sleepBox.getChildren().addAll(sleepLabelBox, sleepSlider);
        
        // Productivity
        VBox prodBox = new VBox(8);
        HBox prodLabelBox = new HBox(10);
        Label prodLabel = new Label("Productivity:");
        prodLabel.setStyle(LABEL_STYLE);
        Label prodValueLabel = new Label("5/10");
        prodValueLabel.setStyle("-fx-text-fill: #9C27B0; -fx-font-weight: bold;");
        prodLabelBox.getChildren().addAll(prodLabel, prodValueLabel);

        Slider productivitySlider = new Slider(0, 10, 5);
        productivitySlider.setShowTickLabels(true);
        productivitySlider.setShowTickMarks(true);
        productivitySlider.setMajorTickUnit(1);
        productivitySlider.setBlockIncrement(1);
        productivitySlider.valueProperty().addListener((obs, oldVal, newVal) -> 
            prodValueLabel.setText(newVal.intValue() + "/10")
        );
        prodBox.getChildren().addAll(prodLabelBox, productivitySlider);

        // Notes
        VBox notesBox = new VBox(8);
        Label notesLabel = new Label("Notes (Optional):");
        notesLabel.setStyle(LABEL_STYLE);
        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Add any notes about your day...");
        notesArea.setWrapText(true);
        notesArea.setPrefRowCount(4);
        notesArea.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        notesBox.getChildren().addAll(notesLabel, notesArea);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button saveButton = new Button("Save Entry");
        saveButton.setStyle(BUTTON_STYLE);
        saveButton.setPrefWidth(120);
        saveButton.setOnAction(e -> {
            String selectedMood = moodComboBox.getValue();
            if (selectedMood == null || selectedMood.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a mood.", ButtonType.OK);
                alert.setTitle("Missing Mood");
                alert.showAndWait();
                return;
            }
            
            String moodValue = selectedMood.split(" ")[1];
            boolean success = moodService.addMoodEntry(
                    user.getId(),
                    moodValue,
                    (int) stressSlider.getValue(),
                    (float) sleepSlider.getValue(),
                    (int) productivitySlider.getValue(),
                    notesArea.getText()
            );
            if(success) {
                window.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save mood entry.", ButtonType.OK);
                alert.setTitle("Save Failed");
                alert.showAndWait();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle(CANCEL_BUTTON_STYLE);
        cancelButton.setPrefWidth(120);
        cancelButton.setOnAction(e -> window.close());

        buttonBox.getChildren().addAll(cancelButton, saveButton);

        // Scroll for content
        ScrollPane scrollPane = new ScrollPane();
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(10));
        contentBox.getChildren().addAll(titleLabel, new Separator(), moodBox, stressBox, sleepBox, prodBox, notesBox);
        scrollPane.setContent(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f5f5;");

        layout.getChildren().addAll(scrollPane, new Separator(), buttonBox);

        Scene scene = new Scene(layout, 450, 700);
        window.setScene(scene);
        window.showAndWait();
    }
}
