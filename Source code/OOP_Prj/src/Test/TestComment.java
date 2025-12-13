package Test;

import java.text.*;
import Model.*;
import PreProcessor.*;
import Data.*;
import java.util.*;


public class TestComment {
    public static void main(String[] args) throws ParseException {
    	List<String> keys = Arrays.asList("bão Yagi");
        FileCollector collector = new FileCollector("data.csv");
        
        System.out.println("Đang đọc Posts...");

        System.out.println("Đang đọc Comments...");
        PreProcessPipeline pipeline = new PreProcessPipeline();
        pipeline.addProcessor(new LowerCaseProcessor());   
        pipeline.addProcessor(new SpecialSymbolRemover());   
        pipeline.addProcessor(new VietnameseNormalizer());
        pipeline.addProcessor(new StopWordsRemover("stopwords.txt"));   
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = sdf.parse("06-09-2024");
        Date date2 = sdf.parse("16-11-2024");
        System.out.println(collector.collect(keys, date1, date2));
        /*
        for (Post p : collector.collect(keys, date1, date2)) {
            if (!p.getComments().isEmpty()) {
                System.out.println("Post " + p.getId() + " có " + p.getComments().size() + " bình luận:");
                List<Comment> commentlist = p.getComments();
                for (Comment c: commentlist) {
                	pipeline.execute(c);
                    System.out.println("   - [" + c.getId() + "] " + c.getCleanContent());
                }
            }
        }
        */
    }
}