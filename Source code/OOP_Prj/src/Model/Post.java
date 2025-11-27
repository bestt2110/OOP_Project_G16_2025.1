package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Model.PostSource;

public class Post {
	
	private String id;
	private String rawContent;
	private LocalDateTime timestamp;
	private int cmt;
	private PostSource source;
	private final List<Comment> comments;
	
	private String cleanContent;
	private String sentimentPoint;
	private String damageType;
	
	public Post(String id, String rawContent, LocalDateTime timestamp, int cmt) {
		super();
		this.id = id;
		this.rawContent = rawContent;
		this.timestamp = timestamp;
		this.cmt = cmt;
		this.comments = new ArrayList<>();
	}

	public Post(String id, String rawContent, LocalDateTime timestamp, int cmt, PostSource source) {
		super();
		this.id = id;
		this.rawContent = rawContent;
		this.timestamp = timestamp;
		this.cmt = cmt;
		this.comments = new ArrayList<>();
		this.setSource(source);
	}

	public String getCleanContent() {
		return cleanContent;
	}

	public void setCleanContent(String cleanContent) {
		this.cleanContent = cleanContent;
	}

	public String getSentimentPoint() {
		return sentimentPoint;
	}

	public void setSentimentPoint(String sentimentPoint) {
		this.sentimentPoint = sentimentPoint;
	}

	public String getDamageType() {
		return damageType;
	}

	public void setDamageType(String damageType) {
		this.damageType = damageType;
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

	public PostSource getSource() {
		return source;
	}

	public void setSource(PostSource source) {
		this.source = source;
	}

	public int getCmt() {
		return cmt;
	}
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}