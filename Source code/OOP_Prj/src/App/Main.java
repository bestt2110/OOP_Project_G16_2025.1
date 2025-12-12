package App;

import Data.*;
import Model.*;
import PreProcessor.LowerCaseProcessor;
import PreProcessor.PreProcessPipeline;
import PreProcessor.SpecialSymbolRemover;
import PreProcessor.StopWordsRemover;
import PreProcessor.VietnameseNormalizer;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    
    // API Key YouTube (Điền key của bạn vào đây)
    private static final String YOUTUBE_API_KEY = "AIzaSyAKBxo0ajeIiRFVFtyJcGX9rQCdz1Sst7E"; 
    
    private static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.println("=== CHƯƠNG TRÌNH THU THẬP DỮ LIỆU ===");
            System.out.println("1. VnExpress (Báo chí)");
            System.out.println("2. YouTube (Mạng xã hội)");
            System.out.println("3. Từ File CSV có sẵn (Offline)");
            System.out.print("Nhập lựa chọn (1-3): ");
            
            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ.");
                return;
            }
            
            IDataCollector collector = null;
            String outputPrefix = "data";
            
            // Các tham số để truyền vào hàm collect
            List<String> keywords = null;
            Date startDate = null;
            Date endDate = null;

            switch (choice) {
                case 1: // VnExpress
                    collector = new VnExpressCollector();
                    outputPrefix = "vnexpress";
                    break;
                    
                case 2: // YouTube
                    collector = new YouTubeCollector(YOUTUBE_API_KEY);
                    outputPrefix = "youtube";
                    break;
                    
                case 3: // File
                    System.out.print("Nhập đường dẫn file (vd: vnexpress_posts.csv): ");
                    String filePath = scanner.nextLine();
                    collector = new FileCollector(filePath);
                    outputPrefix = "file_processed";
                    break;
                    
                default:
                    System.out.println("Lựa chọn sai.");
                    return;
            }

            // --- NHẬP THAM SỐ (Chỉ dành cho Web, File thì bỏ qua để lấy hết) ---
            if (choice != 3) {
                System.out.print("Nhập từ khóa (cách nhau bởi dấu phẩy): ");
                String kwInput = scanner.nextLine();
                keywords = Arrays.asList(kwInput.split(","));
                
                System.out.print("Ngày bắt đầu (dd/MM/yyyy): ");
                startDate = INPUT_DATE_FORMAT.parse(scanner.nextLine() + " 00:00");
                
                System.out.print("Ngày kết thúc (dd/MM/yyyy): ");
                endDate = INPUT_DATE_FORMAT.parse(scanner.nextLine() + " 23:59");
            } else {
                System.out.println("-> Chế độ File: Sẽ đọc toàn bộ dữ liệu trong file.");
            }

            // Khởi tạo
            collector.initialize(new HashMap<>());
            
            // --- CHẠY COLLECT (Đa hình) ---
            System.out.println("\nĐang tiến hành thu thập dữ liệu...");
            List<Post> records = collector.collect(keywords, startDate, endDate);
            
            if (choice != 3) {
            saveDataToCsv(records, outputPrefix);
            }
            else {
            	System.out.println("\nThu thập dữ liệu thành công");
            }
            PreProcessPipeline pipeline = new PreProcessPipeline();
            pipeline.addProcessor(new LowerCaseProcessor());   
            pipeline.addProcessor(new SpecialSymbolRemover());   
            pipeline.addProcessor(new VietnameseNormalizer());
            pipeline.addProcessor(new StopWordsRemover("stopwords.txt"));   
            
            for (Post p : records) {
                if (!p.getComments().isEmpty()) {
                    System.out.println("Post " + p.getId() + " có " + p.getComments().size() + " bình luận:");
                    List<Comment> commentlist = p.getComments();
                    for (Comment c: commentlist) {
                    	pipeline.execute(c);
                        System.out.println("   - [" + c.getId() + "] " + c.getCleanContent());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void saveDataToCsv(List<Post> records, String prefix) {
        if (records.isEmpty()) {
            System.out.println("⚠️ Không có dữ liệu để lưu.");
            return;
        }

        List<String[]> postRows = new ArrayList<>();
        postRows.add(new String[]{"postId", "content", "publishedAt", "likeCount", "commentCount"});

        List<String[]> commentRows = new ArrayList<>();
        commentRows.add(new String[]{"postId", "commentId", "content", "timestamp", "likeCount"});

        SimpleDateFormat csvFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Post p : records) {
            String cleanText = (p.getRawContent() != null) ? p.getRawContent().trim() : "";
            String dateStr = (p.getTimestamp() != null) ? csvFmt.format(p.getTimestamp()) : "";
            
            postRows.add(new String[]{
                p.getId(), cleanText, dateStr, 
                String.valueOf(p.getLike()), String.valueOf(p.getCmt())
            });

            for (Comment c : p.getComments()) {
                String cmtText = (c.getRawContent() != null) ? c.getRawContent().trim() : "";
                String cmtDate = (c.getTimestamp() != null) ? csvFmt.format(c.getTimestamp()) : "";
                
                commentRows.add(new String[]{
                    p.getId(), c.getId(), cmtText, cmtDate, String.valueOf(c.getLikeCount())
                });
            }
        }

        CSVWriterUtil.writeCsv(prefix + "_posts.csv", postRows);
        if (commentRows.size() > 1) {
            CSVWriterUtil.writeCsv(prefix + "_comments.csv", commentRows);
        }
    }
    
}