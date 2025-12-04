package TestBaBai;

import Model.*;
import Data.*;
import PreProcessor.*;
import java.util.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class TestEx1 extends Application {

    // ============================
    // 1. SENTIMENT DICTIONARIES
    // ============================
    private static final Set<String> negativeWords = new HashSet<>(Arrays.asList(
            "bão", "mất", "bay", "khiếp", "hồn", "ám", "kinh", "khóc",
            "phá", "tàn", "hỏng", "buồn"
    ));

    private static final Set<String> positiveWords = new HashSet<>(Arrays.asList(
            "ơn", "ổn", "không", "an", "may", "vui"
    ));

    // ============================
    // STORAGE FOR CHART
    // ============================
    private static Map<LocalDate, Integer> positiveMap;
    private static Map<LocalDate, Integer> negativeMap;


    // ============================
    // 2. SENTIMENT CHECKER
    // ============================
    public static boolean isPositive(String sentence) {
        if (sentence == null) return false;

        String[] words = sentence.split("[\\s\\p{Punct}]+");
        int pos = 0, neg = 0;

        for (String w : words) {
            if (positiveWords.contains(w)) pos++;
            if (negativeWords.contains(w)) neg++;
        }
        return pos > neg;
    }

    // ============================
    // 3. AGGREGATOR (GOM THEO NGÀY)
    // ============================
    public static class SentimentAggregator {

        Map<LocalDate, Integer> posDay = new TreeMap<>();
        Map<LocalDate, Integer> negDay = new TreeMap<>();

        public void processAll(List<Post> posts, PreProcessPipeline pipeline) {

            for (Post post : posts) {

                // Xử lý bài post
                pipeline.execute(post);
                addSentence(post.getCleanContent(), post.getTimestamp());

                // Xử lý comment
                for (Comment c : post.getComments()) {
                    pipeline.execute(c);
                    addSentence(c.getCleanContent(), c.getTimestamp());
                }
            }
        }

        private void addSentence(String sentence, LocalDateTime timestamp) {
            if (sentence == null || sentence.isEmpty()) return;

            LocalDate day = timestamp.toLocalDate();

            if (isPositive(sentence)) {
                posDay.put(day, posDay.getOrDefault(day, 0) + 1);
            } else {
                negDay.put(day, negDay.getOrDefault(day, 0) + 1);
            }
        }
    }

    // ============================
    // 4. START JAVAFX (LINE CHART)
    // ============================
    @Override
    public void start(Stage stage) {

        stage.setTitle("Daily Sentiment Chart");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Ngày");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số lượng câu");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Positive vs Negative Sentences Per Day");

        // Series Positive
        XYChart.Series<String, Number> posSeries = new XYChart.Series<>();
        posSeries.setName("Positive");

        for (LocalDate d : positiveMap.keySet()) {
            posSeries.getData().add(new XYChart.Data<>(d.toString(), positiveMap.get(d)));
        }

        // Series Negative
        XYChart.Series<String, Number> negSeries = new XYChart.Series<>();
        negSeries.setName("Negative");

        for (LocalDate d : negativeMap.keySet()) {
            negSeries.getData().add(new XYChart.Data<>(d.toString(), negativeMap.get(d)));
        }

        chart.getData().addAll(posSeries, negSeries);

        Scene scene = new Scene(chart, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    // ============================
    // 5. MAIN LOGIC
    // ============================
    public static void main(String[] args) {

        try {
            FileCollector collector = new FileCollector();

            System.out.println("Đang đọc Posts...");
            List<Post> posts = collector.collect(
                    "C:\\Users\\admin\\Pictures\\OOP_Project_G16_2025.1\\Source code\\OOP_Prj\\youtubevideos.csv"
            );
            System.out.println("Đã đọc " + posts.size() + " bài viết.");

            System.out.println("Đang đọc Comments...");
            collector.loadComments(
                    "C:\\Users\\admin\\Pictures\\OOP_Project_G16_2025.1\\Source code\\OOP_Prj\\data.csv",
                    posts
            );

            // Preprocess pipeline
            PreProcessPipeline pipeline = new PreProcessPipeline();
            pipeline.addProcessor(new LowerCaseProcessor());
            pipeline.addProcessor(new SpecialSymbolRemover());

            // Aggregator
            SentimentAggregator aggr = new SentimentAggregator();
            aggr.processAll(posts, pipeline);

            positiveMap = aggr.posDay;
            negativeMap = aggr.negDay;

            // Chạy JavaFX
            Application.launch(Main.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


