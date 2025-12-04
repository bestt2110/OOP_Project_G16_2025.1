// File: TestPreProcessor.java (trong gói Test)

package Test;

import Model.Post;
import PreProcessor.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Data.FileCollector;

public class TestPost {

    public static void main(String[] args) {
        
    	FileCollector collector = new FileCollector();
        
        List<Post> posts = collector.collect("youtubevideos.csv");
        
        PreProcessPipeline pipeline = new PreProcessPipeline();
        
        pipeline.addProcessor(new LowerCaseProcessor());    
       

        System.out.println("--- BẮT ĐẦU KIỂM THỬ PREPROCESSOR ---");
        
        for (Post post : posts) {
           
            pipeline.execute(post);
            
            System.out.println("---------------------------------");
            System.out.println("ID: " + post.getId());
            System.out.println("RAW:  " + post.getRawContent());
            System.out.println("CLEAN: " + post.getCleanContent());
            System.out.println("Time: " + post.getTimestamp());
        }
        
        System.out.println("--- CHECK DONE ---");
    }
}