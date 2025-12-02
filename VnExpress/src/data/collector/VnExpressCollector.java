package data.collector;
import data.core.IDataCollector;
import data.model.PostRecord;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class VnExpressCollector implements IDataCollector {

	public VnExpressCollector() {
		// TODO Auto-generated constructor stub
	}
	private static final String SOURCE_NAME = "VnExpress";
    private static final String SEARCH_URL_BASE = "https://timkiem.vnexpress.net/?q=";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT);

    @Override
    public void initialize(Map<String, String> configParams) {
        System.out.println(SOURCE_NAME + " Collector Initialized. Starting Web Scraping...");
    }

    @Override
    public List<PostRecord> collect(List<String> keywords, Date startDate, Date endDate) {
        List<PostRecord> collectedData = new ArrayList<>();
        String query = String.join(" ", keywords); 
        
        System.out.println("Collecting data for query: " + query);

        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String searchUrl = SEARCH_URL_BASE + encodedQuery + "&siteid=1000000"; 
            
            Document searchPage = Jsoup.connect(searchUrl)
                                       .userAgent(USER_AGENT)
                                       .timeout(100000)
                                       .get();
            
            Elements articleLinks = searchPage.select(".item-news a.thumb, .item-news h3 a"); 
            
            int totalProcessed = 0;
            final int MAX_ARTICLES = 30; 
            
            for (Element link : articleLinks) {
                if (totalProcessed >= MAX_ARTICLES) break; 
                
                String articleUrl = link.attr("abs:href"); 
                
                PostRecord article = scrapeArticleDetail(articleUrl);
                
                if (article != null) {
                    try {
                        Date articleDate = DATE_FORMATTER.parse(article.publishedAt); 

                        if (!articleDate.before(startDate) && !articleDate.after(endDate)) {
                            collectedData.add(article);
                            totalProcessed++;
                            System.out.println("-> OK | Date: " + article.publishedAt + " | Title: " + link.text());
                        } 
                    } catch (ParseException e) {
                        System.err.println("Lỗi phân tích cú pháp ngày cho bài viết: " + article.publishedAt + " | URL: " + articleUrl);
                    }
                }
                
                Thread.sleep(1500); 
            }

        } catch (IOException e) {
            System.err.println("Lỗi kết nối hoặc cào web: " + e.getMessage());
        } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
        }
        
        return collectedData;
    }

    private PostRecord scrapeArticleDetail(String url) throws IOException {
        try {
            Document articleDoc = Jsoup.connect(url).userAgent(USER_AGENT).timeout(10000).get();
            
            Element titleElement = articleDoc.selectFirst("h1.title-detail"); 
            String title = (titleElement != null) ? titleElement.text() : "N/A";
            
            Element summaryElement = articleDoc.selectFirst(".sidebar-1 p.description"); 
            String summary = (summaryElement != null) ? summaryElement.text() : "";
            
            String content = articleDoc.select(".fck_detail p").text();
            String fullText = title + ". " + summary + ". " + content;
            
            Element timeElement = articleDoc.selectFirst(".date"); 
            String timeStr = (timeElement != null) ? timeElement.text().trim() : new Date().toString();

            String postId = url.substring(url.lastIndexOf('-') + 1, url.lastIndexOf('.'));
            
            String cleanTimeStr = timeStr;
            Matcher m = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4}).*?(\\d{1,2}:\\d{2})").matcher(timeStr);
            if (m.find()) {
                cleanTimeStr = m.group(1) + " " + m.group(2);
            } else {
                System.err.println("Không thể phân tích cú pháp thời gian: " + timeStr + " | URL: " + url);
                cleanTimeStr = DATE_FORMATTER.format(new Date()); 
           }

            return new PostRecord(
                postId,
                fullText,
                cleanTimeStr, 
                SOURCE_NAME,
                "VnExpress",
                0 
            );
        } catch (Exception e) {
            System.err.println("Không thể cào chi tiết bài viết " + url + ": " + e.getMessage());
            return null;
        }
    }

}
