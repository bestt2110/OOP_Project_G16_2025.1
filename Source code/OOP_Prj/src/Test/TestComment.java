package Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

import Analysis.*;
import Data.FileCollector;
import Model.*;
import PreProcessor.*;

public class TestComment {

    public static void main(String[] args) {

        // =====================================
        // 1. THU THẬP DỮ LIỆU
        // =====================================
        FileCollector collector = new FileCollector();
        System.out.println("Đang đọc Posts...");
        List<Post> posts = collector.collect("youtubevideos.csv");
        System.out.println("Đã đọc " + posts.size() + " bài viết.");

        System.out.println("Đang đọc Comments...");
        collector.loadComments("data.csv", posts);

        // =====================================
        // 2. TIỀN XỬ LÝ COMMENT
        // =====================================
        PreProcessPipeline pipeline = new PreProcessPipeline();
        pipeline.addProcessor(new LowerCaseProcessor());
        pipeline.addProcessor(new SpecialSymbolRemover());

        // Gom tất cả comment
        List<Comment> allComments = new ArrayList<>();
        for (Post p : posts) {
            for (Comment c : p.getComments()) {
                pipeline.execute(c);  // xử lý content
                allComments.add(c);
            }
        }

        System.out.println("Tổng số comment gom lại: " + allComments.size());
        System.out.println("----------------------------------");

        // =====================================
        // 3. PHÂN LOẠI & ĐẾM
        // =====================================
        SatisfactionAnalysis engine = new SatisfactionAnalysis();
        Map<String, ReliefSentimentCount> storedResult = engine.execute(allComments);

        // =====================================
        // 4. IN KẾT QUẢ
        // =====================================
        System.out.println("Kết quả phân loại comment theo loại cứu trợ:");
        for (Map.Entry<String, ReliefSentimentCount> entry : storedResult.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }
}



package UI;

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
import javafx.scene.control.*;
import Analysis.*;
import Model.*;
import PreProcessor.*;
import Data.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        Button btnPie = new Button("Pie Chart");
        Button btnBar = new Button("Bar Chart");
        Button btnBack = new Button("< Back");

        btnLine.setPrefWidth(150);
        btnPie.setPrefWidth(150);
        btnBar.setPrefWidth(150);
        

        btnBack.setOnAction(e -> stageRef.setScene(homeScene));
        btnLine.setOnAction(e -> showChartScene(createLineChart()));
        btnPie.setOnAction(e -> showChartScene(createPieChart()));
        btnBar.setOnAction(e -> showChartScene(createBarChart()));
        

