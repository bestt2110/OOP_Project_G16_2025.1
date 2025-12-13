package Analysis;

import Model.Post;
import Model.AnalysisResult;
import java.util.*;

public class AnalysisEngine {

    // Danh sách các task sẽ chạy (Strategy Pattern)
    private List<AnalysisTask> tasks;

    public AnalysisEngine() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Đăng ký thêm thuật toán vào Engine
     */
    public void addTask(AnalysisTask task) {
        this.tasks.add(task);
    }

    /**
     * Hàm chạy tất cả phân tích (Main chỉ cần gọi hàm này)
     */
    public Map<String, AnalysisResult> executeAnalysis(List<Post> posts) {
        // 1. Tạo Map tổng để chứa kết quả
        Map<String, AnalysisResult> combinedResult = new HashMap<>();

        if (posts == null || posts.isEmpty()) {
            System.out.println("⚠️ Không có dữ liệu để phân tích.");
            return combinedResult;
        }

        System.out.println("Engine đang chạy " + tasks.size() + " thuật toán...");

        // 2. Vòng lặp chính (Che giấu logic lặp khỏi Main)
        for (Post p : posts) {
            // Với mỗi bài viết, chạy qua TẤT CẢ các task đã đăng ký
            for (AnalysisTask task : tasks) {
                // Task tự động update vào Map combinedResult
                task.execute(p, combinedResult);
            }
        }
        return combinedResult;
    }
}