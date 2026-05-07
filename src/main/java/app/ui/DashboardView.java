package app.ui;

import app.models.MoodEntryModel;
import app.models.UserModel;
import app.services.MoodService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.List;

public class DashboardView {

    private final Stage stage;
    private final UserModel user;
    private final MoodService moodService = new MoodService();
    private TableView<MoodEntryModel> table;
    private LineChart<String, Number> lineChart;

    public DashboardView(Stage stage, UserModel user) {
        this.stage = stage;
        this.user = user;
    }

    public Scene createScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        // Top section
        VBox topVBox = new VBox(10);
        Label welcomeLabel = new Label("Welcome, " + user.getUsername() + "!");
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            stage.setScene(loginView.createScene());
        });
        topVBox.getChildren().addAll(welcomeLabel, logoutButton);

        // Center section
        TabPane tabPane = new TabPane();
        Tab entriesTab = new Tab("Mood Entries");
        table = new TableView<>();
        setupTable();
        entriesTab.setContent(table);

        Tab analyticsTab = new Tab("Analytics");
        setupLineChart();
        analyticsTab.setContent(lineChart);
        
        tabPane.getTabs().addAll(entriesTab, analyticsTab);

        // Right section
        VBox rightVBox = new VBox(10);
        Button addEntryButton = new Button("Add New Entry");
        addEntryButton.setOnAction(e -> {
            MoodEntryView moodEntryView = new MoodEntryView(user);
            moodEntryView.display();
            loadMoodEntries(); // Refresh the table and chart
        });
        rightVBox.getChildren().add(addEntryButton);

        borderPane.setTop(topVBox);
        borderPane.setCenter(tabPane);
        borderPane.setRight(rightVBox);

        loadMoodEntries();

        return new Scene(borderPane, 800, 600);
    }

    private void setupTable() {
        TableColumn<MoodEntryModel, String> moodCol = new TableColumn<>("Mood");
        moodCol.setCellValueFactory(new PropertyValueFactory<>("mood"));

        TableColumn<MoodEntryModel, Integer> stressCol = new TableColumn<>("Stress Level");
        stressCol.setCellValueFactory(new PropertyValueFactory<>("stressLevel"));

        TableColumn<MoodEntryModel, Float> sleepCol = new TableColumn<>("Sleep Hours");
        sleepCol.setCellValueFactory(new PropertyValueFactory<>("sleepHours"));

        TableColumn<MoodEntryModel, Integer> prodCol = new TableColumn<>("Productivity");
        prodCol.setCellValueFactory(new PropertyValueFactory<>("productivity"));
        
        TableColumn<MoodEntryModel, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        TableColumn<MoodEntryModel, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        table.getColumns().addAll(moodCol, stressCol, sleepCol, prodCol, notesCol, dateCol);
    }

    private void setupLineChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Stress Level");
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Stress Level Over Time");
    }

    private void loadMoodEntries() {
        List<MoodEntryModel> entries = moodService.getMoodEntries(user.getId());
        ObservableList<MoodEntryModel> observableEntries = FXCollections.observableArrayList(entries);
        table.setItems(observableEntries);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Stress Level");
        for (int i = entries.size() -1; i>=0; i--) {
            MoodEntryModel entry = entries.get(i);
            series.getData().add(new XYChart.Data<>(entry.getCreatedAt().toString(), entry.getStressLevel()));
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }
}
