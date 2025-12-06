package Analysis;

import Model.Post;
import Model.AnalysisResult;
import Model.SatisfactionResult;
import Model.SentimentLabel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SatisfactionAnalysis: Implements AnalysisTask for Problem 3.
 * Logic: Classifies posts into relief item categories (e.g., Food, Shelter) and then
 * aggregates the POSITIVE and NEGATIVE sentiment counts for each category.
 */
public class SatisfactionAnalysis implements AnalysisTask {

    private static final String PROBLEM_NAME = "Problem 3: Relief Item Satisfaction Analysis";
    
    // Map to store keywords for classification: Category Name -> Set of Keywords
    private final Map<String, Set<String>> itemKeywords;

    public SatisfactionAnalysis() {
        // Initialize the keyword map (Should load from a resource file for OCP compliance)
        this.itemKeywords = loadItemKeywords();
    }

    @Override
    public String getProblemName() {
        return PROBLEM_NAME;
    }

    /**
     * Executes the analysis: classifies posts by relief item and aggregates sentiment counts.
     * @param data The list of Post objects (must have clean content and sentiment labels).
     * @return SatisfactionResult containing detailed sentiment counts per category.
     */
    @Override
    public AnalysisResult execute(List<Post> data) {
        if (data == null || data.isEmpty()) {
            return new SatisfactionResult(Map.of());
        }

        // Map to store final results: Category -> Map<Sentiment, Count>
        Map<String, Map<SentimentLabel, Long>> detailedCounts = new HashMap<>();

        // 1. Classification and Aggregation
        for (Post post : data) {
            String category = classifyPost(post);
            
            // Only aggregate if a valid category is found and sentiment is not neutral/unknown
            if (category != null && !category.equals("NONE") && 
                (post.getSentimentLabel() == SentimentLabel.POSITIVE || post.getSentimentLabel() == SentimentLabel.NEGATIVE)) {
                
                // Get or initialize the map for this category
                detailedCounts.putIfAbsent(category, new HashMap<>());
                Map<SentimentLabel, Long> sentimentCounts = detailedCounts.get(category);
                
                SentimentLabel label = post.getSentimentLabel();
                
                // Increment the count for the specific sentiment label
                sentimentCounts.put(label, sentimentCounts.getOrDefault(label, 0L) + 1);
                
                // Set the category back to the Post object (optional but useful for traceability)
                post.setReliefItemCategory(category);
            }
        }

        // 2. Wrap the results into the SatisfactionResult model
        return new SatisfactionResult(detailedCounts);
    }

    /**
     * Attempts to classify a post into one of the predefined relief item categories using keyword matching.
     * This is a crucial step for Problem 3.
     */
    private String classifyPost(Post post) {
        String content = post.getCleanContent();
        if (content == null || content.isEmpty()) {
            return "NONE";
        }
        
        String lowerCaseContent = content.toLowerCase();

        // Iterate through all relief categories and their keywords
        for (Map.Entry<String, Set<String>> entry : itemKeywords.entrySet()) {
            String category = entry.getKey();
            Set<String> keywords = entry.getValue();

            for (String keyword : keywords) {
                if (lowerCaseContent.contains(keyword)) {
                    return category; 
                }
            }
        }
        return "NONE";
    }

    /**
     * Initializes the predefined relief item categories and their associated Vietnamese keywords.
     * Based on problem description: shelter, transportation, food, medical support, and cash assistance.
     */
    private Map<String, Set<String>> loadItemKeywords() {
        Map<String, Set<String>> keywords = new HashMap<>();
        
        keywords.put("Shelter", Set.of("chỗ ở", "nhà ở", "lều", "bạt", "che mưa", "tạm trú"));
        keywords.put("Transportation", Set.of("xe", "chuyển hàng", "đường đi", "cầu", "vận chuyển", "đi lại"));
        keywords.put("Food", Set.of("thức ăn", "gạo", "mì", "nước uống", "đồ hộp", "cơm", "lương thực"));
        keywords.put("Medical Support", Set.of("thuốc", "bác sĩ", "y tế", "sơ cứu", "bệnh viện", "sức khỏe"));
        keywords.put("Cash Assistance", Set.of("tiền", "tiền mặt", "quỹ", "hỗ trợ tài chính", "ủng hộ tiền"));
        
        return keywords;
    }
}