        VBox menuBox = new VBox(20, btnLine, btnPie, btnBar, btnBack);
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
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

/*
    // --------------------------------------------------
    // 4. TẠO LINE CHART
    // --------------------------------------------------
    private LineChart<String, Number> createLineChart() {
    	// =====================================
        // 1. TẢI & XỬ LÝ DỮ LIỆU
        // =====================================
        FileCollector collector = new FileCollector();
        List<Post> posts = collector.collect("youtubevideos.csv");
        collector.loadComments("data.csv", posts);

        PreProcessPipeline pipeline = new PreProcessPipeline();
        pipeline.addProcessor(new LowerCaseProcessor());
        pipeline.addProcessor(new SpecialSymbolRemover());

        List<Comment> allComments = new ArrayList<>();
        for (Post p : posts) {
            for (Comment c : p.getComments()) {
                pipeline.execute(c);
                allComments.add(c);
            }
        }

        SentimentOverTimeAnalysis engine = new SentimentOverTimeAnalysis();
        Map<LocalDate, SentimentCount> result = engine.execute(allComments);


    	Map<LocalDate, SentimentCount> sortedResult =
    			result.entrySet()
    			.stream()
    			.sorted(Map.Entry.comparingByKey())
    			.collect(Collectors.toMap(
    					Map.Entry::getKey,
    					Map.Entry::getValue,
    					(a, b) -> a,
    					java.util.LinkedHashMap::new
    					));


    	// =====================================
    	// 2. TẠO LINE CHART
    	// =====================================
    	CategoryAxis xAxis = new CategoryAxis();
    	NumberAxis yAxis = new NumberAxis();
    	xAxis.setLabel("Ngày");
    	yAxis.setLabel("Số lượng comment");


    	LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
    	chart.setTitle("Sentiment Over Time");


    	XYChart.Series<String, Number> positiveSeries = new XYChart.Series<>();
    	positiveSeries.setName("Positive");


    	XYChart.Series<String, Number> negativeSeries = new XYChart.Series<>();
    	negativeSeries.setName("Negative");


    	for (Map.Entry<LocalDate, SentimentCount> e : sortedResult.entrySet()) {
    		String day = e.getKey().toString();
    		positiveSeries.getData().add(new XYChart.Data<>(day, e.getValue().getPositive()));
    		negativeSeries.getData().add(new XYChart.Data<>(day, e.getValue().getNegative()));
    	}


    		chart.getData().addAll(positiveSeries, negativeSeries);
    		return chart;
    }
    */
    private VBox createLineChart() {
        // =====================================
        // 1. TẢI & XỬ LÝ DỮ LIỆU
        // =====================================
        FileCollector collector = new FileCollector();
        List<Post> posts = collector.collect("youtubevideos.csv");
        collector.loadComments("data.csv", posts);

        PreProcessPipeline pipeline = new PreProcessPipeline();
        pipeline.addProcessor(new LowerCaseProcessor());
        pipeline.addProcessor(new SpecialSymbolRemover());

        List<Comment> allComments = new ArrayList<>();
        for (Post p : posts) {
            for (Comment c : p.getComments()) {
                pipeline.execute(c);
                allComments.add(c);
            }
        }

        SentimentOverTimeAnalysis engine = new SentimentOverTimeAnalysis();
        Map<LocalDate, SentimentCount> result = engine.execute(allComments);

        Map<LocalDate, SentimentCount> sortedResult =
                result.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (a, b) -> a,
                                LinkedHashMap::new
                        ));

        // =====================================
        // 2. TẠO DATE PICKER
        // =====================================
        LocalDate minDate = sortedResult.keySet().iterator().next();
        LocalDate maxDate = sortedResult.keySet().stream().reduce((a,b)->b).orElse(minDate);

        DatePicker startPicker = new DatePicker(minDate);
        DatePicker endPicker = new DatePicker(maxDate);

        // =====================================
        // 3. TẠO CHART
        // =====================================
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setOpacity(0);
        yAxis.setLabel("Số lượng comment");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Sentiment Over Time");
        chart.setCreateSymbols(false);

        // =====================================
        // 4. NÚT VẼ BIỂU ĐỒ
        // =====================================
        Button btnDraw = new Button("Vẽ biểu đồ");
        btnDraw.setOnAction(e -> {
            LocalDate start = startPicker.getValue();
            LocalDate end = endPicker.getValue();

            // Validate ngày
            if (start == null || end == null) {
                showError("Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc!");
                return;
            }
            if (start.isAfter(end)) {
                showError("Ngày bắt đầu không được lớn hơn ngày kết thúc!");
                return;
            }

            // Lọc theo khoảng ngày
            Map<LocalDate, SentimentCount> filtered = sortedResult.entrySet()
                    .stream()
                    .filter(x -> !x.getKey().isBefore(start) && !x.getKey().isAfter(end))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));

            if (filtered.isEmpty()) {
                showError("Không có comment trong khoảng ngày đã chọn!");
                return;
            }

            // Xóa dữ liệu cũ
            chart.getData().clear();

            // Series mới
            XYChart.Series<String, Number> pos = new XYChart.Series<>();
            pos.setName("Positive");
            XYChart.Series<String, Number> neg = new XYChart.Series<>();
            neg.setName("Negative");

            int index = 1;
            for (Map.Entry<LocalDate, SentimentCount> entry : filtered.entrySet()) {
                SentimentCount sc = entry.getValue();
                pos.getData().add(new XYChart.Data<>(String.valueOf(index), sc.getPositive()));
                neg.getData().add(new XYChart.Data<>(String.valueOf(index), sc.getNegative()));
                index++;
            }

            chart.getData().addAll(pos, neg);
        });

        // =====================================
        // 5. GOM LAYOUT UI
        // =====================================
        VBox box = new VBox(10,
                new Label("Chọn khoảng thời gian:"),
                startPicker,
                endPicker,
                btnDraw,
                chart
        );
        box.setPadding(new Insets(10));

        return box;
    }

    // --------------------------------------------------
    // 5. TẠO BAR CHART
    // --------------------------------------------------
    private BarChart<String, Number> createBarChart() {

        // =====================================
        // 1. THU THẬP & TIỀN XỬ LÝ DỮ LIỆU
        // =====================================
        FileCollector collector = new FileCollector();
        List<Post> posts = collector.collect("youtubevideos.csv");
        collector.loadComments("data.csv", posts);

        PreProcessPipeline pipeline = new PreProcessPipeline();
        pipeline.addProcessor(new LowerCaseProcessor());
        pipeline.addProcessor(new SpecialSymbolRemover());

        // Gom tất cả comment
        List<Comment> allComments = new ArrayList<>();
        for (Post p : posts) {
            for (Comment c : p.getComments()) {
                pipeline.execute(c);  // xử lý content
                allComments.add(c);
            }
        }

        // =====================================
        // 2. PHÂN LOẠI & ĐẾM POS/NEG
        // =====================================
        SatisfactionAnalysis engine = new SatisfactionAnalysis();
        Map<String, ReliefSentimentCount> storedResult = engine.execute(allComments);

        // =====================================
        // 3. TẠO BAR CHART
        // =====================================
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Loại cứu trợ");
        yAxis.setLabel("Số lượng comment");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Số lượng comment Positive & Negative theo loại cứu trợ");

        XYChart.Series<String, Number> positiveSeries = new XYChart.Series<>();
        positiveSeries.setName("Positive");

        XYChart.Series<String, Number> negativeSeries = new XYChart.Series<>();
        negativeSeries.setName("Negative");

        // Thêm dữ liệu từ storedResult
        for (Map.Entry<String, ReliefSentimentCount> entry : storedResult.entrySet()) {
            String category = entry.getKey();
            ReliefSentimentCount count = entry.getValue();

            positiveSeries.getData().add(new XYChart.Data<>(category, count.getPositive()));
            negativeSeries.getData().add(new XYChart.Data<>(category, count.getNegative()));
        }

        chart.getData().addAll(positiveSeries, negativeSeries);
        return chart;
    }

    // --------------------------------------------------
    // 6. TẠO PIE CHART
    // --------------------------------------------------
    public PieChart createPieChart() {

        // =====================================
        // 1. TẢI & XỬ LÝ DỮ LIỆU
        // =====================================
        FileCollector collector = new FileCollector();
        List<Post> posts = collector.collect("youtubevideos.csv");
        collector.loadComments("data.csv", posts);

        PreProcessPipeline pipeline = new PreProcessPipeline();
        pipeline.addProcessor(new LowerCaseProcessor());
        pipeline.addProcessor(new SpecialSymbolRemover());

        // Gom tất cả comment
        List<Comment> allComments = new ArrayList<>();
        for (Post p : posts) {
            for (Comment c : p.getComments()) {
                pipeline.execute(c);
                allComments.add(c);
            }
        }

        // Phân loại và đếm comment theo category
        DamageCategoryAnalysis engine = new DamageCategoryAnalysis();
        Map<String, Long> result = engine.execute(allComments);

        // =====================================
        // 2. TẠO PIE CHART CHỈ 5 CATEGORY
        // =====================================
        PieChart chart = new PieChart();
        chart.setTitle("Phân loại comment theo category");

        // Danh sách 5 category chính
        List<String> mainCategories = Arrays.asList(
                "Affected People",
                "Damaged Infrastructure",
                "Houses or Buildings Damaged",
                "Loss of Personal Belongings",
                "Disruption of Economic Production"
        );

        for (String category : mainCategories) {
            Long count = result.get(category);
            if (count != null && count > 0) {
                chart.getData().add(new PieChart.Data(category, count));
            }
        }

        return chart;
    }


    public static void main(String[] args) {
        launch(args);
    }
}