package Model;

import java.time.LocalDateTime;

/**
 * Comment: Model class representing a single comment associated with a Post.
 * This object is processed through the same pipeline as Post objects for analysis.
 */
public class Comment {

    private String postID;
    private String id;
    private String rawContent;
    private LocalDateTime timestamp;
    
    // --- Analysis Fields ---
    private String cleanContent; 
    private SentimentLabel sentimentLabel;

    /**
     * Constructor for data collection phase.
     */
    public Comment(String postID, String id, String rawContent, LocalDateTime timestamp) {
        this.postID = postID;
        this.id = id;
        this.rawContent = rawContent;
        this.timestamp = timestamp;
        // Initialize analysis fields
        this.cleanContent = null;
        this.sentimentLabel = SentimentLabel.UNKNOWN;
    }

    // --- Getters and Setters ---
    
    public String getCleanContent() {
        return cleanContent;
    }
    
    public void setCleanContent(String cleanContent) {
        this.cleanContent = cleanContent;
    }
    
    public SentimentLabel getSentimentLabel() {
        return sentimentLabel;
    }
    
    public void setSentimentLabel(SentimentLabel sentimentLabel) {
        this.sentimentLabel = sentimentLabel;
    }
    
    public String getPostID() {
        return postID;
    }
    
    public String getId() {
        return id;
    }
    
    public String getRawContent() {
        return rawContent;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
        
    @Override
    public String toString() {
        return "Comment{" +
               "id='" + id + '\'' +
               ", postID='" + postID + '\'' +
               ", timestamp=" + timestamp +
               ", sentiment=" + sentimentLabel +
               '}';
    }
}