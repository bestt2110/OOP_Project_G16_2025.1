package UI;

import App.Main; 
import Analysis.*;
import Model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser; // [MỚI] Thư viện chọn file
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class UI extends Application {

    private Stage stageRef;
    private Scene homeScene;       
    private Scene dashboardScene;  
    
    private DatePicker dpStart;
    private DatePicker dpEnd; 
    
    private Label lblStatus;
    private Button btnP1, btnP2, btnP3; 
    private Button btnRun;
    private ProgressIndicator progressIndicator;

    // [MỚI] Biến lưu file người dùng chọn
    private File selectedCsvFile = null;
    private Label lblSelectedFile;
    private Button btnChooseFile;

    @Override
    public void start(Stage primaryStage) {
        stageRef = primaryStage;
        primaryStage.setTitle("Java Project - Group 16");
        createHomeScene();
        createDashboardScene(); 
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    private void createHomeScene() {
        Label title = new Label("JAVA PROJECT BY GROUP 16");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Button btnAnalysis = new Button("Data Analysis");
        Button btnExit = new Button("Exit");
        String btnStyle = "-fx-font-size: 16px; -fx-pref-width: 250px; -fx-pref-height: 40px; -fx-cursor: hand;";
        btnAnalysis.setStyle(btnStyle);
        btnExit.setStyle(btnStyle);

        btnAnalysis.setOnAction(e -> {
            if (!Main.globalData.isEmpty()) {
                lblStatus.setText("✅ " + Main.globalData.size() + " collected");
                enableAnalysisButtons();
            }
            stageRef.setScene(dashboardScene);
        });
        btnExit.setOnAction(e -> System.exit(0));

        VBox box = new VBox(25, title, btnAnalysis, btnExit);
        box.setAlignment(Pos.CENTER);
        homeScene = new Scene(new StackPane(box), 900, 600);
    }

    private void createDashboardScene() {
        Button btnHome = new Button("⬅ Home Screen");
        btnHome.setStyle("-fx-font-size: 14px; -fx-cursor: hand; -fx-base: #ecf0f1;");
        btnHome.setOnAction(e -> stageRef.setScene(homeScene));

        Label lblTitle = new Label("DATA ANALYSIS DASHBOARD");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        BorderPane header = new BorderPane();
        header.setLeft(btnHome);
        header.setCenter(lblTitle);
        BorderPane.setAlignment(lblTitle, Pos.CENTER);
        BorderPane.setMargin(lblTitle, new Insets(0, 100, 0, 0));

        VBox inputBox = new VBox(10);
        inputBox.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 15;");
        
        ComboBox<String> cbbSource = new ComboBox<>();
        cbbSource.getItems().addAll("VnExpress", "YouTube", "File CSV (Offline)");
        cbbSource.setValue("VnExpress");

        TextField txtKw = new TextField("bão Yagi"); 
        this.dpStart = new DatePicker(LocalDate.of(2025, 1, 1));
        this.dpEnd = new DatePicker(LocalDate.now());

        // [MỚI] Nút chọn file và Label hiển thị tên file
        btnChooseFile = new Button("📂 Choose File...");
        lblSelectedFile = new Label("No file selected");
        lblSelectedFile.setStyle("-fx-font-style: italic; -fx-text-fill: #7f8c8d;");
        
        // Mặc định ẩn đi (chỉ hiện khi chọn Offline)
        btnChooseFile.setVisible(false);
        lblSelectedFile.setVisible(false);

        // Sự kiện: Khi chọn File CSV -> Hiện nút chọn file
        cbbSource.setOnAction(e -> {
            boolean isOffline = cbbSource.getValue().contains("File");
            btnChooseFile.setVisible(isOffline);
            lblSelectedFile.setVisible(isOffline);
            txtKw.setDisable(isOffline); // Offline thì không cần nhập keyword
        });

        // Sự kiện: Bấm nút chọn file
        btnChooseFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Data File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            // Mở tại thư mục hiện tại của dự án
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            
            File file = fileChooser.showOpenDialog(stageRef);
            if (file != null) {
                selectedCsvFile = file;
                lblSelectedFile.setText("File: " + file.getName());
                lblSelectedFile.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            }
        });

        HBox row1 = new HBox(10, new Label("Source:"), cbbSource, btnChooseFile, lblSelectedFile);
        row1.setAlignment(Pos.CENTER_LEFT);
        
        HBox row2 = new HBox(10, new Label("Keywords:"), txtKw); // Tách keyword ra dòng riêng cho thoáng
        row2.setAlignment(Pos.CENTER_LEFT);

        HBox row3 = new HBox(10, new Label("From:"), dpStart, new Label("To:"), dpEnd);
        row3.setAlignment(Pos.CENTER_LEFT);
        
        inputBox.getChildren().addAll(new Label("Configuration"), row1, row2, row3);

        btnRun = new Button("▶ Start");
        btnRun.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false); 
        lblStatus = new Label("Ready to start");

        HBox statusBox = new HBox(10, btnRun, progressIndicator, lblStatus);
        
        btnP1 = createChartButton("Sentiment Over Time", "PROBLEM_1");
        btnP2 = createChartButton("Damage Category", "PROBLEM_2");
        btnP3 = createChartButton("Satisfaction", "PROBLEM_3");
        HBox chartBox = new HBox(20, btnP1, btnP2, btnP3);
        chartBox.setAlignment(Pos.CENTER);

        btnRun.setOnAction(e -> {
            // Validate Ngày tháng
            if (dpStart.getValue().isAfter(dpEnd.getValue())) {
                showError("Start date cannot be after End date"); return;
            }

            // [MỚI] Validate Chọn file nếu đang ở chế độ Offline
            String source = cbbSource.getValue();
            String customPath = null;
            
            if (source.contains("File")) {
                if (selectedCsvFile == null || !selectedCsvFile.exists()) {
                    showError("Please select a CSV file first!");
                    return;
                }
                customPath = selectedCsvFile.getAbsolutePath();
            }

            Date start = Date.from(dpStart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(dpEnd.getValue().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

            btnRun.setDisable(true);
            progressIndicator.setVisible(true);
            btnP1.setDisable(true); btnP2.setDisable(true); btnP3.setDisable(true);
            
            // Truyền customPath sang Main
            Main.processDataRequest(source, txtKw.getText(), start, end, this, customPath);
        });

        VBox root = new VBox(20, header, inputBox, statusBox, new Separator(), new Label("RESULTS"), chartBox);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        this.dashboardScene = new Scene(root, 900, 600);
    }

    // ... (Giữ nguyên các hàm notifyNoDataOrError, updateStatus, enableAnalysisButtons, showChart...)
    
    // --- COPY LẠI CÁC HÀM CŨ ĐỂ ĐẢM BẢO FILE CHẠY ĐƯỢC ---
    public void notifyNoDataOrError(String msg) {
        Platform.runLater(() -> {
            progressIndicator.setVisible(false);
            btnRun.setDisable(false);
            btnP1.setDisable(true); btnP2.setDisable(true); btnP3.setDisable(true);
            lblStatus.setText(msg);
            showError(msg); 
        });
    }

    public void updateStatus(String msg) { Platform.runLater(() -> lblStatus.setText(msg)); }

    public void enableAnalysisButtons() {
        Platform.runLater(() -> {
            btnP1.setDisable(false); btnP2.setDisable(false); btnP3.setDisable(false);
            btnRun.setDisable(false); progressIndicator.setVisible(false);
        });
    }

    private Button createChartButton(String text, String type) {
        Button btn = new Button(text);
        btn.setPrefSize(200, 50);
        btn.setDisable(true); 
        btn.setOnAction(e -> showChart(type));
        return btn;
    }

    private void showChart(String problemType) {
        List<Post> data = Main.globalData;
        if (data == null || data.isEmpty()) { showError("No data!"); return; }
        Node chartNode = null;
        String title = "";
        switch (problemType) {
            case "PROBLEM_1":
                Map<String, SentimentResult> res1 = new HashMap<>();
                SentimentOverTimeAnalysis task1 = new SentimentOverTimeAnalysis();
                for (Post p : data) task1.execute(p, res1);
                chartNode = createChartNode(problemType, res1);
                title = "Sentiment Over Time";
                break;
            case "PROBLEM_2":
                Map<String, CountNum> res2 = new HashMap<>();
                DamageCategoryAnalysis task2 = new DamageCategoryAnalysis();
                for (Post p : data) task2.execute(p, res2);
                chartNode = createChartNode(problemType, res2);
                title = "Damage Category Analysis";
                break;
            case "PROBLEM_3":
                Map<String, SentimentResult> res3 = new HashMap<>();
                SatisfactionAnalysis task3 = new SatisfactionAnalysis();
                for (Post p : data) task3.execute(p, res3);
                chartNode = createChartNode(problemType, res3);
                title = "Satisfaction Analysis";
                break;
        }
        if (chartNode != null) switchSceneToChart(chartNode, title);
    }
    
    private Node createChartNode(String problemType, Map<String, ? extends AnalysisResult> results) {
        if (results.isEmpty()) return new StackPane(new Label("No data available."));
        switch (problemType) {
            case "PROBLEM_1": 
                CategoryAxis xAxis = new CategoryAxis(); xAxis.setLabel("Time");
                NumberAxis yAxis = new NumberAxis(); yAxis.setLabel("Count");
                LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
                lineChart.setCreateSymbols(false); 
                XYChart.Series<String, Number> pos = new XYChart.Series<>(); pos.setName("Positive");
                XYChart.Series<String, Number> neg = new XYChart.Series<>(); neg.setName("Negative");
                TreeMap<String, ? extends AnalysisResult> sortedLine = new TreeMap<>(results);
                for (Map.Entry<String, ? extends AnalysisResult> entry : sortedLine.entrySet()) {
                    if (entry.getValue() instanceof SentimentResult) {
                        SentimentResult sr = (SentimentResult) entry.getValue();
                        pos.getData().add(new XYChart.Data<>(entry.getKey(), sr.getPositiveCount()));
                        neg.getData().add(new XYChart.Data<>(entry.getKey(), sr.getNegativeCount()));
                    }
                }
                lineChart.getData().addAll(pos, neg);
                return lineChart;
            case "PROBLEM_2":
                PieChart pieChart = new PieChart();
                for (Map.Entry<String, ? extends AnalysisResult> entry : results.entrySet()) {
                    if (entry.getValue() instanceof CountNum) {
                        int count = ((CountNum) entry.getValue()).getCount();
                        if (count > 0) pieChart.getData().add(new PieChart.Data(entry.getKey(), count));
                    }
                }
                return pieChart;
            case "PROBLEM_3": 
                CategoryAxis xBar = new CategoryAxis(); xBar.setLabel("Category");
                NumberAxis yBar = new NumberAxis(); yBar.setLabel("Count");
                BarChart<String, Number> barChart = new BarChart<>(xBar, yBar);
                XYChart.Series<String, Number> sPos = new XYChart.Series<>(); sPos.setName("Postive");
                XYChart.Series<String, Number> sNeg = new XYChart.Series<>(); sNeg.setName("Negative");
                for (Map.Entry<String, ? extends AnalysisResult> entry : results.entrySet()) {
                    int pVal = 0; int nVal = 0;
                    if (entry.getValue() instanceof SentimentResult) {
                        SentimentResult sr = (SentimentResult) entry.getValue();
                        pVal = sr.getPositiveCount(); nVal = sr.getNegativeCount();
                    } else if (entry.getValue() instanceof CountNum) {
                        nVal = ((CountNum) entry.getValue()).getCount();
                    }
                    if (pVal > 0 || nVal > 0) {
                        sPos.getData().add(new XYChart.Data<>(entry.getKey(), pVal));
                        sNeg.getData().add(new XYChart.Data<>(entry.getKey(), nVal));
                    }
                }
                barChart.getData().addAll(sPos, sNeg);
                return barChart;
        }
        return null;
    }

    private void switchSceneToChart(Node chartNode, String title) {
        BorderPane layout = new BorderPane();
        Button btnBack = new Button("⬅ Back");
        btnBack.setOnAction(e -> stageRef.setScene(dashboardScene));
        layout.setTop(new HBox(10, btnBack, new Label(title)));
        layout.setCenter(chartNode);
        stageRef.setScene(new Scene(layout, 900, 600));
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}