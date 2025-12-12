package Analysis;

import Model.*;
import java.time.LocalDate;
import java.util.*;

public class SentimentOverTimeAnalysis {

    private static final List<String> POSITIVE_WORDS = Arrays.asList("an", "ổn", "toàn", "hết", "đẹp", "thường", "khỏe", "ơn", "ủng", "thiện");
    private static final List<String> NEGATIVE_WORDS = Arrays.asList("tử", "mất", "hỏng", "phá", "thương", "suy", "nề", "lũ", "lụt", "nghiêm");

    /**
     * Phân loại sentiment của 1 câu bình luận
     */
    private SentimentLabel classifySentiment(String content) {
        if (content == null || content.isEmpty()) return SentimentLabel.UNKNOWN;

        String lower = content.toLowerCase();
        int pos = 0, neg = 0;

        for (String w : POSITIVE_WORDS) if (lower.contains(w)) pos++;
        for (String w : NEGATIVE_WORDS) if (lower.contains(w)) neg++;

        if (pos == 0 && neg == 0) return SentimentLabel.NEUTRAL;
        if (pos > neg) return SentimentLabel.POSITIVE;
        if (neg > pos) return SentimentLabel.NEGATIVE;
        return SentimentLabel.NEUTRAL;
    }

    /**
     * Hàm execute cải tiến: trả về Map<LocalDate, SentimentCount>
     */
    public Map<LocalDate, SentimentCount> execute(List<Comment> comments) {

        Map<LocalDate, SentimentCount> result = new HashMap<>();

        for (Comment c : comments) {
            if (c.getTimestamp() == null) continue;

            LocalDate date = c.getTimestamp().toLocalDate();
            SentimentLabel label = classifySentiment(c.getRawContent());

            // Lấy hoặc tạo mới object count
            SentimentCount count = result.getOrDefault(date, new SentimentCount());

            if (label == SentimentLabel.POSITIVE) count.positive++;
            if (label == SentimentLabel.NEGATIVE) count.negative++;

            result.put(date, count);
        }

        return result;
    }
}


