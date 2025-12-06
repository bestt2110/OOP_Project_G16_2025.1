package Model;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * DamageCategoryResult: A specialized AnalysisResult used to encapsulate the final counts 
 * for Problem 2 (Damage Category Analysis).
 * It inherits from AnalysisResult and adds a map storing the count for each damage category.
 */
public class DamageCategoryResult extends AnalysisResult {

    // Map to store the final counts: Category Name (String) -> Count (Long)
    private Map<String, Long> categoryCounts;

    /**
     * Constructor.
     * @param categoryCounts Map containing the count for each damage category.
     */
    public DamageCategoryResult(Map<String, Long> categoryCounts) {
        // Calling the parent constructor (AnalysisResult) with summarized data:
        // label: The name of the problem
        // value: Total number of classified posts
        // unit: "Posts"
        super("Problem 2: Damage Category Summary", 
              (double) categoryCounts.values().stream().mapToLong(Long::longValue).sum(), 
              "Posts");
        this.categoryCounts = categoryCounts;
    }
    
    // Default Constructor
    public DamageCategoryResult() {
        super();
        this.categoryCounts = Map.of(); // Use immutable empty map for default
    }
    
    // --- Getters and Setters ---

    public Map<String, Long> getCategoryCounts() {
        return categoryCounts;
    }

    public void setCategoryCounts(Map<String, Long> categoryCounts) {
        this.categoryCounts = categoryCounts;
        // Update the inherited 'value' (total count of classified posts)
        super.setValue((double) categoryCounts.values().stream().mapToLong(Long::longValue).sum());
    }

    /**
     * Override toString() for better debugging.
     */
    @Override
    public String toString() {
        return "DamageCategoryResult{" +
               "totalCategories=" + categoryCounts.size() +
               ", totalPosts=" + getValue() +
               "} " + super.toString();
    }
}