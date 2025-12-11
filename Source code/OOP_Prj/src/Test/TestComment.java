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