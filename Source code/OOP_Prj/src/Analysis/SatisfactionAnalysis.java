package Analysis;

import Model.*;
import java.util.*;

public class SatisfactionAnalysis implements AnalysisTask<SentimentResult> { 

    private final Map<String, List<String>> itemKeywords;
    private static final List<String> POSITIVE_WORDS = Arrays.asList("an toàn", "bình an", "ổn", "đỡ rồi", "cảm ơn", "biết ơn", "tuyệt vời", "tốt", "kịp thời");
    private static final List<String> NEGATIVE_WORDS = Arrays.asList("chết", "mất tích", "sập", "trôi", "ngập", "hỏng", "đói", "khát", "thiếu thốn", "kêu cứu", "buồn");

    public SatisfactionAnalysis() {
        itemKeywords = loadItemKeywords();
    }

    // Tham số Map<String, SentimentResult>
    @Override
    public void execute(Post p, Map<String, SentimentResult> result) {
        processText(p.getCleanContent(), result);
        if (p.getComments() != null) {
            for (Comment c : p.getComments()) processText(c.getCleanContent(), result);
        }
    }

    private void processText(String content, Map<String, SentimentResult> result) {
        if (content == null || content.isEmpty()) return;

        String category = classifyCategory(content);
        if (category == null) return; 

        // Lấy ra SentimentResult trực tiếp
        SentimentResult sentiment = result.getOrDefault(category, new SentimentResult());

        int posFound = 0;
        int negFound = 0;
        String lower = content.toLowerCase();

        for (String w : POSITIVE_WORDS) if (lower.contains(w)) posFound++;
        for (String w : NEGATIVE_WORDS) if (lower.contains(w)) negFound++;

        if (posFound > 0 || negFound > 0) {
            sentiment.setPositiveCount(sentiment.getPositiveCount() + posFound);
            sentiment.setNegativeCount(sentiment.getNegativeCount() + negFound);
            result.put(category, sentiment);
        }
    }

    private String classifyCategory(String content) {
        String lower = content.toLowerCase();
        for (Map.Entry<String, List<String>> entry : itemKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lower.contains(keyword)) return entry.getKey();
            }
        }
        return null;
    }

    private Map<String, List<String>> loadItemKeywords() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("Chỗ ở (Shelter)", Arrays.asList("chỗ ở", "nhà", "lều", "bạt", "mái"));
        map.put("Vận chuyển (Transport)", Arrays.asList("xe", "thuyền", "cano", "cầu"));
        map.put("Lương thực (Food)", Arrays.asList("thức ăn", "gạo", "mì", "nước", "đói"));
        map.put("Y tế (Medical)", Arrays.asList("thuốc", "bác sĩ", "y tế", "bệnh viện"));
        map.put("Tiền mặt (Cash)", Arrays.asList("tiền", "quỹ", "chuyển khoản"));
        return map;
    }
}