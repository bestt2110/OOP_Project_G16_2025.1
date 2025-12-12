package Data;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;

import Model.Post;
import Model.Comment;

import java.math.BigInteger;
import java.util.*;

// 1. Phải implements IDataCollector
public class YouTubeCollector implements IDataCollector {
    
    private final YouTube youtube;
    private final String apiKey;

    public YouTubeCollector(String apiKey) throws Exception {
        youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                null
        ).setApplicationName("HumanitarianLogisticsDataCollector").build();
        this.apiKey = apiKey;
    }

    @Override
    public void initialize(Map<String, String> configParams) {
        System.out.println("YouTube Collector Initialized.");
    }

    // 2. Phải có hàm collect đúng chuẩn Interface
    @Override
    public List<Post> collect(List<String> keywords, Date startDate, Date endDate) {
        List<Post> allPosts = new ArrayList<>();
        try {
            String query = String.join(" ", keywords);
            System.out.println("YouTube Search Query: " + query);
            
            // Gọi hàm search video
            allPosts = searchVideos(query, 30); // Lấy 30 video demo

            // Lọc ngày tháng (nếu cần thiết, dù search youtube đã khá chuẩn rồi)
            // Và quan trọng: LẤY COMMENT CHO TỪNG VIDEO
            for (Post p : allPosts) {
                // Kiểm tra ngày tháng
                if (startDate != null && p.getTimestamp().before(startDate)) continue;
                if (endDate != null && p.getTimestamp().after(endDate)) continue;

                // 3. Gọi hàm lấy comment và add vào Post
                System.out.println(" -> Lấy comment cho video: " + p.getId());
                List<Comment> comments = getComments(p.getId(), 50); // Lấy tối đa 50 cmt
                for (Comment c : comments) {
                    p.addComment(c);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allPosts;
    }

    /** Helper: Search YouTube videos **/
    private List<Post> searchVideos(String query, int maxResults) throws Exception {
        YouTube.Search.List request = youtube.search()
                .list("snippet")
                .setQ(query)
                .setType("video")
                .setMaxResults((long) maxResults)
                .setKey(apiKey);
        
        SearchListResponse response = request.execute();
        List<SearchResult> items = response.getItems();
        List<Post> videos = new ArrayList<>();
        
        for (SearchResult sr : items) {
            String videoId = sr.getId().getVideoId();
            String title = sr.getSnippet().getTitle();
            String desc = sr.getSnippet().getDescription();
            
            // 4. FIX LỖI NGÀY THÁNG: Lấy mili-giây trực tiếp
            Date publishedAt = new Date(sr.getSnippet().getPublishedAt().getValue());

            // Get statistics
            YouTube.Videos.List statsReq = youtube.videos()
                    .list("statistics")
                    .setId(videoId)
                    .setKey(apiKey);
            VideoListResponse statsResp = statsReq.execute();
            
            if (!statsResp.getItems().isEmpty()) {
                VideoStatistics stats = statsResp.getItems().get(0).getStatistics();
                
                BigInteger commentCountBI = stats.getCommentCount();
                int commentCount = (commentCountBI != null) ? commentCountBI.intValue() : 0;

                BigInteger likeCountBI = stats.getLikeCount();
                int likeCount = (likeCountBI != null) ? likeCountBI.intValue() : 0;
                
                // Nội dung post = Title + Description
                String fullContent = title + "\n" + desc;

                videos.add(new Post(videoId, fullContent, publishedAt, likeCount, commentCount));
            }
        }
        return videos;       
    }

    /** Helper: Get comments for a video **/
    private List<Comment> getComments(String videoId, int maxComments) throws Exception {
        try {
            YouTube.CommentThreads.List request = youtube.commentThreads()
                    .list("snippet")
                    .setVideoId(videoId)
                    .setMaxResults(20L) // Mỗi trang 20 cmt
                    .setOrder("relevance")
                    .setKey(apiKey);
            
            List<Comment> comments = new ArrayList<>();
            
            while (comments.size() < maxComments) {
                CommentThreadListResponse response = request.execute();
                for (CommentThread ct : response.getItems()) {
                    if (comments.size() >= maxComments) break;
                    
                    CommentSnippet snip = ct.getSnippet().getTopLevelComment().getSnippet();
                    String content = snip.getTextOriginal();
                    if (content == null) content = snip.getTextDisplay();
                    
                    String cmtId = ct.getSnippet().getTopLevelComment().getId();
                    
                    // Fix lỗi ngày tháng tương tự
                    Date cmtDate = new Date(snip.getPublishedAt().getValue());
                    
                    long likeL = (snip.getLikeCount() != null) ? snip.getLikeCount() : 0;
                    int finalLike = (int) likeL;
                    
                    comments.add(new Comment(
                            videoId,
                            cmtId,
                            content,
                            cmtDate,
                            finalLike
                    ));
                }
                String nextToken = response.getNextPageToken();
                if (nextToken == null || nextToken.isEmpty()) break;
                request.setPageToken(nextToken);
            }
            return comments;
        } catch (com.google.api.client.googleapis.json.GoogleJsonResponseException e) {
            if (e.getStatusCode() == 403) {
                // System.out.println(" ⚠ Skipping comments (disabled) for " + videoId);
                return Collections.emptyList();
            }
            throw e; 
        }
    }
}