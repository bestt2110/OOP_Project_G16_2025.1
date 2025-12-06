package Model;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * SatisfactionResult: A specialized AnalysisResult used to encapsulate the satisfaction level 
 * for each relief item category (Problem 3).
 * It stores the total count of positive and negative posts per category.
 */
public class SatisfactionResult extends AnalysisResult {

    // Map to store the detailed counts: Relief Category (String) -> Map<SentimentLabel, Long>
    private Map<String, Map<SentimentLabel, Long>> detailedSatisfactionCounts;

    /**
     * Constructor.
     * @param detailedSatisfactionCounts Map containing the counts of POSITIVE/NEGATIVE posts for each relief item category.
     */
    public SatisfactionResult(Map<String, Map<SentimentLabel, Long>> detailedSatisfactionCounts) {
        
        super("Problem 3: Relief Item Satisfaction Summary", 
              (double) calculateTotalPosts(detailedSatisfactionCounts),
              "Posts");
        
        this.detailedSatisfactionCounts = detailedSatisfactionCounts;
    }
    
    /**
     * Helper method to calculate the total number of classified posts.
     */
    private static long calculateTotalPosts(Map<String, Map<SentimentLabel, Long>> countsMap) {
         if (countsMap == null) return 0L;
         
         return countsMap.values().stream()
                           .flatMap(m -> m.values().stream())
                           .mapToLong(Long::longValue).sum();
    }
    
    // Default Constructor
    public SatisfactionResult() {
        super();
        this.detailedSatisfactionCounts = Map.of();
    }
    
    // --- Getters and Setters ---

    public Map<String, Map<SentimentLabel, Long>> getDetailedSatisfactionCounts() {
        return detailedSatisfactionCounts;
    }

    public void setDetailedSatisfactionCounts(Map<String, Map<SentimentLabel, Long>> detailedSatisfactionCounts) {
        this.detailedSatisfactionCounts = detailedSatisfactionCounts;
        // Recalculate and update the inherited 'value'
        super.setValue((double) calculateTotalPosts(detailedSatisfactionCounts));
    }
}
