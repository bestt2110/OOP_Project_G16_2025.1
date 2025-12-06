package Test;

import java.util.List;

import Data.FileCollector;
import Model.Comment;
import Model.Post;
import PreProcessor.*;


public class TestComment {
    public static void main(String[] args) {
        FileCollector collector = new FileCollector();
        
        System.out.println("Đang đọc Posts...");
        List<Post> posts = collector.collect("C:\\Users\\admin\\Pictures\\OOP_Project_G16_2025.1\\Source code\\OOP_Prj\\youtubevideos.csv"); 
        System.out.println("Đã đọc " + posts.size() + " bài viết.");

        System.out.println("Đang đọc Comments...");
        collector.loadComments("C:\\Users\\admin\\Pictures\\OOP_Project_G16_2025.1\\Source code\\OOP_Prj\\data.csv", posts); 
        
        PreProcessPipeline pipeline = new PreProcessPipeline();
        pipeline.addProcessor(new LowerCaseProcessor());   
        pipeline.addProcessor(new SpecialSymbolRemover());   
        
        for (Post p : posts) {
            if (!p.getComments().isEmpty()) {
                System.out.println("Post " + p.getPostId() + " có " + p.getComments().size() + " bình luận:");
                List<Comment> commentlist = p.getComments();
                for (Comment c: commentlist) {
                	pipeline.execute(c);
                    System.out.println("   - [" + c.getId() + "] " + c.getCleanContent());
                }
            }
        }
    }
}