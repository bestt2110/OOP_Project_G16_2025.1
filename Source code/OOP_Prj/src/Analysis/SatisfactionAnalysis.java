package Analysis;

import Model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SatisfactionAnalysis: Implements AnalysisTask for Problem 3.
 * Logic: Classifies posts into relief item categories (e.g., Food, Shelter) and then
 * aggregates the POSITIVE and NEGATIVE sentiment counts for each category.
 */
public class SatisfactionAnalysis {

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
    public Map<String, ReliefSentimentCount> execute(List<Comment> data) {
        Map<String, ReliefSentimentCount> result = new HashMap<>();
        if (data == null || data.isEmpty()) return result;

        for (Comment post : data) {
            String content = post.getRawContent();
            if (content == null || content.isEmpty()) continue;

            // 3a. Xác định loại cứu trợ
            String category = classifyCategory(content);
            if (category == null) continue;

            // 3b. Xác định sentiment dựa trên từ positive/negative
            String lower = content.toLowerCase();
            int posCount = 0, negCount = 0;
            for (String w : POSITIVE_WORDS) if (lower.contains(w)) posCount++;
            for (String w : NEGATIVE_WORDS) if (lower.contains(w)) negCount++;

            if (posCount == 0 && negCount == 0) continue; // không tính NEUTRAL

            // 3c. Cập nhật map
            ReliefSentimentCount count = result.getOrDefault(category, new ReliefSentimentCount(0, 0));
            if (posCount > negCount) count.incrementPositive();
            else if (negCount > posCount) count.incrementNegative();
            result.put(category, count);
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