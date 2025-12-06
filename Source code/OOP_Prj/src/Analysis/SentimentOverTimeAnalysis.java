package Analysis;

import Model.Post;
import Model.SentimentLabel;
import Model.SentimentResult;
import Model.SentimentTimeSeries;
import Model.AnalysisResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * SentimentOverTimeAnalysis: Implements AnalysisTask for Problem 1.
 * Purpose: Analyzes the list of labeled Post objects to track the number of POSITIVE and NEGATIVE 
 * posts over unique days, returning the result as a time series data structure.
 */
public class SentimentOverTimeAnalysis implements AnalysisTask {

    private static final String PROBLEM_NAME = "Problem 1: Public Sentiment Tracking Over Time";

    @Override
    public String getProblemName() {
        return PROBLEM_NAME;
    }

    /**
     * Executes the analysis: aggregates positive and negative post counts by day.
     * @param data The list of Post objects already labeled by the SentimentLabeler.
     * @return SentimentTimeSeries containing a list of daily sentiment results, sorted by date.
     */
    @Override
    public AnalysisResult execute(List<Post> data) {
        if (data == null || data.isEmpty()) {
            System.out.println("SentimentOverTimeAnalysis: Input data is empty.");
            // Return an empty time series object if data is null or empty
            return new SentimentTimeSeries(new ArrayList<>()); 
        }

        // Map to store temporary results: LocalDate (Day) -> SentimentResult (Counts)
        Map<LocalDate, SentimentResult> dailyAggregation = new HashMap<>();

        // 1. Iterate through all posts to aggregate counts by date
        for (Post post : data) {
            // Ensure the post has valid basic data for analysis
            if (post.getSentimentLabel() == null || post.getTimestamp() == null || post.getSentimentLabel() == SentimentLabel.UNKNOWN) {
                continue; 
            }
            
            // Get the date (LocalDate) from the post's timestamp (LocalDateTime).
            LocalDate postDate = post.getTimestamp().toLocalDate(); 
            
            // Get or initialize the SentimentResult object for this specific date
            SentimentResult result = dailyAggregation.getOrDefault(postDate, 
                new SentimentResult(postDate.toString(), 0, 0)
            );

            // Update counts based on the sentiment label (ignoring NEUTRAL and UNKNOWN)
            if (post.getSentimentLabel() == SentimentLabel.POSITIVE) {
                result.setPositiveCount(result.getPositiveCount() + 1);
            } else if (post.getSentimentLabel() == SentimentLabel.NEGATIVE) {
                result.setNegativeCount(result.getNegativeCount() + 1);
            }
            // NEUTRAL posts are counted in total, but not specifically tracked as POSITIVE/NEGATIVE

            // Store the updated result back into the map
            dailyAggregation.put(postDate, result);
        }

        // 2. Convert the Map to a List and Sort
        List<SentimentResult> finalResults = new ArrayList<>(dailyAggregation.values());
        
        // Sort the results by date in ascending order for visualization
        finalResults.sort(Comparator.comparing(r -> LocalDate.parse(r.getDateLabel())));

        // 3. Wrap the final results into the SentimentTimeSeries model
        return new SentimentTimeSeries(finalResults);
    }
}