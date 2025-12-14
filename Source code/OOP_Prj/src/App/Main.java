package App;

import Data.*;
import Model.*;
import PreProcessor.*;
import UI.UI;
import javafx.application.Application;
import javafx.application.Platform;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    
    public static List<Post> globalData = new ArrayList<>();
    
    private static final SimpleDateFormat CSV_DATE_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        Application.launch(UI.class, args);
    }

    public static void processDataRequest(String source, String keywords, Date reqStart, Date reqEnd, UI uiController, String customFilePath) {
        
        new Thread(() -> {
            try {
                System.out.println("--- COLLECTING FROM: " + source + " ---");
                Platform.runLater(() -> uiController.updateStatus("⏳ Connecting to " + source + "..."));

                String cleanSource = source.split(" ")[0]; 
                String dynamicFileName = cleanSource + "_posts.csv";

                IDataCollector collector;
                List<String> kwList = (keywords != null && !keywords.trim().isEmpty()) 
                        ? Arrays.asList(keywords.split(",")) : new ArrayList<>();

                if (source.contains("File")) {
                    System.out.println("-> Offline mode: Read file " + customFilePath);
                    collector = new FileCollector(customFilePath); 
                } else if (source.contains("YouTube")) {
                    collector = new YouTubeCollector();
                } else {
                    collector = new VnExpressCollector();
                }

                collector.initialize(new HashMap<>());

                List<Post> rawPosts = collector.collect(kwList, reqStart, reqEnd);
                if (rawPosts == null) rawPosts = new ArrayList<>();
                System.out.println(rawPosts.size() + " collected");

                if (rawPosts.isEmpty()) {
                    Platform.runLater(() -> uiController.notifyNoDataOrError("⚠️ No posts found"));
                    return;
                }

                Platform.runLater(() -> uiController.updateStatus("🧹 Cleaning data..."));
                PreProcessPipeline pipeline = new PreProcessPipeline();
                pipeline.addProcessor(new LowerCaseProcessor());
                pipeline.addProcessor(new SpecialSymbolRemover());

                for (Post p : rawPosts) {
                    pipeline.execute(p);
                    if (p.getComments() != null) {
                        for (Comment c : p.getComments()) pipeline.execute(c);
                    }
                }

                globalData = rawPosts;

                // Lưu file nếu là chế độ Online
                if (!source.contains("File")) { 
                    saveToCSV(globalData, dynamicFileName);
                }

                Platform.runLater(() -> {
                    String msg = "✅ " + globalData.size() + " posts collected";
                    if (!source.contains("File")) msg += " Saved into: " + dynamicFileName;
                    uiController.updateStatus(msg);
                    uiController.enableAnalysisButtons(); 
                });

            } catch (Throwable e) { 
                e.printStackTrace();
                Platform.runLater(() -> uiController.notifyNoDataOrError("❌ ERROR: " + e.toString()));
            }
        }).start();
    }

    private static void saveToCSV(List<Post> posts, String filename) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
            writer.write('\ufeff'); 
            writer.println("ID,Date,Content,Likes,Comments");

            for (Post p : posts) {
                String cleanContent = p.getRawContent() != null ? p.getRawContent() : "";
                cleanContent = cleanContent.replace("\"", "\"\"").replace("\n", " ").replace("\r", " ").replace(",", ";");
                String dateStr = (p.getTimestamp() != null) ? CSV_DATE_FMT.format(p.getTimestamp()) : "";
                writer.printf("\"%s\",\"%s\",\"%s\",%d,%d%n", p.getId(), dateStr, cleanContent, p.getLikeCount(), p.getCmt());
            }
            System.out.println("-> [SAVED]: " + filename);
        } catch (IOException e) {
            System.err.println("-> Saving error: " + e.getMessage());
        }
    }
}