package Model;

/**
 * AnalysisResult: Model class to encapsulate the final statistical result of an analysis task.
 * This class is used as the Value type in the Map returned by AnalysisTask.execute().
 */
public class AnalysisResult {
    
    // Field: Description of the result category/group (e.g., "Satisfied", "Flood Damage", "Day 25/11")
    private String label;

    // Field: The statistical value (e.g., 60.5)
    private double value;

    // Field: The unit of the value (e.g., "%", "Count", "Posts")
    private String unit;

    /**
     * Constructor full.
     */
    public AnalysisResult(String label, double value, String unit) {
        this.label = label;
        this.value = value;
        this.unit = unit;
    }

    /**
     * Default constructor.
     */
    public AnalysisResult() {
        this.label = "";
        this.value = 0.0;
        this.unit = "";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // Optional: Override toString() for easy logging and debugging
    @Override
    public String toString() {
        return "AnalysisResult{" +
               "label='" + label + '\'' +
               ", value=" + value +
               ", unit='" + unit + '\'' +
               '}';
    }
}
