package App;

import Data.*;
import Model.*;
import PreProcessor.*;
import Analysis.*;
import UI.UI;
import javafx.application.Application;
import javafx.application.Platform;

import java.util.*;

public class Main {
    
    // --- KHO DỮ LIỆU TẬP TRUNG ---
    public static List<Post> globalData = new ArrayList<>();
    
    public static void main(String[] args) {
        Application.launch(UI.class, args);
    }

    // =========================================================
    // HÀM XỬ LÝ LOGIC TRUNG TÂM (KHÔNG LỌC NGÀY)
    // =========================================================
    public static void processDataRequest(String source, String keywords, Date reqStart, Date reqEnd, UI uiController) {
        
        new Thread(() -> {
            try {
                // 1. Cập nhật trạng thái
                Platform.runLater(() -> uiController.updateStatus("⏳ Connecting to " + source + "..."));

                // 2. Chọn Collector
                IDataCollector collector;
                List<String> kwList = (keywords != null && !keywords.isEmpty()) 
                        ? Arrays.asList(keywords.split(",")) : new ArrayList<>();

                if (source.contains("File")) {
                    collector = new FileCollector("vnexpress_posts.csv"); 
                } else if (source.contains("YouTube")) {
                    collector = new YouTubeCollector();
                } else {
                    collector = new VnExpressCollector();
                }

                // 3. Thu thập dữ liệu
                collector.initialize(new HashMap<>());
                
                List<Post> rawPosts = collector.collect(kwList, reqStart, reqEnd);

                if (rawPosts.isEmpty()) {
                    Platform.runLater(() -> {
                        uiController.updateStatus("⚠️ No posts found.");
                        globalData.clear();
                    });
                    return;
                }

                Platform.runLater(() -> uiController.updateStatus("🧹 Cleaning " + rawPosts.size() + " posts..."));

                // 4. Tiền xử lý (Pipeline) - Làm sạch text
                PreProcessPipeline pipeline = new PreProcessPipeline();
                pipeline.addProcessor(new LowerCaseProcessor());
                pipeline.addProcessor(new SpecialSymbolRemover());
                // pipeline.addProcessor(new VietnameseNormalizer());

                for (Post p : rawPosts) {
                    pipeline.execute(p);
                    if (p.getComments() != null) {
                        for (Comment c : p.getComments()) pipeline.execute(c);
                    }
                }

                // 5. Cập nhật vào Kho dữ liệu chung (RAW)
                globalData = rawPosts;

                // 6. Báo cáo hoàn tất
                Platform.runLater(() -> {
                    uiController.updateStatus("✅ Done " + globalData.size() + " collected");
                    uiController.enableAnalysisButtons(); 
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> uiController.updateStatus("❌ Lỗi: " + e.getMessage()));
            }
        }).start();
    }
}