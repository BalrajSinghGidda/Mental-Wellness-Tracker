package app.ui;

import app.models.UserModel;
import app.services.MoodService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MoodEntryView {

    private final MoodService moodService = new MoodService();
    private final UserModel user;

    public MoodEntryView(UserModel user) {
        this.user = user;
    }

    public void display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Mood Entry");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        ComboBox<String> moodComboBox = new ComboBox<>();
        moodComboBox.getItems().addAll("Happy", "Sad", "Angry", "Productive");
        moodComboBox.setPromptText("Select your mood");

        Slider stressSlider = new Slider(0, 10, 5);
        stressSlider.setShowTickLabels(true);
        stressSlider.setShowTickMarks(true);
        stressSlider.setMajorTickUnit(1);
        stressSlider.setBlockIncrement(1);

        Slider sleepSlider = new Slider(0, 12, 8);
        sleepSlider.setShowTickLabels(true);
        sleepSlider.setShowTickMarks(true);
        sleepSlider.setMajorTickUnit(2);
        sleepSlider.setBlockIncrement(1);
        
        Slider productivitySlider = new Slider(0, 10, 5);
        productivitySlider.setShowTickLabels(true);
        productivitySlider.setShowTickMarks(true);
        productivitySlider.setMajorTickUnit(1);
        productivitySlider.setBlockIncrement(1);

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Notes...");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String selectedMood = moodComboBox.getValue();
            if (selectedMood == null || selectedMood.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a mood.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            
            boolean success = moodService.addMoodEntry(
                    user.getId(),
                    selectedMood,
                    (int) stressSlider.getValue(),
                    (float) sleepSlider.getValue(),
                    (int) productivitySlider.getValue(),
                    notesArea.getText()
            );
            if(success) {
                window.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save mood entry.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(
                new Label("Mood:"), moodComboBox,
                new Label("Stress Level:"), stressSlider,
                new Label("Sleep Hours:"), sleepSlider,
                new Label("Productivity:"), productivitySlider,
                new Label("Notes:"), notesArea,
                saveButton
        );

        Scene scene = new Scene(layout, 400, 500);
        window.setScene(scene);
        window.showAndWait();
    }
}
