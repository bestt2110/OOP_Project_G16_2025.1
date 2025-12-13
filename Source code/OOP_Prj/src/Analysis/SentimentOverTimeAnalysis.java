package Analysis;

import Model.*;
import java.time.*;
import java.util.*;

public class SentimentOverTimeAnalysis implements AnalysisTask<SentimentResult> {

    private static final List<String> POSITIVE_WORDS = Arrays.asList("an", "ổn", "toàn");
    private static final List<String> NEGATIVE_WORDS = Arrays.asList("tử", "mất", "hỏng");

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
    /*
    @Override
    public Map<String, SentimentResult> execute(List<Comment> comments) {

        Map<String, SentimentResult> result = new HashMap<>();

        for (Comment c : comments) {
            if (c.getTimestamp() == null) continue;

            Date date = c.getTimestamp();
            SentimentLabel label = classifySentiment(c.getCleanContent());

            // Lấy hoặc tạo mới object count
            SentimentResult count = result.getOrDefault(date, new SentimentResult());

            if (label == SentimentLabel.POSITIVE) count.setPositiveCount(count.getPositiveCount() + 1); 
            if (label == SentimentLabel.NEGATIVE) count.setNegativeCount(count.getNegativeCount() + 1);
            LocalDate localDate = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            String stringDate = localDate.toString();
            result.put(stringDate, count);
        }

        return result;
    }
    */
    @Override
    public void execute(Post p, HashMap<String, SentimentResult> result) {

        //Map<String, SentimentResult> result = new HashMap<>();
        
        Date pDate = p.getTimestamp();
        int pCount = 0;
        int nCount = 0;
        if (!p.getComments().isEmpty()) {
        	List<Comment> comments = p.getComments();
	        for (Comment c : comments) {
	            if (c.getTimestamp() == null) continue;
	
	            Date date = c.getTimestamp();
	            SentimentLabel label = classifySentiment(c.getCleanContent());
	
	            // Lấy hoặc tạo mới object count
	            SentimentResult count = result.getOrDefault(date, new SentimentResult());
	
	            if (label == SentimentLabel.POSITIVE) {
	            	count.setPositiveCount(count.getPositiveCount() + 1);
	            	pCount+=1;
	            }
	            if (label == SentimentLabel.NEGATIVE) {
	            	count.setNegativeCount(count.getNegativeCount() + 1);
	            	nCount+=1;
	            }
		
		        String stringDate = date.toString();
		        result.put(stringDate, count);
	        }
        }
        SentimentResult postcount = result.getOrDefault(pDate, new SentimentResult());
        SentimentLabel label = classifySentiment(p.getCleanContent());
        if (label == SentimentLabel.POSITIVE) {
        	postcount.setPositiveCount(postcount.getPositiveCount() + 1 + pCount);
        }
        if (label == SentimentLabel.NEGATIVE) {
        	postcount.setNegativeCount(postcount.getNegativeCount() + 1 + nCount);
        }

        String stringDate = pDate.toString();
        result.put(stringDate, postcount);
    }
}

