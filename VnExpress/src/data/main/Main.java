package data.main;
import data.core.IDataCollector;
import data.collector.VnExpressCollector;
import data.model.PostRecord;
import data.util.CSVWriterUtil;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    
    private static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.forLanguageTag("vi-VN"));
    
    private static final List<String> KEYWORDS = List.of( "Yagi");
    
    public static void main(String[] args) throws Exception {
        
        Map<String, String> config = new HashMap<>();
        
        IDataCollector collector = new VnExpressCollector(); 
        collector.initialize(config);
        
        Date startDate = INPUT_DATE_FORMAT.parse("01/09/2024 00:00"); 
        Date endDate = INPUT_DATE_FORMAT.parse("30/09/2024 23:59"); 
        
        System.out.println("Searching for articles from VnExpress...");
        System.out.println("Date range: " + INPUT_DATE_FORMAT.format(startDate) + " - " + INPUT_DATE_FORMAT.format(endDate));
        
        List<PostRecord> records = collector.collect(KEYWORDS, startDate, endDate);
        
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"postId", "text", "publishedAt", "source", "authorId", "engagementCount"});

        for (PostRecord r : records) {
            String cleanedText = r.text.replace("\n", " ").replace("\r", " ").replace("\"", "'"); 
            
            rows.add(new String[]{
                r.postId, 
                cleanedText, 
                r.publishedAt, 
                r.source, 
                r.authorId, 
                String.valueOf(r.engagementCount)
            });
        }

        if (!records.isEmpty()) {
            CSVWriterUtil.writeCsv("vnexpress_disaster_data.csv", rows);
        } else {
            System.out.println("No records collected in the specified date range.");
        }
        
        System.out.println("Done. Collected " + records.size() + " records from " + collector.getClass().getSimpleName());
    }
}