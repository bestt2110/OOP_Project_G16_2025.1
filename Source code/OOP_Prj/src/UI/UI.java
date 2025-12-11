package UI;
/*
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class UI extends Application {

    private Scene mainScene;   // Màn hình chọn 3 nút
    private Scene chartScene;  // Màn hình biểu đồ
    private Stage stageRef;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("UI JavaFX: Chọn biểu đồ");
        stageRef = primaryStage;

        // ------------------------------
        // GIAO DIỆN 1: CHỈ HIỂN THỊ 3 NÚT
        // ------------------------------
        Button btnLine = new Button("Bài 1");
        Button btnBar = new Button("Bài 2");
        Button btnPie = new Button("Bài 3");

        btnLine.setPrefWidth(150);
        btnBar.setPrefWidth(150);
        btnPie.setPrefWidth(150);

        VBox menuBox = new VBox(20, btnLine, btnBar, btnPie);
        menuBox.setAlignment(Pos.CENTER);

        StackPane mainRoot = new StackPane(menuBox);
        mainRoot.setPadding(new Insets(20));

        mainScene = new Scene(mainRoot);

        // Hành động khi ấn các nút — chuyển sang màn hình biểu đồ tương ứng
        btnLine.setOnAction(e -> showChartScene(createLineChart()));
        btnBar.setOnAction(e -> showChartScene(createBarChart()));
        btnPie.setOnAction(e -> showChartScene(createPieChart()));

        primaryStage.setScene(mainScene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    // ------------------------------
    // HIỆN SCENE BIỂU ĐỒ + NÚT BACK
    // ------------------------------
    private void showChartScene(javafx.scene.Node chart) {
        double width = stageRef.getWidth();
        double height = stageRef.getHeight();

        Button btnBack = new Button("< Back");
        btnBack.setOnAction(e -> {
            stageRef.setScene(mainScene);
            stageRef.setWidth(width);
            stageRef.setHeight(height);
        });

        BorderPane chartRoot = new BorderPane();
        chartRoot.setPadding(new Insets(10));
        chartRoot.setTop(btnBack);
        BorderPane.setMargin(btnBack, new Insets(5));
        chartRoot.setCenter(chart);

        chartScene = new Scene(chartRoot);

        stageRef.setScene(chartScene);
        stageRef.setWidth(width);
        stageRef.setHeight(height);
    }

    // ------------------------------
    // TẠO LINE CHART
    // ------------------------------
    private LineChart<Number, Number> createLineChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Thời điểm (ms)");
        yAxis.setLabel("Giá trị");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Line Chart: Dữ liệu mẫu");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Series A");
        series.getData().add(new XYChart.Data<>(0, 23));
        series.getData().add(new XYChart.Data<>(1, 14));
        series.getData().add(new XYChart.Data<>(2, 15));
        series.getData().add(new XYChart.Data<>(3, 24));
        series.getData().add(new XYChart.Data<>(4, 34));
        series.getData().add(new XYChart.Data<>(5, 36));

        lineChart.getData().add(series);
        return lineChart;
    }

    // ------------------------------
    // TẠO BAR CHART
    // ------------------------------
    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Quý");
        yAxis.setLabel("Doanh thu (k)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Bar Chart: Doanh thu theo quý");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("2025");
        series1.getData().add(new XYChart.Data<>("Q1", 150));
        series1.getData().add(new XYChart.Data<>("Q2", 200));
        series1.getData().add(new XYChart.Data<>("Q3", 180));
        series1.getData().add(new XYChart.Data<>("Q4", 220));

        barChart.getData().add(series1);
        return barChart;
    }

    // ------------------------------
    // TẠO PIE CHART
    // ------------------------------
    private PieChart createPieChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Pie Chart: Thị phần");
        pieChart.getData().add(new PieChart.Data("Nhóm A", 30));
        pieChart.getData().add(new PieChart.Data("Nhóm B", 25));
        pieChart.getData().add(new PieChart.Data("Nhóm C", 20));
        pieChart.getData().add(new PieChart.Data("Nhóm D", 25));
        return pieChart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
*/
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class UI extends Application {

    private Scene homeScene;    // Scene giao diện chính
    private Scene mainScene;    // Scene 3 nút chọn biểu đồ
    private Scene chartScene;   // Scene hiển thị biểu đồ
    private Stage stageRef;

    @Override
    public void start(Stage primaryStage) {
        stageRef = primaryStage;
        primaryStage.setTitle("Java Project - Group 16");

        createHomeScene();
        createMainScene();

        primaryStage.setScene(homeScene);
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    // --------------------------------------------------
    // 1. GIAO DIỆN TRANG CHỦ
    // --------------------------------------------------
    private void createHomeScene() {
        Label title = new Label("JAVA PROJECT BY GROUP 16");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Button btnAnalysis = new Button("Phân tích dữ liệu");
        Button btnMembers = new Button("Thông tin thành viên");
        Button btnExit = new Button("Exit");

        btnAnalysis.setPrefWidth(200);
        btnMembers.setPrefWidth(200);
        btnExit.setPrefWidth(200);

        btnAnalysis.setOnAction(e -> stageRef.setScene(mainScene));
        btnExit.setOnAction(e -> stageRef.close());

        VBox box = new VBox(25, title, btnAnalysis, btnMembers, btnExit);
        box.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(box);
        root.setPadding(new Insets(20));

        homeScene = new Scene(root);
    }

    // --------------------------------------------------
    // 2. GIAO DIỆN CHỌN 3 LOẠI BIỂU ĐỒ
    // --------------------------------------------------
    private void createMainScene() {
        Button btnLine = new Button("Line Chart");
        Button btnBar = new Button("Bar Chart");
        Button btnPie = new Button("Pie Chart");
        Button btnBack = new Button("< Back");

        btnLine.setPrefWidth(150);
        btnBar.setPrefWidth(150);
        btnPie.setPrefWidth(150);

        btnBack.setOnAction(e -> stageRef.setScene(homeScene));
        btnLine.setOnAction(e -> showChartScene(createLineChart()));
        btnBar.setOnAction(e -> showChartScene(createBarChart()));
        btnPie.setOnAction(e -> showChartScene(createPieChart()));

        VBox menuBox = new VBox(20, btnLine, btnBar, btnPie, btnBack);
        menuBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(menuBox);
        root.setPadding(new Insets(20));

        mainScene = new Scene(root);
    }

    // --------------------------------------------------
    // 3. HIỂN THỊ SCENE BIỂU ĐỒ + NÚT BACK
    // --------------------------------------------------
    private void showChartScene(javafx.scene.Node chart) {
        double width = stageRef.getWidth();
        double height = stageRef.getHeight();

        Button btnBack = new Button("< Back");
        btnBack.setOnAction(e -> {
            stageRef.setScene(mainScene);
            stageRef.setWidth(width);
            stageRef.setHeight(height);
        });

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(btnBack);
        BorderPane.setMargin(btnBack, new Insets(5));
        root.setCenter(chart);

        chartScene = new Scene(root);

        stageRef.setScene(chartScene);
        stageRef.setWidth(width);
        stageRef.setHeight(height);
    }

    // --------------------------------------------------
    // 4. TẠO LINE CHART
    // --------------------------------------------------
    private LineChart<Number, Number> createLineChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Thời điểm (ms)");
        yAxis.setLabel("Giá trị");

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Line Chart: Dữ liệu mẫu");

        XYChart.Series<Number, Number> s = new XYChart.Series<>();
        s.setName("Series A");
        s.getData().add(new XYChart.Data<>(0, 23));
        s.getData().add(new XYChart.Data<>(1, 14));
        s.getData().add(new XYChart.Data<>(2, 15));
        s.getData().add(new XYChart.Data<>(3, 24));
        s.getData().add(new XYChart.Data<>(4, 34));
        s.getData().add(new XYChart.Data<>(5, 36));

        chart.getData().add(s);
        return chart;
    }

    // --------------------------------------------------
    // 5. TẠO BAR CHART
    // --------------------------------------------------
    private BarChart<String, Number> createBarChart() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        x.setLabel("Quý");
        y.setLabel("Doanh thu (k)");

        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setTitle("Bar Chart: Doanh thu theo quý");

        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("2025");
        s.getData().add(new XYChart.Data<>("Q1", 150));
        s.getData().add(new XYChart.Data<>("Q2", 200));
        s.getData().add(new XYChart.Data<>("Q3", 180));
        s.getData().add(new XYChart.Data<>("Q4", 220));

        chart.getData().add(s);
        return chart;
    }

    // --------------------------------------------------
    // 6. TẠO PIE CHART
    // --------------------------------------------------
    private PieChart createPieChart() {
        PieChart chart = new PieChart();
        chart.setTitle("Pie Chart: Thị phần");
        chart.getData().add(new PieChart.Data("Nhóm A", 30));
        chart.getData().add(new PieChart.Data("Nhóm B", 25));
        chart.getData().add(new PieChart.Data("Nhóm C", 20));
        chart.getData().add(new PieChart.Data("Nhóm D", 25));
        return chart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

