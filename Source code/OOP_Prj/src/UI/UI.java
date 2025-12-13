package UI;

import App.Main; 
import Analysis.*;
import Model.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class UI extends Application {

    private Stage stageRef;
    
    // Khai báo các Scene
    private Scene homeScene;       
    private Scene dashboardScene;  
    
    // DatePicker toàn cục
    private DatePicker dpStart;
    private DatePicker dpEnd; 
    
    // Controls
    private Label lblStatus;
    private Button btnP1, btnP2, btnP3; 
    private Button btnRun;
    private ProgressIndicator progressIndicator;

    @Override
    public void start(Stage primaryStage) {
        stageRef = primaryStage;
        primaryStage.setTitle("Java Project - Group 16");

        // 1. Khởi tạo giao diện
        createHomeScene();
        createDashboardScene(); 

        // 2. Mặc định vào trang chủ
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    // ==============================
    // 1. GIAO DIỆN TRANG CHỦ (HOME)
    // ==============================
    private void createHomeScene() {
        Label title = new Label("JAVA PROJECT BY GROUP 16");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button btnAnalysis = new Button("Data Analysis");
        Button btnExit = new Button("Exit");

        String btnStyle = "-fx-font-size: 16px; -fx-pref-width: 250px; -fx-pref-height: 40px; -fx-cursor: hand;";
        btnAnalysis.setStyle(btnStyle);
        btnExit.setStyle(btnStyle);

        // Sự kiện
        btnAnalysis.setOnAction(e -> {
            // Nếu Main đã chạy xong từ Console thì cập nhật trạng thái ngay
            if (!Main.globalData.isEmpty()) {
                lblStatus.setText("✅ " + Main.globalData.size() + " collected");
                enableAnalysisButtons();
            }
            stageRef.setScene(dashboardScene);
        });
        
        btnExit.setOnAction(e -> {
            System.exit(0);
        });

        VBox box = new VBox(25, title, btnAnalysis, btnExit);
        box.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(box);
        root.setPadding(new Insets(20));

        homeScene = new Scene(root, 900, 600);
    }

    // ==============================
    // 2. GIAO DIỆN CHÍNH (DASHBOARD)
    // ==============================
    private void createDashboardScene() {
        // --- HEADER ---
        // Nút quay về Home
        Button btnHome = new Button("⬅ Home Screen");
        btnHome.setStyle("-fx-font-size: 14px; -fx-cursor: hand; -fx-base: #ecf0f1;");
        btnHome.setOnAction(e -> stageRef.setScene(homeScene));

        Label lblTitle = new Label("DATA ANALYSIS DASHBOARD");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        lblTitle.setStyle("-fx-text-fill: #2c3e50;");

        // Đặt nút Home và Title cùng 1 hàng
        BorderPane header = new BorderPane();
        header.setLeft(btnHome);
        header.setCenter(lblTitle);
        // Căn chỉnh để Title vẫn ở giữa dù có nút Home bên trái
        BorderPane.setAlignment(lblTitle, Pos.CENTER);
        BorderPane.setMargin(lblTitle, new Insets(0, 100, 0, 0));

        // --- INPUT SECTION ---
        VBox inputBox = new VBox(10);
        inputBox.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 15; -fx-background-radius: 5;");
        
        ComboBox<String> cbbSource = new ComboBox<>();
        cbbSource.getItems().addAll("VnExpress", "YouTube", "File CSV (Offline)");
        cbbSource.setValue("VnExpress");

        TextField txtKw = new TextField("bão Yagi"); 
        txtKw.setPromptText("Type keywords...");
        txtKw.setPrefWidth(200);

        this.dpStart = new DatePicker(LocalDate.of(2025, 1, 1));
        this.dpEnd = new DatePicker(LocalDate.now());

        HBox row1 = new HBox(10, new Label("Source:"), cbbSource, new Label("Keywords:"), txtKw);
        row1.setAlignment(Pos.CENTER_LEFT);
        HBox row2 = new HBox(10, new Label("From:"), dpStart, new Label("To:"), dpEnd);
        row2.setAlignment(Pos.CENTER_LEFT);
        
        inputBox.getChildren().addAll(new Label("Collector"), row1, row2);

        // --- BUTTONS ---
        btnRun = new Button("▶ Start");
        btnRun.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnRun.setPrefWidth(200);
        
        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(30, 30);
        progressIndicator.setVisible(false);

        lblStatus = new Label("Ready to start");
        lblStatus.setStyle("-fx-text-fill: #7f8c8d;");

        HBox statusBox = new HBox(10, btnRun, progressIndicator, lblStatus);
        statusBox.setAlignment(Pos.CENTER_LEFT);

        Label lblChart = new Label("DATA ANALYSIS");
        lblChart.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        btnP1 = createChartButton("Sentiment Over Time", "PROBLEM_1");
        btnP2 = createChartButton("Damage Category Analysis", "PROBLEM_2");
        btnP3 = createChartButton("Satisfaction Analysis", "PROBLEM_3");

        HBox chartBox = new HBox(20, btnP1, btnP2, btnP3);
        chartBox.setAlignment(Pos.CENTER);

        // Sự kiện chạy
        btnRun.setOnAction(e -> {
            if (dpStart.getValue().isAfter(dpEnd.getValue())) {
                showError("Starting day can't be before the ending day"); 
                return;
            }
            Date start = Date.from(dpStart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(dpEnd.getValue().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

            btnRun.setDisable(true);
            progressIndicator.setVisible(true);
            
            Main.processDataRequest(cbbSource.getValue(), txtKw.getText(), start, end, this);
        });

        VBox root = new VBox(20, header, inputBox, statusBox, new Separator(), lblChart, chartBox);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        this.dashboardScene = new Scene(root, 900, 600);
    }

    private Button createChartButton(String text, String type) {
        Button btn = new Button(text);
        btn.setPrefSize(200, 50);
        btn.setDisable(true); 
        btn.setStyle("-fx-cursor: hand;");
        btn.setOnAction(e -> showChart(type));
        return btn;
    }

    // Callbacks
    public void updateStatus(String msg) { 
        javafx.application.Platform.runLater(() -> lblStatus.setText(msg));
    }

    public void enableAnalysisButtons() {
        javafx.application.Platform.runLater(() -> {
            btnP1.setDisable(false);
            btnP2.setDisable(false);
            btnP3.setDisable(false);
            btnRun.setDisable(false);
            progressIndicator.setVisible(false);
        });
    }

    // =======================================================
    // [MODIFIED] TÁCH LOGIC ĐỂ HỖ TRỢ STRICT TYPE (GENERICS)
    // =======================================================
    private void showChart(String problemType) {
        List<Post> data = Main.globalData;
        if (data == null || data.isEmpty()) {
            showError("No data!"); return;
        }

        System.out.println("Running " + problemType + "...");
        Node chartNode = null;
        String title = "Analysis Result";

        // Tách riêng từng case để khởi tạo đúng loại Map cụ thể
        switch (problemType) {
            case "PROBLEM_1":
                // 1. Tạo Map chuyên dụng cho SentimentResult
                Map<String, SentimentResult> res1 = new HashMap<>();
                SentimentOverTimeAnalysis task1 = new SentimentOverTimeAnalysis();
                
                // 2. Chạy execute (không cần ép kiểu trong task nữa)
                for (Post p : data) task1.execute(p, res1);
                
                // 3. Tạo biểu đồ
                chartNode = createChartNode(problemType, res1);
                title = "Sentiment Over Time";
                break;

            case "PROBLEM_2":
                // 1. Tạo Map chuyên dụng cho B2Result
                Map<String, B2Result> res2 = new HashMap<>();
                DamageCategoryAnalysis task2 = new DamageCategoryAnalysis();
                
                for (Post p : data) task2.execute(p, res2);
                
                chartNode = createChartNode(problemType, res2);
                title = "Damage Category Analysis";
                break;

            case "PROBLEM_3":
                // 1. Tạo Map chuyên dụng cho SentimentResult (Bài 3 dùng Sentiment)
                Map<String, SentimentResult> res3 = new HashMap<>();
                SatisfactionAnalysis task3 = new SatisfactionAnalysis();
                
                for (Post p : data) task3.execute(p, res3);
                
                chartNode = createChartNode(problemType, res3);
                title = "Satisfaction Analysis";
                break;
        }

        if (chartNode != null) {
            switchSceneToChart(chartNode, title);
        }
    }

    // [MODIFIED] Sửa tham số thành Wildcard (? extends AnalysisResult)
    private Node createChartNode(String problemType, Map<String, ? extends AnalysisResult> results) {
        if (results.isEmpty()) return new StackPane(new Label("Không có dữ liệu."));

        switch (problemType) {
            case "PROBLEM_1": 
                CategoryAxis xAxis = new CategoryAxis(); xAxis.setLabel("Time");
                NumberAxis yAxis = new NumberAxis(); yAxis.setLabel("Count");
                LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
                lineChart.setTitle("Xu hướng Cảm xúc (Raw Data)");
                lineChart.setCreateSymbols(false); 
                XYChart.Series<String, Number> pos = new XYChart.Series<>(); pos.setName("Positive");
                XYChart.Series<String, Number> neg = new XYChart.Series<>(); neg.setName("Negative");
                
                // Dùng TreeMap để sắp xếp ngày tháng
                TreeMap<String, ? extends AnalysisResult> sortedLine = new TreeMap<>(results);
                
                for (Map.Entry<String, ? extends AnalysisResult> entry : sortedLine.entrySet()) {
                    if (entry.getKey() == null) continue;
                    
                    // Vẫn cần instanceof ở đây vì hàm này vẽ chung
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
                pieChart.setTitle("Damage Type Proportion");
                for (Map.Entry<String, ? extends AnalysisResult> entry : results.entrySet()) {
                    if (entry.getValue() instanceof B2Result) {
                        int count = ((B2Result) entry.getValue()).getCount();
                        if (count > 0) pieChart.getData().add(new PieChart.Data(entry.getKey(), count));
                    }
                }
                return pieChart;

            case "PROBLEM_3": 
                CategoryAxis xBar = new CategoryAxis(); xBar.setLabel("Category");
                NumberAxis yBar = new NumberAxis(); yBar.setLabel("Satisfaction");
                BarChart<String, Number> barChart = new BarChart<>(xBar, yBar);
                barChart.setTitle("Thống kê Nhu cầu / Hài lòng");
                barChart.setCategoryGap(20);
                XYChart.Series<String, Number> sPos = new XYChart.Series<>(); sPos.setName("Postive");
                XYChart.Series<String, Number> sNeg = new XYChart.Series<>(); sNeg.setName("Negative");
                
                for (Map.Entry<String, ? extends AnalysisResult> entry : results.entrySet()) {
                    if (entry.getKey() == null) continue;
                    int pVal = 0; int nVal = 0;
                    
                    if (entry.getValue() instanceof SentimentResult) {
                        SentimentResult sr = (SentimentResult) entry.getValue();
                        pVal = sr.getPositiveCount(); nVal = sr.getNegativeCount();
                    } else if (entry.getValue() instanceof B2Result) {
                        nVal = ((B2Result) entry.getValue()).getCount();
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
        Button btnBack = new Button("⬅ Back to Dashboard");
        btnBack.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");
        btnBack.setOnAction(e -> {
            if (dashboardScene != null) stageRef.setScene(dashboardScene);
        });
        
        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        HBox topBox = new HBox(20, btnBack, lblTitle);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(10));
        topBox.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 0 0 1 0;");
        
        layout.setTop(topBox);
        layout.setCenter(chartNode);
        stageRef.setScene(new Scene(layout, 900, 600));
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}