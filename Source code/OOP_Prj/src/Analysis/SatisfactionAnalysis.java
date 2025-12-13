package Analysis;

import java.util.*;
import Model.*;

public class SatisfactionAnalysis implements AnalysisTask<SentimentResult> {
	// ================================
    // 1. Loại cứu trợ
    // ================================
    private final Map<String, List<String>> itemKeywords;

    // ================================
    // 2. Từ phân loại sentiment
    // ================================
    private static final List<String> POSITIVE_WORDS = Arrays.asList("an", "ổn", "toàn", "hết", "đẹp", "thường", "khỏe", "ơn", "ủng", "thiện", "ngon");
    private static final List<String> NEGATIVE_WORDS = Arrays.asList("tử", "mất", "hỏng", "phá", "thương", "suy", "nề", "lũ", "lụt", "nghiêm", "hỏng");

    public SatisfactionAnalysis() {
        itemKeywords = loadItemKeywords();
    }

    // ================================
    // 3. Hàm chính: gom Map<Type, (pos,neg)>
    // ================================
    public Map<String, SentimentResult> execute(List<Comment> data) {
    	Map<String, SentimentResult> result = new HashMap<>();
        if (data == null || data.isEmpty()) return result;

        for (Comment c : data) {
            String content = c.getRawContent();
            if (content == null || content.isEmpty()) continue;

            // 1. Tìm loại cứu trợ
            String category = classifyCategory(content);
            if (category == null) continue;

            // 2. Đếm sentiment
            String lower = content.toLowerCase();
            // Không có từ tích cực hay tiêu cực thì bỏ qua (neutral)

            // 3. Lấy kết quả cũ hoặc tạo mới
            SentimentResult sentiment = result.getOrDefault(category, new SentimentResult());
            for (String w : POSITIVE_WORDS) {
                if (lower.contains(w)) sentiment.setPositiveCount(sentiment.getPositiveCount() + 1); 
            }
            for (String w : NEGATIVE_WORDS) {
                if (lower.contains(w)) sentiment.setNegativeCount(sentiment.getNegativeCount() + 1); 
            }
            if (sentiment.getPositiveCount() == 0 && sentiment.getNegativeCount() == 0) continue;
            // 5. Lưu vào map
            result.put(category, sentiment);
        }

        return result;
    }

    // ================================
    // 4. Xác định loại cứu trợ từ từ khóa
    // ================================
    private String classifyCategory(String content) {
        String lower = content.toLowerCase();
        for (Map.Entry<String, List<String>> entry : itemKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lower.contains(keyword)) return entry.getKey();
            }
        }
        return null;
    }

    // ================================
    // 5. Tải danh sách từ khóa cho các loại cứu trợ
    // ================================
    private Map<String, List<String>> loadItemKeywords() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("Shelter", Arrays.asList("chỗ ở", "nhà", "lều", "bạt", "che mưa", "tạm trú"));
        map.put("Transportation", Arrays.asList("xe", "chuyển hàng", "đường đi", "cầu", "vận chuyển", "đi lại", "xây"));
        map.put("Food", Arrays.asList("thức ăn", "gạo", "mì", "nước uống", "đồ hộp", "cơm", "lương thực", "khoai"));
        map.put("Medical Support", Arrays.asList("thuốc", "bác sĩ", "y tế", "sơ cứu", "bệnh viện", "sức khỏe", "cứu"));
        map.put("Cash Assistance", Arrays.asList("tiền", "tiền mặt", "quỹ", "hỗ trợ tài chính", "ủng hộ tiền", "thiện"));
        return map;
    }
}
