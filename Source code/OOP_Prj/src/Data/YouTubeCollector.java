package Data;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;

import Model.Post;
import Model.Comment;

import java.math.BigInteger;
import java.util.*;

public class YouTubeCollector implements IDataCollector {
    
    private final YouTube youtube;
    private final String apiKey;
    private static final String YOUTUBE_API_KEY = "AIzaSyA1KCwPZS7KEtCm7Q1_60T-xHqNT9x9v5E"; 

    public YouTubeCollector() throws Exception {
        youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                null
        ).setApplicationName("HumanitarianLogisticsDataCollector").build();
        this.apiKey = YOUTUBE_API_KEY;
    }

    @Override
    public void initialize(Map<String, String> configParams) {
        System.out.println("YouTube Collector Initialized.");
    }

    @Override
    public List<Post> collect(List<String> keywords, Date startDate, Date endDate) {
        List<Post> allPosts = new ArrayList<>();
        try {
            String query = String.join(" ", keywords);
            System.out.println("YouTube Search Query: " + query);
            
            allPosts = searchVideos(query, 30); 

            Iterator<Post> iterator = allPosts.iterator();
            while (iterator.hasNext()) {
                Post p = iterator.next();

                if (startDate != null && p.getTimestamp().before(startDate)) {
                    iterator.remove(); continue;
                }
                if (endDate != null && p.getTimestamp().after(endDate)) {
                    iterator.remove(); continue;
                }
                
                List<Comment> comments = getComments(p.getId(), 50, startDate, endDate);
                
                for (Comment c : comments) {
                	if (startDate != null && p.getTimestamp().before(startDate)) {
                        iterator.remove(); continue;
                    }
                    if (endDate != null && p.getTimestamp().after(endDate)) {
                        iterator.remove(); continue;
                    }
                    p.addComment(c);
                }
                System.out.println(" Finish collecting from post " + p.getId());
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
            
            Date publishedAt = new Date(sr.getSnippet().getPublishedAt().getValue());

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
                
                String fullContent = title + "\n" + desc;

                videos.add(new Post(videoId, fullContent, publishedAt, likeCount, commentCount));
            }
        }
        return videos;        
    }

    private List<Comment> getComments(String videoId, int maxComments, Date startDate, Date endDate) throws Exception {
        try {
            YouTube.CommentThreads.List request = youtube.commentThreads()
                    .list("snippet")
                    .setVideoId(videoId)
                    .setMaxResults(20L)
                    .setOrder("relevance")
                    .setKey(apiKey);
            
            List<Comment> comments = new ArrayList<>();
            
            while (comments.size() < maxComments) {
                CommentThreadListResponse response = request.execute();
                
                for (CommentThread ct : response.getItems()) {
                    if (comments.size() >= maxComments) break;
                    
                    CommentSnippet snip = ct.getSnippet().getTopLevelComment().getSnippet();
                    
                    Date cmtDate = new Date(snip.getPublishedAt().getValue());
                    
                    if (startDate != null && cmtDate.before(startDate)) {
                        continue;
                    }
                    if (endDate != null && cmtDate.after(endDate)) {
                        continue;
                    }

                    String content = snip.getTextOriginal();
                    if (content == null) content = snip.getTextDisplay();
                    
                    String cmtId = ct.getSnippet().getTopLevelComment().getId();
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
                return Collections.emptyList();
            }
            throw e; 
        }
    }
}