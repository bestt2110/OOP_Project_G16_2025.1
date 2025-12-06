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
        List<Post> posts = collector.collect("youtubevideos.csv"); 
        System.out.println("Đã đọc " + posts.size() + " bài viết.");

        System.out.println("Đang đọc Comments...");
        collector.loadComments("data.csv", posts); 
        
        PreProcessPipeline pipeline = new PreProcessPipeline();
        pipeline.addProcessor(new LowerCaseProcessor());   
        pipeline.addProcessor(new SpecialSymbolRemover());   
        pipeline.addProcessor(new VietnameseNormalizer());
        pipeline.addProcessor(new StopWordsRemover("stopwords.txt"));   
        
        for (Post p : posts) {
            if (!p.getComments().isEmpty()) {
                System.out.println("Post " + p.getId() + " có " + p.getComments().size() + " bình luận:");
                List<Comment> commentlist = p.getComments();
                for (Comment c: commentlist) {
                	pipeline.execute(c);
                    System.out.println("   - [" + c.getId() + "] " + c.getCleanContent());
                }
            }
        }
    }
}