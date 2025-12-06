package Test;

import Model.Post;
import PreProcessor.*;
import java.util.List;

import Data.FileCollector;

public class TestPost {

    public static void main(String[] args) {
        
    	FileCollector collector = new FileCollector();
        
        List<Post> posts = collector.collect("C:\\Users\\admin\\Pictures\\OOP_Project_G16_2025.1\\Source code\\OOP_Prj\\youtubevideos.csv");
        
        PreProcessPipeline pipeline = new PreProcessPipeline();
        
        pipeline.addProcessor(new LowerCaseProcessor());   
        pipeline.addProcessor(new SpecialSymbolRemover());   
        pipeline.addProcessor(new VietnameseNormalizer());
        pipeline.addProcessor(new StopWordsRemover("stopwords.txt"));   
       

        System.out.println("--- BẮT ĐẦU KIỂM THỬ PREPROCESSOR ---");
        
        for (Post post : posts) {
           
            pipeline.execute(post);
            
            System.out.println("---------------------------------");
            System.out.println("ID: " + post.getId());
            System.out.println("RAW:  " + post.getDamageTypeLabel());
            System.out.println("CLEAN: " + post.getCleanContent());
            System.out.println("Time: " + post.getTimestamp());
        }
        
        System.out.println("--- CHECK DONE ---");
    }
}