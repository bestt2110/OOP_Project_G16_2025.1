package API;

import Model.Post;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * AnalysisAPIClient: Handles all communication with the external Analytical Models (Python API).
 * It encapsulates the complexities of HTTP requests, JSON serialization, and response parsing.
 * * NOTE: In the initial phase (feature/analysis-core), this class uses Mocking logic
 * to assign labels so that AnalysisTask classes can be developed immediately.
 */
public class AnalysisAPIClient {

    private static final String API_ENDPOINT = "http://localhost:5000/api/classify";

    // Singleton pattern (Optional but recommended for utility classes like this)
    private static AnalysisAPIClient instance;

    private AnalysisAPIClient() {
        // Private constructor for Singleton pattern
    }

    public static AnalysisAPIClient getInstance() {
        if (instance == null) {
            instance = new AnalysisAPIClient();
        }
        return instance;
    }

    /**
     * The main classification method. It sends raw post content to the API
     * and updates all necessary labels (Sentiment, Damage, Relief) in the Post objects.
     * * @param posts List of posts that need classification labels.
     * @return The updated list of posts with labels assigned.
     */
    public List<Post> classifyAll(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            System.out.println("API Client Mock: No posts provided for classification.");
            return posts;
        }

        // This simulates the actual Python API call and response.
        // Replace this block with actual HTTP request and JSON parsing later.
        System.out.println("API Client Mock: Simulating Python classification for " + posts.size() + " posts...");
        
        String[] sentiments = {"POSITIVE", "NEGATIVE", "NEUTRAL"};
        String[] damageTypes = {"HOUSE_DAMAGE", "INFRASTRUCTURE_DISRUPT", "AFFECTED_PEOPLE", "NONE"};
        String[] reliefItems = {"SHELTER", "FOOD", "MEDICAL_SUPPORT", "CASH_ASSISTANCE"};
        
        for (int i = 0; i < posts.size(); i++) {
            Post p = posts.get(i);
            int rand = ThreadLocalRandom.current().nextInt(10); // Random number 0-9

            // 1. Assign Sentiment label (Used for Problem 1 & 3)
            p.setSentimentLabel(sentiments[rand % sentiments.length]);

            // 2. Assign Damage Type label (Used for Problem 2)
            // Assign NONE to 20% of posts to simulate noise filtering
            if (rand < 8) {
                p.setDamageTypeLabel(damageTypes[rand % (damageTypes.length - 1)]); // Use only 3 main categories
            } else {
                p.setDamageTypeLabel("NONE"); // 20% are unrelated to damage
            }

            // 3. Assign Relief Item label (Used for Problem 3)
            p.setReliefTypeLabel(reliefItems[i % reliefItems.length]); 
            
            // NOTE: You must ensure the Post.java class has corresponding setters 
            // (setSentimentLabel, setDamageTypeLabel, setReliefTypeLabel).
        }
        // === MOCKING LOGIC END ===
        // ----------------------------------------------------------------------------------

        System.out.println("API Client Mock: Classification labels assigned successfully.");
        return posts;
    }
}