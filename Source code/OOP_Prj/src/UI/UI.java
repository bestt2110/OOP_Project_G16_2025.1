package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import Analysis.*;
import Model.*;
import PreProcessor.*;
import Data.*;
import App.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UI extends Application {

    private Scene homeScene;    
    private Scene mainScene;    
    private Scene chartScene;   
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

    // ==============================
    // 1. GIAO DIỆN TRANG CHỦ
    // ==============================
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

    // ==============================
    // 2. GIAO DIỆN CHỌN BIỂU ĐỒ
    // ==============================
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

        // Cho chart giãn theo BorderPane
        if (chart instanceof PieChart) {
            ((PieChart) chart).setAnimated(true);
        } else if (chart instanceof BarChart) {
            ((BarChart<?, ?>) chart).setAnimated(true);
            ((BarChart<?, ?>) chart).setCategoryGap(20);
            ((BarChart<?, ?>) chart).setBarGap(5);
        } else if (chart instanceof VBox) {
            VBox vbox = (VBox) chart;
            VBox.setVgrow(vbox.getChildren().get(vbox.getChildren().size()-1), Priority.ALWAYS);
        }

        chartScene = new Scene(root, width, height);
        stageRef.setScene(chartScene);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private VBox createLineChart() {
        // ------------------------------
        // Thu thập dữ liệu
        // ------------------------------
    	IDataCollector collector = null;
        String outputPrefix = "data";
    	ComboBox<String> sourceComboBox = new ComboBox<>();
        sourceComboBox.getItems().addAll("ytb", "vn", "both");
        sourceComboBox.setValue("both");

        // (OPTIONAL) xử lý ngay khi đổi lựa chọn
        sourceComboBox.setOnAction(e -> {
            String selected = sourceComboBox.getValue();

            switch (selected) {
                case "ytb":
                	collector = new VnExpressCollector();
                    outputPrefix = "vnexpress";
                    List<Post> records = collector.collect(keywords, startDate, endDate);
                    PreProcessPipeline pipeline = new PreProcessPipeline();
                    pipeline.addProcessor(new LowerCaseProcessor());   
                    pipeline.addProcessor(new SpecialSymbolRemover());   
                    pipeline.addProcessor(new VietnameseNormalizer());
                    pipeline.addProcessor(new StopWordsRemover("stopwords.txt"));   
                    
                    //List<Comment> allComments = new ArrayList<>();
                    
                    for (Post p : records) {
                    	System.out.println("Bắt đầu tiền xử lí post " + p.getId());
                    	pipeline.execute(p);
                    	System.out.println("Tiền xử lí post " + p.getId() + " hoàn thành");
                        if (!p.getComments().isEmpty()) {
                            System.out.println("Post " + p.getId() + " có " + p.getComments().size() + " bình luận:");
                            List<Comment> commentlist = p.getComments();
                            for (Comment c: commentlist) {
                            	pipeline.execute(c);
                            	//allComments.add(c);
                                System.out.println("   - [" + c.getId() + "] " + c.getCleanContent());
                            }
                        }
                    }
                    
                    SentimentOverTimeAnalysis engine = new SentimentOverTimeAnalysis();
                    //DamageCategoryAnalysis engine = new DamageCategoryAnalysis();
                    //SatisfactionAnalysis engine = new SatisfactionAnalysis();
                    HashMap<String, SentimentResult> result = new HashMap<>();
                    for (Post p: records) {
                    	engine.execute(p,result);
                    }
                    break;
                case "vn":
                    // TODO: xử lý khi chọn VNExpress
                    break;
                case "both":
                    // TODO: xử lý khi chọn cả hai
                    break;
            }
        });
        /*
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
	*/
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

        // ------------------------------
        // DatePicker chọn khoảng thời gian
        // ------------------------------
        LocalDate minDate = sortedResult.keySet().iterator().next();
        LocalDate maxDate = sortedResult.keySet().stream().reduce((a, b) -> b).orElse(minDate);

        DatePicker startPicker = new DatePicker(minDate);
        DatePicker endPicker = new DatePicker(maxDate);

        // ------------------------------
        // LineChart
        // ------------------------------
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số lượng comment");
        
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setOpacity(0);
        
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Sentiment Over Time");
        chart.setCreateSymbols(false);

        // ------------------------------
        // Button vẽ chart
        // ------------------------------
        Button btnDraw = new Button("Vẽ biểu đồ");
        btnDraw.setOnAction(e -> {
            LocalDate start = startPicker.getValue();
            LocalDate end = endPicker.getValue();

            if (start == null || end == null) {
                showError("Vui lòng chọn đầy đủ ngày!");
                return;
            }
            if (start.isAfter(end)) {
                showError("Ngày bắt đầu không được lớn hơn ngày kết thúc!");
                return;
            }

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

            chart.getData().clear();

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

        VBox box = new VBox(10,
                new Label("Chọn khoảng thời gian:"),
                startPicker,
                endPicker,
                btnDraw,
                chart
        );
        box.setPadding(new Insets(10));
        VBox.setVgrow(chart, Priority.ALWAYS);

        return box;
    }
    
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