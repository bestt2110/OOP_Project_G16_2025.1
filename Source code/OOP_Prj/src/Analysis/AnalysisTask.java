package Analysis;

import Model.*;
import Model.AnalysisResult;
import java.util.List;
import java.util.Map;

/**
 * Interface AnalysisTask defines the common contract for all statistical analysis problems.
 * Purpose: Ensures abstraction and polymorphism, making it easy to add or remove problems.
 */
public interface AnalysisTask <T extends AnalysisResult> {

    /**
     * Executes the statistical analysis logic on the labeled Post data.
     * @param data A list of Post objects that have been pre-processed and labeled.
     * @return Map<String, AnalysisResult> The analysis results. The key represents the 
     * grouping category (e.g., "POSITIVE", "Flood"), and the value is the AnalysisResult object.
     */
    Map<String, T> execute(List<Comment> data);
    /**
     * Returns the display name of the analysis problem (used for UI or reporting).
     * @return The problem name (e.g., "Problem 1: Public Sentiment Tracking").
     */
    //String getProblemName();
}
