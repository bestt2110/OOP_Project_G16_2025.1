// File: TestPreProcessor.java (trong gói Test)

package Test;

import Model.Post;
import PreProcessor.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Analysis.*;
import Data.FileCollector;

public class TestPost {

    public static void main(String[] args) {
        
    	FileCollector collector = new FileCollector();
        
        List<Post> posts = collector.collect("C:\\Users\\admin\\Pictures\\OOP_Project_G16_2025.1\\Source code\\OOP_Prj\\youtubevideos.csv");
        
        PreProcessPipeline pipeline = new PreProcessPipeline();
        
        pipeline.addProcessor(new LowerCaseProcessor());    
       

        System.out.println("--- BẮT ĐẦU KIỂM THỬ PREPROCESSOR ---");
        
        for (Post post : posts) {
           
            pipeline.execute(post);
            
            System.out.println("---------------------------------");
            System.out.println("ID: " + post.getPostId());
            System.out.println("RAW:  " + post.getRawContent());
            System.out.println("CLEAN: " + post.getCleanContent());
        }
        
        System.out.println("--- CHECK DONE ---");
    }
}