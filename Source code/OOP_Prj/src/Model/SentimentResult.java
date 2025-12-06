package Model;

/**
 * SentimentResult: A specialized model class to store the results of Problem 1 (Public Sentiment Tracking).
 * It extends AnalysisResult to inherit basic properties (label, value, unit) and adds specific 
 * fields for tracking positive and negative post counts over a specific date/time.
 */
public class SentimentResult extends AnalysisResult {

    // Field: The date or time range this sentiment count corresponds to.
    private String dateLabel;

    // Field: The total count of posts classified as POSITIVE for this date.
    private int positiveCount;

    // Field: The total count of posts classified as NEGATIVE for this date.
    private int negativeCount;

    /**
     * Full constructor for SentimentResult.
     * @param dateLabel The date/time label (e.g., "2025-11-25").
     * @param positiveCount The number of positive posts.
     * @param negativeCount The number of negative posts.
     */
    public SentimentResult(String dateLabel, int positiveCount, int negativeCount) {
        // Calling the parent constructor (AnalysisResult) with summarized data
        super(dateLabel, positiveCount + negativeCount, "Posts");
        
        this.dateLabel = dateLabel;
        this.positiveCount = positiveCount;
        this.negativeCount = negativeCount;
    }
    
    /**
     * Default constructor.
     */
    public SentimentResult() {
        super();
        this.dateLabel = "";
        this.positiveCount = 0;
        this.negativeCount = 0;
    }



    public String getDateLabel() {
        return dateLabel;
    }

    public void setDateLabel(String dateLabel) {
        this.dateLabel = dateLabel;
        super.setLabel(dateLabel);
    }

    public int getPositiveCount() {
        return positiveCount;
    }

    public void setPositiveCount(int positiveCount) {
        this.positiveCount = positiveCount;
        super.setValue(positiveCount + this.negativeCount);
    }

    public int getNegativeCount() {
        return negativeCount;
    }

    public void setNegativeCount(int negativeCount) {
        this.negativeCount = negativeCount;
        super.setValue(this.positiveCount + negativeCount);
    }
    
    // Override toString() for better logging/debugging
    @Override
    public String toString() {
        return "SentimentResult{" +
               "dateLabel='" + dateLabel + '\'' +
               ", positiveCount=" + positiveCount +
               ", negativeCount=" + negativeCount +
               "} " + super.toString();
    }
}	