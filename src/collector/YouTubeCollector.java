package collector;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import java.math.BigInteger;
import java.util.*;
public class YouTubeCollector {
    private final YouTube youtube;
    public YouTubeCollector(String apiKey) throws Exception {
        youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                null
        ).setApplicationName("HumanitarianLogisticsDataCollector").build();
        this.apiKey = apiKey;
    }
    private final String apiKey;

    /** Search YouTube videos **/
    public List<VideoRecord> searchVideos(String query, int maxResults) throws Exception {
        YouTube.Search.List request = youtube.search()
                .list("snippet")
                .setQ(query)
                .setType("video")
                .setMaxResults((long) maxResults)
                .setKey(apiKey);
        SearchListResponse response = request.execute();
        List<SearchResult> items = response.getItems();
        List<VideoRecord> videos = new ArrayList<>();
        for (SearchResult sr : items) {
            String videoId = sr.getId().getVideoId();
            String title = sr.getSnippet().getTitle();
            String publishedAt = sr.getSnippet().getPublishedAt().toStringRfc3339();

            // Get statistics (comment count)
            YouTube.Videos.List statsReq = youtube.videos()
                    .list("statistics")
                    .setId(videoId)
                    .setKey(apiKey);
            VideoListResponse statsResp = statsReq.execute();
            VideoStatistics stats = statsResp.getItems().get(0).getStatistics();
            BigInteger commentCountBI = stats.getCommentCount();
            long commentCount = (commentCountBI != null) ? commentCountBI.longValue() : 0L;
            videos.add(new VideoRecord(videoId, title, publishedAt, commentCount));
        }
        return videos;
    }

    /** Get comments for a video **/
    public List<CommentRecord> getComments(String videoId, int maxComments) throws Exception {
        try {
            YouTube.CommentThreads.List request = youtube.commentThreads()
                    .list("snippet")
                    .setVideoId(videoId)
                    .setMaxResults(100L)
                    .setOrder("time")
                    .setKey(apiKey);
            List<CommentRecord> comments = new ArrayList<>();
            int collected = 0;
            while (true) {
                CommentThreadListResponse response = request.execute();
                for (CommentThread ct : response.getItems()) {
                    if (collected >= maxComments) return comments;
                    CommentSnippet snip = ct.getSnippet().getTopLevelComment().getSnippet();
                    comments.add(new CommentRecord(
                            videoId,
                            ct.getId(),
                            snip.getTextOriginal(),
                            snip.getPublishedAt().toStringRfc3339()
                    ));
                    collected++;
                }
                String nextToken = response.getNextPageToken();
                if (nextToken == null) break;
                request.setPageToken(nextToken);
            }
            return comments;
        } catch (com.google.api.client.googleapis.json.GoogleJsonResponseException e) {
            if (e.getStatusCode() == 403) {
                System.out.println(" ⚠ Skipping video " + videoId + " (comments disabled)");
                return Collections.emptyList();
            }
            throw e; // throw unexpected errors
        }
    }

}