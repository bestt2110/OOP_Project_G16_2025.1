package youtubecollector;
public class CommentRecord {
    public String videoId;
    public String commentId;
    public String text;
    public String publishedAt;
    public long likeCount;
    public CommentRecord(String videoId, String commentId, String text, String publishedAt, long likeCount) {
        this.videoId = videoId;
   	    this.commentId = commentId;
        this.text = text;
        this.publishedAt = publishedAt;
        this.likeCount = likeCount;
    }
}