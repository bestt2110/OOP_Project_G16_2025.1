package collector;
public class CommentRecord {
    public String videoId;
    public String commentId;
    public String text;
    public String publishedAt;
    public CommentRecord(String videoId, String commentId, String text, String publishedAt) {
        this.videoId = videoId;
   	this.commentId = commentId;
        this.text = text;
        this.publishedAt = publishedAt;
    }
}