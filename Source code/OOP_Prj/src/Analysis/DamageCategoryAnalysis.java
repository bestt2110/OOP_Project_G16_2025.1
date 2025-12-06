package Analysis;

import Model.Post;
import Model.AnalysisResult;
import Model.DamageCategoryResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DamageCategoryAnalysis: Implements AnalysisTask for Problem 2.
 * Logic: Classifies Post content into predefined damage categories using a simple keyword matching algorithm, 
 * and aggregates the counts for each category.
 */
public class DamageCategoryAnalysis implements AnalysisTask {

    private static final String PROBLEM_NAME = "Problem 2: Damage Category Classification";

    // Map to store keywords for classification: Category Name -> Set of Keywords
    private final Map<String, Set<String>> categoryKeywords;

    public DamageCategoryAnalysis() {
        this.categoryKeywords = loadCategoryKeywords();
    }

    @Override
    public String getProblemName() {
        return PROBLEM_NAME;
    }

    /**
     * Executes the analysis: classifies posts into damage categories and counts the occurrences.
     * @param data The list of Post objects (should have clean content).
     * @return DamageCategoryResult containing the count for each category.
     */
    @Override
    public AnalysisResult execute(List<Post> data) {
        if (data == null || data.isEmpty()) {
            return new DamageCategoryResult(Map.of());
        }

        // Map to store the final counts: Category Name -> Count
        Map<String, Long> finalCounts = new HashMap<>();

        // 1. Classify each post and aggregate counts
        for (Post post : data) {
            String category = classifyPost(post);
            
            // 2. Assign the category back to the Post object
            post.setDamageCategory(category);
            
            // 3. Aggregate only if a valid category was found
            if (category != null && !category.equals("NONE")) {
                finalCounts.put(category, finalCounts.getOrDefault(category, 0L) + 1);
            }
        }

        // 4. Wrap the results into the DamageCategoryResult model
        return new DamageCategoryResult(finalCounts);
    }
    
    /**
     * Attempts to classify a post into one of the predefined damage categories using keyword matching.
     * @param post The post to classify.
     * @return The determined category name or "NONE".
     */
    private String classifyPost(Post post) {
        String content = post.getCleanContent();
        if (content == null || content.isEmpty()) {
            return "NONE";
        }
        
        String lowerCaseContent = content.toLowerCase();

        // Iterate through all categories and their keywords
        for (Map.Entry<String, Set<String>> entry : categoryKeywords.entrySet()) {
            String category = entry.getKey();
            Set<String> keywords = entry.getValue();

            // Check if content contains any keyword from the set
            for (String keyword : keywords) {
                if (lowerCaseContent.contains(keyword)) {
                    // Return the first matching category (simplification)
                    return category; 
                }
            }
        }

        return "NONE";
    }
    
    /**
     * Initializes the predefined damage categories and their associated Vietnamese keywords.
     * This should ideally load from a file resource for OCP compliance (similar to SentimentLabeler).
     * @return Map of damage categories to keyword sets.
     */
    private Map<String, Set<String>> loadCategoryKeywords() {
        Map<String, Set<String>> keywords = new HashMap<>();
        
        // Problem 2 Categories:
        keywords.put("Affected People", Set.of("người bị thương", "mất tích", "cần cứu", "tử vong", "người dân gặp nạn"));
        keywords.put("Damaged Infrastructure", Set.of("cầu sập", "đứt đường", "mất điện", "cột điện", "trạm y tế", "trạm xá"));
        keywords.put("Houses or Buildings Damaged", Set.of("nhà sập", "tường đổ", "thiệt hại nhà", "mất nhà", "lều", "mái nhà"));
        keywords.put("Loss of Personal Belongings", Set.of("mất tài sản", "tư trang", "mất đồ", "quần áo", "đồ đạc bị trôi"));
        keywords.put("Disruption of Economic Production", Set.of("mất mùa", "ao cá", "cây trồng", "thất thu", "ngừng sản xuất"));
        
        return keywords;
    }
}
