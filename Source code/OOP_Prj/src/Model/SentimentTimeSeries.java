package Model;

import java.util.List;

/**
 * SentimentTimeSeries: A specialized AnalysisResult used to encapsulate the full, 
 * time-series result set for Problem 1 (Public Sentiment Tracking).
 * It inherits from AnalysisResult (the base return type for AnalysisTask.execute()) 
 * and adds a list of daily sentiment results for detailed visualization.
 */
public class SentimentTimeSeries extends AnalysisResult {

    // Field: The list of detailed sentiment results, one for each analyzed day.
    private List<SentimentResult> dailyResults;

    /**
     * Constructor.
     * @param dailyResults A list of SentimentResult objects, each representing one day's sentiment count.
     */
    public SentimentTimeSeries(List<SentimentResult> dailyResults) {
        // Calling the parent constructor (AnalysisResult) with summarized data:
        // label: The name of the problem
        // value: The total number of days analyzed
        // unit: "Days"
        super("Problem 1: Public Sentiment Tracking", (double) dailyResults.size(), "Days");
        this.dailyResults = dailyResults;
    }
    
    /**
     * Default Constructor.
     */
    public SentimentTimeSeries() {
        super();
        this.dailyResults = List.of();
        super.setLabel("Problem 1: Public Sentiment Tracking");
    }
    
    // --- Getters and Setters ---
    
    public List<SentimentResult> getDailyResults() {
        return dailyResults;
    }

    public void setDailyResults(List<SentimentResult> dailyResults) {
        this.dailyResults = dailyResults;
        // Update the inherited 'value' (total count of days)
        super.setValue((double) dailyResults.size());
    }
    
    /**
     * Override toString() for better debugging.
     */
    @Override
    public String toString() {
        return "SentimentTimeSeries{" +
               "dailyResultsCount=" + dailyResults.size() +
               "} " + super.toString();
    }
}
