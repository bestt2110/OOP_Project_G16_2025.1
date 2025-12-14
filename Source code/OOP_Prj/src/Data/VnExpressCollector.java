package Data;

import Model.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VnExpressCollector implements IDataCollector {

    private static final String SEARCH_URL_BASE = "https://timkiem.vnexpress.net/?q=";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT);

    @Override
    public void initialize(Map<String, String> configParams) {
        System.out.println("--- VnExpress Collector (DEBUG MODE) ---");
    }

    @Override
    public List<Post> collect(List<String> keywords, Date startDate, Date endDate) {
        List<Post> collectedData = new ArrayList<>();
        Set<String> processedUrls = new HashSet<>();
        
        String query = String.join(" ", keywords); 
        int totalProcessed = 0;
        final int MAX_ARTICLES = 30;
        
        System.out.println("VnExpress Search Query " + query);
        
        int currentPage = 1;
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            
            while (totalProcessed < MAX_ARTICLES) {
                String searchUrl = SEARCH_URL_BASE + encodedQuery + "&siteid=1000000&page=" + currentPage;
                System.out.println("Accessing page: " + searchUrl);
                
                Document searchPage = Jsoup.connect(searchUrl)
                                           .userAgent(USER_AGENT)
                                           .timeout(30000).get();
                
                Elements articleLinks = searchPage.select(".item-news h3 a");
                
                System.out.println(articleLinks.size() + " found");
                
                if (articleLinks.isEmpty()) {
                    System.out.println("   -> CẢNH BÁO: Không thấy link nào. Có thể hết kết quả hoặc sai Selector.");
                    break;
                }

                for (Element link : articleLinks) {
                    if (totalProcessed >= MAX_ARTICLES) break;

                    String articleUrl = link.attr("abs:href");
                    System.out.println("   -> Processing: " + articleUrl);
                    
                    if (processedUrls.contains(articleUrl)) {
                        System.out.println("      -> Already collected. Pass");
                        continue;
                    }
                    processedUrls.add(articleUrl);

                    Post article = scrapeArticleDetail(articleUrl);

                    if (article != null) {
                        Date articleDate = article.getTimestamp();
                        boolean isAfterStart = (startDate == null) || !articleDate.before(startDate);
                        boolean isBeforeEnd = (endDate == null) || !articleDate.after(endDate);

                        if (isAfterStart && isBeforeEnd) {
                            collectedData.add(article);
                            totalProcessed++;
                            System.out.println("      -> ✅ ACCEPTED: " + link.text());
                        } else {
                            System.out.println("      -> ❌ REJECTED:  (" + DATE_FORMATTER.format(articleDate) + ") isn't in filtering period");
                        }
                    } else {
                        System.out.println("      -> ❌ REJECTED: CONTENT OR DATE CAN'T BE SCRAPED");
                    }
                    Thread.sleep(1000); 
                }
                currentPage++;
                Thread.sleep(2000);
            }

        } catch (IOException e) {
            System.err.println("Connecting error " + e.getMessage());
        } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
        }
        
        return collectedData;
    }
    
    private Post scrapeArticleDetail(String url) {
        try {
            Document articleDoc = Jsoup.connect(url).userAgent(USER_AGENT).timeout(10000).get();
            
            Element titleElement = articleDoc.selectFirst("h1.title-detail"); 
            String title = (titleElement != null) ? titleElement.text() : "N/A";
            
            Element summaryElement = articleDoc.selectFirst(".sidebar-1 p.description"); 
            String summary = (summaryElement != null) ? summaryElement.text() : "";
            
            String content = articleDoc.select(".fck_detail p").text();
            String fullText = title + ". " + summary + ". " + content;
            
            String postId = "";
            try {
                 postId = url.substring(url.lastIndexOf('-') + 1, url.lastIndexOf('.'));
            } catch (Exception e) { postId = String.valueOf(url.hashCode()); }
            
            Element timeElement = articleDoc.selectFirst(".date"); 
            String rawtimeStr = (timeElement != null) ? timeElement.text().trim() : "";  
            
            Date finalDate = null;
            Matcher m = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4}).*?(\\d{1,2}:\\d{2})").matcher(rawtimeStr);
            if (m.find()) {
                String cleanTimeStr = m.group(1) + " " + m.group(2);
                try {
                    finalDate = DATE_FORMATTER.parse(cleanTimeStr);
                } catch (ParseException e) {}
            } 
            
            if (finalDate == null) {
                System.out.println("      -> ⚠️ WARNING: CAN'T PARSE DATE. ORIGINAL STRING WAS '" + rawtimeStr + "'");
                return null; 
            }
            
            return new Post(postId, fullText, finalDate, 0, 0);

        } catch (Exception e) {
            System.err.println("      -> ERROR WHEN SCRAPING " + e.getMessage());
            return null;
        }
    }
}