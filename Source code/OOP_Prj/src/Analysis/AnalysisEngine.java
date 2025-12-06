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
    private final List<AnalysisTask> tasks;

    /**
     * Constructor: Initializes the list of Analysis Tasks.
     */
    public AnalysisEngine(List<AnalysisTask> initialTasks) {
        this.tasks = (initialTasks != null) ? new ArrayList<>(initialTasks) : new ArrayList<>();
    }
    
    /**
     * Default Constructor: Initializes an empty list of Analysis Tasks.
     * Tasks must be added dynamically using addTask().
     */
    public AnalysisEngine() {
        this(null);
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
            String problemName = task.getProblemName();
            System.out.println("-> Executing Task: " + task.getProblemName());
            
            try {
                // Calls the execute() method; the specific logic is within each Task implementation
                AnalysisResult result = task.execute(data);
                
                // Combines the result into the consolidated Map, using the problem name as the key
                combinedResults.put(problemName, result);
                
                System.out.println("-> Task completed: " + problemName);
            } catch (Exception e) {
                // IMPORTANT: Handle exceptions from individual analysis tasks gracefully
                System.err.println("!! ERROR during execution of " + problemName + ": " + e.getMessage());
                // Continue to the next task if one fails
            }
        }
        
        System.out.println("Analysis Engine: All tasks completed successfully.");
        return combinedResults;
    }
    
    /**
     * Adds a new AnalysisTask dynamically to the engine.
     * @param task The AnalysisTask to add.
     */
    public void addTask(AnalysisTask task) {
    	if (task != null) {
            this.tasks.add(task);
    	}
    }
    
    /**
     * Returns the list of currently registered analysis tasks.
     * @return List of AnalysisTask objects.
     */
    public List<AnalysisTask> getTasks() {
        return tasks;
    }
}