package Analysis;

import Model.Post;
import Model.AnalysisResult;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * AnalysisEngine: The core controller for the Analysis module.
 * It manages and executes all registered AnalysisTask implementations 
 * using polymorphism to process the data and generate combined results.
 */
public class AnalysisEngine {

    // List containing the registered Analysis Tasks (Problem 1, 2, 3,...)
    private List<AnalysisTask> tasks;

    /**
     * Constructor: Initializes the list of Analysis Tasks.
     * NOTE: Specific tasks (SentimentOverTimeAnalysis, etc.) will be added here
     * once their corresponding feature branches are merged.
     */
    public AnalysisEngine() {
        this.tasks = new ArrayList<>();
        
        // --- Dependency Injection/Initialization Point ---
        // Once the feature/analysis-probX branches are merged, 
        // you will uncomment or add the specific tasks here:
        // tasks.add(new SentimentOverTimeAnalysis());
        // tasks.add(new DamageCategoryAnalysis());
        // tasks.add(new SatisfactionAnalysis());
    }

    /**
     * Executes all registered analysis tasks sequentially.
     * @param data The pre-processed and labeled list of posts.
     * @return A consolidated map containing results from all analysis tasks.
     */
    public Map<String, AnalysisResult> runAllAnalysis(List<Post> data) {
        if (data == null || data.isEmpty()) {
            System.out.println("Analysis Engine: Cannot run analysis. Input data is empty.");
            return new HashMap<>();
        }
        
        Map<String, AnalysisResult> combinedResults = new HashMap<>();
        
        System.out.println("Analysis Engine: Starting execution of " + tasks.size() + " tasks.");
        
        // Main loop utilizing Polymorphism
        for (AnalysisTask task : tasks) {
            System.out.println("-> Executing Task: " + task.getProblemName());
            
            // Calls the generic execute() method; the specific logic is within each Task implementation
            Map<String, AnalysisResult> taskResults = task.execute(data);
            
            // Combines the results of each Task into the consolidated Map
            combinedResults.putAll(taskResults);
            System.out.println("-> Task completed with " + taskResults.size() + " results.");
        }
        
        System.out.println("Analysis Engine: All tasks completed successfully.");
        return combinedResults;
    }
    
    /**
     * Adds a new AnalysisTask dynamically to the engine.
     * @param task The AnalysisTask to add.
     */
    public void addTask(AnalysisTask task) {
        this.tasks.add(task);
    }
    
    /**
     * Returns the list of currently registered analysis tasks.
     * @return List of AnalysisTask objects.
     */
    public List<AnalysisTask> getTasks() {
        return tasks;
    }
}