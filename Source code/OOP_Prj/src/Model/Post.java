package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Model.PostSource;

/**
 * Post: Model class representing a single social media post or comment.
 * It contains fields for raw collection data (Source, Timestamp) and
 * fields added during the preprocessing and analysis pipeline (SentimentLabel, etc.).
 */
public class Post {
	
	private String postId; 
	private String rawContent; 
	private String cleanContent; 
	private LocalDateTime timestamp; 
	private int commentCount;
    private int likeCount; 
    private int shareCount; 
	private PostSource source;
	private final List<Comment> comments;
	private SentimentLabel sentimentLabel;
	private String damageCategory; 
    private String reliefItemCategory; 

    
	public Post(String postId, String rawContent, LocalDateTime timestamp, int commentCount, PostSource source) {
		this.postId = postId;
		this.rawContent = rawContent;
		this.timestamp = timestamp;
		this.commentCount = commentCount;
        this.source = source;
		this.comments = new ArrayList<>();
        // Initialize analysis fields to default safe values
        this.sentimentLabel = SentimentLabel.UNKNOWN; 
        this.damageCategory = null;
        this.reliefItemCategory = null;
        this.likeCount = 0;
        this.shareCount = 0;
	}

    /**
     * Simplified constructor, defaulting source to OTHER_SOCIAL.
     */
    public Post(String postId, String rawContent, LocalDateTime timestamp, int commentCount) {
        this(postId, rawContent, timestamp, commentCount, PostSource.OTHER_SOCIAL);
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

	public String getDamageCategory() { 
		return damageCategory;
	}


	public void setDamageCategory(String damageCategory) { 
		this.damageCategory = damageCategory;
	}
    
    public String getReliefItemCategory() {
        return reliefItemCategory;
    }


    public void setReliefItemCategory(String reliefItemCategory) {
        this.reliefItemCategory = reliefItemCategory;
    }

	
	public String getPostId() {
		return postId;
	}

	public String getRawContent() {
		return rawContent;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public PostSource getSource() {
		return source;
	}

	public void setSource(PostSource source) {
		this.source = source;
	}

	public int getCommentCount() { 
		return commentCount;
	}
    
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }
	
	public List<Comment> getComments() {
		return comments;
	}
	
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
    
    public void setComments(List<Comment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
    }
    
    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", source=" + source +
                ", timestamp=" + timestamp +
                ", sentiment=" + sentimentLabel +
                ", damage='" + damageCategory + '\'' +
                ", contentSnippet='" + (cleanContent != null && cleanContent.length() > 30 ? cleanContent.substring(0, 30) + "..." : cleanContent) + '\'' +
                '}';
    }
}	