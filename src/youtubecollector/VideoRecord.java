package youtubecollector;
public class VideoRecord {
    public String videoId;
    public String title;
    public String publishedAt;
    public long commentCount;
    public VideoRecord(String videoId, String title, String publishedAt, long commentCount) {
        this.videoId = videoId;
        this.title = title;
        this.publishedAt = publishedAt;
        this.commentCount = commentCount;
    }
}