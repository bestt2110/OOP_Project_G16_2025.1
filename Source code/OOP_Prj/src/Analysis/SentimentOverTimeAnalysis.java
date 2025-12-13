package Analysis;

import Model.*;
import java.time.ZoneId;
import java.util.*;

public class SentimentOverTimeAnalysis implements AnalysisTask<SentimentResult> {

    private static final List<String> POSITIVE_WORDS = Arrays.asList(
            "an toàn", "bình an", "ổn", "đỡ rồi", "qua khỏi", "sống sót", "khỏe",
            "cảm ơn", "biết ơn", "tri ân", "tuyệt vời", "ấm lòng", "tốt", "ngon", 
            "chu đáo", "nhiệt tình", "kịp thời", "quý hóa", 
            "cố lên", "kiên cường", "vượt qua", "khắc phục", "đồng bào"
    );

    private static final List<String> NEGATIVE_WORDS = Arrays.asList(
            "tử vong", "chết", "mất tích", "thi thể", "bị thương", "tang thương",
            "sập", "đổ", "trôi", "cuốn trôi", "ngập", "hỏng", "hư hại", "tan hoang", "trắng tay",
            "cô lập", "chia cắt", "kêu cứu", "mắc kẹt", "kiệt sức", "lạnh", "rét", 
            "đói", "khát", "thiếu thốn", "hết sạch", "chưa có",
            "lo lắng", "sợ hãi", "hoang mang", "tuyệt vọng", "đau xót", "khổ", "buồn"
    );

    private enum SentimentLabel { POSITIVE, NEGATIVE, NEUTRAL, UNKNOWN }

    // Hàm thực thi chính - KHÔNG CẦN ÉP KIỂU
    @Override
    public void execute(Post p, Map<String, SentimentResult> result) {
        // 1. Xử lý Post
        String pDateKey = getDateStr(p.getTimestamp());
        if (pDateKey != null) {
            SentimentLabel label = classifySentiment(p.getCleanContent());
            updateMap(result, pDateKey, label);
        }

        // 2. Xử lý Comment
        if (p.getComments() != null) {
            for (Comment c : p.getComments()) {
                String cDateKey = getDateStr(c.getTimestamp());
                if (cDateKey != null) {
                    SentimentLabel label = classifySentiment(c.getCleanContent());
                    updateMap(result, cDateKey, label);
                }
            }
        }
    }

    // Hàm update nhận Map<String, SentimentResult> -> Lấy ra dùng luôn
    private void updateMap(Map<String, SentimentResult> result, String key, SentimentLabel label) {
        if (label == SentimentLabel.UNKNOWN || label == SentimentLabel.NEUTRAL) return;

        // Tự động hiểu là SentimentResult, không cần ép kiểu
        SentimentResult sr = result.getOrDefault(key, new SentimentResult());

        if (label == SentimentLabel.POSITIVE) {
            sr.setPositiveCount(sr.getPositiveCount() + 1);
        } else if (label == SentimentLabel.NEGATIVE) {
            sr.setNegativeCount(sr.getNegativeCount() + 1);
        }
        
        result.put(key, sr);
    }

    private SentimentLabel classifySentiment(String content) {
        if (content == null || content.isEmpty()) return SentimentLabel.UNKNOWN;
        String lower = content.toLowerCase();
        int pos = 0, neg = 0;
        for (String w : POSITIVE_WORDS) if (lower.contains(w)) pos++;
        for (String w : NEGATIVE_WORDS) if (lower.contains(w)) neg++;
        
        if (pos > neg) return SentimentLabel.POSITIVE;
        if (neg > pos) return SentimentLabel.NEGATIVE;
        return SentimentLabel.NEUTRAL;
    }

    private String getDateStr(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
    }
}