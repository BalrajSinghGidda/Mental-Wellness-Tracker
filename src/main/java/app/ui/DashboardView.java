package app.ui;

import app.models.MoodEntryModel;
import app.models.UserModel;
import app.services.MoodService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.List;

public class DashboardView {

    private final Stage stage;
    private final UserModel user;
    private final MoodService moodService = new MoodService();
    private TableView<MoodEntryModel> table;
    private LineChart<String, Number> lineChart;
    private static final String BUTTON_STYLE = "-fx-font-size: 13; -fx-padding: 10 25; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;";
    private static final String LOGOUT_BUTTON_STYLE = "-fx-font-size: 13; -fx-padding: 10 25; -fx-background-color: #f44336; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;";

    public DashboardView(Stage stage, UserModel user) {
        this.stage = stage;
        this.user = user;
    }

    public Scene createScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #f5f5f5;");

        // Top section - Header
        VBox topVBox = new VBox(0);
        topVBox.setStyle("-fx-background-color: #4CAF50;");
        topVBox.setPadding(new Insets(20));

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.SPACE_BETWEEN);
        headerBox.setPadding(new Insets(10));

        VBox titleBox = new VBox(5);
        Label titleLabel = new Label("MindMosaic");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");
        
        Label welcomeLabel = new Label("Welcome, " + user.getUsername() + "!");
        welcomeLabel.setFont(Font.font("Arial", 14));
        welcomeLabel.setStyle("-fx-text-fill: #e8f5e9;");
        titleBox.getChildren().addAll(titleLabel, welcomeLabel);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(LOGOUT_BUTTON_STYLE);
        logoutButton.setPrefWidth(100);
        logoutButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            stage.setScene(loginView.createScene());
        });

        headerBox.getChildren().addAll(titleBox, logoutButton);
        topVBox.getChildren().add(headerBox);

        // Center section - Tabs with content
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Mood Entries Tab
        Tab entriesTab = new Tab("📋 Mood Entries");
        entriesTab.setStyle("-fx-font-size: 13;");
        table = new TableView<>();
        table.setStyle("-fx-font-size: 12;");
        setupTable();
        VBox entriesContainer = new VBox(10);
        entriesContainer.setPadding(new Insets(10));
        entriesContainer.getChildren().add(table);
        entriesTab.setContent(entriesContainer);

        // Analytics Tab
        Tab analyticsTab = new Tab("📊 Analytics");
        analyticsTab.setStyle("-fx-font-size: 13;");
        setupLineChart();
        VBox analyticsContainer = new VBox(10);
        analyticsContainer.setPadding(new Insets(10));
        analyticsContainer.getChildren().add(lineChart);
        analyticsTab.setContent(analyticsContainer);
        
        tabPane.getTabs().addAll(entriesTab, analyticsTab);

        // Right section - Action button
        VBox rightVBox = new VBox(15);
        rightVBox.setPadding(new Insets(20));
        rightVBox.setPrefWidth(150);
        rightVBox.setAlignment(Pos.TOP_CENTER);
        
        Button addEntryButton = new Button("➕ Add New Entry");
        addEntryButton.setStyle(BUTTON_STYLE);
        addEntryButton.setPrefWidth(Double.MAX_VALUE);
        addEntryButton.setWrapText(true);
        addEntryButton.setPrefHeight(60);
        addEntryButton.setOnAction(e -> {
            MoodEntryView moodEntryView = new MoodEntryView(user);
            moodEntryView.display();
            loadMoodEntries();
        });
        
        Label infoLabel = new Label("Track your mood regularly\nto get insights into your\nmental wellness patterns.");
        infoLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11; -fx-text-alignment: center; -fx-wrap-text: true;");
        infoLabel.setWrapText(true);
        
        rightVBox.getChildren().addAll(addEntryButton, new Separator(), infoLabel);

        borderPane.setTop(topVBox);
        borderPane.setCenter(tabPane);
        borderPane.setRight(rightVBox);

        loadMoodEntries();

        return new Scene(borderPane, 1000, 700);
    }

    private void setupTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST);
        
        TableColumn<MoodEntryModel, String> moodCol = new TableColumn<>("Mood");
        moodCol.setCellValueFactory(new PropertyValueFactory<>("mood"));
        moodCol.setPrefWidth(80);

        TableColumn<MoodEntryModel, Integer> stressCol = new TableColumn<>("Stress");
        stressCol.setCellValueFactory(new PropertyValueFactory<>("stressLevel"));
        stressCol.setPrefWidth(70);

        TableColumn<MoodEntryModel, Float> sleepCol = new TableColumn<>("Sleep (h)");
        sleepCol.setCellValueFactory(new PropertyValueFactory<>("sleepHours"));
        sleepCol.setPrefWidth(80);

        TableColumn<MoodEntryModel, Integer> prodCol = new TableColumn<>("Productivity");
        prodCol.setCellValueFactory(new PropertyValueFactory<>("productivity"));
        prodCol.setPrefWidth(90);
        
        TableColumn<MoodEntryModel, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        notesCol.setPrefWidth(150);

        TableColumn<MoodEntryModel, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        dateCol.setPrefWidth(100);

        table.getColumns().addAll(moodCol, stressCol, sleepCol, prodCol, notesCol, dateCol);
    }

    private void setupLineChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis(0, 10, 1);
        xAxis.setLabel("Date");
        xAxis.setStyle("-fx-font-size: 11;");
        yAxis.setLabel("Stress Level (0-10)");
        yAxis.setStyle("-fx-font-size: 11;");
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Stress Level Trends");
        lineChart.setTitleFont(Font.font("Arial", FontWeight.BOLD, 14));
        lineChart.setLegendVisible(true);
        lineChart.setPrefHeight(400);
        lineChart.setStyle("-fx-font-size: 12;");
    }

    private void loadMoodEntries() {
        List<MoodEntryModel> entries = moodService.getMoodEntries(user.getId());
        ObservableList<MoodEntryModel> observableEntries = FXCollections.observableArrayList(entries);
        table.setItems(observableEntries);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Stress Level");
        for (int i = entries.size() - 1; i >= 0; i--) {
            MoodEntryModel entry = entries.get(i);
            series.getData().add(new XYChart.Data<>(entry.getCreatedAt().toString(), entry.getStressLevel()));
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }
}
