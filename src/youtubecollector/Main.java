package youtubecollector;
import java.util.*;
public class Main {
    private static final String API_KEY = "AIzaSyAKBxo0ajeIiRFVFtyJcGX9rQCdz1Sst7E";
    private static final String QUERY = "bão Yagi";
    public static void main(String[] args) throws Exception {
        YouTubeCollector collector = new YouTubeCollector(API_KEY);
        System.out.println("Searching for videos...");
        List<VideoRecord> videos = collector.searchVideos(QUERY, 20);
        
        // Save videos.csv
        List<String[]> videoRows = new ArrayList<>();
        videoRows.add(new String[]{"videoId", "title", "publishedAt", "commentCount"});
        for (VideoRecord v : videos) {
            videoRows.add(new String[]{v.videoId, v.title, v.publishedAt, String.valueOf(v.commentCount)});
        }
        CSVWriterUtil.writeCsv("youtubevideos.csv", videoRows);

        // Collect comments
        List<String[]> commentRows = new ArrayList<>();
        commentRows.add(new String[]{"videoId", "commentId", "text", "publishedAt"});

        for (VideoRecord v : videos) {
            System.out.println("Collecting comments for: " + v.videoId);
            List<CommentRecord> comments = collector.getComments(v.videoId, 300);

            for (CommentRecord c : comments) {
                commentRows.add(new String[]{c.videoId, c.commentId, c.text, c.publishedAt});
            }
        }

        CSVWriterUtil.writeCsv("youtubecomments.csv", commentRows);

        System.out.println("Done.");
    }
}